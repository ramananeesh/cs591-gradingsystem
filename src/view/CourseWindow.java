package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class CourseWindow extends JFrame {
	private static final String TITLE = "Grading System - Course";
	private static final String BACKGROUND_PICTURE_FILE_NAME = "src/resource/background.jpg";


	public CourseWindow() {
		setTitle(TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLayout(null);
		setResizable(false);
		setLocationRelativeTo(null);
		setContentPane(new JLabel(new ImageIcon(BACKGROUND_PICTURE_FILE_NAME)));
		setBounds(SizeManager.windowBounds);

		String[] tableCourseColumn = {
				"#", "Course Name", "Semester"
		};
		String[][] tableCourseData = { // TODO load course data
				{"CS505", "Introduction to Natural Language Processing", "Spring 2020"},
				{"CS542", "Machine Learning", "Spring 2020"},
				{"CS585", "Image & Video Computing", "Spring 2020"},
				{"CS591 P1", "Topics in Computer Science", "Fall 2019"},
				{"CS480/680", "Introduction to Computer Graphics", "Fall 2019"},
				{"CS530", "Graduate Algorithms", "Fall 2019"}
		};

		DefaultTableModel modelCourse = new DefaultTableModel(tableCourseData, tableCourseColumn);
		JTable tableCourse = new JTable(modelCourse);
		tableCourse.setBounds(SizeManager.tableCourseBounds);
		tableCourse.setRowHeight(SizeManager.tableRowHeight);
		tableCourse.setFont(FontManager.fontTable);
		int[] tableColumWidth = {
				SizeManager.tableCourseColumnNumberWidth,
				SizeManager.tableCourseColumnNameWidth,
				SizeManager.tableCourseColumnSemesterWidth
		};
		for (int i = 0; i < 3; ++i)
			tableCourse.getColumnModel().getColumn(i).setPreferredWidth(tableColumWidth[i]);
		DefaultTableCellRenderer render = new DefaultTableCellRenderer();
		render.setHorizontalAlignment(SwingConstants.CENTER);
		render.setVerticalAlignment(SwingConstants.CENTER);
		tableCourse.setDefaultRenderer(Object.class, render);
		TableRowSorter<DefaultTableModel> sorterCourse = new TableRowSorter<>(modelCourse);
		tableCourse.setRowSorter(sorterCourse);
		tableCourse.getTableHeader().setFont(tableCourse.getFont());
		JScrollPane tableCourseScrollPane = new JScrollPane(tableCourse);
		tableCourseScrollPane.setSize(tableCourse.getWidth(), tableCourse.getHeight());
		tableCourseScrollPane.setVisible(true);
		tableCourseScrollPane.setLocation(tableCourse.getX(), tableCourse.getY());
		add(tableCourseScrollPane);

		String[] semester = {"All", "Fall 2019", "Spring 2020"}; // TODO

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
//		textSearch.addFocusListener(new FocusListener() {
//			@Override
//			public void focusGained(FocusEvent e) {
//				if (textSearch.getText().equals("")) {
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

		textSearch.getDocument().addDocumentListener(new SearchDocumentListener(sorterCourse, textSearch, boxFilter));
		boxFilter.addActionListener(new SearchActionListener(sorterCourse, textSearch, boxFilter));

		JButton buttonAdd = new JButton("Add");
		buttonAdd.setFont(FontManager.fontButton);
		buttonAdd.setBounds(SizeManager.buttonAddBounds);
		buttonAdd.setForeground(ColorManager.lightColor);
		buttonAdd.setBackground(ColorManager.primaryColor);
		buttonAdd.addActionListener(e -> {
			// TODO
		});
		add(buttonAdd);

		JButton buttonView = new JButton("View");
		buttonView.setFont(FontManager.fontButton);
		buttonView.setBounds(SizeManager.buttonViewBounds);
		buttonView.setForeground(ColorManager.lightColor);
		buttonView.setBackground(ColorManager.primaryColor);
		buttonView.addActionListener(e -> {
			String[] courseName = new String[3];
			for (int i = 0; i < 3; ++i) {
				courseName[i] = tableCourse.getValueAt(tableCourse.getSelectedRow(), i).toString();
			}
			new MenuWindow(courseName);
			setVisible(false);
			dispose();
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
	}
}
