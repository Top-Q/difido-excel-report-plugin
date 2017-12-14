package il.co.topq.difido.model;

public class TableCell {

	public enum ValueType {
		STRING, INT
	}

	public enum Style {
		REGULAR, BOLD, ITALIC;
	}
	
	private final int column;

	private ValueType type = ValueType.STRING;

	private Style style;

	private Object value;

	public TableCell(int column) {
		this.column = column;
	}
	
	public void setValue(Object value) {
		this.value = value;
	}

	public ValueType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public Style getStyle() {
		return style;
	}

	public void setStyle(Style style) {
		this.style = style;
	}

	public int getColumn() {
		return column;
	}

	public void setType(ValueType type) {
		this.type = type;
	}
	
	
	
	

}
