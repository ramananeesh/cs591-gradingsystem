package view;

import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import controller.Master;
import model.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.text.DecimalFormat;
import java.util.List;

/**
 * The {@code GradePanel} class represents the panel for viewing or modifying
 * all the grades
 */
public class ViewGradePanel extends JPanel implements Observer {
	/** The title for the window when GradePanel displays */
	private static final String TITLE = "Grading System - Grade";
	private Master controller;

	/** The frame which contains this panel. */
	private MainFrame frame;
	private MyTableModel gradeTableModel;
	private JTable gradeTable;
	private JComboBox<String> categoryComboBox;
	private JComboBox<String> itemComboBox;
	private JComboBox<String> gradeOptionsComboBox;
	private DefaultComboBoxModel<String> itemsModel;
	private Object[] gradeTableColumnNames;
	private boolean editable;
	private boolean hasComment;

	DecimalFormat df = new DecimalFormat("##.##");

	/**
	 * Initializes a newly created {@code GradePanel} object
	 */
	public ViewGradePanel(MainFrame frame, String[] courseData, boolean editable, Master controller) {
		this.frame = frame;
		this.controller = controller;
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.panelBounds);
		setOpaque(false);
		this.editable = editable;

		ArrayList<Category> categories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
		ArrayList<String> categoryNames = new ArrayList<>();
		for (Category c : categories) {
			categoryNames.add(c.getFieldName());
		}

		// grade table
		ArrayList<String> gradeTableColumnNamesList = new ArrayList<>();
		List<Item> allItems = controller.getAllItemsForCourse(controller.getCurrentCourse());
		List<String> allItemNames = new ArrayList<>();
		List<Integer> allCategoryIDs = new ArrayList<>();
		List<Integer> allItemIDs = new ArrayList<>();
		for (Item item : allItems) {
			allItemNames.add(item.getFieldName());
			allCategoryIDs.add(item.getCategoryId());
			allItemIDs.add(item.getId());
		}
//        ArrayList<String> allItemNames = controller.getAllItemNames(controller.getCurrentCourse());
		gradeTableColumnNamesList.add("Student Name");
		gradeTableColumnNamesList.add("BUID");
		gradeTableColumnNamesList.addAll(allItemNames);
		gradeTableColumnNames = gradeTableColumnNamesList.toArray();

		ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();

		String[][] gradeTableRowData = new String[students.size()][];
		Boolean[] gradeTableRowComment = new Boolean[students.size()];
		for (int i = 0; i < gradeTableRowData.length; i++) {
			gradeTableRowData[i] = new String[gradeTableColumnNames.length];
			gradeTableRowData[i][0] = students.get(i).getFname() + " " + students.get(i).getLname();
			gradeTableRowData[i][1] = students.get(i).getBuid();
			for (int j = 2; j < gradeTableColumnNames.length; j++) {
				String temp = controller.getStudentScoreByID(students.get(i),
						controller.getCurrentCourse().getCourseId(), allCategoryIDs.get(j - 2), allItemIDs.get(j - 2));
				gradeTableRowData[i][j] = temp.split(" ")[0];
				if (temp.split(" ")[1] == "1") {
					gradeTableRowComment[i] = true;
				} else {
					gradeTableRowComment[i] = false;
				}
			}
		}

		if (editable) {
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

		ArrayList<Integer> commentIndeces = controller.getStudentIndecesForComments(controller.getCurrentCourse(), -1,
				-1);
		for (int i : commentIndeces) {
			gradeTableModel.setRowColor(i, Color.YELLOW);
		}

		gradeTable = new JTable(gradeTableModel);
		gradeTable.setRowHeight(SizeManager.tableRowHeight);
		gradeTable.setFont(FontManager.fontTable);
		DefaultTableCellRenderer gradeTableRender = new DefaultTableCellRenderer();
		DefaultTableCellRenderer gradeRender = new DefaultTableCellRenderer() {
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				MyTableModel model = (MyTableModel) table.getModel();
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				c.setBackground(model.getRowColor(row));
				return c;
			}
		};
		gradeTableRender.setHorizontalAlignment(SwingConstants.CENTER);
		gradeTableRender.setVerticalAlignment(SwingConstants.CENTER);
		gradeTable.setDefaultRenderer(Object.class, gradeRender);
		TableRowSorter<DefaultTableModel> gradeTableRowSorter = new TableRowSorter<>(gradeTableModel);
		gradeTable.setRowSorter(gradeTableRowSorter);
		gradeTable.getTableHeader().setFont(gradeTable.getFont());
		JScrollPane gradeTableScrollPane = new JScrollPane(gradeTable);
		gradeTableScrollPane.setBounds(SizeManager.tableCourseBounds);
		add(gradeTableScrollPane);

