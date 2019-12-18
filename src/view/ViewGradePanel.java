package view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import javax.swing.JTextArea;
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
import helper.Statistics;
import model.Category;
import model.Course;
import model.CourseStudent;
import model.GradeEntry;
import model.Item;

/**
 * The {@code GradePanel} class represents the panel for viewing or modifying all the grades.
 */
public class ViewGradePanel extends JPanel implements Observer {

	/** The title for the window when GradePanel displays */
	private static final String TITLE = "Grading System - Grade";

	/** Decimal format. */
	private DecimalFormat decimalFormat = new DecimalFormat("##.##");

	/** Controller. */
	private Master controller;

	/** Grade table model. */
	private MyTableModel gradeTableModel;

	/** Grade table. */
	private JTable gradeTable;

	/** Category combo box. */
	private JComboBox<String> categoryComboBox;

	/** Item combo box. */
	private JComboBox<String> itemComboBox;

	/** Grade options combo box. */
	private JComboBox<String> gradeOptionsComboBox;

	/** Grade table columns. */
	private Object[] gradeTableColumnNames;

	/** Whether table is editable or not. */
	private boolean isEditable;

	/** Whether table has comment or not. */
	private boolean hasComment;

	/** Statistics for grades. */
	private List<Double> statisticsGrades;

	/** Statistics for grades array. */
	private Double[] statisticsGradesArray;

	/** Number of active students. */
	private int activeStudentSize = 0;

	/**
	 * Initializes a newly created {@code GradePanel} object
	 */
	public ViewGradePanel(MainFrame frame, String[] courseData, boolean isEditable, Master controller) {
		this.controller = controller;
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.getContentPaneBounds());
		setOpaque(false);
		this.isEditable = isEditable;

		ArrayList<Category> categories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
		ArrayList<String> categoryNames = new ArrayList<>();
		for (Category c : categories) {
			categoryNames.add(c.getCategoryName());
		}

		// grade table
		ArrayList<String> gradeTableColumnNamesList = new ArrayList<>();
		List<Item> allItems = controller.getAllItemsForCourse(controller.getCurrentCourse());
		List<String> allItemNames = new ArrayList<>();
		List<Integer> allCategoryIDs = new ArrayList<>();
		List<Integer> allItemIDs = new ArrayList<>();
		List<Double> allItemWeights = new ArrayList<>();
		List<Double> allCategoryWeights = new ArrayList<>();
		for (Item item : allItems) {
			allItemNames.add(item.getItemName());
			allCategoryIDs.add(item.getCategoryId());
			allItemIDs.add(item.getItemId());
			allItemWeights.add(item.getWeight());
			allCategoryWeights.add(controller.getCurrentCourse().getCategoryById(item.getCategoryId()).getWeight());
		}
		gradeTableColumnNamesList.add("Student Name");
		gradeTableColumnNamesList.add("BU ID");
		gradeTableColumnNamesList.addAll(allItemNames);
		gradeTableColumnNames = gradeTableColumnNamesList.toArray();

		ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();
		for (CourseStudent s : students) {
			if (s.isActive()) {
				activeStudentSize += 1;
			}
		}

