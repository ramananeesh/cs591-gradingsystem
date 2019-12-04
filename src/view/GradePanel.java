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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private DefaultComboBoxModel<String> itemsModel;

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
		String[] gradeTableColumnNames = { "Student Name", "BUID", "Score", "Comments" };

		ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();

		String[][] gradeTableRowData = new String[students.size()][];
		for (int i = 0; i < gradeTableRowData.length; i++) {
			gradeTableRowData[i] = new String[gradeTableColumnNames.length];
			gradeTableRowData[i][0] = students.get(i).getFname() + " " + students.get(i).getLname();
			gradeTableRowData[i][1] = students.get(i).getBuid();
			gradeTableRowData[i][2] = "";
			gradeTableRowData[i][3] = "";
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
		categoryComboNames.add("None");
		categoryComboNames.addAll(categoryNames);
		Object[] categoryComboBoxItems = categoryComboNames.toArray(); // TODO
		categoryComboBox = new JComboBox<>(convertObjectArrayToString(categoryComboBoxItems));
		categoryComboBox.setBounds(SizeManager.categoryBounds);
		categoryComboBox.setFont(FontManager.fontFilter);
		DefaultListCellRenderer categoryComboBoxRenderer = new DefaultListCellRenderer();
		categoryComboBoxRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		categoryComboBox.setRenderer(categoryComboBoxRenderer);
		add(categoryComboBox);

		// item combo box
		ArrayList<String> itemComboNames = new ArrayList<>();
		String[] itemComboItems = { "None" };
		itemsModel = new DefaultComboBoxModel<String>(itemComboItems);
		itemComboBox = new JComboBox<>(itemComboItems);
		itemComboBox.setBounds(SizeManager.itemBounds);
		itemComboBox.setFont(FontManager.fontSearch);
		itemComboBox.setRenderer(categoryComboBoxRenderer);
		add(itemComboBox);

		// grade options combo
		String[] gradeOptionsItems = { "Points Lost", "Percentage" };
		gradeOptionsComboBox = new JComboBox<>(gradeOptionsItems);
		// gradeOptionsComboBox.setBounds(SizeManager.filterCourseBounds);
		gradeOptionsComboBox.setBounds(SizeManager.comboBounds);
		gradeOptionsComboBox.setFont(FontManager.fontFilter);
		gradeOptionsComboBox.setRenderer(categoryComboBoxRenderer);
		add(gradeOptionsComboBox);

		categoryComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index = categoryComboBox.getSelectedIndex();
				if (index != 0) {
					// index-1 here because index=0 is "All"
					ArrayList<Item> items = controller.getAllItemsForCourseCategory(controller.getCurrentCourse(),
							index - 1);

					String[] itemComboNames = new String[items.size()];
					int i = 0;
					for (Item item : items) {
						itemComboNames[i++] = item.getFieldName();
					}

					DefaultComboBoxModel<String> newComboModel = new DefaultComboBoxModel<String>(itemComboNames);

					itemComboBox.setModel(newComboModel);
				}
			}
		});

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
		saveButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (categoryComboBox.getSelectedIndex() == 0) {
					JOptionPane.showMessageDialog(null, "Please select a category", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();

				Category cat = categories.get(categoryComboBox.getSelectedIndex() - 1);
				Item it = controller.getCurrentCourse().getItemByItemName(cat.getId(),
						(String) itemComboBox.getSelectedItem());

				for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
					// System.out.println(gradeTableModel.getValueAt(count, 0).toString());
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("Buid", gradeTableModel.getValueAt(i, 1).toString());
					for (int j = 2; j < gradeTableModel.getColumnCount(); j++) {
						String value = gradeTableModel.getValueAt(i, j).toString();
						if (gradeTableColumnNames[j].equals("Score")) {
							if (gradeOptionsComboBox.getSelectedItem().equals("Points Lost")) {
								double numValue = Double.parseDouble(value);
								double rawScore = it.getMaxPoints() + numValue; // + because numValue is -ve
								double percentage = 100.0 * rawScore / it.getMaxPoints();
								map.put("Percentage", Double.toString(percentage));
								// make value = raw score
								value = Double.toString(rawScore);
							} else {
								// if score entered is percentage
								double percentage = Double.parseDouble(value);
								double rawScore = percentage * it.getMaxPoints() / 100.0;
								map.put("Percentage", Double.toString(percentage));
								// make value = raw score
								value = Double.toString(rawScore);
							}
						}
						map.put(gradeTableColumnNames[j], value);
					}
					values.add(map);
				}

				controller.editGradesForCategoryItemInCourse(controller.getCurrentCourse(), cat.getId(), it.getId(),
						values);

				// test to see if edit works. Remove after linking to db
				ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();
				for (CourseStudent s : students) {
					System.out.println("Student: " + s.getBuid());
					HashMap<String, Double> grades = s.getAllGradeEntries();
					for (String key : grades.keySet()) {
						System.out.print(key + ": " + grades.get(key)+"\t");
					}
					System.out.println();
				}
			}
		});
		add(saveButton);

		// category label
		JLabel categoryLabel = new JLabel("Category : ");
		categoryLabel.setBounds(SizeManager.labelCategoryBounds);
		categoryLabel.setFont(FontManager.fontLabel);
		categoryLabel.setVerticalAlignment(SwingConstants.CENTER);
		categoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(categoryLabel);

		// item label
		JLabel itemLabel = new JLabel("Item : ");
		itemLabel.setBounds(SizeManager.labelItemBounds);
		itemLabel.setFont(FontManager.fontLabel);
		itemLabel.setVerticalAlignment(SwingConstants.CENTER);
		itemLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(itemLabel);

		JLabel gradeOptionsLabel = new JLabel("Options : ");
		gradeOptionsLabel.setBounds(SizeManager.labelComboBounds);
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
