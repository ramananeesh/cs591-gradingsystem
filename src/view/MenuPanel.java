package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controller.Master;
import db.Create;
import db.Update;
import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;
import helper.Statistics;
import model.Category;
import model.Course;
import model.CourseStudent;
import model.GradeEntry;
import model.Item;

/**
 * The {@code MenuPanel} class represents the panel for viewing or modifying all
 * the grades
 */
public class MenuPanel extends JPanel implements Observer {
	/** The title for the window when MenuPanel displays */
	private static final String TITLE = "Grading System - Main Menu";
	private Master controller;
	/** The frame which contains this panel. */
	private MainFrame frame;

	private MyTableModel studentTableModel;

	private String[] tableStudentColumns;

	private String[] tableCategoryColumns;

	private DefaultTableModel categoryTableModel;

	private String[] tableItemColumns;

	private DefaultTableModel itemTableModel;

	private JTable tableCategory;

	private JTable tableItem;

	private JTable tableStudent;

	private DefaultComboBoxModel studentComboModel;

	private JComboBox<String> studentComboEdit;

//	private JComboBox<String> categoryEditItemCombo;

	private JTable editItemTable;
	private final JButton[] buttons = new JButton[4];
	private final JMenuBar menuBar = new JMenuBar();
	private final String[] menuName = new String[]{"  File  ", "  Edit  ", "  Grade  "};
	private final String[][] menuItemName = new String[][]{
			{"Add Student", "Add Students from File", "Add Category", "Add Item", null, "Back", "Exit"},
			{"Edit Student", "Edit Category", "Edit Item"},
			{"Edit All Grades", "Edit by Student", "View Grade", "Finalize Grade"}};
	private final ActionListener[][] menuActionListener;

	/**
	 * Initializes a newly created {@code MenuPanel} object
	 */
	public MenuPanel(MainFrame frame, String[] courseData, Master controller) { // TODO data should not be String array
		this.frame = frame;
		this.controller = controller;
		this.controller.addObserver(this);
		
		

		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.contentPaneBounds);
		setOpaque(false);

		UIManager.put("Table.font", FontManager.fontMenuTable);
		UIManager.put("TableHeader.font", FontManager.fontMenuTable);
		UIManager.put("Menu.font", FontManager.fontMenu);
		UIManager.put("MenuItem.font", FontManager.fontMenu);
		UIManager.put("TextField.font", FontManager.fontLabel);
		UIManager.put("ComboBox.font", FontManager.fontLabel);

		DefaultTableCellRenderer tableRender = new DefaultTableCellRenderer();

