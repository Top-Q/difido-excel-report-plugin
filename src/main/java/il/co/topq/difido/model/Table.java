package il.co.topq.difido.model;

import java.util.ArrayList;
import java.util.List;

public class Table {

	private String name;

	private List<TableRow> rows;

	public Table(String name) {
		rows = new ArrayList<TableRow>();
		this.name = name;
	}
	
	public TableRow createRow(int rownum) {
		TableRow row = new TableRow(rownum);
		rows.add(row);
		return row;
	}

	public Table() {
		this(null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TableRow> getRows() {
		return rows;
	}
	

}
