package view;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import controller.Master;
import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;
import helper.Statistics;
import model.Course;

public class FinializePanel extends JPanel implements Observer {

	/** The title for the window when GradePanel displays */
	private static final String TITLE = "Grading System - Finalize";
	private DefaultTableModel gradeTableModel;
	private JTable gradeTable;
	private Master controller;

	public FinializePanel(MainFrame frame, Master controller) {
		this.controller = controller;
		this.controller.addObserver(this);
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.contentPaneBounds);
		setOpaque(false);

		// TODO data for test
		Course course = controller.getCurrentCourse();
		double[] grades = new double[100];
		Random random = new Random();
		for (int i = 0; i < grades.length; ++i) {
			grades[i] = 80 + (random.nextDouble() - 0.5) * 40;
		}
		Statistics statistics = new Statistics(grades);

		// title label
		JLabel titleLabel = new JLabel(
				String.format("%s - %s - %s", course.getCourseNumber(), course.getCourseName(), course.getTerm()));
		titleLabel.setBounds(SizeManager.finalizeTitleLabelBounds);
		titleLabel.setFont(FontManager.fontLabel);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		add(titleLabel);

		// statistics label
		JLabel statisticsLabel = new JLabel(statistics.toString());
		statisticsLabel.setBounds(SizeManager.labelGradeStatisticsBounds);
		statisticsLabel.setFont(FontManager.fontLabel);
		statisticsLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		statisticsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(statisticsLabel);

		Object[][] gradeTableRowData = controller.getFinalGradesData(controller.getCurrentCourse());
		Object[] gradeTableColumnNames = { "Name", "ID", "Actual Percentage", "Curved Percentage", "Grade" };
		gradeTableModel = new DefaultTableModel(gradeTableRowData, gradeTableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column > 3;
			}
		};
		gradeTable = new JTable(gradeTableModel);
		gradeTable.setRowHeight(SizeManager.tableRowHeight);
		gradeTable.setFont(FontManager.fontTable);
		DefaultTableCellRenderer gradeTableRender = new DefaultTableCellRenderer();
		gradeTableRender.setHorizontalAlignment(SwingConstants.CENTER);
		gradeTableRender.setVerticalAlignment(SwingConstants.CENTER);
		gradeTable.setDefaultRenderer(Object.class, gradeTableRender);
		JTableHeader gradeTableHeader = gradeTable.getTableHeader();
		gradeTableHeader.setBackground(ColorManager.primaryColor);
		gradeTableHeader.setForeground(ColorManager.lightColor);
		gradeTableHeader.setFont(gradeTable.getFont());
		gradeTableHeader.setEnabled(false);
		JScrollPane gradeTableScrollPane = new JScrollPane(gradeTable);
		gradeTableScrollPane.setBounds(SizeManager.tableCourseBounds);
		add(gradeTableScrollPane);

		JButton backButton = new JButton("Back");
		backButton.setFont(FontManager.fontButton);
		backButton.setBounds(SizeManager.finalizeButtonBackBounds);
		backButton.setForeground(ColorManager.lightColor);
		backButton.setBackground(ColorManager.primaryColor);
		backButton.addActionListener(e -> {
			frame.changePanel(this, new MenuPanel(frame,
					new String[] { course.getCourseNumber(), course.getCourseName(), course.getTerm() }, controller));
		});
		add(backButton);

		JButton curveButton = new JButton("Curve");
		curveButton.setFont(FontManager.fontButton);
		curveButton.setBounds(SizeManager.finalizeButtonCurveBounds);
		curveButton.setForeground(ColorManager.lightColor);
		curveButton.setBackground(ColorManager.primaryColor);
		curveButton.addActionListener(e -> {
			// TODO curve
			JTextField percentageTextField = new JTextField();
			Object[] fields = { "Percentage: ", percentageTextField };
			UIManager.put("OptionPane.minimumSize",
					new Dimension(SizeManager.optionPaneWidth, SizeManager.optionPaneRowHeight * fields.length));
			while (true) {
				int reply = JOptionPane.showConfirmDialog(this, fields, "Curve", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (reply == JOptionPane.OK_OPTION) {
					try {
						double percentage = Double.parseDouble(percentageTextField.getText().trim());
						if (percentage <= 0 || percentage > 100) {
							JOptionPane.showMessageDialog(this, "Please Enter valid weight percentage", "Error",
									JOptionPane.ERROR_MESSAGE);
							continue;
						}
						controller.setCurveForCourse(controller.getCurrentCourse(), percentage, true);
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(this, "Please Enter valid weight percentage", "Error",
								JOptionPane.ERROR_MESSAGE);
						continue;
					}
					break;
				} else {
					return;
				}
			}
		});
		add(curveButton);

		JButton saveButton = new JButton("Save");
		saveButton.setFont(FontManager.fontButton);
		saveButton.setBounds(SizeManager.finalizeButtonSaveBounds);
		saveButton.setForeground(ColorManager.lightColor);
		saveButton.setBackground(ColorManager.primaryColor);
		saveButton.addActionListener(e -> {
			// TODO save
			JLabel label = new JLabel("Saved");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(FontManager.fontTitle);
			Object[] fields = { label };
			UIManager.put("OptionPane.minimumSize",
					new Dimension(SizeManager.optionPaneWidth, SizeManager.optionPaneRowHeight * fields.length));
			while (true) {
				int reply = JOptionPane.showConfirmDialog(this, fields, "Curve", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (reply == JOptionPane.OK_OPTION) {
					break;
				} else {
					return;
				}
			}
		});
		add(saveButton);

		JButton finalizeButton = new JButton("Finalize");
		finalizeButton.setFont(FontManager.fontButton);
		finalizeButton.setBounds(SizeManager.finalizeButtonFinalizeBounds);
		finalizeButton.setForeground(ColorManager.lightColor);
		finalizeButton.setBackground(ColorManager.primaryColor);
		finalizeButton.addActionListener(e -> {
			// TODO finalize
			JLabel label = new JLabel("Finalized");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(FontManager.fontTitle);
			Object[] fields = { label };
			UIManager.put("OptionPane.minimumSize",
					new Dimension(SizeManager.optionPaneWidth, SizeManager.optionPaneRowHeight * fields.length));
			while (true) {
				int reply = JOptionPane.showConfirmDialog(this, fields, "Curve", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (reply == JOptionPane.OK_OPTION) {
					controller.finalizeCourse(course);
					break;
				} else {
					return;
				}
			}
		});
		add(finalizeButton);

		setVisible(true);
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		Object[][] gradeTableRowData = controller.getFinalGradesData(controller.getCurrentCourse());
		Object[] gradeTableColumnNames = { "Name", "ID", "Actual Percentage", "Curved Percentage", "Grade" };
		gradeTableModel = new DefaultTableModel(gradeTableRowData, gradeTableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column > 3;
			}
		};
		gradeTable.setModel(gradeTableModel);
	}

}
