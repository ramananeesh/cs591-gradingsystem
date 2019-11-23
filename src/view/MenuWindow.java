package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuWindow extends JFrame {
	private static final String TITLE = "Grading System - Main Menu";

	public MenuWindow(String[] courseData) { // TODO data should not be String array
		setTitle(TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setBounds(SizeManager.windowBounds);

		String[] menuName = {"  File  ", "  Edit  ", "  Grade  "};
		String[][] menuItemName = {
				{"Add Student", "Add Category", "Add Item", "|", "Back", "Exit"},
				{"Edit Student", "Edit Category", "Edit Student"},
				{"Edit All Grades", "Edit by Student", "View Grade"}
		};
		ActionListener[][] menuActionListener = { // TODO complete menu action
				{ // File
						e -> { // Add Student
							System.exit(0);

						},
						e -> { // Add Category
							System.exit(0);

						},
						e -> { // Add Item
							System.exit(0);

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

						},
						e -> { // Edit Category

						},
						e -> { // Edit Item

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

		UIManager.put("Menu.font", FontManager.fontMenu);
		UIManager.put("MenuItem.font", FontManager.fontMenu);
		JMenuBar menuBar = new JMenuBar();
		menuBar.setLayout(new GridBagLayout());
		for (int i = 0; i < menuName.length; ++i) {
			JMenu menu = new JMenu(menuName[i]);
			for (int j = 0; j < menuItemName[i].length; ++j) {
				if (menuItemName[i][j].equals("|")) {
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
		textInfo.setText(courseData[0] + "\n" + courseData[1] + "\n" + courseData[2]); // TODO load info from database
		textInfo.setFont(FontManager.fontText);
		textInfo.setEditable(false);
		textInfo.setBorder(new LineBorder(ColorManager.primaryColor, SizeManager.lineThickness));
		StyledDocument doc = textInfo.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		add(textInfo);

		UIManager.put("Table.font", FontManager.fontMenuTable);
		UIManager.put("TableHeader.font", FontManager.fontMenuTable);

		String[] tableStudentColumn = {"Student"};
		String[][] tableStudentData; // TODO load student from database
		tableStudentData = new String[100][1];
		for (int i = 0; i < 100; ++i) {
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

		String[] tableCategoryColumn = {"Category"};
		String[][] tableCategoryData; // TODO load Category from database
		tableCategoryData = new String[100][1];
		for (int i = 0; i < 100; ++i) {
			tableCategoryData[i][0] = String.format("Category %010d", i + 1);
		}
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

		String[] tableItemColumn = {"Item"};
		String[][] tableItemData; // TODO load Item from database
		tableItemData = new String[100][1];
		for (int i = 0; i < 100; ++i) {
			tableItemData[i][0] = String.format("Item %010d", i + 1);
		}
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

		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		render.setVerticalAlignment(SwingConstants.CENTER);
		for (JTable table : new JTable[]{tableStudent, tableCategory, tableItem}) {
			table.setDefaultRenderer(Object.class, render);
			table.setRowHeight(SizeManager.menuTableRowHeight);
			table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
				@Override
				public void valueChanged(ListSelectionEvent e) {
					// TODO
				}
			});

			JTableHeader tableHeader = table.getTableHeader();
			tableHeader.setPreferredSize(new Dimension(SizeManager.panelWidth, SizeManager.menuTableRowHeight));
			tableHeader.setForeground(ColorManager.lightColor);
			tableHeader.setBackground(ColorManager.primaryColor);
		}

		setVisible(true);
	}
}