		String[][] gradeTableRowData = new String[activeStudentSize][];
		Boolean[] gradeTableRowComment = new Boolean[activeStudentSize];
		statisticsGrades = new ArrayList<Double>();
		int curr = 0;
		for (int i = 0; i < gradeTableRowData.length; i++) {
			if (!students.get(curr).isActive()) {
				i--;
				curr++;
				continue;
			}
			gradeTableRowData[i] = new String[gradeTableColumnNames.length];
			gradeTableRowData[i][0] = students.get(curr).getFirstName() + " " + students.get(curr).getLastName();
			gradeTableRowData[i][1] = students.get(curr).getStudentId();
			double categoryGrade = 0;
			for (int j = 2; j < gradeTableColumnNames.length; j++) {
				String temp = controller.getStudentScoreByID(students.get(curr),
						controller.getCurrentCourse().getCourseId(), allCategoryIDs.get(j - 2), allItemIDs.get(j - 2));
				gradeTableRowData[i][j] = temp.split(" ")[0];
				categoryGrade += Double.parseDouble(gradeTableRowData[i][j]) * allItemWeights.get(j - 2) * allCategoryWeights.get(j - 2);
				double weight = Double.parseDouble(gradeTableRowData[i][j]) * 100;
				gradeTableRowData[i][j] = (int) weight + "%";
				if (temp.split(" ")[1].equals("1")) {
					gradeTableRowComment[i] = true;
				} else {
					gradeTableRowComment[i] = false;
				}
			}
			statisticsGrades.add(categoryGrade * 100);
			curr++;
		}
		statisticsGradesArray = new Double[statisticsGrades.size()];
		Statistics statistics = new Statistics(statisticsGrades.toArray(statisticsGradesArray));
		// statistics label
		JLabel statisticsLabel = new JLabel(statistics.toString());
		statisticsLabel.setBounds(SizeManager.getViewGradeStatBounds());
		statisticsLabel.setFont(FontManager.getFontLabel());
		statisticsLabel.setVerticalAlignment(SwingConstants.TOP);
		statisticsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(statisticsLabel);


