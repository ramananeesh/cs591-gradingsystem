package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class GradePanel extends JPanel {
	private static final String TITLE = "Grading System - Grade";
	private MainFrame frame;

	/**
	 * Initializes a newly created {@code GradePanel} object
	 */
	public GradePanel(MainFrame frame, String[] courseData) {
		this.frame = frame;
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.panelBounds);

		String[] tableGradeColumn = {
				"Student Name", "Homework 1", "Homework 2", "Homework 3", "Homework 4", "Homework 5", "Midterm", "Final Exam"
		};
		String[][] tableCourseData = new String[100][1];//{ // TODO load course data
		tableCourseData[0][0] = "Average";
		tableCourseData[1][0] = "Median";
		for (int i = 3; i < 100; ++i) {
			tableCourseData[i][0] = String.format("Student %02d", i - 2);
		}

		DefaultTableModel modelGrade = new DefaultTableModel(tableCourseData, tableGradeColumn) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column > 0;
			}
		};
		JTable tableGrade = new JTable(modelGrade);
		tableGrade.setRowHeight(SizeManager.tableRowHeight);
		tableGrade.setFont(FontManager.fontTable);
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		render.setVerticalAlignment(SwingConstants.CENTER);
		tableGrade.setDefaultRenderer(Object.class, render);
		TableRowSorter<DefaultTableModel> sorterGrade = new TableRowSorter<>(modelGrade);
		tableGrade.setRowSorter(sorterGrade);
		tableGrade.getTableHeader().setFont(tableGrade.getFont());
		JScrollPane tableCourseScrollPane = new JScrollPane(tableGrade);
		tableCourseScrollPane.setBounds(SizeManager.tableCourseBounds);
		add(tableCourseScrollPane);

		String[] category = {"All", "Homework", "Exam"}; // TODO
		JComboBox<String> boxCategory = new JComboBox<>(category);
		boxCategory.setBounds(SizeManager.filterCourseBounds);
		boxCategory.setFont(FontManager.fontFilter);
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		boxCategory.setRenderer(renderer);
		add(boxCategory);

		String[] item = {"All", "Homework 1", "Homework 2", "Midterm", "Final Exam"}; // TODO
		JComboBox<String> boxItem = new JComboBox<>(item);
		boxItem.setBounds(SizeManager.searchCourseBounds);
		boxItem.setFont(FontManager.fontSearch);
		boxItem.setRenderer(renderer);
		add(boxItem);

		boxCategory.addActionListener(e -> search(boxCategory, boxItem, sorterGrade));
		boxItem.addActionListener(e -> search(boxCategory, boxItem, sorterGrade));

		JButton buttonBack = new JButton("Back");
		buttonBack.setFont(FontManager.fontButton);
		buttonBack.setBounds(SizeManager.buttonAddBounds);
		buttonBack.setForeground(ColorManager.lightColor);
		buttonBack.setBackground(ColorManager.primaryColor);
		buttonBack.addActionListener(e -> {
			frame.switchPanel(this, new MenuPanel(frame, courseData));
		});
		add(buttonBack);

		UIManager.put("OptionPane.messageFont", FontManager.fontLabel);
		UIManager.put("OptionPane.buttonFont", FontManager.fontLabel);
		JButton buttonSave = new JButton("Save");
		buttonSave.setFont(FontManager.fontButton);
		buttonSave.setBounds(SizeManager.buttonViewBounds);
		buttonSave.setForeground(ColorManager.lightColor);
		buttonSave.setBackground(ColorManager.primaryColor);
		buttonSave.addActionListener(e -> {
			// TODO
		});

		add(buttonSave);

		JLabel labelFilter = new JLabel("Category : ");
		labelFilter.setBounds(SizeManager.labelFilterBounds);
		labelFilter.setFont(FontManager.fontLabel);
		labelFilter.setVerticalAlignment(SwingConstants.CENTER);
		labelFilter.setHorizontalAlignment(SwingConstants.RIGHT);
		add(labelFilter);

		JLabel labelSearch = new JLabel("Item : ");
		labelSearch.setBounds(SizeManager.labelSearchBounds);
		labelSearch.setFont(FontManager.fontLabel);
		labelSearch.setVerticalAlignment(SwingConstants.CENTER);
		labelSearch.setHorizontalAlignment(SwingConstants.RIGHT);
		add(labelSearch);

		initRandom(tableGrade);
		computeStatistics(tableGrade);

		setVisible(true);
	}

	private static void search(JComboBox<String> boxCategory, JComboBox<String> boxItem, TableRowSorter<DefaultTableModel> sorter) {
		// TODO
	}

	private void initRandom(JTable tableGrade) {
		Random random = new Random();
		for (int i = 3; i < 100; ++i) {
			for (int j = 1; j < 8; ++j) {
				tableGrade.setValueAt(String.valueOf(random.nextInt(20) + 80), i, j);
			}
		}
	}

	private void computeStatistics(JTable tableGrade) {
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