		// category combo box
		ArrayList<String> categoryComboNames = new ArrayList<>();
		categoryComboNames.add("All");
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
		String[] itemComboItems = { "All", "None" };
		itemsModel = new DefaultComboBoxModel<String>(itemComboItems);
		itemComboBox = new JComboBox<>(itemComboItems);
		itemComboBox.setBounds(SizeManager.itemBounds);
		itemComboBox.setFont(FontManager.fontSearch);
		itemComboBox.setRenderer(categoryComboBoxRenderer);
		add(itemComboBox);

		categoryComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int index = categoryComboBox.getSelectedIndex();
				// grade table
				ArrayList<String> gradeTableColumnNamesList = new ArrayList<>();
				List<Integer> categoryIDs = new ArrayList<>();
				List<Integer> itemIDs = new ArrayList<>();

				gradeTableColumnNamesList.add("Student Name");
				gradeTableColumnNamesList.add("BUID");
				if (index == 0) {
					ArrayList<String> allItemNames = controller.getAllItemNames(controller.getCurrentCourse());
					gradeTableColumnNamesList.addAll(allItemNames);

					String[] itemComboNames = new String[2];
					itemComboNames[0] = "All";
					itemComboNames[1] = "None";
					DefaultComboBoxModel<String> newComboModel = new DefaultComboBoxModel<String>(itemComboNames);

					itemComboBox.setModel(newComboModel);
				} else {
					// index-1 here because index=0 is "All"
					ArrayList<Item> items = controller.getAllItemsForCourseCategory(controller.getCurrentCourse(),
							index - 1);

					String[] itemComboNames = new String[items.size() + 1];

					int k = 1;
					itemComboNames[0] = "All";
					for (Item item : items) {
						itemComboNames[k] = item.getFieldName();
						gradeTableColumnNamesList.add(itemComboNames[k]);
						categoryIDs.add(item.getCategoryId());
						itemIDs.add(item.getId());
						k++;
					}

					DefaultComboBoxModel<String> newComboModel = new DefaultComboBoxModel<String>(itemComboNames);

					itemComboBox.setModel(newComboModel);

				}
				gradeTableColumnNames = gradeTableColumnNamesList.toArray();
				ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();
				String[][] gradeTableRowData = new String[students.size()][];
				Boolean[] gradeTableRowComment = new Boolean[students.size()];

