package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import controller.Master;
import model.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

public class CoursePanel extends JPanel implements Observer {
	private Master controller;
	private static final String TITLE = "Grading System - Course";
	private MainFrame frame;
	private DefaultTableModel modelCourse;
	private String[] tableCourseColumns;
	private JTable tableCourse;

	/**
	 * Initializes a newly created {@code CoursePanel} object
	 */
	public CoursePanel(MainFrame frame, Master controller) {
		this.controller = controller;
		this.frame = frame;
		this.controller.addObserver(this);
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.panelBounds);
		setOpaque(false);

		UIManager.put("TextField.font", FontManager.fontSearch);
		UIManager.put("ComboBox.font", FontManager.fontFilter);

		tableCourseColumns = new String[] { "#", "Course Name", "Semester" };
//		String[][] tableCourseData = { // TODO load course data
//				{ "CS505", "Introduction to Natural Language Processing", "Spring 2020" },
//				{ "CS542", "Machine Learning", "Spring 2020" }, { "CS585", "Image & Video Computing", "Spring 2020" },
//				{ "CS591 P1", "Topics in Computer Science", "Fall 2019" },
//				{ "CS480/680", "Introduction to Computer Graphics", "Fall 2019" },
//				{ "CS530", "Graduate Algorithms", "Fall 2019" } };

		String[][] tableCourseData = new String[][] {};
		modelCourse = new DefaultTableModel(tableCourseData, tableCourseColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCourse = new JTable(modelCourse);
		tableCourse.setBounds(SizeManager.tableCourseBounds);
		tableCourse.setRowHeight(SizeManager.tableRowHeight);
		tableCourse.setFont(FontManager.fontTable);

		for (int i = 0; i < 3; ++i) {
			tableCourse.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.courseTableColumnWidth[i]);
		}
		DefaultTableCellRenderer tableRender = new DefaultTableCellRenderer();
		tableRender.setHorizontalAlignment(SwingConstants.CENTER);
		tableRender.setVerticalAlignment(SwingConstants.CENTER);
		tableCourse.setDefaultRenderer(Object.class, tableRender);
		TableRowSorter<DefaultTableModel> sorterCourse = new TableRowSorter<>(modelCourse);
		tableCourse.setRowSorter(sorterCourse);
		JTableHeader tableHeader = tableCourse.getTableHeader();
		tableHeader.setBackground(ColorManager.primaryColor);
		tableHeader.setForeground(ColorManager.lightColor);
		tableHeader.setFont(tableCourse.getFont());
		JScrollPane tableCourseScrollPane = new JScrollPane(tableCourse);
		tableCourseScrollPane.setBounds(tableCourse.getBounds());
		tableCourse.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(tableCourseScrollPane);

		String[] semester = { "All", "Fall 2019", "Spring 2020" }; // TODO

		JComboBox<String> boxFilter = new JComboBox<>(semester);
		boxFilter.setBounds(SizeManager.filterCourseBounds);
		boxFilter.setFont(FontManager.fontFilter);
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		boxFilter.setRenderer(renderer);
		add(boxFilter);

		JTextField textSearch = new JTextField();
		textSearch.setBounds(SizeManager.searchCourseBounds);
		textSearch.setFont(FontManager.fontSearch);
		textSearch.setHorizontalAlignment(SwingConstants.CENTER);
//		textSearch.addFocusListener(new FocusListener() { TODO may display hint words in search bar
//			@Override
//			public void focusGained(FocusEvent e) {
//				if (textSearch.getText().equals("Search Bar")) {
//					textSearch.setText("");
//				}
//			}
//
//			@Override
//			public void focusLost(FocusEvent e) {
//				if (textSearch.getText().equals("")) {
//					textSearch.setText("Search Bar");
//				}
//			}
//		});
		add(textSearch);

		textSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				search(sorterCourse, textSearch, boxFilter);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				search(sorterCourse, textSearch, boxFilter);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				search(sorterCourse, textSearch, boxFilter);
			}
		});
		boxFilter.addActionListener(e -> search(sorterCourse, textSearch, boxFilter));

		JButton buttonAdd = new JButton("Add");
		buttonAdd.setFont(FontManager.fontButton);
		buttonAdd.setBounds(SizeManager.buttonAddBounds);
		buttonAdd.setForeground(ColorManager.lightColor);
		buttonAdd.setBackground(ColorManager.primaryColor);
		buttonAdd.addActionListener(e -> {
			try {
				JTextField numberField = new JTextField();
				JTextField nameField = new JTextField();
				JTextField termField = new JTextField();
				JComboBox<String> templateCombo = new JComboBox<>();
				templateCombo.addItem("None");
				for (String[] tableCourseDatum : tableCourseData) {
					templateCombo.addItem(tableCourseDatum[0] + " " + tableCourseDatum[2]);
				}
				Object[] fields = { "Course Id: ", numberField, "Course Name: ", nameField, "Course Term: ", termField,
						"Template: ", templateCombo, };
				while (true) { // TODO
					int reply = JOptionPane.showConfirmDialog(null, fields, "Add a Course",
							JOptionPane.OK_CANCEL_OPTION);
					if (reply == JOptionPane.OK_OPTION) {
						String courseId = numberField.getText();
						String courseName = nameField.getText();
						String courseTerm = termField.getText();
						int templateIndex = templateCombo.getSelectedIndex();
						if (templateIndex != 0) {
							Course templateCourse = controller.getTemplateCourse(templateIndex - 1);

							ArrayList<Category> templateCategories = controller
									.getTemplateCategoriesForCourse(templateCourse);

							controller.addNewCourse(courseId, courseName, courseTerm, templateCategories,
									new ArrayList<CourseStudent>());

						} else {
							controller.addNewCourse(courseId, courseName, courseTerm);
						}
						break;
					} else {
						return;
					}
				}
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, "Error", "Error", JOptionPane.ERROR_MESSAGE);
			}
		});
		add(buttonAdd);

		UIManager.put("OptionPane.messageFont", FontManager.fontLabel);
		UIManager.put("OptionPane.buttonFont", FontManager.fontLabel);
		JButton buttonView = new JButton("View");
		buttonView.setFont(FontManager.fontButton);
		buttonView.setBounds(SizeManager.buttonViewBounds);
		buttonView.setForeground(ColorManager.lightColor);
		buttonView.setBackground(ColorManager.primaryColor);
		buttonView.addActionListener(e -> {
			if (tableCourse.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(this, "Please select a course.", "Error", JOptionPane.WARNING_MESSAGE);
			} else {
				String[] courseData = new String[3];
				int selectedRow = tableCourse.getSelectedRow();
				for (int i = 0; i < 3; ++i) {
					courseData[i] = tableCourse.getValueAt(selectedRow, i).toString();
				}
				controller.setCurrentCourse(selectedRow);
				frame.changePanel(this, new MenuPanel(frame, courseData, controller));
			}
		});
		add(buttonView);

		JLabel labelFilter = new JLabel("Semester : ");
		labelFilter.setBounds(SizeManager.labelFilterBounds);
		labelFilter.setFont(FontManager.fontLabel);
		labelFilter.setVerticalAlignment(SwingConstants.CENTER);
		labelFilter.setHorizontalAlignment(SwingConstants.RIGHT);
		add(labelFilter);

		JLabel labelSearch = new JLabel("Search : ");
		labelSearch.setBounds(SizeManager.labelSearchBounds);
		labelSearch.setFont(FontManager.fontLabel);
		labelSearch.setVerticalAlignment(SwingConstants.CENTER);
		labelSearch.setHorizontalAlignment(SwingConstants.RIGHT);
		add(labelSearch);

		setVisible(true);

		// testing purposes
		controller.addNewCourse("CS565", "Data Mining", "Fall 2019");
		Course course = controller.getCourse(0);
		
		Category categoryHW = new Category(1, "Homework", 0.3, course.getCourseId());
		Category categoryExam = new Category(2, "Exam", 0.2, course.getCourseId());
		controller.addNewCategoryForCourse(course, categoryHW.getId(),categoryHW.getFieldName(),categoryHW.getWeight(),categoryHW.getCourseId());
		controller.addNewCategoryForCourse(course, categoryExam.getId(),categoryExam.getFieldName(),categoryExam.getWeight(),categoryExam.getCourseId());
		controller.addItemForCourseCategory(course, 0, "HW1", 0.4, 100);
        controller.addItemForCourseCategory(course, 0, "HW2", 0.2, 100);
		controller.addItemForCourseCategory(course, 1, "Exam1", 0.5, 100);
	}

	private static void search(TableRowSorter<DefaultTableModel> sorter, JTextField search,
			JComboBox<String> boxFilter) {
		if (search.getText().length() != 0 && !Objects.equals(boxFilter.getSelectedItem(), "All")) {
			sorter.setRowFilter(RowFilter.andFilter(
					new ArrayList<>(Arrays.asList(RowFilter.regexFilter("(?i)" + boxFilter.getSelectedItem()),
							RowFilter.regexFilter("(?i)" + search.getText())))));
		} else if (!Objects.equals(boxFilter.getSelectedItem(), "All")) {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + boxFilter.getSelectedItem()));
		} else if (search.getText().length() != 0) {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search.getText()));
		} else {
			sorter.setRowFilter(null);
		}
	}

	public DefaultTableModel addCourseRowsToModel(DefaultTableModel model, String[][] tableCourseData) {

		for (int i = 0; i < tableCourseData.length; i++) {
			model.addRow(tableCourseData[i]);
		}
		return model;
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		String[][] tableCourseData = controller.getAllCourseDetails();
		modelCourse = new DefaultTableModel(tableCourseData, tableCourseColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCourse.setModel(modelCourse);
		TableRowSorter<DefaultTableModel> sorterCourse = new TableRowSorter<>(modelCourse);
		tableCourse.setRowSorter(sorterCourse);

	}
}
