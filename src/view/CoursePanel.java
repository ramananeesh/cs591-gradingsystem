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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * The {@code CoursePanel} class represents the panel for viewing all the courses
 */
public class CoursePanel extends JPanel {
	/** The title for the window when CoursePanel displays */
	private static final String TITLE = "Grading System - Course";

	/** The frame which contains this panel. */
	private MainFrame frame;

	/**
	 * Initializes a newly created {@code CoursePanel} object
	 */
	public CoursePanel(MainFrame frame) {
		this.frame = frame;
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.panelBounds);
		setOpaque(false);

		UIManager.put("TextField.font", FontManager.fontSearch);
		UIManager.put("ComboBox.font", FontManager.fontFilter);

		// course table
		String[] courseTableColumnNames = {"#", "Course Name", "Semester"}; // The table head of course table.
		String[][] courseTableRowData = { // TODO test data, need to be replaced when database exists
				{"CS505", "Introduction to Natural Language Processing", "Spring 2020"},
				{"CS542", "Machine Learning", "Spring 2020"},
				{"CS585", "Image & Video Computing", "Spring 2020"},
				{"CS591 P1", "Topics in Computer Science", "Fall 2019"},
				{"CS480/680", "Introduction to Computer Graphics", "Fall 2019"},
				{"CS530", "Graduate Algorithms", "Fall 2019"}
		};
		DefaultTableModel courseTableModel = new DefaultTableModel(courseTableRowData, courseTableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable courseTable = new JTable(courseTableModel);
		courseTable.setBounds(SizeManager.tableCourseBounds);
		courseTable.setRowHeight(SizeManager.tableRowHeight);
		courseTable.setFont(FontManager.fontTable);
		for (int i = 0; i < 3; ++i) {
			courseTable.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.courseTableColumnWidth[i]);
		}
		DefaultTableCellRenderer courseTableRender = new DefaultTableCellRenderer();
		courseTableRender.setHorizontalAlignment(SwingConstants.CENTER);
		courseTableRender.setVerticalAlignment(SwingConstants.CENTER);
		courseTable.setDefaultRenderer(Object.class, courseTableRender);
		TableRowSorter<DefaultTableModel> courseTableRowSorter = new TableRowSorter<>(courseTableModel);
		courseTable.setRowSorter(courseTableRowSorter);
		JTableHeader courseTableHeader = courseTable.getTableHeader();
		courseTableHeader.setBackground(ColorManager.primaryColor);
		courseTableHeader.setForeground(ColorManager.lightColor);
		courseTableHeader.setFont(courseTable.getFont());
		JScrollPane courseTableScrollPane = new JScrollPane(courseTable);
		courseTableScrollPane.setBounds(courseTable.getBounds());
		courseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(courseTableScrollPane);

		// semester combo box
		String[] semesterComboBoxItems = {"All", "Fall 2019", "Spring 2020"}; // TODO test data, need to be replaced when database exists
		JComboBox<String> semesterComboBox = new JComboBox<>(semesterComboBoxItems);
		semesterComboBox.setBounds(SizeManager.filterCourseBounds);
		semesterComboBox.setFont(FontManager.fontFilter);
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		semesterComboBox.setRenderer(renderer);
		add(semesterComboBox);

