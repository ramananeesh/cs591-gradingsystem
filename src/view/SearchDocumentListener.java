package view;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class SearchDocumentListener implements DocumentListener {
	private JTextField search;
	private TableRowSorter<DefaultTableModel> sorter;
	private JComboBox<String> boxFilter;

	public SearchDocumentListener(TableRowSorter<DefaultTableModel> sorter, JTextField textSearch, JComboBox<String> boxFilter) {
		this.search = textSearch;
		this.sorter = sorter;
		this.boxFilter = boxFilter;
	}

	public void search() {
		SearchActionListener.search(search, boxFilter, sorter);
	}

	/**
	 * Gives notification that there was an insert into the document.  The
	 * range given by the DocumentEvent bounds the freshly inserted region.
	 *
	 * @param e the document event
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		search();
	}

	/**
	 * Gives notification that a portion of the document has been
	 * removed.  The range is given in terms of what the view last
	 * saw (that is, before updating sticky positions).
	 *
	 * @param e the document event
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {
		search();
	}

	/**
	 * Gives notification that an attribute or set of attributes changed.
	 *
	 * @param e the document event
	 */
	@Override
	public void changedUpdate(DocumentEvent e) {
		search();
	}
}
