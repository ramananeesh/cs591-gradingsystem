package view;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
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
import model.GradeEntry;
import model.Item;

/**
 * The {@code GradePanel} class represents the panel for viewing or modifying all the grades.
 */
public class GradePanel extends JPanel implements Observer {

	/** The title for the window when GradePanel displays. */
	private static final String TITLE = "Grading System - Grade";

	/** Save button. */
	private final JButton saveButton;

	/** Controller. */
	private Master controller;

	/** Grade table model. */
	private DefaultTableModel gradeTableModel;

	/** Grade table. */
	private JTable gradeTable;

	/** Category combo box. */
	private JComboBox<String> categoryComboBox;

	/** Item combo box. */
	private JComboBox<String> itemComboBox;

	/** Grade options combo box. */
	private JComboBox<String> gradeOptionsComboBox;

	/** Grade table columns names. */
	private String[] gradeTableColumnNames;

	/** Whether table is editable or not. */
	private boolean isEditable;

	/** Decimal format. */
	private DecimalFormat decimalFormat = new DecimalFormat("##.##");

	/**
	 * Initializes a newly created {@code GradePanel} object
	 */
	public GradePanel(MainFrame frame, String[] courseData, boolean isEditable, Master controller) {
		/** The frame which contains this panel. */
		this.controller = controller;
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.getContentPaneBounds());
		setOpaque(false);
		this.isEditable = isEditable;

		// grade table
		ArrayList<String> gradeTableColumnNamesList = new ArrayList<>();
		ArrayList<Category> categories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
		ArrayList<String> categoryNames = new ArrayList<>();
		for (Category c : categories) {
			categoryNames.add(c.getCategoryName());
		}

		Course currCourse = controller.getCurrentCourse();
		ArrayList<String> allItemNames = controller.getAllItemNames(controller.getCurrentCourse());
		gradeTableColumnNames = new String[]{"Student Name", "BUID", "Score", "Comments"};

		ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();

		String[][] gradeTableRowData = new String[students.size()][];
		for (int i = 0; i < gradeTableRowData.length; i++) {
			gradeTableRowData[i] = new String[gradeTableColumnNames.length];
			gradeTableRowData[i][0] = students.get(i).getFirstName() + " " + students.get(i).getLastName();
			gradeTableRowData[i][1] = students.get(i).getStudentId();
			gradeTableRowData[i][2] = "";
			gradeTableRowData[i][3] = "";
		}

