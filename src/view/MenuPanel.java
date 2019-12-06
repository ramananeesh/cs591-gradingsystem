package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;
import javafx.scene.control.ComboBox;
import model.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controller.Master;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.awt.Desktop;

/**
 * The {@code MenuPanel} class represents the panel for viewing or modifying all
 * the grades
 */
public class MenuPanel extends JPanel implements Observer {
	private Master controller;

	/** The title for the window when MenuPanel displays */
	private static final String TITLE = "Grading System - Main Menu";

	/** The frame which contains this panel. */
	private MainFrame frame;

	private DefaultTableModel studentTableModel;

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

	/**
	 * Initializes a newly created {@code MenuPanel} object
	 */
	public MenuPanel(MainFrame frame, String[] courseData, Master controller) { // TODO data should not be String array
		this.frame = frame;
		this.controller = controller;
		this.controller.addObserver(this);
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.panelBounds);
		setOpaque(false);

		UIManager.put("Table.font", FontManager.fontMenuTable);
		UIManager.put("TableHeader.font", FontManager.fontMenuTable);
		UIManager.put("Menu.font", FontManager.fontMenu);
		UIManager.put("MenuItem.font", FontManager.fontMenu);
		UIManager.put("TextField.font", FontManager.fontLabel);
		UIManager.put("ComboBox.font", FontManager.fontLabel);
		UIManager.put("OptionPane.minimumSize", SizeManager.optionPaneDimension);

		DefaultTableCellRenderer tableRender = new DefaultTableCellRenderer();
		tableRender.setHorizontalAlignment(SwingConstants.CENTER);
		tableRender.setVerticalAlignment(SwingConstants.CENTER);

		String courseString = courseData[0] + "\n" + courseData[1] + "\n" + courseData[2] + "\n\n";

