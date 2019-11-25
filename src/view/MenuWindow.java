package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

public class MenuWindow extends JFrame {
	private static final String TITLE = "Grading System - Main Menu";

	public MenuWindow(String[] courseData) { // TODO data should not be String array
		setTitle(TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setBounds(SizeManager.windowBounds);

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

		String[] menuName = {"  File  ", "  Edit  ", "  Grade  "};
		String[][] menuItemName = {
				{"Add Student", "Add Category", "Add Item", null, "Back", "Exit"},
				{"Edit Student", "Edit Category", "Edit Item"},
				{"Edit All Grades", "Edit by Student", "View Grade"}
		};
		ActionListener[][] menuActionListener = { // TODO complete menu action
				{ // File
						e -> { // Add Student
							try {
								JTextField nameField = new JTextField();
								JTextField BUIDField = new JTextField();
								JTextField emailField = new JTextField();
								JComboBox<String> levelCombo = new JComboBox<String>(new String[]{"Undergraduate", "Graduate"});
								Object[] fields = {"Name: ", nameField, "BU ID: ", BUIDField, "Email: ", emailField, "Level: ", levelCombo,};

								while (true) {
									int reply = JOptionPane.showConfirmDialog(null, fields, "Add Student", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {

										break;
									} else {
										return;
									}
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null,
										"Error", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						},
						e -> { // Add Category
							try {
								JTextField categoryField = new JTextField();
								JTextField percentageField = new JTextField();
								Object[] fields = {"Category: ", categoryField, "Percentage: ", percentageField,};

								while (true) {
									int reply = JOptionPane.showConfirmDialog(null, fields, "Add Category", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {
										JOptionPane.showMessageDialog(null,
												"Please edit the percentage for all other categories", "Warning",
												JOptionPane.WARNING_MESSAGE);
										break;
									} else {
										return;
									}
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null,
										"Error", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						},
						e -> { // Add Item
							try {
								JComboBox<String> categoryCombo = new JComboBox<String>();
								/***for test***/
								categoryCombo.addItem("Homework");
								categoryCombo.addItem("Exam");
								categoryCombo.addItem("Project");
								/******/
//								for(int i = 0; i < category.size(); i++) {
//									categoryCombo.addItem(category.get(i).getCategoryName());
//								}
								JTextField itemField = new JTextField();
								JTextField percentageField = new JTextField();
								Object[] fields = {"Category: ", categoryCombo, "Item: ", itemField, "Percentage: ", percentageField,};

								while (true) {
									int reply = JOptionPane.showConfirmDialog(null, fields, "Add Item", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {

										JOptionPane.showMessageDialog(null,
												"Please edit the percentage for all other items", "Warning",
												JOptionPane.WARNING_MESSAGE);
										break;
									} else {
										return;
									}
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null,
										"Error", "Error",
										JOptionPane.ERROR_MESSAGE);
							}

						},
						null, // Separator
						e -> { // Back
							new CourseWindow();
							dispose();
						},
						e -> {
							System.exit(0);
						}
				},
				{
						e -> { // Edit Student
							try {
								JComboBox<String> studentCombo = new JComboBox<String>();
								/***for test***/
								studentCombo.addItem("Student1");
								studentCombo.addItem("Student2");
								studentCombo.addItem("Student3");
								/******/
//								for(int i = 0; i < student.size(); i++) {
//									studentCombo.addItem(student.get(i).getStudentName());
//								}
								JTextField BUIDField = new JTextField();
								JTextField emailField = new JTextField();
								JComboBox<String> levelCombo = new JComboBox<String>(new String[]{"Undergraduate", "Graduate"});
								Object[] fields = {"Student: ", studentCombo, "BU ID: ", BUIDField, "Email: ", emailField, "Level: ", levelCombo,};

								while (true) {
									int reply = JOptionPane.showConfirmDialog(null, fields, "Edit Student", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {

										break;
									} else {
										return;
									}
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null,
										"Error", "Error",
										JOptionPane.ERROR_MESSAGE);
							}

						},
						e -> { // Edit Category
							try {
								String[][] categoryData;

//								for(int i = 0; i < category.size(); i++) {
//									categoryData[i][0] = category.get(i).getCategoryName();
//									categoryData[i][1] = category.get(i).getPercentage();
//								}
								/***for test***/
								categoryData = new String[][]{
										{"Homework", "50%",},
										{"Exam", "25%",},
										{"Project", "25%",}
								};
								/******/

								String[] categoryColumn = {"Category", "Percentage"};
								JTable categoryTable = new JTable(categoryData, categoryColumn) {
									public boolean isCellEditable(int row, int column) {
										return column > 0;
									}
								};
								categoryTable.setDefaultRenderer(Object.class, tableRender);
								categoryTable.setRowHeight(SizeManager.tableRowHeight);
								JScrollPane categoryScrollPane = new JScrollPane(categoryTable);

								while (true) {
									int reply = JOptionPane.showConfirmDialog(null, categoryScrollPane, "Edit Category", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {

										break;
									} else {
										return;
									}
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null,
										"Error", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						},

						e -> { // Edit Item
							try {
								JComboBox<String> categoryCombo = new JComboBox<String>();
								/***for test***/
								categoryCombo.addItem("Homework");
								categoryCombo.addItem("Exam");
								categoryCombo.addItem("Project");
								/******/
//								for(int i = 0; i < category.size(); i++) {
//									categoryCombo.addItem(category.get(i).getCategoryName());
//								}

								String[][] itemData;
//								for(int i = 0; i < category.size(); i++) {
//									data[i][0] = category.get(i).getCategoryName();
//									data[i][1] = category.get(i).getPercentage();
//								}
								/***for test***/
								itemData = new String[][]{
										{"Homework1", "50%",},
										{"Homework2", "50%",}
								};
								/******/

								String[] itemColumn = {"Item", "Percentage"};
								JTable itemTable = new JTable(itemData, itemColumn) {
									public boolean isCellEditable(int row, int column) {
										return column > 0;
									}
								};
								itemTable.setRowHeight(SizeManager.tableRowHeight);
								itemTable.setDefaultRenderer(Object.class, tableRender);
								JScrollPane itemScrollPane = new JScrollPane(itemTable);
								Object[] fields = {"Category: ", categoryCombo, "Item: ", itemScrollPane,};


								while (true) {
									int reply = JOptionPane.showConfirmDialog(null, fields, "Edit Item", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {

										break;
									} else {
										return;
									}
								}
							} catch (Exception e1) {
								JOptionPane.showMessageDialog(null,
										"Error", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
				},
				{
						e -> { // Edit All Grades
							new GradeWindow(courseData);
							dispose();
						},
						e -> { // Edit by Student

						},
						e -> { // View Grades

						}
				}
		};

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

		JTextPane textInfo = new JTextPane();
		textInfo.setBounds(SizeManager.textInfoBounds);
		textInfo.setText(courseString); // TODO load info from database
		textInfo.setFont(FontManager.fontText);
		textInfo.setEditable(false);
		textInfo.setBorder(new LineBorder(ColorManager.primaryColor, SizeManager.lineThickness));
		StyledDocument doc = textInfo.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		add(textInfo);


		Random random = new Random();

		String[] tableStudentColumn = {"Student"};
		String[][] tableStudentData; // TODO load student from database
		tableStudentData = new String[100][1];
		for (int i = 0; i < 100; ++i) {
			String randomName = "";
			tableStudentData[i][0] = String.format("Student %010d", i + 1);
		}
		DefaultTableModel modelStudent = new DefaultTableModel(tableStudentData, tableStudentColumn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable tableStudent = new JTable(modelStudent);
		tableStudent.setRowHeight(SizeManager.menuTableRowHeight);
		JScrollPane tableStudentScrollPane = new JScrollPane(tableStudent);
		tableStudentScrollPane.setBounds(SizeManager.tableStudentBounds);
		add(tableStudentScrollPane);

		String[] tableCategoryColumn = {"Category Name", "Weight"}; // TODO load Category from database
		String[][] tableCategoryData = {
				{"All", "100%"},
				{"Homework", "25%"},
				{"Project", "25%"},
				{"Presentation", "25%"},
				{"Exam", "25%"}
		};
//		String[][] tableCategoryData = new String[100][2];
//		for (int i = 0; i < 100; ++i) {
//			tableCategoryData[i][0] = String.format("Category %010d", i + 1);
//			tableCategoryData[i][1] = String.format("%d%%", random.nextInt(100));
//		}
		DefaultTableModel modelCategory = new DefaultTableModel(tableCategoryData, tableCategoryColumn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable tableCategory = new JTable(modelCategory);
		JScrollPane tableCategoryScrollPane = new JScrollPane(tableCategory);
		tableCategoryScrollPane.setBounds(SizeManager.tableCategoryBounds);
		add(tableCategoryScrollPane);

		String[] tableItemColumn = {"Item Name", "Weight"};
		String[][] tableItemData = {
				{"All", "100%"},
				{"Homework 1", "5%"},
				{"Homework 2", "5%"},
				{"Homework 3", "5%"},
				{"Homework 4", "5%"},
				{"Homework 5", "5%"},
				{"Project 1", "5%"},
				{"Project 2", "5%"},
				{"Project 3", "5%"},
				{"Project 4", "5%"},
				{"Project 5", "5%"},
				{"Presentation 1", "5%"},
				{"Presentation 2", "5%"},
				{"Presentation 3", "5%"},
				{"Presentation 4", "5%"},
				{"Presentation 5", "5%"},
				{"Exam 1", "5%"},
				{"Exam 2", "5%"},
				{"Exam 3", "5%"},
				{"Exam 4", "5%"},
				{"Exam 5", "5%"}
		};
//		String[][] tableItemData; // TODO load Item from database
//		tableItemData = new String[100][2];
//		for (int i = 0; i < 100; ++i) {
//			tableItemData[i][0] = String.format("Item %010d", i + 1);
//			tableItemData[i][1] = String.format("%d%%", random.nextInt(100));
//		}
		DefaultTableModel modelItem = new DefaultTableModel(tableItemData, tableItemColumn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable tableItem = new JTable(modelItem);
		JScrollPane tableItemScrollPane = new JScrollPane(tableItem);
		tableItemScrollPane.setBounds(SizeManager.tableItemBounds);
		add(tableItemScrollPane);

		for (int i = 0; i < 2; ++i) {
			tableCategory.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumWidth[i]);
			tableItem.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumWidth[i]);
		}
		final int[] selectedRow = {-1, -1, -1};
		for (JTable table : new JTable[]{tableStudent, tableCategory, tableItem}) {
			table.setDefaultRenderer(Object.class, tableRender);
			table.setRowHeight(SizeManager.menuTableRowHeight);
			table.setRowSelectionAllowed(true);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(e -> {
				String text = courseString;
				if (tableStudent.getSelectedRow() != -1) {
					text += tableStudent.getValueAt(tableStudent.getSelectedRow(), 0) + "\n\n";
				}
				if (tableCategory.getSelectedRow() != -1) {
					if (tableCategory.getSelectedRow() != selectedRow[1]) {
						Object[][] newTableItemData;
						if (tableCategory.getSelectedRow() == 0) {
							newTableItemData = tableItemData;
						} else {
							newTableItemData = new Object[5][2];
							String category = (String) tableCategory.getValueAt(tableCategory.getSelectedRow(), 0);
							for (int i = 0; i < 5; ++i) {
								newTableItemData[i][0] = category + " " + (i + 1);
								newTableItemData[i][1] = "5%";
							}
						}
						DefaultTableModel newTableItemModel = new DefaultTableModel(newTableItemData, tableItemColumn);
						tableItem.setModel(newTableItemModel);
						for (int i = 0; i < 2; ++i) {
							tableCategory.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumWidth[i]);
							tableItem.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumWidth[i]);
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

				textInfo.setText(text);

				selectedRow[0] = tableStudent.getSelectedRow();
				selectedRow[1] = tableCategory.getSelectedRow();
				selectedRow[2] = tableItem.getSelectedRow();			});

			JTableHeader tableHeader = table.getTableHeader();
			tableHeader.setPreferredSize(new Dimension(SizeManager.panelWidth, SizeManager.menuTableRowHeight));
			tableHeader.setForeground(ColorManager.lightColor);
			tableHeader.setBackground(ColorManager.primaryColor);
		}

		setVisible(true);
	}
}