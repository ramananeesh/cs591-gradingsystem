package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SearchActionListener implements ActionListener {
	private JTextField search;
	private TableRowSorter<DefaultTableModel> sorter;
	private JComboBox<String> boxFilter;

	public SearchActionListener(TableRowSorter<DefaultTableModel> sorter, JTextField textSearch, JComboBox<String> boxFilter) {
		this.search = textSearch;
		this.sorter = sorter;
		this.boxFilter = boxFilter;
	}

	/**
	 * Invoked when an action occurs.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		search(search, boxFilter, sorter);
	}

	public static void search(JTextField search, JComboBox<String> boxFilter, TableRowSorter<DefaultTableModel> sorter) {
		if (search.getText().length() != 0 && !Objects.equals(boxFilter.getSelectedItem(), "All")) {
			sorter.setRowFilter(RowFilter.andFilter(new ArrayList<>(Arrays.asList(
					RowFilter.regexFilter("(?i)" + boxFilter.getSelectedItem()),
					RowFilter.regexFilter("(?i)" + search.getText()))))
			);
		} else if (!Objects.equals(boxFilter.getSelectedItem(), "All")) {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + boxFilter.getSelectedItem()));
		} else if (search.getText().length() != 0) {
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + search.getText()));
		} else {
			sorter.setRowFilter(null);
		}
	}
}
