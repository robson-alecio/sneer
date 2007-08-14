package sneer.games.mediawars.mp3sushi.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JTextField;

public class ListModelStringFilter<T> extends DefaultListModel implements ActionListener, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<T> _stringList;
	private ArrayList<Integer> _filteredStringList = new ArrayList<Integer>();
	private JTextField _filter;
	private String _filterString;
	private String[] _filterValue;
	
	public ListModelStringFilter(ArrayList<T> stringList, JTextField filter) {
		_stringList = stringList;
		_filter = filter;
		_filterString = _filter.getText();
		this.calculateFilteredStringList();
		filter.addActionListener(this);
		filter.addKeyListener(this);
	}

	private void calculateFilteredStringList() {
		_filterValue = _filterString.split("\\s++");
		int ndx = 0;
		int ndxOfFilteredString = 0;
		for (T eachObject : _stringList) {
			String eachString = eachObject.toString(); 
			if (ndxOfFilteredString == _filteredStringList.size()) {
				if (filterOkForString(eachString)) {
					_filteredStringList.add(ndx);
					ndxOfFilteredString ++;
					fireIntervalAdded(this, ndxOfFilteredString, ndxOfFilteredString+1);
				}	
			} else {
				int firstNdx =  _filteredStringList.get(ndxOfFilteredString);
				if (filterOkForString(eachString)) {
					if (firstNdx > ndx) {
						_filteredStringList.add(ndxOfFilteredString, ndx);
						fireIntervalAdded(this, ndxOfFilteredString, ndxOfFilteredString + 1);
					}
					ndxOfFilteredString ++;
				} else {
					if (firstNdx == ndx) {
						_filteredStringList.remove(ndxOfFilteredString);
						fireIntervalRemoved(this, ndxOfFilteredString, ndxOfFilteredString + 1);
					}
				} 
				
			}
			ndx++;
		}
	}
	
	private boolean filterOkForString(String string) {
		if (_filterValue.length == 0) return true;
		for (int i = 0; i < _filterValue.length; i++) {
			String filter = _filterValue[i];
			if (!string.toUpperCase().contains(filter.toUpperCase())) return false;
		}
		return true;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
	}

	@Override
	public int getSize() {
		return _filteredStringList.size();
	}
	
	@Override
	public T getElementAt(int index) {
		return _stringList.get(_filteredStringList.get(index));
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// Implement Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		refreshList();
	}

	public void refreshList() {
		String tmp = _filter.getText();
		if (tmp.equals(_filterString)) return;
		_filterString = tmp;
		this.calculateFilteredStringList();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}
