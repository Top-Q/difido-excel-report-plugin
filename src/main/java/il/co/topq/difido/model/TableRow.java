package il.co.topq.difido.model;

import java.util.ArrayList;
import java.util.List;

public class TableRow {

	private List<TableCell> cells;

	private final int rowNum;
	
	private boolean seperator;

	public TableRow(int rowNum) {
		cells = new ArrayList<TableCell>();
		this.rowNum = rowNum;

	}

	public TableCell createCell(int column) {
		TableCell cell = new TableCell(column);
		cells.add(cell);
		return cell;
	}

	public int getRowNum() {
		return rowNum;
	}

	public void addCell(TableCell cell) {
		cells.add(cell);
	}

	public List<TableCell> getCells() {
		return cells;
	}

	public boolean isSeperator() {
		return seperator;
	}

	public void setSeperator(boolean seperator) {
		this.seperator = seperator;
	}
	
	

}