		// search text field
		JTextField searchTextField = new JTextField();
		searchTextField.setBounds(SizeManager.searchCourseBounds);
		searchTextField.setFont(FontManager.fontSearch);
		searchTextField.setHorizontalAlignment(SwingConstants.CENTER);
//		textSearch.addFocusListener(new FocusListener() { TODO may display hint words in search bar by using focus listener
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
		add(searchTextField);
		searchTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				searchCourseTable(courseTableRowSorter, searchTextField, semesterComboBox);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				searchCourseTable(courseTableRowSorter, searchTextField, semesterComboBox);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				searchCourseTable(courseTableRowSorter, searchTextField, semesterComboBox);
			}
		});
		semesterComboBox.addActionListener(e -> searchCourseTable(courseTableRowSorter, searchTextField, semesterComboBox));

		// add button
		JButton addButton = new JButton("Add");
		addButton.setFont(FontManager.fontButton);
		addButton.setBounds(SizeManager.buttonAddBounds);
		addButton.setForeground(ColorManager.lightColor);
		addButton.setBackground(ColorManager.primaryColor);
		addButton.addActionListener(actionEvent -> {
			try {
				JTextField courseNumberField = new JTextField();
				JTextField courseNameField = new JTextField();
				JTextField semesterField = new JTextField();
				JComboBox<String> templateComboBox = new JComboBox<>();
				for (String[] courseTableRowDatum : courseTableRowData) {
					templateComboBox.addItem(courseTableRowDatum[0] + " " + courseTableRowDatum[2]);
				}
				Object[] fields = {"Course Number: ", courseNumberField, "Course Name: ", courseNameField, "Semester: ", semesterField, "Template: ", templateComboBox,};
				while (true) { // TODO
					int reply = JOptionPane.showConfirmDialog(null, fields, "Add a Course", JOptionPane.OK_CANCEL_OPTION);
					if (reply == JOptionPane.OK_OPTION) {

						break;
					} else {
						return;
					}
				}
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this,
						"Error", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		});
		add(addButton);

		// view button
		UIManager.put("OptionPane.messageFont", FontManager.fontLabel);
		UIManager.put("OptionPane.buttonFont", FontManager.fontLabel);
		JButton buttonView = new JButton("View");
		buttonView.setFont(FontManager.fontButton);
		buttonView.setBounds(SizeManager.buttonViewBounds);
		buttonView.setForeground(ColorManager.lightColor);
		buttonView.setBackground(ColorManager.primaryColor);
		buttonView.addActionListener(e -> {
			if (courseTable.getSelectedRow() == -1) {
				JOptionPane.showMessageDialog(this, "Please select a course.", "Error", JOptionPane.WARNING_MESSAGE);
			} else {
				String[] courseData = new String[3];
				for (int i = 0; i < 3; ++i) {
					courseData[i] = courseTable.getValueAt(courseTable.getSelectedRow(), i).toString();
				}
				frame.changePanel(this, new MenuPanel(frame, courseData));
			}
		});
		add(buttonView);

		// semester label
		JLabel semesterLabel = new JLabel("Semester : ");
		semesterLabel.setBounds(SizeManager.labelFilterBounds);
		semesterLabel.setFont(FontManager.fontLabel);
		semesterLabel.setVerticalAlignment(SwingConstants.CENTER);
		semesterLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(semesterLabel);

		// search label
		JLabel searchLabel = new JLabel("Search : ");
		searchLabel.setBounds(SizeManager.labelSearchBounds);
		searchLabel.setFont(FontManager.fontLabel);
		searchLabel.setVerticalAlignment(SwingConstants.CENTER);
		searchLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(searchLabel);

		setVisible(true);
	}

	/**
	 * Search something in the course table by specified text and filter
	 *
	 * @param courseTableRowSorter a TableRowSorter for a JTable
	 * @param searchTextField      a JTextField contains key words
	 * @param semesterComboBox     a JComboBox which can be used for selecting a semester
	 */
	private void searchCourseTable(TableRowSorter<DefaultTableModel> courseTableRowSorter, JTextField searchTextField, JComboBox<String> semesterComboBox) {
		if (searchTextField.getText().length() != 0 && !Objects.equals(semesterComboBox.getSelectedItem(), "All")) {
			courseTableRowSorter.setRowFilter(RowFilter.andFilter(new ArrayList<>(Arrays.asList(
					RowFilter.regexFilter("(?i)" + semesterComboBox.getSelectedItem()),
					RowFilter.regexFilter("(?i)" + searchTextField.getText()))))
			);
		} else if (!Objects.equals(semesterComboBox.getSelectedItem(), "All")) {
			courseTableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + semesterComboBox.getSelectedItem()));
		} else if (searchTextField.getText().length() != 0) {
			courseTableRowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchTextField.getText()));
		} else {
			courseTableRowSorter.setRowFilter(null);
		}
	}
}