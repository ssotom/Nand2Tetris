package co.edu.eafit;

class SymbolTable {
	String type;
	String kind;
	short num;

	public SymbolTable(String type, String kind, short num)  {
		this.type = type;
		this.kind = kind;
		this.num = num;
	}

	public String toString() {
		return type + " " + kind + " " + num; 
	}
}