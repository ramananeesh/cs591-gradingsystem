package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.Master;
import model.Category;
import model.Course;
import model.CourseStudent;
import model.Item;

import java.util.*;

/**
 * The {@code GradePanel} class represents the panel for viewing or modifying
 * all the grades
 */
public class GradePanel extends JPanel {
	/** The title for the window when GradePanel displays */
	private static final String TITLE = "Grading System - Grade";
	private Master controller;

	/** The frame which contains this panel. */
	private MainFrame frame;
	private DefaultTableModel gradeTableModel;
	private JTable gradeTable;
	private JComboBox<String> categoryComboBox;
	private JComboBox<String> itemComboBox;
	private JComboBox<String> gradeOptionsComboBox;

	/**
	 * Initializes a newly created {@code GradePanel} object
	 */
	public GradePanel(MainFrame frame, String[] courseData, boolean editable, Master controller) {
		this.frame = frame;
		this.controller = controller;
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.panelBounds);
		setOpaque(false);

		// grade table
		List<String> gradeTableColumnNamesList = new ArrayList<>();
		List<Category> categories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
		List<String> categoryNames = new ArrayList<>();
		for (Category c : categories) {
			categoryNames.add(c.getFieldName());
		}

		Course currCourse = controller.getCurrentCourse();
		ArrayList<String> allItemNames = controller.getAllItemNames(controller.getCurrentCourse());
		gradeTableColumnNamesList.add("Student Name");
		gradeTableColumnNamesList.add("BUID");
		gradeTableColumnNamesList.addAll(allItemNames);
		Object[] gradeTableColumnNames = gradeTableColumnNamesList.toArray();

		ArrayList<HashMap<String, Double>> listOfGrades = controller
				.getAllGradeEntriesForAllStudents(controller.getCurrentCourse());
		ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();

		String[][] gradeTableRowData = new String[students.size()][];
		for (int i = 0; i < gradeTableRowData.length; i++) {
			HashMap<String, Double> grades = students.get(i).getAllGradeEntries();
			gradeTableRowData[i] = new String[gradeTableColumnNames.length];
			gradeTableRowData[i][0] = students.get(i).getFname() + " " + students.get(i).getLname();
			gradeTableRowData[i][1] = students.get(i).getBuid();
			for (int j = 0; j < grades.size(); j++) {
				gradeTableRowData[i][j + 2] = Double.toString(grades.get(gradeTableColumnNames[j]));
			}
		}

