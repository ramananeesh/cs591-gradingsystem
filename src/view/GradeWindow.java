package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class GradeWindow extends JFrame {
	private static final String TITLE = "Grading System - Grade";
	private static final String BACKGROUND_PICTURE_FILE_NAME = "src/resource/background.jpg";


	public GradeWindow(String[] courseData) {
		setTitle(TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setContentPane(new JLabel(new ImageIcon(BACKGROUND_PICTURE_FILE_NAME)));
		setBounds(SizeManager.windowBounds);

		String[] tableGradeColumn = {
				"Student Name", "Homework 1", "Homework 2", "Midterm", "Final Exam"
		};
		String[][] tableCourseData = { // TODO load course data
				{"Student 1", "100", "100", "100"},
				{"Student 2", "100", "100"},
				{"Student 3"},
				{"Student 4"},
				{"Student 5"},
				{"Student 6"}
		};

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

		boxCategory.addActionListener(e -> {
			search(boxCategory, boxItem, sorterGrade);
		});
		boxItem.addActionListener(e -> {
			search(boxCategory, boxItem, sorterGrade);
		});

		JButton buttonBack = new JButton("Back");
		buttonBack.setFont(FontManager.fontButton);
		buttonBack.setBounds(SizeManager.buttonAddBounds);
		buttonBack.setForeground(ColorManager.lightColor);
		buttonBack.setBackground(ColorManager.primaryColor);
		buttonBack.addActionListener(e -> {
			new MenuWindow(courseData); //TODO
			dispose();
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

		setVisible(true);
	}

	private static void search(JComboBox<String> boxCategory, JComboBox<String> boxItem, TableRowSorter<DefaultTableModel> sorter) {
		// TODO
	}
}
