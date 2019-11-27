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

/**
 * The {@code MenuPanel} class represents the panel for viewing or modifying all the grades
 */
public class MenuPanel extends JPanel {
	/** The title for the window when MenuPanel displays */
	private static final String TITLE = "Grading System - Main Menu";

	/** The frame which contains this panel. */
	private MainFrame frame;

	/**
	 * Initializes a newly created {@code MenuPanel} object
	 */
	public MenuPanel(MainFrame frame, String[] courseData) { // TODO data should not be String array
		this.frame = frame;
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

		String courseString = courseData[0] + "\n" + courseData[1] + "\n" + courseData[2] + "\n\n"; // TODO test data, need to be replaced when database exists

		String[] menuName = {"  File  ", "  Edit  ", "  Grade  "};
		String[][] menuItemName = {
				{"Add Student", "Add Category", "Add Item", null, "Back", "Exit"},
				{"Edit Student", "Edit Category", "Edit Item"},
				{"Edit All Grades", "Edit by Student", "View Grade"}
		};
		ActionListener[][] menuActionListener = { // TODO complete menu action
				{ // File
						addStudent -> { // Add Student
							try {
								JTextField nameTextField = new JTextField();
								JTextField buidTextField = new JTextField();
								JTextField emailTextField = new JTextField();
								JComboBox<String> levelComboBox = new JComboBox<>(new String[]{"Undergraduate", "Graduate"});
								Object[] fields = {"Name: ", nameTextField, "BU ID: ", buidTextField, "Email: ", emailTextField, "Level: ", levelComboBox,};

								while (true) {
									int reply = JOptionPane.showConfirmDialog(this, fields, "Add Student", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {

										break;
									} else {
										return;
									}
								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
							}
						},
						addCategory -> { // Add Category
							try {
								JTextField categoryTextField = new JTextField();
								JTextField weightField = new JTextField();
								Object[] fields = {"Category: ", categoryTextField, "Weight: ", weightField,};

								while (true) {
									int reply = JOptionPane.showConfirmDialog(this, fields, "Add Category", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {
										JOptionPane.showMessageDialog(this, "Please edit the percentage for all other categories", "Warning", JOptionPane.WARNING_MESSAGE);
										break;
									} else {
										return;
									}
								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
							}
						},
						addItem -> { // Add Item
							try {
								String[] categoryComboBoxItems = {"Homework", "Exam", "Project"}; // TODO test data, need to be replaced when database exists
								JComboBox<String> categoryCombo = new JComboBox<>(categoryComboBoxItems);
								JTextField itemField = new JTextField();
								JTextField percentageField = new JTextField();
								Object[] fields = {"Category: ", categoryCombo, "Item: ", itemField, "Weight: ", percentageField,};

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
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
							}

						},
						null, // Separator
						back -> frame.changePanel(this, new CoursePanel(frame)), // Back
						exit -> System.exit(0)
				},
				{
						editStudent -> { // Edit Student
							try {
								String[] studentComboBoxItems = {"Student 1", "Student 2", "Student 3"}; // TODO test data, need to be replaced when database exists
								JComboBox<String> studentCombo = new JComboBox<>(studentComboBoxItems);
								JTextField buidTextField = new JTextField();
								JTextField emailTextField = new JTextField();
								JComboBox<String> levelComboBox = new JComboBox<>(new String[]{"Undergraduate", "Graduate"});
								Object[] fields = {"Student: ", studentCombo, "BU ID: ", buidTextField, "Email: ", emailTextField, "Level: ", levelComboBox,};

								while (true) {
									int reply = JOptionPane.showConfirmDialog(this, fields, "Edit Student", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {
										break;
									} else {
										return;
									}
								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
							}
						},
						editCategory -> { // Edit Category
							try {
								String[][] categoryRowData = { // TODO test data, need to be replaced when database exists
										{"Homework", "50%",},
										{"Exam", "25%",},
										{"Project", "25%",}
								};
								String[] categoryColumnNames = {"Category", "Weight"};
								JTable categoryTable = new JTable(categoryRowData, categoryColumnNames) {
									public boolean isCellEditable(int row, int column) {
										return column > 0;
									}
								};
								categoryTable.setDefaultRenderer(Object.class, tableRender);
								categoryTable.setRowHeight(SizeManager.tableRowHeight);
								JScrollPane categoryScrollPane = new JScrollPane(categoryTable);

								while (true) {
									int reply = JOptionPane.showConfirmDialog(this, categoryScrollPane, "Edit Category", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {
										break;
									} else {
										return;
									}
								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this, "Error", "Error", JOptionPane.ERROR_MESSAGE);
							}
						},
						editItem -> { // Edit Item
							try {
								String[] categoryComboBoxItems = {"Homework", "Exam", "Project"};
								JComboBox<String> categoryCombo = new JComboBox<>(categoryComboBoxItems);
								String[][] itemRowData = { // TODO test data, need to be replaced when database exists
										{"Homework 1", "50%",},
										{"Homework 2", "50%",}
								};
								String[] itemColumnNames = {"Item", "Weight"};
								JTable itemTable = new JTable(itemRowData, itemColumnNames) {
									public boolean isCellEditable(int row, int column) {
										return column > 0;
									}
								};
								itemTable.setRowHeight(SizeManager.tableRowHeight);
								itemTable.setDefaultRenderer(Object.class, tableRender);
								JScrollPane itemScrollPane = new JScrollPane(itemTable);
								Object[] fields = {"Category: ", categoryCombo, "Item: ", itemScrollPane,};


								while (true) {
									int reply = JOptionPane.showConfirmDialog(this, fields, "Edit Item", JOptionPane.OK_CANCEL_OPTION);
									if (reply == JOptionPane.OK_OPTION) {

										break;
									} else {
										return;
									}
								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(this,
										"Error", "Error",
										JOptionPane.ERROR_MESSAGE);
							}
						}
				},
				{
						editAllGrades -> { // Edit All Grades
							frame.changePanel(this, new GradePanel(frame, courseData, true));
						},
						editByStudent -> { // Edit by Student
							// TODO complete
						},
						viewGrades -> { // View Grades
							frame.changePanel(this, new GradePanel(frame, courseData, false));
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

		String[] studentTableColumnNames = {"Student"};
		String[][] studentTableRowData; // TODO load student from database
		studentTableRowData = new String[100][1];
		for (int i = 0; i < 100; ++i) {
			studentTableRowData[i][0] = String.format("Student %010d", i + 1);
		}
		DefaultTableModel studentTableModel = new DefaultTableModel(studentTableRowData, studentTableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable studentTable = new JTable(studentTableModel);
		studentTable.setRowHeight(SizeManager.menuTableRowHeight);
		JScrollPane studentTableScrollPane = new JScrollPane(studentTable);
		studentTableScrollPane.setBounds(SizeManager.tableStudentBounds);
		add(studentTableScrollPane);

		String[] categoryTableColumnNames = {"Category", "Weight"};
		String[][] categoryTableRowData = { // TODO
				{"All", "100%"},
				{"Homework", "25%"},
				{"Project", "25%"},
				{"Presentation", "25%"},
				{"Exam", "25%"}
		};
		DefaultTableModel categoryTableModel = new DefaultTableModel(categoryTableRowData, categoryTableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable categoryTable = new JTable(categoryTableModel);
		JScrollPane categoryTableScrollPane = new JScrollPane(categoryTable);
		categoryTableScrollPane.setBounds(SizeManager.tableCategoryBounds);
		add(categoryTableScrollPane);

		String[] itemTableColumnNames = {"Item", "Weight"};
		String[][] itemTableRowData = { // TODO
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
		DefaultTableModel itemTableModel = new DefaultTableModel(itemTableRowData, itemTableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable itemTable = new JTable(itemTableModel);
		JScrollPane itemTableScrollPane = new JScrollPane(itemTable);
		itemTableScrollPane.setBounds(SizeManager.tableItemBounds);
		add(itemTableScrollPane);

		for (int i = 0; i < 2; ++i) {
			categoryTable.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
			itemTable.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
		}
		for (JTable table : new JTable[]{studentTable, categoryTable, itemTable}) {
			table.setDefaultRenderer(Object.class, tableRender);
			table.setRowHeight(SizeManager.menuTableRowHeight);
			table.setRowSelectionAllowed(true);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.getSelectionModel().addListSelectionListener(listSelectionEvent -> { // TODO test data
				String text = courseString;
				if (studentTable.getSelectedRow() != -1) {
					text += studentTable.getValueAt(studentTable.getSelectedRow(), 0) + "\n\n";
				}
				if (categoryTable.getSelectedRow() != -1) {
					if (listSelectionEvent.getSource() == categoryTable.getSelectionModel()) {
						Object[][] newTableItemData;
						if (categoryTable.getSelectedRow() == 0) {
							newTableItemData = itemTableRowData;
						} else {
							newTableItemData = new Object[5][2];
							String category = (String) categoryTable.getValueAt(categoryTable.getSelectedRow(), 0);
							for (int i = 0; i < 5; ++i) {
								newTableItemData[i][0] = category + " " + (i + 1);
								newTableItemData[i][1] = "5%";
							}
						}
						DefaultTableModel newTableItemModel = new DefaultTableModel(newTableItemData, itemTableColumnNames);
						itemTable.setModel(newTableItemModel);
						for (int i = 0; i < 2; ++i) {
							categoryTable.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
							itemTable.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.tableCategoryItemColumnWidth[i]);
						}
					}
					text += "Category: ";
					text += categoryTable.getValueAt(categoryTable.getSelectedRow(), 0) + "\n";
					text += "Weight: ";
					text += categoryTable.getValueAt(categoryTable.getSelectedRow(), 1) + "\n\n";
				}
				if (itemTable.getSelectedRow() != -1) {
					text += "Item: ";
					text += itemTable.getValueAt(itemTable.getSelectedRow(), 0) + "\n";
					text += "Weight: ";
					text += itemTable.getValueAt(itemTable.getSelectedRow(), 1) + "\n\n";
				}
				textInfo.setText(text);
			});
			JTableHeader tableHeader = table.getTableHeader();
			tableHeader.setPreferredSize(new Dimension(SizeManager.panelWidth, SizeManager.menuTableRowHeight));
			tableHeader.setForeground(ColorManager.lightColor);
			tableHeader.setBackground(ColorManager.primaryColor);
		}

		setVisible(true);
	}
}