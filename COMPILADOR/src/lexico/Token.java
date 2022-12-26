package src.lexico;

public class Token{

    public static final int IDENTIFIER = 0;
    public static final int  NUMBER = 1;
    public static final int OPERATOR = 2;
    public static final int PUNCTUANTION = 3;
    public static final int ATTRIBUTION = 4; 
    public static final int RELACIONAL = 5;
    public static final int RESERVEDWORD = 6;
    
    private int type;
    private String value;

    public Token(int type, String value){
        super();
        this.type = type;
        this.value = value;
    }

    public Token(){
        super();
    }
    
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getText() {
		return value;
	}

	public void setText(String value) {
		this.value = value;
	}

    public String toString(){
		return "Token [ " + value + " | " + getName(type) + " ]";
    }

    public String getName(int value){
        if(value == 0)
            return "IDENTIFIER";
        else if(value == 1)
            return "NUMBER";
        else if(value == 2)
            return "OPERATOR";
        else if(value == 3)
            return "PUNCTUANTION";
        else if(value == 4)
            return "ATTRIBUTION";
        else if(value == 5)
            return "RELACIONAL";
        else if(value == 6)
            return "RESERVED WORD";
        else 
            return null;
    }

}