		String[] menuName = { "  File  ", "  Edit  ", "  Grade  " };
		String[][] menuItemName = {
				{ "Add Student", "Add Students from File", "Add Category", "Add Item", null, "Back", "Exit" },
				{ "Edit Student", "Edit Category", "Edit Item" },
				{ "Edit All Grades", "Edit by Student", "View Grade" } };
		ActionListener[][] menuActionListener = { // TODO complete menu action
				{ // File
						addStudent -> { // Add Student
							try {
								JTextField nameField = new JTextField();
								JTextField BUIDField = new JTextField();
								JTextField emailField = new JTextField();
								JComboBox<String> levelCombo = new JComboBox<>(
										new String[] { "Undergraduate", "Graduate" });
								Object[] fields = { "Name: ", nameField, "BU ID: ", BUIDField, "Email: ", emailField,
										"Level: ", levelCombo, };

								while (true) {
									int reply = JOptionPane.showConfirmDialog(null, fields, "Add Student",
											JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {

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

							// invoke the showsOpenDialog function to show the save dialog
							int r = j.showOpenDialog(null);

							// if the user selects a file
							if (r == JFileChooser.APPROVE_OPTION)

							{
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
								Object[] fields = { "Category: ", categoryField, "Percentage: ", percentageField, };

								while (true) {
									int reply = JOptionPane.showConfirmDialog(this, fields, "Add Category",
											JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {
										JOptionPane.showMessageDialog(this,
												"Please edit the percentage for all other categories", "Warning",
												JOptionPane.WARNING_MESSAGE);
										String fieldName = categoryField.getText();
										try {
											double weight = Double.parseDouble(percentageField.getText());
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
								Object[] fields = { "Category: ", categoryCombo, "Item: ", itemField, "Percentage: ",
										percentageField, "Max Points: ", maxPointsField, };

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
											double itemWeight = Double.parseDouble(percentageField.getText());
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
						exit -> System.exit(0) },
				{ editStudent -> { // Edit Student
					try {
						String[] studentDataForCombo = controller.getCurrentCourse().getStudentNamesAsList();
						studentComboModel = new DefaultComboBoxModel(studentDataForCombo);
						studentComboEdit = new JComboBox<>(studentComboModel);

						JTextField BUIDField = new JTextField();
						JTextField nameField = new JTextField();
						JTextField emailField = new JTextField();
						JComboBox<String> levelCombo = new JComboBox<String>(
								new String[] { "Undergraduate", "Graduate" });
						Object[] fields = { "Student: ", studentComboEdit, "BU ID: ", BUIDField, "Name:", nameField,
								"Email: ", emailField, "Level: ", levelCombo, };

						while (true) {
							int reply = JOptionPane.showConfirmDialog(this, fields, "Edit Student",
									JOptionPane.OK_CANCEL_OPTION);
							if (reply == JOptionPane.OK_OPTION) {
								int chosenIndex = studentComboEdit.getSelectedIndex();
								if (chosenIndex != -1) {

									String buid = BUIDField.getText().trim();
									String name = nameField.getText().trim();
									String email = emailField.getText().trim();
									String level = (String) levelCombo.getSelectedItem();

									if (buid.equals("") && email.equals("")) {
										return;
									}
									HashMap<String, String> map = new HashMap<String, String>();

									map.put("Buid", buid);
									map.put("Name", name);
									map.put("Email", email);
									map.put("Type", level);

									controller.modifyStudentForCourse(controller.getCurrentCourse(), chosenIndex, map);
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

						String[] categoryColumn = { "Category", "Percentage" };
						DefaultTableModel tableModel = new DefaultTableModel(categoryData, categoryColumn);
						JTable categoryTable = new JTable(tableModel) {
							public boolean isCellEditable(int row, int column) {
								return column > 0;
							}
						};
						categoryTable.setDefaultRenderer(Object.class, tableRender);
						categoryTable.setRowHeight(SizeManager.tableRowHeight);
						JScrollPane categoryScrollPane = new JScrollPane(categoryTable);

						while (true) {
							int reply = JOptionPane.showConfirmDialog(this, categoryScrollPane, "Edit Category",
									JOptionPane.OK_CANCEL_OPTION);
							boolean flag = false;
							ArrayList<HashMap<String, Double>> modifiedData = new ArrayList<HashMap<String, Double>>();
							if (reply == JOptionPane.OK_OPTION) {

								for (int i = 0; i < tableModel.getRowCount(); i++) {
									HashMap<String, Double> m = new HashMap<String, Double>();
									String key = (String) tableModel.getValueAt(i, 0);
									key = key.trim();
									if (!key.equals("")) {
										try {
											double value = Double.parseDouble((String) tableModel.getValueAt(i, 1));
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
								return;
							}
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}, editItem -> { // Edit Item
					try {
						JComboBox<String> categoryCombo = new JComboBox<>();

						ArrayList<Category> categories = controller.getCurrentCourse().getCategories();
						for (int i = 0; i < categories.size(); i++) {
							categoryCombo.addItem(categories.get(i).getFieldName());
						}

						String[][] itemData;
						if (categoryCombo.getSelectedIndex() < 0)
							return;

						Category chosenCategory = categories.get(categoryCombo.getSelectedIndex());
						itemData = controller.getItemDetailsForCourseCategory(controller.getCurrentCourse(),
								categoryCombo.getSelectedIndex(), true);
						String[] itemColumn = { "Item", "Percentage", "Max Points" };
						DefaultTableModel tableModel = new DefaultTableModel(itemData, itemColumn);

						JTable itemTable = new JTable(tableModel) {
							public boolean isCellEditable(int row, int column) {
								return column > 0;
							}
						};
						itemTable.setRowHeight(SizeManager.tableRowHeight);
						itemTable.setDefaultRenderer(Object.class, tableRender);
						JScrollPane itemScrollPane = new JScrollPane(itemTable);
						Object[] fields = { "Category: ", categoryCombo, "Item: ", itemScrollPane, };

						while (true) {
							int reply = JOptionPane.showConfirmDialog(this, fields, "Edit Item",
									JOptionPane.OK_CANCEL_OPTION);
							boolean flag = false;
							HashMap<String, ArrayList<Double>> map = new HashMap<String, ArrayList<Double>>();
							if (reply == JOptionPane.OK_OPTION) {
								for (int i = 0; i < tableModel.getRowCount(); i++) {
									String key = (String) tableModel.getValueAt(i, 0);
									ArrayList<Double> l = new ArrayList<Double>();
									for (int j = 1; j < tableModel.getColumnCount(); j++) {
										String str = (String) tableModel.getValueAt(i, j);
										if (str.trim().equals("")) {
											flag = true;
											break;
										}
										try {
											double value = Double.parseDouble((String) tableModel.getValueAt(i, j));
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
									controller.modifyItemsForCourseCategory(controller.getCurrentCourse(),
											categoryCombo.getSelectedIndex(), map);
									break;
								}

							} else {
								return;
							}
						}
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} }, { editAllGrades -> { // Edit All Grades
					frame.changePanel(this, new GradePanel(frame, courseData, true, this.controller));
				}, editByStudent -> { // Edit by Student
					try {
						String[] studentComboBoxItems = { "Student 1", "Student 2", "Student 3" }; // TODO test data,
																									// need to be
																									// replaced when
																									// database exists
						JComboBox<String> studentComboBox = new JComboBox<>(studentComboBoxItems);
						JComboBox<String> categoryComboBox = new JComboBox<>(new String[] { "Homework", "Exam" });
						JComboBox<String> itemComboBox = new JComboBox<>(
								new String[] { "Homework 1", "Homework 2", "Midterm", "Exam" });
						JTextField gradeTextField = new JTextField();
						Object[] fields = { "Student: ", studentComboBox, "Category: ", categoryComboBox, "Item: ",
								itemComboBox, "Grade: ", gradeTextField, };
						while (true) {
							int reply = JOptionPane.showConfirmDialog(this, fields, "Edit by Student",
									JOptionPane.OK_CANCEL_OPTION);
							if (reply == JOptionPane.OK_OPTION) {
								break;
							} else {
								return;
							}
						}
					} catch (Exception e) {
						JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}, viewGrades -> { // View Grades
					frame.changePanel(this, new GradePanel(frame, courseData, false, this.controller));
				} } };

		JMenuBar menuBar = new JMenuBar();
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
		informationTextPane.setText(courseString); // TODO load info from database
		informationTextPane.setFont(FontManager.fontText);
		informationTextPane.setEditable(false);
		informationTextPane.setBorder(new LineBorder(ColorManager.primaryColor, SizeManager.lineThickness));
		StyledDocument doc = informationTextPane.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		add(informationTextPane);

		tableStudentColumns = new String[] { "Student Name", "Student ID" };
		String[][] tableStudentData; // TODO load student from database
		tableStudentData = controller.getAllStudentsForCourse(controller.getCurrentCourse());

		studentTableModel = new DefaultTableModel(tableStudentData, tableStudentColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableStudent = new JTable(studentTableModel);
		tableStudent.setRowHeight(SizeManager.menuTableRowHeight);
		JScrollPane tableStudentScrollPane = new JScrollPane(tableStudent);
		tableStudentScrollPane.setBounds(SizeManager.tableStudentBounds);
		add(tableStudentScrollPane);

		tableCategoryColumns = new String[] { "Category Name", "Weight" };

		String[][] tableCategoryData = controller.getCurrentCourse().getCategoryDataForList();
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

		tableItemColumns = new String[] { "Item Name", "Weight" };
		String[][] tableItemData = controller.getAllItemsDetailsForCourse(controller.getCurrentCourse());
		itemTableModel = new DefaultTableModel(tableItemData, tableItemColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableItem = new JTable(itemTableModel);
		JScrollPane tableItemScrollPane = new JScrollPane(tableItem);
		tableItemScrollPane.setBounds(SizeManager.tableItemBounds);
		add(tableItemScrollPane);

		for (int i = 0; i < 2; ++i) {
			tableCategory.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
			tableItem.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
		}
		for (JTable table : new JTable[] { tableStudent, tableCategory, tableItem }) {
			table.setDefaultRenderer(Object.class, tableRender);
			table.setRowHeight(SizeManager.menuTableRowHeight);
			table.setRowSelectionAllowed(true);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(listSelectionEvent -> {
				String text = courseString;
				if (tableStudent.getSelectedRow() != -1) {

					text += controller.getCurrentCourse().getStudent(tableStudent.getSelectedRow()).getStudentDetails()
							+ "\n\n";
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
					text += tableCategory.getValueAt(tableCategory.getSelectedRow(), 1) + "\n\n";
				}
				if (tableItem.getSelectedRow() != -1) {
					text += "Item: ";
					text += tableItem.getValueAt(tableItem.getSelectedRow(), 0) + "\n";
					text += "Weight: ";
					text += tableItem.getValueAt(tableItem.getSelectedRow(), 1) + "\n\n";
				}
				informationTextPane.setText(text);
			});

			JTableHeader tableHeader = table.getTableHeader();
			tableHeader.setPreferredSize(new Dimension(SizeManager.panelWidth, SizeManager.menuTableRowHeight));
			tableHeader.setForeground(ColorManager.lightColor);
			tableHeader.setBackground(ColorManager.primaryColor);
		}

		setVisible(true);
	}

	public ArrayList<HashMap<String, String>> processCsvFile(String filePath) {
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

		this.controller = controller;

		String[][] tableCategoryData = controller.getCurrentCourse().getCategoryDataForList();
		categoryTableModel = new DefaultTableModel(tableCategoryData, tableCategoryColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCategory.setModel(categoryTableModel);

		String[][] tableItemData = controller.getAllItemsDetailsForCourse(controller.getCurrentCourse());
		itemTableModel = new DefaultTableModel(tableItemData, tableItemColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableItem.setModel(itemTableModel);

		String[][] tableStudentData; // TODO load student from database
		tableStudentData = controller.getAllStudentsForCourse(controller.getCurrentCourse());

		studentTableModel = new DefaultTableModel(tableStudentData, tableStudentColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableStudent.setModel(studentTableModel);
		
		String[] studentDataForCombo = controller.getCurrentCourse().getStudentNamesAsList();
		studentComboModel = new DefaultComboBoxModel(studentDataForCombo);
		studentComboEdit.setModel(studentComboModel);
	}
}