		if (isEditable) {
			gradeTableModel = new MyTableModel(gradeTableRowData, gradeTableColumnNames) {

				@Override
				public boolean isCellEditable(int row, int column) {
					return column > 0;
				}
			};
		} else {
			gradeTableModel = new MyTableModel(gradeTableRowData, gradeTableColumnNames) {

				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}

		ArrayList<Integer> commentIndeces = controller.getStudentIndicesForComments(controller.getCurrentCourse(), -1,
				-1);
		for (int i : commentIndeces) {
			gradeTableModel.setRowColor(i, Color.YELLOW);
		}

		gradeTable = new JTable(gradeTableModel);
		gradeTable.setRowHeight(SizeManager.getTableRowHeight());
		gradeTable.setFont(FontManager.getFontTable());
		DefaultTableCellRenderer gradeRender = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
			                                               boolean hasFocus, int row, int column) {
				MyTableModel model = (MyTableModel) table.getModel();
				Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				component.setBackground(model.getRowColor(row));
				return component;
			}
		};
		gradeRender.setHorizontalAlignment(SwingConstants.CENTER);
		gradeRender.setVerticalAlignment(SwingConstants.CENTER);
		gradeTable.setDefaultRenderer(Object.class, gradeRender);
		TableRowSorter<DefaultTableModel> gradeTableRowSorter = new TableRowSorter<>(gradeTableModel);
		gradeTable.setRowSorter(gradeTableRowSorter);

		JTableHeader gradeTableHeader = gradeTable.getTableHeader();
		gradeTableHeader.setFont(gradeTable.getFont());
		gradeTableHeader.setEnabled(false);
		gradeTableHeader.setPreferredSize(new Dimension(gradeTable.getWidth(), gradeTable.getRowHeight()));
		gradeTableHeader.setBackground(ColorManager.getPrimaryColor());
		gradeTableHeader.setForeground(ColorManager.getLightColor());

		JScrollPane gradeTableScrollPane = new JScrollPane(gradeTable);
		gradeTableScrollPane.setBounds(SizeManager.getTableCourseBounds());
		add(gradeTableScrollPane);

		// category combo box
		ArrayList<String> categoryComboNames = new ArrayList<>();
		categoryComboNames.add("All");
		categoryComboNames.addAll(categoryNames);
		Object[] categoryComboBoxItems = categoryComboNames.toArray();
		categoryComboBox = new JComboBox<>(convertObjectArrayToString(categoryComboBoxItems));
		categoryComboBox.setBounds(SizeManager.getViewGradeComboBounds()[1]);
		categoryComboBox.setFont(FontManager.getFontFilter());
		DefaultListCellRenderer categoryComboBoxRenderer = new DefaultListCellRenderer();
		categoryComboBoxRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		categoryComboBox.setRenderer(categoryComboBoxRenderer);
		add(categoryComboBox);

		// item combo box
		String[] itemComboItems = {"All", "None"};
		itemComboBox = new JComboBox<>(itemComboItems);
		itemComboBox.setBounds(SizeManager.getViewGradeComboBounds()[3]);
		itemComboBox.setFont(FontManager.getFontSearch());
		itemComboBox.setRenderer(categoryComboBoxRenderer);
		add(itemComboBox);

		categoryComboBox.addActionListener(e -> {
			int index = categoryComboBox.getSelectedIndex();
			// grade table
			ArrayList<String> gradeTableColumnNamesList12 = new ArrayList<>();
			List<Integer> categoryIDs = new ArrayList<>();
			List<Integer> itemIDs = new ArrayList<>();
			List<Double> allItemWeights12 = new ArrayList<>();
			List<Double> allCategoryWeights12 = new ArrayList<>();

			gradeTableColumnNamesList12.add("Student Name");
			gradeTableColumnNamesList12.add("BUID");
			if (index == 0) {    //All
				ArrayList<String> allItemNames12 = controller.getAllItemNames(controller.getCurrentCourse());
				ArrayList<Item> allItems12 = controller.getAllItemsForCourse(controller.getCurrentCourse());
				gradeTableColumnNamesList12.addAll(allItemNames12);

				String[] itemComboNames1 = new String[2];
				itemComboNames1[0] = "All";
				itemComboNames1[1] = "None";
				DefaultComboBoxModel<String> newComboModel = new DefaultComboBoxModel<String>(itemComboNames1);
				itemComboBox.setModel(newComboModel);

				for (Item item : allItems12) {
					allItemWeights12.add(item.getWeight());
					allCategoryWeights12.add(controller.getCurrentCourse().getCategoryById(item.getCategoryId()).getWeight());
				}
			} else {
				// index-1 here because index=0 is "All"
				ArrayList<Item> items = controller.getAllItemsForCourseCategory(controller.getCurrentCourse(),
						index - 1);

				String[] itemComboNames1 = new String[items.size() + 1];

				int k = 1;
				itemComboNames1[0] = "All";
				for (Item item : items) {
					itemComboNames1[k] = item.getItemName();
					gradeTableColumnNamesList12.add(itemComboNames1[k]);
					categoryIDs.add(item.getCategoryId());
					itemIDs.add(item.getItemId());
					allItemWeights12.add(item.getWeight());
					allCategoryWeights12.add(controller.getCurrentCourse().getCategoryById(item.getCategoryId()).getWeight());
					k++;
				}

				DefaultComboBoxModel<String> newComboModel = new DefaultComboBoxModel<String>(itemComboNames1);
				itemComboBox.setModel(newComboModel);

			}
			gradeTableColumnNames = gradeTableColumnNamesList12.toArray();
			ArrayList<CourseStudent> students13 = controller.getCurrentCourse().getStudents();
			String[][] gradeTableRowData12 = new String[activeStudentSize][];
			Boolean[] gradeTableRowComment1 = new Boolean[activeStudentSize];

			statisticsGrades = new ArrayList<Double>();
			int curr12 = 0;
			for (int i = 0; i < gradeTableRowData12.length; i++) {
				if (!students13.get(curr12).isActive()) {
					i--;
					curr12++;
					continue;
				}
				hasComment = false;
				gradeTableRowData12[i] = new String[gradeTableColumnNames.length];
				gradeTableRowData12[i][0] = students13.get(curr12).getFirstName() + " " + students13.get(curr12).getLastName();
				gradeTableRowData12[i][1] = students13.get(curr12).getStudentId();
				double categoryGrade = 0;
				for (int j = 2; j < gradeTableColumnNames.length; j++) {
					String temp;
					if (index == 0) {
						temp = controller.getStudentScoreByID(students13.get(curr12),
								controller.getCurrentCourse().getCourseId(), allCategoryIDs.get(j - 2),
								allItemIDs.get(j - 2));

					} else {
						temp = controller.getStudentScoreByID(students13.get(curr12),
								controller.getCurrentCourse().getCourseId(), categoryIDs.get(j - 2),
								itemIDs.get(j - 2));
					}
					gradeTableRowData12[i][j] = temp.split(" ")[0];
					if (index == 0) {
						categoryGrade += Double.parseDouble(gradeTableRowData12[i][j]) * allItemWeights12.get(j - 2) * allCategoryWeights12.get(j - 2);
					} else {
						categoryGrade += Double.parseDouble(gradeTableRowData12[i][j]) * allItemWeights12.get(j - 2);
					}
					double weight = Double.parseDouble(gradeTableRowData12[i][j]) * 100;
					gradeTableRowData12[i][j] = (int) weight + "%";
					gradeTableRowComment1[i] = temp.split(" ")[1].equals("1");
				}
				statisticsGrades.add(categoryGrade);
				curr12++;
			}
			statisticsGradesArray = new Double[statisticsGrades.size()];
			Statistics statistics12 = new Statistics(statisticsGrades.toArray(statisticsGradesArray));
			statisticsLabel.setText(statistics12.toString());
			statisticsLabel.repaint();

			if (isEditable) {
				gradeTableModel = new MyTableModel(gradeTableRowData12, gradeTableColumnNames) {

					@Override
					public boolean isCellEditable(int row, int column) {
						return column > 0;
					}
				};
			} else {
				gradeTableModel = new MyTableModel(gradeTableRowData12, gradeTableColumnNames) {

					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
			}
			gradeTable.setModel(gradeTableModel);

			int catIndex = categoryComboBox.getSelectedIndex();
			if (catIndex == 0) {
				catIndex = -1;
			} else {
				catIndex = catIndex - 1;
			}
			int itIndex = itemComboBox.getSelectedIndex();
			if (categoryComboBox.getSelectedIndex() == 0 || itIndex == 0) {
				itIndex = -1;
			} else {
				itIndex -= 1;
			}

			ArrayList<Integer> commentIndeces12 = controller
					.getStudentIndicesForComments(controller.getCurrentCourse(), catIndex, itIndex);
			for (int i = 0; i < gradeTableModel.getRowCount(); i++) {
				for (int j : commentIndeces12) {
					if (i == j) {
						gradeTableModel.setRowColor(i, Color.YELLOW);
					} else {
						gradeTableModel.setRowColor(i, Color.white);
					}
				}
			}
			for (int i : commentIndeces12) {
				gradeTableModel.setRowColor(i, Color.YELLOW);
			}

		});

		itemComboBox.addActionListener(e -> {
			int categoryIndex = categoryComboBox.getSelectedIndex();
			int itemIndex = itemComboBox.getSelectedIndex();
			ArrayList<String> gradeTableColumnNamesList1 = new ArrayList<>();
			gradeTableColumnNamesList1.add("Student Name");
			gradeTableColumnNamesList1.add("BU ID");
			ArrayList<Category> allCategories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
			List<String> allItemNames1 = new ArrayList<>();
			List<Integer> allCategoryIDs1 = new ArrayList<>();
			List<Integer> allItemIDs1 = new ArrayList<>();
			List<Double> allItemWeights1 = new ArrayList<>();
			List<Double> allCategoryWeights1 = new ArrayList<>();
			if (itemIndex == 0) {
				if (categoryIndex == 0) {
					List<Item> allItems1 = controller.getAllItemsForCourse(controller.getCurrentCourse());
					for (Item item : allItems1) {
						allItemNames1.add(item.getItemName());
						allCategoryIDs1.add(item.getCategoryId());
						allItemIDs1.add(item.getItemId());
						allItemWeights1.add(item.getWeight());
						allCategoryWeights1.add(controller.getCurrentCourse().getCategoryById(item.getCategoryId()).getWeight());
					}
				} else {
					ArrayList<Item> allItems1 = controller
							.getAllItemsForCourseCategory(controller.getCurrentCourse(), categoryIndex - 1);
					for (Item item : allItems1) {
						allItemNames1.add(item.getItemName());
						allCategoryIDs1.add(item.getCategoryId());
						allItemIDs1.add(item.getItemId());
						allItemWeights1.add(item.getWeight());
						allCategoryWeights1.add(controller.getCurrentCourse().getCategoryById(item.getCategoryId()).getWeight());
					}
				}
			} else if (categoryIndex == 0 && itemIndex == 1) {
				for (Category c : allCategories) {
					allItemNames1.add(c.getCategoryName());
					allCategoryIDs1.add(c.getCategoryId());
					allCategoryWeights1.add(c.getWeight());
				}
			} else {
				Item item = controller
						.getAllItemsForCourseCategory(controller.getCurrentCourse(), categoryIndex - 1)
						.get(itemIndex - 1);
				allItemNames1.add(item.getItemName());
				allCategoryIDs1.add(item.getCategoryId());
				allItemIDs1.add(item.getItemId());

			}
			gradeTableColumnNamesList1.addAll(allItemNames1);
			gradeTableColumnNames = gradeTableColumnNamesList1.toArray();
			ArrayList<CourseStudent> students12 = controller.getCurrentCourse().getStudents();
			String[][] gradeTableRowData1 = new String[activeStudentSize][];
			statisticsGrades = new ArrayList<Double>();
			int curr1 = 0;
			for (int i = 0; i < gradeTableRowData1.length; i++) {
				if (!students12.get(curr1).isActive()) {
					i--;
					curr1++;
					continue;
				}
				hasComment = false;
				gradeTableRowData1[i] = new String[gradeTableColumnNames.length];
				gradeTableRowData1[i][0] = students12.get(curr1).getFirstName() + " " + students12.get(curr1).getLastName();
				gradeTableRowData1[i][1] = students12.get(curr1).getStudentId();
				double categoryGrade = 0;
				for (int j = 2; j < gradeTableColumnNames.length; j++) {
					String temp;
					if (allItemIDs1.size() > 0) {
						temp = controller.getStudentScoreByID(students12.get(curr1),
								controller.getCurrentCourse().getCourseId(), allCategoryIDs1.get(j - 2),
								allItemIDs1.get(j - 2));
					} else {
						temp = controller.getStudentScoreByID(students12.get(curr1),
								controller.getCurrentCourse().getCourseId(), allCategoryIDs1.get(j - 2));
					}
					gradeTableRowData1[i][j] = temp.split(" ")[0];
					if (categoryIndex == 0) {
						if (itemIndex == 0) {
							categoryGrade += Double.parseDouble(gradeTableRowData1[i][j]) * allItemWeights1.get(j - 2) * allCategoryWeights1.get(j - 2);
						} else {
							categoryGrade += Double.parseDouble(gradeTableRowData1[i][j]) * allCategoryWeights1.get(j - 2);
						}
					} else {
						if (itemIndex == 0) {
							categoryGrade += Double.parseDouble(gradeTableRowData1[i][j]) * allItemWeights1.get(j - 2);
						} else {
							statisticsGrades.add(Double.parseDouble(gradeTableRowData1[i][j]));
						}
					}
					double weight = Double.parseDouble(gradeTableRowData1[i][j]) * 100;
					gradeTableRowData1[i][j] = (int) weight + "%";
					if (temp.split(" ")[1].equals("1")) {
						hasComment = true;
					}
				}
				if (categoryGrade != 0) {
					statisticsGrades.add(categoryGrade);
				}
				curr1++;
			}
			statisticsGradesArray = new Double[statisticsGrades.size()];
			Statistics statistics1 = new Statistics(statisticsGrades.toArray(statisticsGradesArray));
			statisticsLabel.setText(statistics1.toString());
			statisticsLabel.repaint();

			if (isEditable) {
				gradeTableModel = new MyTableModel(gradeTableRowData1, gradeTableColumnNames) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return column > 0;
					}
				};
			} else {
				gradeTableModel = new MyTableModel(gradeTableRowData1, gradeTableColumnNames) {
					@Override
					public boolean isCellEditable(int row, int column) {
						return false;
					}
				};
			}
			gradeTable.setModel(gradeTableModel);

			int catIndex = categoryComboBox.getSelectedIndex();
			if (catIndex == 0) {
				catIndex = -1;
			} else {
				catIndex = catIndex - 1;
			}
			int itIndex = itemComboBox.getSelectedIndex();
			if (categoryComboBox.getSelectedIndex() == 0 || itIndex == 0) {
				itIndex = -1;
			} else {
				itIndex -= 1;
			}

			ArrayList<Integer> commentIndeces1 = controller
					.getStudentIndicesForComments(controller.getCurrentCourse(), catIndex, itIndex);
			for (int i : commentIndeces1) {
				gradeTableModel.setRowColor(i, Color.YELLOW);
			}
		});
		gradeTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					int selectedRow = gradeTable.getSelectedRow();
					int catIndex = categoryComboBox.getSelectedIndex();
					if (catIndex == 0) {
						catIndex = -1;
					} else {
						catIndex = catIndex - 1;
					}
					int itIndex = itemComboBox.getSelectedIndex();
					if (categoryComboBox.getSelectedIndex() == 0 || itIndex == 0) {
						itIndex = -1;
					} else {
						itIndex -= 1;
					}

					String comments = controller.getCommentsForRowIndex(controller.getCurrentCourse(), selectedRow,
							catIndex, itIndex);

					if (!comments.equals("")) {
						JTextArea text = new JTextArea(comments);
						text.setEditable(false);
						JOptionPane.showMessageDialog(null, comments, "Comments For Studnet",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
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
		JButton saveButton = new JButton("Save");
		saveButton.setFont(FontManager.getFontButton());
		saveButton.setBounds(SizeManager.getButtonViewBounds());
		saveButton.setForeground(ColorManager.getLightColor());
		saveButton.setBackground(ColorManager.getPrimaryColor());
		saveButton.addActionListener(e -> {
			if (categoryComboBox.getSelectedIndex() == 0) {
				JOptionPane.showMessageDialog(null, "Please select a category", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			ArrayList<HashMap<String, String>> values = new ArrayList<HashMap<String, String>>();

			Category cat = categories.get(categoryComboBox.getSelectedIndex() - 1);
			Item it = controller.getCurrentCourse().getItemByItemName(cat.getCategoryId(),
					(String) itemComboBox.getSelectedItem());

			DefaultTableModel duplicate = gradeTableModel;
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
								map.put("Percentage", decimalFormat.format(percentage));
								// make value = raw score
								value = Double.toString(rawScore);
							}

						} else {
							// if score entered is percentage
							double percentage = Double.parseDouble(value);
							double rawScore = percentage * it.getMaxPoints() / 100.0;
							map.put("Percentage", decimalFormat.format(percentage));
							// make value = raw score
							value = Double.toString(rawScore);
						}
					}
					map.put((String) gradeTableColumnNames[j], value);
				}
				values.add(map);
			}

			controller.editGradesForCategoryItemInCourse(controller.getCurrentCourse(), cat.getCategoryId(), it.getItemId(),
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

		Course course = controller.getCurrentCourse();

		// title label
		JLabel titleLabel = new JLabel(String.format("%s - %s - %s", course.getCourseNumber(), course.getCourseName(), course.getTerm()));
		titleLabel.setBounds(SizeManager.getViewGradeTitleBounds());
		titleLabel.setFont(FontManager.getFontLabel());
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.CENTER);
		add(titleLabel);

		// category label
		JLabel categoryLabel = new JLabel("Category : ");
		categoryLabel.setBounds(SizeManager.getViewGradeComboBounds()[0]);
		categoryLabel.setFont(FontManager.getFontLabel());
		categoryLabel.setVerticalAlignment(SwingConstants.CENTER);
		categoryLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(categoryLabel);

		// item label
		JLabel itemLabel = new JLabel("Item : ");
		itemLabel.setBounds(SizeManager.getViewGradeComboBounds()[2]);
		itemLabel.setFont(FontManager.getFontLabel());
		itemLabel.setVerticalAlignment(SwingConstants.CENTER);
		itemLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		add(itemLabel);

		setVisible(true);
	}

	/**
	 * Convert Object[] to String[].
	 *
	 * @param array Object array.
	 * @return String array.
	 */
	private String[] convertObjectArrayToString(Object[] array) {
		String[] str = new String[array.length];
		int i = 0;
		for (Object o : array) {
			str[i++] = o.toString();
		}
		return str;
	}

	/**
	 * Update grade table.
	 *
	 * @param categories Categories.
	 */
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
			gradeTableModel = new MyTableModel(gradeTableRowData, gradeTableColumnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return column > 0;
				}
			};
		} else {
			gradeTableModel = new MyTableModel(gradeTableRowData, gradeTableColumnNames) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
		}
		gradeTable.setModel(gradeTableModel);

		gradeTable.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					int selectedRow = gradeTable.getSelectedRow();
					int catIndex = categoryComboBox.getSelectedIndex();
					if (catIndex == 0) {
						catIndex = -1;
					} else {
						catIndex = catIndex - 1;
					}
					int itIndex = itemComboBox.getSelectedIndex();
					if (categoryComboBox.getSelectedIndex() == 0 || itIndex == 0) {
						itIndex = -1;
					} else {
						itIndex -= 1;
					}

					String comments = controller.getCommentsForRowIndex(controller.getCurrentCourse(), selectedRow,
							catIndex, itIndex);

					if (!comments.equals("")) {
						JTextArea text = new JTextArea(comments);
						text.setEditable(false);
						Object[] fields = {"Comments", text};
						JOptionPane.showMessageDialog(null, comments, "Comments For Studnet",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});
	}

	/**
	 * Generate some random data which is used for testing
	 *
	 * @param tableGrade Grade table.
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
		ArrayList<Category> categories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
		updateGradesTable(categories);
		gradeTable.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() >= 2) {
					int selectedRow = gradeTable.getSelectedRow();
					int catIndex = categoryComboBox.getSelectedIndex();
					if (catIndex == 0) {
						catIndex = -1;
					} else {
						catIndex = catIndex - 1;
					}
					int itIndex = itemComboBox.getSelectedIndex();
					if (categoryComboBox.getSelectedIndex() == 0 || itIndex == 0) {
						itIndex = -1;
					} else {
						itIndex -= 1;
					}

					String comments = controller.getCommentsForRowIndex(controller.getCurrentCourse(), selectedRow,
							catIndex, itIndex);

					if (!comments.equals("")) {
						JTextArea text = new JTextArea(comments);
						text.setEditable(false);
						JOptionPane.showMessageDialog(null, comments, "Comments For Studnet",
								JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		});

	}

	static class MyTableModel extends DefaultTableModel {
		List<Color> rowColor;

		public MyTableModel(String[][] gradeTableRowData, Object[] gradeTableColumnNames) {
			super(gradeTableRowData, gradeTableColumnNames);
			rowColor = new ArrayList<>();
			for (int i = 0; i < gradeTableRowData.length; i++) {
				rowColor.add(Color.white);
			}
		}

		public void setRowColor(int row, Color c) {
			rowColor.set(row, c);
			fireTableRowsUpdated(row, row);
		}

		public Color getRowColor(int row) {
			return rowColor.get(row);
		}
	}

}