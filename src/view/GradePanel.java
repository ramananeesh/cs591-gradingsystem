package view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * The {@code GradePanel} class represents the panel for viewing or modifying
 * all the grades
 */
public class GradePanel extends JPanel implements Observer {
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
	private String[] gradeTableColumnNames;
	private boolean editable;

	DecimalFormat df = new DecimalFormat("##.##");
	private final JButton saveButton;

	/**
	 * Initializes a newly created {@code GradePanel} object
	 */
	public GradePanel(MainFrame frame, String[] courseData, boolean editable, Master controller) {
		this.frame = frame;
		this.controller = controller;
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.contentPaneBounds);
		setOpaque(false);
		this.editable = editable;

		// grade table
		ArrayList<String> gradeTableColumnNamesList = new ArrayList<>();
		ArrayList<Category> categories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
		ArrayList<String> categoryNames = new ArrayList<>();
		for (Category c : categories) {
			categoryNames.add(c.getFieldName());
		}

		Course currCourse = controller.getCurrentCourse();
		ArrayList<String> allItemNames = controller.getAllItemNames(controller.getCurrentCourse());
		gradeTableColumnNames = new String[]{"Student Name", "BUID", "Score", "Comments"};

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
		gradeTableHeader.setBackground(ColorManager.primaryColor);
		gradeTableHeader.setForeground(ColorManager.lightColor);
		JScrollPane gradeTableScrollPane = new JScrollPane(gradeTable);
		gradeTableScrollPane.setBounds(SizeManager.tableCourseBounds);
//		add(gradeTableScrollPane);

		JLabel hintLabel = new JLabel("Please select a category and an item that you want to grade.");
		hintLabel.setFont(FontManager.fontLabel);
		hintLabel.setHorizontalAlignment(SwingConstants.CENTER);
		hintLabel.setVerticalAlignment(SwingConstants.CENTER);
		hintLabel.setBounds(SizeManager.tableCourseBounds);
		add(hintLabel);

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
		String[] itemComboItems = {"None"};
		itemsModel = new DefaultComboBoxModel<String>(itemComboItems);
		itemComboBox = new JComboBox<>(itemComboItems);
		itemComboBox.setBounds(SizeManager.itemBounds);
		itemComboBox.setFont(FontManager.fontSearch);
		itemComboBox.setRenderer(categoryComboBoxRenderer);
		add(itemComboBox);

		// grade options combo
		String[] gradeOptionsItems = {"Points Lost", "Percentage"};
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
			}
		});

		itemComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (categoryComboBox.getSelectedIndex() != 0) {
					updateGradesTable(categories);
				}
				// TODO when itemComboBox.getSelectedIndex() == 0, item is not "None"
//				if (categoryComboBox.getSelectedIndex() == 0 || itemComboBox.getSelectedIndex() == 0) {
//					remove(gradeTableScrollPane);
//				}
//				if (categoryComboBox.getSelectedIndex() != 0 && itemComboBox.getSelectedIndex() != 0) {
//					add(gradeTableScrollPane);
//				}

			}
		});

		gradeOptionsComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (categoryComboBox.getSelectedIndex() != 0) {
					updateGradesTable(categories);
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
		saveButton = new JButton("Save");
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

				DefaultTableModel duplicate = gradeTableModel;
				for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
					int j = 2;
					String value = gradeTableModel.getValueAt(i, j).toString().trim();

					if (gradeOptionsComboBox.getSelectedItem().equals("Points Lost")) {
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
						Double val = Double.parseDouble(value);
						if (val < 0 || val > 100) {
							JOptionPane.showMessageDialog(null, "Please enter valid percentage values for score",
									"Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					}

				}

				for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
					// System.out.println(gradeTableModel.getValueAt(count, 0).toString());
					HashMap<String, String> map = new HashMap<String, String>();
					map.put("Buid", gradeTableModel.getValueAt(i, 1).toString());
					for (int j = 2; j < gradeTableModel.getColumnCount(); j++) {
						String value = gradeTableModel.getValueAt(i, j).toString();
						if (gradeTableColumnNames[j].equals("Score")) {
							if (gradeOptionsComboBox.getSelectedItem().equals("Points Lost")) {
								if (value.equals("")) {
									// if value is empty then it means no points lost
									value = Double.toString(it.getMaxPoints());
									map.put("Percentage", Double.toString(100));
								} else {
									double numValue = Double.parseDouble(value);
									double rawScore = it.getMaxPoints() + numValue; // + because numValue is -ve
									double percentage = 100.0 * rawScore / it.getMaxPoints();
									map.put("Percentage", df.format(percentage));
									// make value = raw score
									value = Double.toString(rawScore);
								}

							} else {
								// if score entered is percentage
								double percentage = Double.parseDouble(value);
								double rawScore = percentage * it.getMaxPoints() / 100.0;
								map.put("Percentage", df.format(percentage));
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
						System.out.print(key + ": " + grades.get(key) + "\t");
					}
					System.out.println();
				}

				updateGradesTable(categories);
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

		if (controller.getCurrentCourse().isFinalized()) {
			lock();
		}

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

	public void updateGradesTable(ArrayList<Category> categories) {
		Category category = categories.get(categoryComboBox.getSelectedIndex() - 1);

		Item item = controller.getCurrentCourse().getItemByItemName(category.getId(),
				(String) itemComboBox.getSelectedItem());

		ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();

		String[][] gradeTableRowData = new String[students.size()][];
		for (int i = 0; i < gradeTableRowData.length; i++) {
			gradeTableRowData[i] = new String[gradeTableColumnNames.length];
			gradeTableRowData[i][0] = students.get(i).getFname() + " " + students.get(i).getLname();
			gradeTableRowData[i][1] = students.get(i).getBuid();
			GradeEntry grade = students.get(i).getGradeEntryForItemInCategory(
					controller.getCurrentCourse().getCourseId(), category.getId(), item.getId());
			if (grade != null) {
				if (gradeOptionsComboBox.getSelectedItem().equals("Points Lost")) {
					double pointsLost = grade.getPointsEarned() - item.getMaxPoints();
					gradeTableRowData[i][2] = Double.toString(pointsLost);
				} else {
					gradeTableRowData[i][2] = df.format(grade.getPercentage());
				}
				gradeTableRowData[i][3] = grade.getComments();
			} else {
				gradeTableRowData[i][2] = "";
				gradeTableRowData[i][3] = "";
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
		gradeTable.setModel(gradeTableModel);
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

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		ArrayList<Category> categories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
		updateGradesTable(categories);
	}

	private void lock() {
		gradeTable.setEnabled(false);
		saveButton.setEnabled(false);
	}
}