				for (int i = 0; i < gradeTableRowData.length; i++) {
					hasComment = false;
					gradeTableRowData[i] = new String[gradeTableColumnNames.length];
					gradeTableRowData[i][0] = students.get(i).getFname() + " " + students.get(i).getLname();
					gradeTableRowData[i][1] = students.get(i).getBuid();
					for (int j = 2; j < gradeTableColumnNames.length; j++) {
						String temp;
						if (index == 0) {
							temp = controller.getStudentScoreByID(students.get(i),
									controller.getCurrentCourse().getCourseId(), allCategoryIDs.get(j - 2),
									allItemIDs.get(j - 2));

						} else {
							temp = controller.getStudentScoreByID(students.get(i),
									controller.getCurrentCourse().getCourseId(), categoryIDs.get(j - 2),
									itemIDs.get(j - 2));
						}
						gradeTableRowData[i][j] = temp.split(" ")[0];
						if (temp.split(" ")[1] == "1") {
							gradeTableRowComment[i] = true;
						} else {
							gradeTableRowComment[i] = false;
						}
					}
				}
				if (editable) {
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

				int catIndex = categoryComboBox.getSelectedIndex();
				if(catIndex==0) {
					catIndex=-1;
				}
				else {
					catIndex=catIndex-1;
				}
				int itIndex = itemComboBox.getSelectedIndex();
				if(categoryComboBox.getSelectedIndex()==0 || itIndex==0) {
					itIndex = -1;
				}
				else {
					itIndex -=1;
				}
				
				ArrayList<Integer> commentIndeces = controller
						.getStudentIndecesForComments(controller.getCurrentCourse(),catIndex, itIndex);
				for(int i =0;i<gradeTableModel.getRowCount();i++) {
					for(int j: commentIndeces) {
						if(i==j) {
							gradeTableModel.setRowColor(i, Color.YELLOW);
						}
						else {
							gradeTableModel.setRowColor(i, Color.white);
						}
					}
				}
				for (int i : commentIndeces) {
					gradeTableModel.setRowColor(i, Color.YELLOW);
				}

			}
		});

		itemComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int categoryIndex = categoryComboBox.getSelectedIndex();
				int itemIndex = itemComboBox.getSelectedIndex();
				ArrayList<String> gradeTableColumnNamesList = new ArrayList<>();
				gradeTableColumnNamesList.add("Student Name");
				gradeTableColumnNamesList.add("BUID");
				// TODO Auto-generated method stub
				ArrayList<Category> allCategories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
				List<String> allItemNames = new ArrayList<>();
				List<Integer> allCategoryIDs = new ArrayList<>();
				List<Integer> allItemIDs = new ArrayList<>();
				if (itemIndex == 0) {
					if (categoryIndex == 0) {
						List<Item> allItems = controller.getAllItemsForCourse(controller.getCurrentCourse());
						for (Item item : allItems) {
							allItemNames.add(item.getFieldName());
							allCategoryIDs.add(item.getCategoryId());
							allItemIDs.add(item.getId());
						}
					} else {
						ArrayList<Item> allItems = controller
								.getAllItemsForCourseCategory(controller.getCurrentCourse(), categoryIndex - 1);
						for (Item item : allItems) {
							allItemNames.add(item.getFieldName());
							allCategoryIDs.add(item.getCategoryId());
							allItemIDs.add(item.getId());
						}
					}
				} else if (categoryIndex == 0 && itemIndex == 1) {
					for (Category c : allCategories) {
						allItemNames.add(c.getFieldName());
						allCategoryIDs.add(c.getId());
					}
				} else {
					Item item = controller
							.getAllItemsForCourseCategory(controller.getCurrentCourse(), categoryIndex - 1)
							.get(itemIndex - 1);
					allItemNames.add(item.getFieldName());
					allCategoryIDs.add(item.getCategoryId());
					allItemIDs.add(item.getId());

				}
				gradeTableColumnNamesList.addAll(allItemNames);
				gradeTableColumnNames = gradeTableColumnNamesList.toArray();
				ArrayList<CourseStudent> students = controller.getCurrentCourse().getStudents();
				String[][] gradeTableRowData = new String[students.size()][];
				for (int i = 0; i < gradeTableRowData.length; i++) {
					hasComment = false;
					gradeTableRowData[i] = new String[gradeTableColumnNames.length];
					gradeTableRowData[i][0] = students.get(i).getFname() + " " + students.get(i).getLname();
					gradeTableRowData[i][1] = students.get(i).getBuid();
					for (int j = 2; j < gradeTableColumnNames.length; j++) {
						String temp;
						if (allItemIDs.size() > 0) {
							temp = controller.getStudentScoreByID(students.get(i),
									controller.getCurrentCourse().getCourseId(), allCategoryIDs.get(j - 2),
									allItemIDs.get(j - 2));
						} else {
							temp = controller.getStudentScoreByID(students.get(i),
									controller.getCurrentCourse().getCourseId(), allCategoryIDs.get(j - 2));
						}
						gradeTableRowData[i][j] = temp.split(" ")[0];
						if (temp.split(" ")[1] == "1") {
							hasComment = true;
						}
					}
				}
				if (editable) {
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
				
				int catIndex = categoryComboBox.getSelectedIndex();
				if(catIndex==0) {
					catIndex=-1;
				}
				else {
					catIndex=catIndex-1;
				}
				int itIndex = itemComboBox.getSelectedIndex();
				if(categoryComboBox.getSelectedIndex()==0 || itIndex==0) {
					itIndex = -1;
				}
				else {
					itIndex -=1;
				}
				
				ArrayList<Integer> commentIndeces = controller
						.getStudentIndecesForComments(controller.getCurrentCourse(),catIndex, itIndex);
				for (int i : commentIndeces) {
					gradeTableModel.setRowColor(i, Color.YELLOW);
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
						map.put((String) gradeTableColumnNames[j], value);
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

	static class MyTableModel extends DefaultTableModel {
		List<Color> rowColor;

		public MyTableModel(String[][] gradeTableRowData, Object[] gradeTableColumnNames) {
			super(gradeTableRowData, gradeTableColumnNames);
			rowColor = new ArrayList<Color>();
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

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		ArrayList<Category> categories = controller.getAllCategoriesForCourse(controller.getCurrentCourse());
		updateGradesTable(categories);
	}
}