		DefaultTableCellRenderer studentRender = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			                                               boolean hasFocus, int row, int column) {
				MyTableModel model = (MyTableModel) table.getModel();
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(model.getRowColor(row));
				return c;
			}
		};
		studentRender.setHorizontalAlignment(SwingConstants.CENTER);
		studentRender.setVerticalAlignment(SwingConstants.CENTER);
		tableRender.setHorizontalAlignment(SwingConstants.CENTER);
		tableRender.setVerticalAlignment(SwingConstants.CENTER);

		String courseString = courseData[0] + "\n" + courseData[1] + "\n" + courseData[2];

		menuActionListener = new ActionListener[][]{ // TODO complete menu action
				{ // File
						addStudent -> { // Add Student
							try {
								JTextField fNameField = new JTextField();
								JTextField lNameField = new JTextField();
								JTextField BUIDField = new JTextField();
								JTextField emailField = new JTextField();
								JComboBox<String> levelCombo = new JComboBox<>(
										new String[]{"Undergraduate", "Graduate"});
								Object[] fields = {"First Name: ", fNameField, "Last Name: ", lNameField, "BU ID: ",
										BUIDField, "Email: ", emailField, "Level: ", levelCombo,};
								UIManager.put("OptionPane.minimumSize", new Dimension(SizeManager.optionPaneWidth,
										SizeManager.optionPaneRowHeight * fields.length));

								while (true) {
									int reply = JOptionPane.showConfirmDialog(null, fields, "Add Student",
											JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {
										String firstName = fNameField.getText();
										String lastName = lNameField.getText();
										String BUID = BUIDField.getText();
										String email = emailField.getText();
										String level = (String) levelCombo.getSelectedItem();
										String[] info = new String[]{firstName, lastName, BUID, email, level};
										controller.addStudentForCourse(controller.getCurrentCourse(), info);

										break;
									} else {
										return;
									}
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
							}
						}, addStudentsFromFile -> {

					// create an object of JFileChooser class
					JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
					UIManager.put("FileChooser.listFont",new Font("Cascadia", Font.BOLD,35 ));
					j.setPreferredSize(new Dimension(SizeManager.optionPaneWidth,
							SizeManager.optionPaneRowHeight * 6));
					// invoke the showsOpenDialog function to show the save dialog
					int r = j.showOpenDialog(null);

					// if the user selects a file
					if (r == JFileChooser.APPROVE_OPTION) {
						// set the label to the path of the selected file
						ArrayList<HashMap<String, String>> students = processCsvFile(
								j.getSelectedFile().getAbsolutePath());
						controller.addStudentsForCourse(controller.getCurrentCourse(), students);
					}
					// if the user cancelled the operation
					else {
						System.out.println("cancelled");
					}
				}, addCategory -> { // Add Category
					try {
						JTextField categoryField = new JTextField();
						JTextField percentageField = new JTextField();
						Object[] fields = {"Category: ", categoryField, "Percentage (%): ", percentageField,};
						UIManager.put("OptionPane.minimumSize", new Dimension(SizeManager.optionPaneWidth,
								SizeManager.optionPaneRowHeight * fields.length));

						while (true) {
							int reply = JOptionPane.showConfirmDialog(this, fields, "Add Category",
									JOptionPane.OK_CANCEL_OPTION);
							if (reply == JOptionPane.OK_OPTION) {
								JOptionPane.showMessageDialog(this,
										"Please edit the percentage for all other categories", "Warning",
										JOptionPane.WARNING_MESSAGE);
								String fieldName = categoryField.getText();
								try {
									double weight = Double.parseDouble(percentageField.getText()) / 100;
									controller.addNewCategoryForCourse(controller.getCurrentCourse(), fieldName,
											weight, controller.getCurrentCourse().getCourseId());
								} catch (Exception ex) {
									JOptionPane.showMessageDialog(this, "Error",
											"Please Enter valid weight percentage between 0 and 1",
											JOptionPane.ERROR_MESSAGE);
									continue;
								}
								break;
							} else {
								return;
							}
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}, addItem -> { // Add Item
					try {
						JComboBox<String> categoryCombo = new JComboBox<>();

						ArrayList<Category> categories = controller.getCurrentCourse().getCategories();
						for (int i = 0; i < categories.size(); i++) {
							categoryCombo.addItem(categories.get(i).getFieldName());
						}
						JTextField itemField = new JTextField();
						JTextField percentageField = new JTextField();
						JTextField maxPointsField = new JTextField();
						Object[] fields = {"Category: ", categoryCombo, "Item: ", itemField, "Percentage (%): ",
								percentageField, "Max Points: ", maxPointsField,};
						UIManager.put("OptionPane.minimumSize", new Dimension(SizeManager.optionPaneWidth,
								SizeManager.optionPaneRowHeight * fields.length));

						while (true) {
							int reply = JOptionPane.showConfirmDialog(null, fields, "Add Item",
									JOptionPane.OK_CANCEL_OPTION);
							if (reply == JOptionPane.OK_OPTION) {

								JOptionPane.showMessageDialog(null,
										"Please edit the percentage for all other items", "Warning",
										JOptionPane.WARNING_MESSAGE);
								int categoryIndex = categoryCombo.getSelectedIndex();
								if (categoryIndex < 0) {
									JOptionPane.showMessageDialog(null, "Error", "Please select a category",
											JOptionPane.ERROR_MESSAGE);
									continue;
								}
								String fieldName = itemField.getText();
								try {
									double itemWeight = Double.parseDouble(percentageField.getText()) / 100;
									double maxPoints = Double.parseDouble(maxPointsField.getText());
									controller.addItemForCourseCategory(controller.getCurrentCourse(),
											categoryIndex, fieldName, itemWeight, maxPoints);

								} catch (Exception ex) {
									JOptionPane.showMessageDialog(null, "Error",
											"Please Enter valid weight percentage between 0 and 1",
											JOptionPane.ERROR_MESSAGE);
									continue;
								}
								break;
							} else {
								return;
							}
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
					}

				}, null, // Separator
						back -> frame.changePanel(this, new CoursePanel(frame, controller)), // Back
						exit -> System.exit(0)},
				{editStudent -> { // Edit Student
					try {
						String[] studentDataForCombo = controller.getCurrentCourse().getStudentNamesAsList();
						studentComboModel = new DefaultComboBoxModel(studentDataForCombo);
						studentComboEdit = new JComboBox<>(studentComboModel);

						JTextField nameField = new JTextField();
						JComboBox<String> levelCombo = new JComboBox<String>(
								new String[]{"Undergraduate", "Graduate"});
						JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Active", "Freeze"});
						Object[] fields = {"Student: ", studentComboEdit, "Name:", nameField, "Level: ", levelCombo,
								"status: ", statusCombo,};
						UIManager.put("OptionPane.minimumSize", new Dimension(SizeManager.optionPaneWidth,
								SizeManager.optionPaneRowHeight * fields.length));

						while (true) {
							int reply = JOptionPane.showConfirmDialog(this, fields, "Edit Student",
									JOptionPane.OK_CANCEL_OPTION);
							if (reply == JOptionPane.OK_OPTION) {
								int chosenIndex = studentComboEdit.getSelectedIndex();
								if (chosenIndex != -1) {

									String name = nameField.getText().trim();
									String level = (String) levelCombo.getSelectedItem();
									boolean status;
									if (statusCombo.getSelectedIndex() == 0)
										status = true;
									else
										status = false;

									HashMap<String, String> map = new HashMap<String, String>();

									map.put("Name", name);
									map.put("Type", level);

									controller.modifyStudentForCourse(controller.getCurrentCourse(), chosenIndex, map,
											status);
								}
								break;
							} else {
								return;
							}
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}, editCategory -> { // Edit Category
					try {
						String[][] categoryData;

						categoryData = controller.getCurrentCourse().getCategoryDataForList();
						for (int i = 0; i < categoryData.length; i++) {
							categoryData[i][1] = String.valueOf(Double.parseDouble(categoryData[i][1]) * 100);
						}

						String[] categoryColumn = {"Category", "Percentage"};
						DefaultTableModel tableModel = new DefaultTableModel(categoryData, categoryColumn);
						JTable categoryTable = new JTable(tableModel) {
							public boolean isCellEditable(int row, int column) {
								return column > 0;
							}
						};
						categoryTable.setDefaultRenderer(Object.class, tableRender);
						categoryTable.setRowHeight(SizeManager.tableRowHeight);
						JScrollPane categoryScrollPane = new JScrollPane(categoryTable);
						Object[] options = {"Save", "Delete", "Cancel"};
						while (true) {
//							int reply = JOptionPane.showConfirmDialog(this, categoryScrollPane, "Edit Category",
//									JOptionPane.OK_CANCEL_OPTION);
							int reply = JOptionPane.showOptionDialog(this, categoryScrollPane, "Edit Category", 0,
									JOptionPane.INFORMATION_MESSAGE, null, options, null);
							boolean flag = false;
							ArrayList<HashMap<String, Double>> modifiedData = new ArrayList<HashMap<String, Double>>();
							if (reply == JOptionPane.OK_OPTION) {

								for (int i = 0; i < tableModel.getRowCount(); i++) {
									HashMap<String, Double> m = new HashMap<String, Double>();
									String key = (String) tableModel.getValueAt(i, 0);
									key = key.trim();
									if (!key.equals("")) {
										try {
											double value = Double.parseDouble((String) tableModel.getValueAt(i, 1)) / 100;
											m.put(key, value);
											modifiedData.add(m);
										} catch (Exception e) {
											JOptionPane.showMessageDialog(this, "Please Enter correct Values", "Error",
													JOptionPane.ERROR_MESSAGE);
											flag = true;
											break;
										}
									} else {
										JOptionPane.showMessageDialog(this, "Please Enter correct Values", "Error",
												JOptionPane.ERROR_MESSAGE);
										flag = true;
										break;
									}
								}
								if (flag == false) {
									controller.modifyCategoriesForCourse(controller.getCurrentCourse(), modifiedData);
									break;
								}
							} else {
								if (reply == 1) {
									controller.deleteCategoryForCourse(controller.getCurrentCourse(),
											categoryTable.getSelectedRow());
								}
								return;
							}
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}, editItem -> { // Edit Item
					try {

//						categoryEditItemCombo = new JComboBox<>();
//
						ArrayList<Category> categories = controller.getCurrentCourse().getCategories();
//						for (int i = 0; i < categories.size(); i++) {
//							categoryEditItemCombo.addItem(categories.get(i).getFieldName());
//						}
//
						String[][] itemData;
//						if (categoryEditItemCombo.getSelectedIndex() < 0)
//							return;

//						Category chosenCategory = categories.get(categoryEditItemCombo.getSelectedIndex());
//						itemData = controller.getItemDetailsForCourseCategory(controller.getCurrentCourse(),
//								categoryEditItemCombo.getSelectedIndex(), true);
						itemData = controller.getAllItemDetailsForCourse(controller.getCurrentCourse(), true);
						ArrayList<Item> allItems = controller.getAllItemsForCourse(controller.getCurrentCourse());
						for (int i = 0; i < itemData.length; i++) {
							itemData[i][2] = String.valueOf(Double.parseDouble(itemData[i][2]) * 100);
						}
						String[] itemColumn = {"Category", "Item", "Percentage", "Max Points"};
						DefaultTableModel tableEditItemModel = new DefaultTableModel(itemData, itemColumn);

						editItemTable = new JTable(tableEditItemModel) {
							public boolean isCellEditable(int row, int column) {
								return column > 0;
							}
						};
						editItemTable.setRowHeight(SizeManager.tableRowHeight);
						editItemTable.setDefaultRenderer(Object.class, tableRender);
						JScrollPane itemScrollPane = new JScrollPane(editItemTable);
//						Object[] fields = { "Category: ", categoryEditItemCombo, "Item: ", itemScrollPane, };
						Object[] fields = {itemScrollPane,};
						UIManager.put("OptionPane.minimumSize", new Dimension(SizeManager.optionPaneWidth,
								SizeManager.optionPaneRowHeight * fields.length));
						Object[] options = {"Save", "Delete", "Cancel"};
						while (true) {
//							int reply = JOptionPane.showConfirmDialog(this, fields, "Edit Item",JOptionPane.OK_CANCEL_OPTION);
							int reply = JOptionPane.showOptionDialog(this, fields, "Edit Item", 0,
									JOptionPane.INFORMATION_MESSAGE, null, options, null);
							boolean flag = false;
							HashMap<String, ArrayList<Double>> map = new HashMap<String, ArrayList<Double>>();
							if (reply == JOptionPane.OK_OPTION) {
								for (int i = 0; i < tableEditItemModel.getRowCount(); i++) {
									String key = (String) tableEditItemModel.getValueAt(i, 1);
									ArrayList<Double> l = new ArrayList<Double>();
									for (int j = 2; j < tableEditItemModel.getColumnCount(); j++) {
										String str = (String) tableEditItemModel.getValueAt(i, j);
										if (str.trim().equals("")) {
											flag = true;
											break;
										}
										try {
											double value;
											if (j == 2) {
												value = Double
														.parseDouble((String) tableEditItemModel.getValueAt(i, j)) / 100;
											} else {
												value = Double
														.parseDouble((String) tableEditItemModel.getValueAt(i, j));
											}
											l.add(value);
										} catch (Exception e) {
											JOptionPane.showMessageDialog(this, "Please Enter correct Values", "Error",
													JOptionPane.ERROR_MESSAGE);
											flag = true;
											break;
										}
									}
									if (flag) {
										break;
									}
									map.put(key, l);
								}
								if (flag == false) {
									controller.modifyItemsForCourse(controller.getCurrentCourse(), map);
									System.out.println(map);
									break;
								}

							} else {
								if (reply == 1) {
									Item item = allItems.get(editItemTable.getSelectedRow());
									controller.deleteItemFromCourse(controller.getCurrentCourse(), item);
								}
								return;
							}
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}}, {editAllGrades -> { // Edit All Grades
			frame.changePanel(this, new GradePanel(frame, courseData, true, this.controller));
		}, editByStudent -> { // Edit by Student
			try {
				Course course = controller.getCurrentCourse();
				List<CourseStudent> studentList = course.getStudents();
				List<Category> categoryList = course.getCategories();
				List<Item> itemList = categoryList.get(0).getItems();
				String[] studentComboBoxItems = new String[studentList.size()];
				for (int i = 0; i < studentList.size(); ++i) {
					studentComboBoxItems[i] = studentList.get(i).getName();
				}
				String[] categoryComboBoxItems = new String[categoryList.size()];
				for (int i = 0; i < categoryList.size(); ++i) {
					categoryComboBoxItems[i] = categoryList.get(i).getFieldName();
				}
				String[] itemComboBoxItems = new String[itemList.size()];
				for (int i = 0; i < itemList.size(); ++i) {
					itemComboBoxItems[i] = itemList.get(i).getFieldName();
				}
				JComboBox<String> studentComboBox = new JComboBox<>(studentComboBoxItems);
				JComboBox<String> categoryComboBox = new JComboBox<>(categoryComboBoxItems);
				JComboBox<String> itemComboBox = new JComboBox<>(itemComboBoxItems);
				categoryComboBox.addActionListener(e -> {
					int categoryIndex = categoryComboBox.getSelectedIndex();
					List<Item> newItemList = categoryList.get(categoryIndex).getItems();
					String[] newItemComboBoxItems = new String[newItemList.size()];
					for (int i = 0; i < newItemList.size(); ++i) {
						newItemComboBoxItems[i] = newItemList.get(i).getFieldName();
					}
					itemComboBox.setModel(new DefaultComboBoxModel<>(newItemComboBoxItems));
				});
				JTextField gradeTextField = new JTextField();
				JTextField commentTextField = new JTextField();
				Object[] fields = {"Student: ", studentComboBox, "Category: ", categoryComboBox, "Item: ",
						itemComboBox, "Points Earned: ", gradeTextField, "Comment: ", commentTextField};
				UIManager.put("OptionPane.minimumSize", new Dimension(SizeManager.optionPaneWidth,
						SizeManager.optionPaneRowHeight * fields.length));

				while (true) {
					int reply = JOptionPane.showConfirmDialog(this, fields, "Edit by Student",
							JOptionPane.OK_CANCEL_OPTION);
					if (reply == JOptionPane.OK_OPTION) {
						CourseStudent student = controller.getCurrentCourse().getStudent(studentComboBox.getSelectedIndex());
						Category category = categoryList.get(categoryComboBox.getSelectedIndex());
						Item item = itemList.get(itemComboBox.getSelectedIndex());
						GradeEntry gradeEntry = student.getGradeEntryForItemInCategory(course.getCourseId(), category.getId(), item.getId());
						double pointsEarned = Double.parseDouble(gradeTextField.getText());
						if (gradeEntry != null) {
							gradeEntry.setPointsEarned(pointsEarned);
							gradeEntry.setPercentage(pointsEarned / item.getMaxPoints() * 100);
							gradeEntry.setComments(commentTextField.getText());
							Update.updateCourseStudentGradeEntry(student, course.getCourseId(), category.getId(), item.getId());
						} else {
							gradeEntry = new GradeEntry(
									item.getFieldName(), item.getId(), category.getId(),
									item.getMaxPoints(), pointsEarned, pointsEarned / item.getMaxPoints() * 100,
									course.getCourseId(), commentTextField.getText());
							student.addGradeEntry(gradeEntry);
							Create.insertNewGradeEntry(gradeEntry, student.getBuid());
						}
						controller.fireUpdate();
						break;
					} else {
						return;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}, viewGrades -> { // View Grades
			frame.changePanel(this, new ViewGradePanel(frame, courseData, false, this.controller));
		}, finalize -> {

			if (!controller.getCurrentCourse().isFinalized()) {
				if (!controller.canBeFinalized(controller.getCurrentCourse())) {
					JOptionPane.showMessageDialog(this,
							"Please make sure that all category weights and item weights within categories sum to 100%",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			controller.initiateCourseFinalization(controller.getCurrentCourse());
			frame.changePanel(this, new FinializePanel(frame, controller));
		}}};

		menuBar.setLayout(new GridBagLayout());
		for (int i = 0; i < menuName.length; ++i) {
			JMenu menu = new JMenu(menuName[i]);
			for (int j = 0; j < menuItemName[i].length; ++j) {
				if (menuItemName[i][j] == null) {
					menu.addSeparator();
				} else {
					JMenuItem menuItem = new JMenuItem(menuItemName[i][j]);
					menuItem.addActionListener(menuActionListener[i][j]);
					menu.add(menuItem);
				}
			}
			menuBar.add(menu);
		}
		menuBar.setBounds(SizeManager.menuBarBounds);

		add(menuBar);

		JTextPane informationTextPane = new JTextPane();
		informationTextPane.setBounds(SizeManager.textInfoBounds);
		informationTextPane.setText(courseString + "\n" ); //+ getStatisticsForCourse()
		informationTextPane.setFont(FontManager.fontText);
		informationTextPane.setEditable(false);
		StyledDocument doc = informationTextPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);

		add(informationTextPane);

		tableStudentColumns = new String[]{"Student Name", "Email"};
		String[][] tableStudentData;
		tableStudentData = controller.getAllStudentsForCourse(controller.getCurrentCourse());
		Boolean[] statusData = controller.getAllStudentsStatusForCourse(controller.getCurrentCourse());
		studentTableModel = new MyTableModel(tableStudentData, tableStudentColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		for (int i = 0; i < studentTableModel.getRowCount(); i++) {
			if (statusData[i] == false) {
				studentTableModel.setRowColor(i, Color.gray);
			} else {
				studentTableModel.setRowColor(i, Color.WHITE);
			}
		}

		tableStudent = new JTable(studentTableModel);
		tableStudent.setRowHeight(SizeManager.menuTableRowHeight);
		JScrollPane tableStudentScrollPane = new JScrollPane(tableStudent);
		tableStudentScrollPane.setBounds(SizeManager.tableStudentBounds);

		add(tableStudentScrollPane);

		tableCategoryColumns = new String[]{"Category Name", "Weight"};

		String[][] tableCategoryData = controller.getCurrentCourse().getCategoryDataForList();
		for (int i = 0; i < tableCategoryData.length; i++) {
			double weight = Double.parseDouble(tableCategoryData[i][1]) * 100;
			tableCategoryData[i][1] = (int) weight + "%";
		}

		categoryTableModel = new DefaultTableModel(tableCategoryData, tableCategoryColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCategory = new JTable(categoryTableModel);

		JScrollPane tableCategoryScrollPane = new JScrollPane(tableCategory);
		tableCategoryScrollPane.setBounds(SizeManager.tableCategoryBounds);

		add(tableCategoryScrollPane);

		tableItemColumns = new String[]{"Item Name", "Weight"};
		String[][] tableItemData = controller.getAllItemsDetailsForCourse(controller.getCurrentCourse());
		for (int i = 0; i < tableItemData.length; i++) {
			double weight = Double.parseDouble(tableItemData[i][1]) * 100;
			tableItemData[i][1] = (int) weight + "%";
		}

		itemTableModel = new DefaultTableModel(tableItemData, tableItemColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		}

		;
		tableItem = new JTable(itemTableModel);

		JScrollPane tableItemScrollPane = new JScrollPane(tableItem);
		tableItemScrollPane.setBounds(SizeManager.tableItemBounds);

		add(tableItemScrollPane);

		for (int i = 0; i < 2; ++i) {
			tableCategory.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
			tableItem.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
		}
		for (JScrollPane scrollPane : new JScrollPane[]{tableStudentScrollPane, tableCategoryScrollPane, tableItemScrollPane}) {
			scrollPane.setOpaque(false);
			scrollPane.getViewport().setOpaque(false);
		}
		tableStudent.setDefaultRenderer(Object.class, studentRender);
		for (JTable table : new JTable[]{tableCategory, tableItem}) {
			table.setDefaultRenderer(Object.class, tableRender);
		}
		for (JTable table : new JTable[]{tableStudent, tableCategory, tableItem}) {
			table.setRowHeight(SizeManager.menuTableRowHeight);
			table.setRowSelectionAllowed(true);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
				String text = courseString + "\n" +  "\n\n"; //getStatisticsForCourse() +

				if (tableStudent.getSelectedRow() != -1) {
					CourseStudent courseStudent = controller.getCurrentCourse().getStudent(tableStudent.getSelectedRow());
					text += courseStudent.getStudentDetails() + "\n" + "Grade : " + getStatisticsForStudent(courseStudent) + "\n\n";
				}
				if (tableCategory.getSelectedRow() != -1) {
					if (listSelectionEvent.getSource() == tableCategory.getSelectionModel()) {
						Object[][] newTableItemData;

						if (tableCategory.getSelectedRow() < 0) {
							return;
						}
						newTableItemData = controller.getItemDetailsForCourseCategory(controller.getCurrentCourse(),
								tableCategory.getSelectedRow(), false);
						DefaultTableModel newTableItemModel = new DefaultTableModel(newTableItemData, tableItemColumns);
						tableItem.setModel(newTableItemModel);
						for (int i = 0; i < 2; ++i) {
							tableCategory.getColumnModel().getColumn(i)
									.setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
							tableItem.getColumnModel().getColumn(i)
									.setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
						}
					}
					text += "Category: ";
					text += tableCategory.getValueAt(tableCategory.getSelectedRow(), 0) + "\n";
					text += "Weight: ";
					text += tableCategory.getValueAt(tableCategory.getSelectedRow(), 1) + "\n";
					Category category = controller.getCurrentCourse().getCategory(tableCategory.getSelectedRow());
					text += getStatisticsForCategory(category) + "\n\n";

				}
				if (tableItem.getSelectedRow() != -1) {
					text += "Item: ";
					text += tableItem.getValueAt(tableItem.getSelectedRow(), 0) + "\n";
					text += "Weight: ";
					text += tableItem.getValueAt(tableItem.getSelectedRow(), 1) + "\n";
					if (tableCategory.getSelectedRow() != -1) {
						Category category = controller.getCurrentCourse().getCategory(tableCategory.getSelectedRow());
						Item item = category.getItem(tableItem.getSelectedRow());
						text += getStatisticsForItem(item);
					} else {
						int categoryIndex = 0, itemIndex = 0, selectedRow = tableItem.getSelectedRow();
						List<Category> categoryList = controller.getCurrentCourse().getCategories();
						while (selectedRow > 0) {
							Category category = categoryList.get(categoryIndex);
							if (selectedRow >= category.getItems().size()) {
								categoryIndex++;
								selectedRow -= category.getItems().size();
							} else {
								itemIndex = selectedRow;
								selectedRow = 0;
							}
						}
						text += getStatisticsForItem(controller.getCurrentCourse().getCategory(categoryIndex).getItem(itemIndex));
					}
				}
				informationTextPane.setText(text);
			});

			JTableHeader tableHeader = table.getTableHeader();
			tableHeader.setPreferredSize(new Dimension(table.getWidth(), table.getRowHeight()));
			tableHeader.setForeground(ColorManager.lightColor);
			tableHeader.setBackground(ColorManager.primaryColor);
			tableHeader.setEnabled(false);
		}

		for (int i = 0; i < 4; ++i) {
			buttons[i] = new JButton(menuItemName[2][i]);
			buttons[i].setForeground(ColorManager.lightColor);
			buttons[i].setBackground(ColorManager.primaryColor);
			buttons[i].addActionListener(menuActionListener[2][i]);
			buttons[i].setBounds(SizeManager.menuButtonsBounds[i]);
			buttons[i].setFont(FontManager.fontMenuButton);
			add(buttons[i]);
		}

		if (controller.getCurrentCourse().isFinalized()) {
			lock();
		}

		setVisible(true);

	}

	static class MyTableModel extends DefaultTableModel {
		List<Color> rowColor;

		public MyTableModel(String[][] tableRowData, Object[] tableColumnNames) {
			super(tableRowData, tableColumnNames);
			rowColor = new ArrayList<Color>();
			for (int i = 0; i < tableRowData.length; i++) {
				rowColor.add(Color.white);
			}
		}

		public void setRowColor(int row, Color c) {
			rowColor.set(row, c);
			fireTableRowsUpdated(row, row);
		}

		public Color getRowColor(int row) {
			return rowColor.get(row);
		}

	}

	private ArrayList<HashMap<String, String>> processCsvFile(String filePath) {
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";

		ArrayList<HashMap<String, String>> students = new ArrayList<HashMap<String, String>>();
		try {

			br = new BufferedReader(new FileReader(filePath));
			while ((line = br.readLine()) != null) {
				HashMap<String, String> map = new HashMap<String, String>();
				// use comma as separator
				String[] values = line.split(cvsSplitBy);
				map.put("fname", values[0]);
				map.put("lname", values[1]);
				map.put("buid", values[2]);
				map.put("email", values[3]);
				map.put("type", values[4]);

				students.add(map);
			}
			return students;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

		if (controller.getCurrentCourse().isFinalized()) {
			lock();
			return;
		}
		this.controller = controller;

		String[][] tableCategoryData = controller.getCurrentCourse().getCategoryDataForList();
		for (int i = 0; i < tableCategoryData.length; i++) {
			double weight = Double.parseDouble(tableCategoryData[i][1]) * 100;
			tableCategoryData[i][1] = (int) weight + "%";
		}
		categoryTableModel = new DefaultTableModel(tableCategoryData, tableCategoryColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCategory.setModel(categoryTableModel);

		String[][] tableItemData = controller.getAllItemsDetailsForCourse(controller.getCurrentCourse());
		for (int i = 0; i < tableItemData.length; i++) {
			double weight = Double.parseDouble(tableItemData[i][1]) * 100;
			tableItemData[i][1] = (int) weight + "%";
		}
		itemTableModel = new DefaultTableModel(tableItemData, tableItemColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableItem.setModel(itemTableModel);

		String[][] tableStudentData;
		tableStudentData = controller.getAllStudentsForCourse(controller.getCurrentCourse());

		studentTableModel = new MyTableModel(tableStudentData, tableStudentColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		Boolean[] statusData = controller.getAllStudentsStatusForCourse(controller.getCurrentCourse());
		for (int i = 0; i < studentTableModel.getRowCount(); i++) {
			if (statusData[i] == false) {
				System.out.println(i);
				studentTableModel.setRowColor(i, Color.gray);
			} else {
				studentTableModel.setRowColor(i, Color.WHITE);
			}
		}
		tableStudent.setModel(studentTableModel);
	}

	private void lock() {
		List<String> ableFunction = Arrays.asList("Back", "Exit", "View Grade", "Finalize Grade");
		for (int i = 0; i < menuBar.getMenuCount(); ++i) {
			JMenu menu = menuBar.getMenu(i);
			for (int j = 0; j < menu.getItemCount(); ++j) {
				JMenuItem menuItem = menu.getItem(j);
				if (menuItem != null && !ableFunction.contains(menuItem.getText())) {
					menuItem.setEnabled(false);
				}
			}
		}
		buttons[0].setEnabled(false);
		buttons[1].setEnabled(false);
		revalidate();
		repaint();
	}

	private String getStatisticsForCourse() {
		Course course = controller.getCurrentCourse();
		List<Category> categoryList = controller.getAllCategoriesForCourse(course);
		List<CourseStudent> studentList = course.getStudents();
		double[] statisticsGrade = new double[studentList.size()];
		Arrays.fill(statisticsGrade, 0);
		for (int i = 0; i < studentList.size(); ++i) {
			CourseStudent courseStudent = studentList.get(i);
			for (Category category : categoryList) {
				for (Item item : category.getItems()) {
					GradeEntry gradeEntry = courseStudent.getGradeEntryForItemInCategory(course.getCourseId(), category.getId(), item.getId());
					if (gradeEntry != null) {
						statisticsGrade[i] += gradeEntry.getPercentage() * category.getWeight() * item.getWeight();
					}
				}
			}
		}
		Statistics statistics = new Statistics(statisticsGrade);
		return String.format("Mean = %.1f, Median = %.1f, Standard Deviation = %.1f",
				statistics.getMean(), statistics.getMedian(), statistics.getStandardDeviation());
	}

	private String getStatisticsForStudent(CourseStudent courseStudent) {
		Course course = controller.getCurrentCourse();
		double grade = 0;
		for (Category category : course.getCategories()) {
			for (Item item : category.getItems()) {
				GradeEntry gradeEntry = courseStudent.getGradeEntryForItemInCategory(course.getCourseId(), category.getId(), item.getId());
				if (gradeEntry != null) {
					grade += gradeEntry.getPercentage() * category.getWeight() * item.getWeight();
				}
			}
		}
		return Double.toString(grade);
	}

	private String getStatisticsForCategory(Category category) {
		Course course = controller.getCurrentCourse();
		List<CourseStudent> studentList = course.getStudents();
		double[] statisticsGrade = new double[studentList.size()];
		Arrays.fill(statisticsGrade, 0);
		for (int i = 0; i < studentList.size(); ++i) {
			CourseStudent courseStudent = studentList.get(i);
			for (Item item : category.getItems()) {
				GradeEntry gradeEntry = courseStudent.getGradeEntryForItemInCategory(course.getCourseId(), category.getId(), item.getId());
				if (gradeEntry != null) {
					statisticsGrade[i] += gradeEntry.getPercentage() * item.getWeight();
				}
			}
		}
		Statistics statistics = new Statistics(statisticsGrade);
		return String.format("Mean = %.1f, Median = %.1f, Standard Deviation = %.1f",
				statistics.getMean(), statistics.getMedian(), statistics.getStandardDeviation());
	}

	private String getStatisticsForItem(Item item) {
		Course course = controller.getCurrentCourse();
		List<CourseStudent> studentList = course.getStudents();
		double[] statisticsGrade = new double[studentList.size()];
		Arrays.fill(statisticsGrade, 0);
		for (int i = 0; i < studentList.size(); ++i) {
			CourseStudent courseStudent = studentList.get(i);
			GradeEntry gradeEntry = courseStudent.getGradeEntryForItemInCategory(course.getCourseId(), item.getCategoryId(), item.getId());
			if (gradeEntry != null) {
				statisticsGrade[i] += gradeEntry.getPercentage();
			}
		}
		Statistics statistics = new Statistics(statisticsGrade);
		return String.format("Mean = %.1f, Median = %.1f, Standard Deviation = %.1f",
				statistics.getMean(), statistics.getMedian(), statistics.getStandardDeviation());
	}
}