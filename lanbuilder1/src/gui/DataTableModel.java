package gui;

import javax.swing.table.AbstractTableModel;

import data.DictionaryMap;

@SuppressWarnings("serial")
public abstract class DataTableModel<T> extends AbstractTableModel {
	protected String[] columnNames;

	protected DictionaryMap<Integer, T> data;
	
	public DataTableModel(){
		super();		
	}
	
	public DataTableModel(String[] columnNames, DictionaryMap<Integer, T> data){
		super();
		this.columnNames = columnNames;
		this.data = data;
	}
	
	public int getColumnCount() {
		return columnNames.length;
	}
	
	public int getRowCount() {
		return data.size();
	}
	
	public String getColumnName(int col){
		return columnNames[col];
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class getColumnClass(int c) {
		return getValueAt(0, c).getClass();
	}
	
	protected void update(){
		fireTableDataChanged();
	}
}