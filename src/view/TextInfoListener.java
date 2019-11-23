package view;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TextInfoListener implements ListSelectionListener {
	JTextPane textInfo;
	JTable tableStudent, tableCategory, tableItem;

	public TextInfoListener(JTextPane textInfo, JTable tableStudent, JTable tableCategory, JTable tableItem) {
		this.textInfo = textInfo;
		this.tableStudent = tableStudent;
		this.tableCategory = tableCategory;
		this.tableItem = tableItem;
		// TODO make info text change when tables are selected
	}

	/**
	 * Called whenever the value of the selection changes.
	 *
	 * @param e the event that characterizes the change.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {

	}
}