		if (editable) {
			gradeTableModel = new DefaultTableModel(gradeTableRowData, gradeTableColumnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return column > 0;
				}
			};
		} else {
			gradeTableModel = new DefaultTableModel(gradeTableRowData, gradeTableColumnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		gradeTable = new JTable(gradeTableModel);
		gradeTable.setRowHeight(SizeManager.tableRowHeight);
		gradeTable.setFont(FontManager.fontTable);
		DefaultTableCellRenderer gradeTableRender = new DefaultTableCellRenderer();
		gradeTableRender.setHorizontalAlignment(SwingConstants.CENTER);
		gradeTableRender.setVerticalAlignment(SwingConstants.CENTER);
		gradeTable.setDefaultRenderer(Object.class, gradeTableRender);
		TableRowSorter<DefaultTableModel> gradeTableRowSorter = new TableRowSorter<>(gradeTableModel);
		gradeTable.setRowSorter(gradeTableRowSorter);
		gradeTable.getTableHeader().setFont(gradeTable.getFont());
		JScrollPane gradeTableScrollPane = new JScrollPane(gradeTable);
		gradeTableScrollPane.setBounds(SizeManager.tableCourseBounds);
		add(gradeTableScrollPane);

		// category combo box
		ArrayList<String> categoryComboNames = new ArrayList<>();
		categoryComboNames.add("All");
		categoryComboNames.addAll(categoryNames);
		Object[] categoryComboBoxItems = categoryComboNames.toArray(); // TODO
		categoryComboBox = new JComboBox<>(convertObjectArrayToString(categoryComboBoxItems));
		categoryComboBox.setBounds(SizeManager.filterCourseBounds);
		categoryComboBox.setFont(FontManager.fontFilter);
		DefaultListCellRenderer categoryComboBoxRenderer = new DefaultListCellRenderer();
		categoryComboBoxRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		categoryComboBox.setRenderer(categoryComboBoxRenderer);
		add(categoryComboBox);

		// item combo box
		ArrayList<String> itemComboNames = new ArrayList<>();
		itemComboNames.add("All");
		itemComboNames.addAll(allItemNames);
		Object[] itemComboItems = itemComboNames.toArray(); // TODO
		itemComboBox = new JComboBox<>(convertObjectArrayToString(itemComboItems));
		itemComboBox.setBounds(SizeManager.searchCourseBounds);
		itemComboBox.setFont(FontManager.fontSearch);
		itemComboBox.setRenderer(categoryComboBoxRenderer);
		add(itemComboBox);

		// grade options combo
		String[] gradeOptionsItems = { "Points Lost", "Percentage" };
		gradeOptionsComboBox = new JComboBox<>(gradeOptionsItems);
		gradeOptionsComboBox.setBounds(SizeManager.searchCourseBounds);
		gradeOptionsComboBox.setFont(FontManager.fontSearch);
		gradeOptionsComboBox.setRenderer(categoryComboBoxRenderer);
		add(gradeOptionsComboBox);

		categoryComboBox.addActionListener(
				e -> searchGradeTable(gradeTableRowSorter, categoryComboBox, itemComboBox, gradeOptionsComboBox));
		itemComboBox.addActionListener(
				e -> searchGradeTable(gradeTableRowSorter, categoryComboBox, itemComboBox, gradeOptionsComboBox));
		gradeOptionsComboBox.addActionListener(
				e -> searchGradeTable(gradeTableRowSorter, categoryComboBox, itemComboBox, gradeOptionsComboBox));

		// back button
		JButton backButton = new JButton("Back");
		backButton.setFont(FontManager.fontButton);
		backButton.setBounds(SizeManager.buttonAddBounds);
		backButton.setForeground(ColorManager.lightColor);
		backButton.setBackground(ColorManager.primaryColor);
		backButton.addActionListener(e -> frame.changePanel(this, new MenuPanel(frame, courseData, controller)));
		add(backButton);

		// save button
		UIManager.put("OptionPane.messageFont", FontManager.fontLabel);
		UIManager.put("OptionPane.buttonFont", FontManager.fontLabel);
		JButton saveButton = new JButton("Save");
		saveButton.setFont(FontManager.fontButton);
		saveButton.setBounds(SizeManager.buttonViewBounds);
		saveButton.setForeground(ColorManager.lightColor);
		saveButton.setBackground(ColorManager.primaryColor);
		saveButton.addActionListener(e -> {
			// TODO save grade data to database
		});
		add(saveButton);

		// category label
		JLabel categoryLabel = new JLabel("Category : ");
		categoryLabel.setBounds(SizeManager.labelFilterBounds);
		categoryLabel.setFont(FontManager.fontLabel);
		categoryLabel.setVerticalAlignment(SwingConstants.CENTER);
		categoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(categoryLabel);

		// item label
		JLabel itemLabel = new JLabel("Item : ");
		itemLabel.setBounds(SizeManager.labelSearchBounds);
		itemLabel.setFont(FontManager.fontLabel);
		itemLabel.setVerticalAlignment(SwingConstants.CENTER);
		itemLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(itemLabel);
		
		JLabel gradeOptionsLabel = new JLabel("Options : ");
		gradeOptionsLabel.setBounds(SizeManager.labelSearchBounds);
		gradeOptionsLabel.setFont(FontManager.fontLabel);
		gradeOptionsLabel.setVerticalAlignment(SwingConstants.CENTER);
		gradeOptionsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(gradeOptionsLabel);

//		generateRandomTestData(gradeTable);

		setVisible(true);
	}

	public String[] convertObjectArrayToString(Object[] arr) {
		String[] str = new String[arr.length];
		int i = 0;
		for (Object o : arr) {
			str[i++] = o.toString();
		}
		return str;
	}

	/**
	 * Search something in the grade table by specified text and filter
	 *
	 * @param gradeTableRowSorter a TableRowSorter for a JTable
	 * @param categoryComboBox    a JComboBox which can be used for selecting a
	 *                            category
	 * @param itemComboBox        a JComboBox which can be used for selecting an
	 *                            item
	 */
	private static void searchGradeTable(TableRowSorter<DefaultTableModel> gradeTableRowSorter,
			JComboBox<String> categoryComboBox, JComboBox<String> itemComboBox,
			JComboBox<String> gradeOptionsComboBox) {
		// TODO
	}

	/**
	 * Generate some random data which is used for testing
	 *
	 * @param tableGrade
	 */
	private void generateRandomTestData(JTable tableGrade) {
		Random random = new Random();
		for (int i = 3; i < 100; ++i) {
			for (int j = 1; j < 8; ++j) {
				tableGrade.setValueAt(String.valueOf(random.nextInt(20) + 80), i, j);
			}
		}
		for (int i = 1; i < 8; ++i) {
			List<Integer> rank = new ArrayList<>();
			for (int j = 3; j < 100; ++j) {
				rank.add(Integer.valueOf((String) tableGrade.getValueAt(j, i)));
			}
			double sum = 0;
			for (Integer integer : rank) {
				sum += integer;
			}
			rank.sort(Integer::compareTo);
			tableGrade.setValueAt(String.valueOf(rank.get(rank.size() / 2)), 1, i);
			tableGrade.setValueAt(String.format("%.2f", sum / rank.size()), 0, i);
		}
	}
}
