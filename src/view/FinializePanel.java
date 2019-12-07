package view;

import controller.Master;
import helper.ColorManager;
import helper.FontManager;
import helper.SizeManager;
import helper.Statistics;
import model.Course;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.util.Random;

public class FinializePanel extends JPanel {

	/** The title for the window when GradePanel displays */
	private static final String TITLE = "Grading System - Finalize";

	public FinializePanel(MainFrame frame, Master controller) {
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.panelBounds);
		setOpaque(false);

		// TODO data for test
		Course course = controller.getCurrentCourse();
//		Course course = new Course(111, "CS591", "Java", "Fall 2019");
		double[] grades = new double[100];
		Random random = new Random();
		for (int i = 0; i < grades.length; ++i) {
			grades[i] = 80 + (random.nextDouble() - 0.5) * 40;
		}
		Statistics statistics = new Statistics(grades);

		// title label
		JLabel titleLabel = new JLabel(String.format("%s - %s - %s", course.getCourseNumber(), course.getCourseName(), course.getTerm()));
		titleLabel.setBounds(SizeManager.finalizeTitleLabelBounds);
		titleLabel.setFont(FontManager.fontLabel);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		add(titleLabel);

		// statistics label
		JLabel statisticsLabel = new JLabel();
		statisticsLabel.setBounds(SizeManager.labelGradeStatisticsBounds);
		statisticsLabel.setFont(FontManager.fontLabel);
		statisticsLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		statisticsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(statisticsLabel);

		Object[][] gradeTableRowData = {
				{"Test", "123", "e@e.e", "100"},
				{"Test", "123", "e@e.e", "100"},
				{"Test", "123", "e@e.e", "100"}
		};
		Object[] gradeTableColumnNames = {"Name", "ID", "Email", "Grade"};
		TableModel gradeTableModel = new DefaultTableModel(gradeTableRowData, gradeTableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 3;
			}
		};
		JTable gradeTable = new JTable(gradeTableModel);
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
		JScrollPane gradeTableScrollPane = new JScrollPane(gradeTable);
		gradeTableScrollPane.setBounds(SizeManager.tableCourseBounds);
		add(gradeTableScrollPane);

		JButton backButton = new JButton("Back");
		backButton.setFont(FontManager.fontButton);
		backButton.setBounds(SizeManager.finalizeButtonBackBounds);
		backButton.setForeground(ColorManager.lightColor);
		backButton.setBackground(ColorManager.primaryColor);
		backButton.addActionListener(e -> {
			frame.changePanel(this, new MenuPanel(frame, new String[]{course.getCourseNumber(), course.getCourseName(), course.getTerm()}, controller));
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
			Object[] fields = {
					"Percentage: ", percentageTextField
			};
			while (true) {
				int reply = JOptionPane.showConfirmDialog(this, fields, "Curve",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (reply == JOptionPane.OK_OPTION) {
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
			Object[] fields = {
					label
			};
			while (true) {
				int reply = JOptionPane.showConfirmDialog(this, fields, "Curve",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
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
			Object[] fields = {
					label
			};
			while (true) {
				int reply = JOptionPane.showConfirmDialog(this, fields, "Curve",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
				if (reply == JOptionPane.OK_OPTION) {
					break;
				} else {
					return;
				}
			}
		});
		add(finalizeButton);

		setVisible(true);
	}

}
