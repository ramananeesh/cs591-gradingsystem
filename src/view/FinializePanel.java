package view;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JComponent;
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

/**
 * Panel for finalize course.
 */
public class FinializePanel extends JPanel implements Observer {

	/** The title for the window when GradePanel displays. */
	private static final String TITLE = "Grading System - Finalize";

	/** Curve button. */
	private final JButton curveButton;

	/** Save button. */
	private final JButton saveButton;

	/** Finalize button. */
	private final JButton finalizeButton;

	/** Grade table model. */

	private DefaultTableModel gradeTableModel;

	/** Grade table. */
	private JTable gradeTable;

	/** Controller. */
	private Master controller;

	/**
	 * Initiate a newly created FinalizePanel object.
	 *
	 * @param frame      Main frame.
	 * @param controller Controller.
	 */
	public FinializePanel(MainFrame frame, Master controller) {
		this.controller = controller;
		this.controller.addObserver(this);
		frame.setTitle(TITLE);
		setLayout(null);
		setBounds(SizeManager.getContentPaneBounds());
		setOpaque(false);

		// TODO data for test
		Course course = controller.getCurrentCourse();

//		Statistics statistics = new Statistics(course.getFinalGradesForStats());

		// title label
		JLabel titleLabel = new JLabel(
				String.format("%s - %s - %s", course.getCourseNumber(), course.getCourseName(), course.getTerm()));
		titleLabel.setBounds(SizeManager.getFinalizeTitleLabelBounds());
		titleLabel.setFont(FontManager.getFontLabel());
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		titleLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		add(titleLabel);

		Object[][] gradeTableRowData = controller.getFinalGradesData(controller.getCurrentCourse());
		Object[] gradeTableColumnNames = {"Name", "ID", "Actual Percentage", "Curved Percentage", "Grade"};
		gradeTableModel = new DefaultTableModel(gradeTableRowData, gradeTableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column > 3;
			}
		};
		gradeTable = new JTable(gradeTableModel);
		gradeTable.setRowHeight(SizeManager.getTableRowHeight());
		gradeTable.setFont(FontManager.getFontTable());
		DefaultTableCellRenderer gradeTableRender = new DefaultTableCellRenderer();
		gradeTableRender.setHorizontalAlignment(SwingConstants.CENTER);
		gradeTableRender.setVerticalAlignment(SwingConstants.CENTER);
		gradeTable.setDefaultRenderer(Object.class, gradeTableRender);
		JTableHeader gradeTableHeader = gradeTable.getTableHeader();
		gradeTableHeader.setBackground(ColorManager.getPrimaryColor());
		gradeTableHeader.setForeground(ColorManager.getLightColor());
		gradeTableHeader.setFont(gradeTable.getFont());
		gradeTableHeader.setEnabled(false);
		JScrollPane gradeTableScrollPane = new JScrollPane(gradeTable);
		gradeTableScrollPane.setBounds(SizeManager.getTableCourseBounds());
		add(gradeTableScrollPane);

		List<Double> statisticsGrades = new ArrayList<>();
		for (Object[] gradeTableRowDatum : gradeTableRowData) {
			statisticsGrades.add(Double.parseDouble((String) gradeTableRowDatum[2]) / 100);
		}

		// statistics label
		Double[] statisticsGradesArray = new Double[statisticsGrades.size()];
		Statistics statistics = new Statistics(statisticsGrades.toArray(statisticsGradesArray));
		JLabel statisticsLabel = new JLabel(statistics.toString());
		statisticsLabel.setBounds(SizeManager.getLabelGradeStatisticsBounds());
		statisticsLabel.setFont(FontManager.getFontLabel());
		statisticsLabel.setVerticalAlignment(SwingConstants.BOTTOM);
		statisticsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(statisticsLabel);

		JButton backButton = new JButton("Back");
		backButton.setFont(FontManager.getFontButton());
		backButton.setBounds(SizeManager.getFinalizeButtonBackBounds());
		backButton.setForeground(ColorManager.getLightColor());
		backButton.setBackground(ColorManager.getPrimaryColor());
		backButton.addActionListener(e ->
				frame.changePanel(this, new MenuPanel(frame,
						new String[]{course.getCourseNumber(), course.getCourseName(), course.getTerm()}, controller)));
		add(backButton);

		curveButton = new JButton("Curve");
		curveButton.setFont(FontManager.getFontButton());
		curveButton.setBounds(SizeManager.getFinalizeButtonCurveBounds());
		curveButton.setForeground(ColorManager.getLightColor());
		curveButton.setBackground(ColorManager.getPrimaryColor());
		curveButton.addActionListener(e -> {
			JTextField percentageTextField = new JTextField();
			Object[] fields = {"Percentage: ", percentageTextField};
			UIManager.put("OptionPane.minimumSize",
					new Dimension(SizeManager.getOptionPaneWidth(), SizeManager.getOptionPaneRowHeight() * fields.length));
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

		saveButton = new JButton("Save");
		saveButton.setFont(FontManager.getFontButton());
		saveButton.setBounds(SizeManager.getFinalizeButtonSaveBounds());
		saveButton.setForeground(ColorManager.getLightColor());
		saveButton.setBackground(ColorManager.getPrimaryColor());
		saveButton.addActionListener(e -> {
			JLabel label = new JLabel("Saved");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(FontManager.getFontTitle());
			Object[] fields = {label};
			UIManager.put("OptionPane.minimumSize",
					new Dimension(SizeManager.getOptionPaneWidth(), SizeManager.getOptionPaneRowHeight() * fields.length));
			while (true) {
				int reply = JOptionPane.showConfirmDialog(this, fields, "Save", JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.INFORMATION_MESSAGE);
				if (reply == JOptionPane.OK_OPTION) {
					break;
				} else {
					return;
				}
			}
		});
		add(saveButton);

		finalizeButton = new JButton("Finalize");
		finalizeButton.setFont(FontManager.getFontButton());
		finalizeButton.setBounds(SizeManager.getFinalizeButtonFinalizeBounds());
		finalizeButton.setForeground(ColorManager.getLightColor());
		finalizeButton.setBackground(ColorManager.getPrimaryColor());
		finalizeButton.addActionListener(e -> {
			JLabel label = new JLabel("Finalized");
			label.setHorizontalAlignment(SwingConstants.CENTER);
			label.setFont(FontManager.getFontTitle());
			Object[] fields = {label};
			UIManager.put("OptionPane.minimumSize",
					new Dimension(SizeManager.getOptionPaneWidth(), SizeManager.getOptionPaneRowHeight() * fields.length));
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

		if (controller.getCurrentCourse().isFinalized()) {
			lock();
		}

		setVisible(true);
	}

	/**
	 * Update table.
	 *
	 * @param observable the observable object.
	 * @param argument   an argument passed to the notifyObservers method.
	 */
	@Override
	public void update(Observable observable, Object argument) {
		Object[][] gradeTableRowData = controller.getFinalGradesData(controller.getCurrentCourse());
		Object[] gradeTableColumnNames = {"Name", "ID", "Actual Percentage", "Curved Percentage", "Grade"};
		gradeTableModel = new DefaultTableModel(gradeTableRowData, gradeTableColumnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column > 3;
			}
		};
		gradeTable.setModel(gradeTableModel);
	}

	/**
	 * Lock some components after finalized.
	 */
	private void lock() {
		for (JComponent component : new JComponent[]{gradeTable, saveButton, finalizeButton, curveButton}) {
			component.setEnabled(false);
		}
	}

}