package view;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

import controller.Master;
import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;
import model.Category;
import model.Course;
import model.CourseStudent;

/**
 * Panel for viewing courses.
 */
public class CoursePanel extends JPanel implements Observer {

	/** Title of window. */
	private static final String TITLE = "Grading System - Course";

	/** Controller. */
	private Master controller;
	/** Course table model. */
	private DefaultTableModel modelCourse;

	/** Course table columns. */
	private String[] tableCourseColumns;

	/** Course table. */
	private JTable tableCourse;

	/**
	 * Initializes a newly created {@code CoursePanel} object
	 */
	public CoursePanel(MainFrame frame, Master controller) {

		this.controller = controller;
		this.controller.addObserver(this);
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.getContentPaneBounds());
		setLocation(0, 0);
		setOpaque(false);

		UIManager.put("TextField.font", FontManager.getFontSearch());
		UIManager.put("ComboBox.font", FontManager.getFontFilter());

		tableCourseColumns = new String[]{"#", "Course Name", "Semester"};

		String[][] tableCourseData = controller.getAllCourseDetails();
		modelCourse = new DefaultTableModel(tableCourseData, tableCourseColumns) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableCourse = new JTable(modelCourse);
		tableCourse.setBounds(SizeManager.getTableCourseBounds());
		tableCourse.setRowHeight(SizeManager.getTableRowHeight());
		tableCourse.setFont(FontManager.getFontTable());

		for (int i = 0; i < 3; ++i) {
			tableCourse.getColumnModel().getColumn(i).setPreferredWidth(SizeManager.getCourseTableColumnWidth()[i]);
		}
		DefaultTableCellRenderer tableRender = new DefaultTableCellRenderer();
		tableRender.setHorizontalAlignment(SwingConstants.CENTER);
		tableRender.setVerticalAlignment(SwingConstants.CENTER);
		tableCourse.setDefaultRenderer(Object.class, tableRender);
		TableRowSorter<DefaultTableModel> sorterCourse = new TableRowSorter<>(modelCourse);
		tableCourse.setRowSorter(sorterCourse);
		JTableHeader tableHeader = tableCourse.getTableHeader();
		tableHeader.setBackground(ColorManager.getPrimaryColor());
		tableHeader.setForeground(ColorManager.getLightColor());
		tableHeader.setFont(tableCourse.getFont());
		tableHeader.setEnabled(false);
		tableHeader.setPreferredSize(new Dimension(tableCourse.getWidth(), tableCourse.getRowHeight()));
		JScrollPane tableCourseScrollPane = new JScrollPane(tableCourse);
		tableCourseScrollPane.setBounds(tableCourse.getBounds());
		tableCourse.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		add(tableCourseScrollPane);
		JPanel thisPanel = this;
		tableCourse.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent mouseEvent) {
				if (mouseEvent.getClickCount() == 2 && tableCourse.getSelectedRow() != -1) {
					String[] courseData = new String[3];
					int selectedRow = tableCourse.getSelectedRow();
					for (int i = 0; i < 3; ++i) {
						courseData[i] = tableCourse.getValueAt(selectedRow, i).toString();
					}
					controller.setCurrentCourse(selectedRow);
					frame.changePanel(thisPanel, new MenuPanel(frame, courseData, controller));
				}
			}
		});

		String[] semester = {"All", "Fall 2019", "Spring 2020"};

		JComboBox<String> boxFilter = new JComboBox<>(semester);
		boxFilter.setBounds(SizeManager.getFilterCourseBounds());
		boxFilter.setFont(FontManager.getFontFilter());
		DefaultListCellRenderer renderer = new DefaultListCellRenderer();
		renderer.setHorizontalAlignment(SwingConstants.CENTER);
		boxFilter.setRenderer(renderer);
		add(boxFilter);

		JTextField textSearch = new JTextField();
		textSearch.setBounds(SizeManager.getSearchCourseBounds());
		textSearch.setFont(FontManager.getFontSearch());
		textSearch.setHorizontalAlignment(SwingConstants.CENTER);
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
		buttonAdd.setFont(FontManager.getFontButton());
		buttonAdd.setBounds(SizeManager.getButtonAddBounds());
		buttonAdd.setForeground(ColorManager.getLightColor());
		buttonAdd.setBackground(ColorManager.getPrimaryColor());
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
				Object[] fields = {"Course Id: ", numberField, "Course Name: ", nameField, "Course Term: ", termField,
						"Template: ", templateCombo,};
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
									new ArrayList<CourseStudent>(), true);

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

		UIManager.put("OptionPane.messageFont", FontManager.getFontLabel());
		UIManager.put("OptionPane.buttonFont", FontManager.getFontLabel());
		JButton buttonView = new JButton("View");
		buttonView.setFont(FontManager.getFontButton());
		buttonView.setBounds(SizeManager.getButtonViewBounds());
		buttonView.setForeground(ColorManager.getLightColor());
		buttonView.setBackground(ColorManager.getPrimaryColor());
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
		labelFilter.setBounds(SizeManager.getLabelFilterBounds());
		labelFilter.setFont(FontManager.getFontLabel());
		labelFilter.setVerticalAlignment(SwingConstants.CENTER);
		labelFilter.setHorizontalAlignment(SwingConstants.RIGHT);
		add(labelFilter);

		JLabel labelSearch = new JLabel("Search : ");
		labelSearch.setBounds(SizeManager.getLabelSearchBounds());
		labelSearch.setFont(FontManager.getFontLabel());
		labelSearch.setVerticalAlignment(SwingConstants.CENTER);
		labelSearch.setHorizontalAlignment(SwingConstants.RIGHT);
		add(labelSearch);

		setVisible(true);
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

	/**
	 * Add course rows to course table model.
	 *
	 * @param model           Course table model.
	 * @param tableCourseData Course table data.
	 * @return New course table model.
	 */
	public DefaultTableModel addCourseRowsToModel(DefaultTableModel model, String[][] tableCourseData) {
		for (String[] tableCourseDatum : tableCourseData) {
			model.addRow(tableCourseDatum);
		}
		return model;
	}

	/**
	 * Update table.
	 *
	 * @param observable the observable object.
	 * @param argument   an argument passed to the notifyObservers method.
	 */
	@Override
	public void update(Observable observable, Object argument) {
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