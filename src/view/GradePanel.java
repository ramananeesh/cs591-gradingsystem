package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.Master;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * The {@code GradePanel} class represents the panel for viewing or modifying all the grades
 */
public class GradePanel extends JPanel {
	/** The title for the window when GradePanel displays */
	private static final String TITLE = "Grading System - Grade";
	private Master controller;

	/** The frame which contains this panel. */
	private MainFrame frame;

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
		String[] gradeTableColumnNames = {
				"Student Name", "Homework 1", "Homework 2", "Homework 3", "Homework 4", "Homework 5", "Midterm", "Final Exam"
		}; // TODO test data, need to be replaced when database exists
		String[][] gradeTableRowData = new String[100][1];
		gradeTableRowData[0][0] = "Average"; // TODO move statistics part to a separated place
		gradeTableRowData[1][0] = "Median";
		for (int i = 3; i < 100; ++i) {
			gradeTableRowData[i][0] = String.format("Student %02d", i - 2);
		}
		DefaultTableModel gradeTableModel;
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
		JTable gradeTable = new JTable(gradeTableModel);
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
		String[] categoryComboBoxItems = {"All", "Homework", "Exam"}; // TODO
		JComboBox<String> categoryComboBox = new JComboBox<>(categoryComboBoxItems);
		categoryComboBox.setBounds(SizeManager.filterCourseBounds);
		categoryComboBox.setFont(FontManager.fontFilter);
		DefaultListCellRenderer categoryComboBoxRenderer = new DefaultListCellRenderer();
		categoryComboBoxRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		categoryComboBox.setRenderer(categoryComboBoxRenderer);
		add(categoryComboBox);

		// item combo box
		String[] itemComboBoxItems = {"All", "Homework 1", "Homework 2", "Midterm", "Final Exam"}; // TODO
		JComboBox<String> itemComboBox = new JComboBox<>(itemComboBoxItems);
		itemComboBox.setBounds(SizeManager.searchCourseBounds);
		itemComboBox.setFont(FontManager.fontSearch);
		itemComboBox.setRenderer(categoryComboBoxRenderer);
		add(itemComboBox);
		categoryComboBox.addActionListener(e -> searchGradeTable(gradeTableRowSorter, categoryComboBox, itemComboBox));
		itemComboBox.addActionListener(e -> searchGradeTable(gradeTableRowSorter, categoryComboBox, itemComboBox));

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

		generateRandomTestData(gradeTable);

		setVisible(true);
	}

	/**
	 * Search something in the grade table by specified text and filter
	 *
	 * @param gradeTableRowSorter a TableRowSorter for a JTable
	 * @param categoryComboBox    a JComboBox which can be used for selecting a category
	 * @param itemComboBox        a JComboBox which can be used for selecting an item
	 */
	private static void searchGradeTable(TableRowSorter<DefaultTableModel> gradeTableRowSorter, JComboBox<String> categoryComboBox, JComboBox<String> itemComboBox) {
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