		if (isEditable) {
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
		gradeTable.setRowHeight(SizeManager.getTableRowHeight());
		gradeTable.setFont(FontManager.getFontTable());
		TableRowSorter<DefaultTableModel> gradeTableRowSorter = new TableRowSorter<>(gradeTableModel);
		gradeTable.setRowSorter(gradeTableRowSorter);
		DefaultTableCellRenderer gradeTableRender = new DefaultTableCellRenderer();
		gradeTableRender.setHorizontalAlignment(SwingConstants.CENTER);
		gradeTableRender.setVerticalAlignment(SwingConstants.CENTER);
		gradeTable.setDefaultRenderer(Object.class, gradeTableRender);
		JTableHeader gradeTableHeader = gradeTable.getTableHeader();
		gradeTableHeader.setFont(gradeTable.getFont());
		gradeTableHeader.setEnabled(false);
		gradeTableHeader.setPreferredSize(new Dimension(gradeTable.getWidth(), gradeTable.getRowHeight()));
		gradeTableHeader.setBackground(ColorManager.getPrimaryColor());
		gradeTableHeader.setForeground(ColorManager.getLightColor());
		JScrollPane gradeTableScrollPane = new JScrollPane(gradeTable);
		gradeTableScrollPane.setBounds(SizeManager.getTableCourseBounds());

		JLabel hintLabel = new JLabel("Please select a category and an item that you want to grade.");
		hintLabel.setFont(FontManager.getFontLabel());
		hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hintLabel.setVerticalAlignment(SwingConstants.CENTER);
		hintLabel.setBounds(SizeManager.getTableCourseBounds());
		add(hintLabel);

		// category combo box
		ArrayList<String> categoryComboNames = new ArrayList<>();
		categoryComboNames.add("None");
		categoryComboNames.addAll(categoryNames);
		Object[] categoryComboBoxItems = categoryComboNames.toArray(); // TODO
		categoryComboBox = new JComboBox<>(convertObjectArrayToString(categoryComboBoxItems));
		categoryComboBox.setBounds(SizeManager.getCategoryBounds());
		categoryComboBox.setFont(FontManager.getFontFilter());
		DefaultListCellRenderer categoryComboBoxRenderer = new DefaultListCellRenderer();
		categoryComboBoxRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		categoryComboBox.setRenderer(categoryComboBoxRenderer);
		add(categoryComboBox);

		// item combo box
		ArrayList<String> itemComboNames = new ArrayList<>();
		String[] itemComboItems = {"None"};
		DefaultComboBoxModel<String> itemsModel = new DefaultComboBoxModel<String>(itemComboItems);
		itemComboBox = new JComboBox<>(itemComboItems);
		itemComboBox.setBounds(SizeManager.getItemBounds());
		itemComboBox.setFont(FontManager.getFontSearch());
		itemComboBox.setRenderer(categoryComboBoxRenderer);
		add(itemComboBox);

		// grade options combo
		String[] gradeOptionsItems = {"Points Lost", "Percentage"};
		gradeOptionsComboBox = new JComboBox<>(gradeOptionsItems);
		gradeOptionsComboBox.setBounds(SizeManager.getComboBounds());
		gradeOptionsComboBox.setFont(FontManager.getFontFilter());
		gradeOptionsComboBox.setRenderer(categoryComboBoxRenderer);
		add(gradeOptionsComboBox);

		categoryComboBox.addActionListener(e -> {
			int index = categoryComboBox.getSelectedIndex();
			if (index != 0) {
				// index-1 here because index = 0 is "All"
				ArrayList<Item> items = controller.getAllItemsForCourseCategory(controller.getCurrentCourse(),
						index - 1);

				String[] itemComboNames1 = new String[items.size()];
				int i = 0;
				for (Item item : items) {
					itemComboNames1[i++] = item.getItemName();
				}

				DefaultComboBoxModel<String> newComboModel = new DefaultComboBoxModel<String>(itemComboNames1);

				itemComboBox.setModel(newComboModel);
				updateGradesTable(categories);
				add(gradeTableScrollPane);
			}
			if (categoryComboBox.getSelectedIndex() == 0) {
				remove(gradeTableScrollPane);
				add(hintLabel);
				revalidate();
				repaint();
			}
			if (categoryComboBox.getSelectedIndex() != 0) {
				remove(hintLabel);
				add(gradeTableScrollPane);
				revalidate();
				repaint();
			}
		});

		itemComboBox.addActionListener(e -> {
			if (categoryComboBox.getSelectedIndex() != 0) {
				updateGradesTable(categories);
			}
		});

		gradeOptionsComboBox.addActionListener(e -> {
			if (categoryComboBox.getSelectedIndex() != 0) {
				updateGradesTable(categories);
			}
		});

		// back button
		JButton backButton = new JButton("Back");
		backButton.setFont(FontManager.getFontButton());
		backButton.setBounds(SizeManager.getButtonAddBounds());
		backButton.setForeground(ColorManager.getLightColor());
		backButton.setBackground(ColorManager.getPrimaryColor());
		backButton.addActionListener(e -> frame.changePanel(this, new MenuPanel(frame, courseData, controller)));
		add(backButton);

		// save button
		UIManager.put("OptionPane.messageFont", FontManager.getFontLabel());
		UIManager.put("OptionPane.buttonFont", FontManager.getFontLabel());
		saveButton = new JButton("Save");
		saveButton.setFont(FontManager.getFontButton());
		saveButton.setBounds(SizeManager.getButtonViewBounds());
		saveButton.setForeground(ColorManager.getLightColor());
		saveButton.setBackground(ColorManager.getPrimaryColor());
		saveButton.addActionListener(e -> {
			if (categoryComboBox.getSelectedIndex() == 0) {
				JOptionPane.showMessageDialog(null, "Please select a category", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			ArrayList<HashMap<String, String>> values = new ArrayList<>();

			Category category = categories.get(categoryComboBox.getSelectedIndex() - 1);
			Item item = controller.getCurrentCourse().getItemByItemName(category.getCategoryId(),
					(String) itemComboBox.getSelectedItem());

			for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
				int j = 2;
				String value = gradeTableModel.getValueAt(i, j).toString().trim();

				if (Objects.equals(gradeOptionsComboBox.getSelectedItem(), "Points Lost")) {
					if (value.equals("")) {
					} else {
						Double val = Double.parseDouble(value);
						if (val > 0) {
							JOptionPane.showMessageDialog(null, "Points lost means score has to be negative",
									"Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				} else {
					if (value.equals("")) {
						JOptionPane.showMessageDialog(null,
								"Percentage Score cannot be empty. Please enter 100 if full score", "Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					double val = Double.parseDouble(value);
					if (val < 0 || val > 100) {
						JOptionPane.showMessageDialog(null, "Please enter valid percentage values for score",
								"Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

			}

			for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("Buid", gradeTableModel.getValueAt(i, 1).toString());
				for (int j = 2; j < gradeTableModel.getColumnCount(); j++) {
					String value = gradeTableModel.getValueAt(i, j).toString();
					if (gradeTableColumnNames[j].equals("Score")) {
						if (gradeOptionsComboBox.getSelectedItem().equals("Points Lost")) {
							if (value.equals("")) {
								// if value is empty then it means no points lost
								value = Double.toString(item.getMaxPoints());
								map.put("Percentage", Double.toString(100));
							} else {
								double numValue = Double.parseDouble(value);
								double rawScore = item.getMaxPoints() + numValue; // + because numValue is -ve
								double percentage = 100.0 * rawScore / item.getMaxPoints();
								map.put("Percentage", decimalFormat.format(percentage));
								// make value = raw score
								value = Double.toString(rawScore);
							}

						} else {
							// if score entered is percentage
							double percentage = Double.parseDouble(value);
							double rawScore = percentage * item.getMaxPoints() / 100.0;
							map.put("Percentage", decimalFormat.format(percentage));
							// make value = raw score
							value = Double.toString(rawScore);
						}
					}
					map.put(gradeTableColumnNames[j], value);
				}
				values.add(map);
			}

			controller.editGradesForCategoryItemInCourse(controller.getCurrentCourse(), category.getCategoryId(), item.getItemId(),
					values);

			// test to see if edit works. Remove after linking to db
			ArrayList<CourseStudent> students1 = controller.getCurrentCourse().getStudents();
			for (CourseStudent s : students1) {
				System.out.println("Student: " + s.getStudentId());
				HashMap<String, Double> grades = s.getAllGradeEntries();
				for (String key : grades.keySet()) {
					System.out.print(key + ": " + grades.get(key) + "\t");
				}
				System.out.println();
			}

			updateGradesTable(categories);
		});
		add(saveButton);

		// category label
		JLabel categoryLabel = new JLabel("Category : ");
		categoryLabel.setBounds(SizeManager.getLabelCategoryBounds());
		categoryLabel.setFont(FontManager.getFontLabel());
		categoryLabel.setVerticalAlignment(SwingConstants.CENTER);
		categoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(categoryLabel);

		// item label
		JLabel itemLabel = new JLabel("Item : ");
		itemLabel.setBounds(SizeManager.getLabelItemBounds());
		itemLabel.setFont(FontManager.getFontLabel());
		itemLabel.setVerticalAlignment(SwingConstants.CENTER);
		itemLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(itemLabel);

		JLabel gradeOptionsLabel = new JLabel("Options : ");
		gradeOptionsLabel.setBounds(SizeManager.getLabelComboBounds());
		gradeOptionsLabel.setFont(FontManager.getFontLabel());
		gradeOptionsLabel.setVerticalAlignment(SwingConstants.CENTER);
		gradeOptionsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(gradeOptionsLabel);

		if (controller.getCurrentCourse().isFinalized()) {
			lock();
		}

		setVisible(true);
	}

	private String[] convertObjectArrayToString(Object[] arr) {
		String[] str = new String[arr.length];
		int i = 0;
		for (Object o : arr) {
			str[i++] = o.toString();
		}
		return str;
	}

	private void updateGradesTable(ArrayList<Category> categories) {
		Category category = categories.get(categoryComboBox.getSelectedIndex() - 1);

		Item item = controller.getCurrentCourse().getItemByItemName(category.getCategoryId(),
				(String) itemComboBox.getSelectedItem());

		ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();

		String[][] gradeTableRowData = new String[students.size()][];
		for (int i = 0; i < gradeTableRowData.length; i++) {
			gradeTableRowData[i] = new String[gradeTableColumnNames.length];
			gradeTableRowData[i][0] = students.get(i).getFirstName() + " " + students.get(i).getLastName();
			gradeTableRowData[i][1] = students.get(i).getStudentId();
			GradeEntry grade = students.get(i).getGradeEntryForItemInCategory(
					controller.getCurrentCourse().getCourseId(), category.getCategoryId(), item.getItemId());
			if (grade != null) {
				if (Objects.equals(gradeOptionsComboBox.getSelectedItem(), "Points Lost")) {
					double pointsLost = grade.getPointsEarned() - item.getMaxPoints();
					gradeTableRowData[i][2] = Double.toString(pointsLost);
				} else {
					gradeTableRowData[i][2] = decimalFormat.format(grade.getPercentage());
				}
				gradeTableRowData[i][3] = grade.getComments();
			} else {
				gradeTableRowData[i][2] = "";
				gradeTableRowData[i][3] = "";
			}

		}

		if (isEditable) {
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
		gradeTable.setModel(gradeTableModel);
	}

	/**
	 * Generate some random data which is used for testing
	 *
	 * @param gradeTable Grade table.
	 */
	private void generateRandomTestData(JTable gradeTable) {
		Random random = new Random();
		for (int i = 3; i < 100; ++i) {
			for (int j = 1; j < 8; ++j) {
				gradeTable.setValueAt(String.valueOf(random.nextInt(20) + 80), i, j);
			}
		}
		for (int i = 1; i < 8; ++i) {
			List<Integer> rank = new ArrayList<>();
			for (int j = 3; j < 100; ++j) {
				rank.add(Integer.valueOf((String) gradeTable.getValueAt(j, i)));
			}
			double sum = 0;
			for (Integer integer : rank) {
				sum += integer;
			}
			rank.sort(Integer::compareTo);
			gradeTable.setValueAt(String.valueOf(rank.get(rank.size() / 2)), 1, i);
			gradeTable.setValueAt(String.format("%.2f", sum / rank.size()), 0, i);
		}
	}

	/**
	 * Update table.
	 *
	 * @param observable the observable object.
	 * @param argument   an argument passed to the notifyObservers method.
	 */
	@Override
	public void update(Observable observable, Object argument) {
		ArrayList<Category> categories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
		updateGradesTable(categories);
	}

	/**
	 * Lock some components after finalized.
	 */
	private void lock() {
		gradeTable.setEnabled(false);
		saveButton.setEnabled(false);
	}

}
