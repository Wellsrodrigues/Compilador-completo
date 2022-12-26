package src.sintatico;

import src.lexico.Scanner;
import src.lexico.Token;
import src.principal.Error_Exception;
import java.util.LinkedList;

public class Parser {

	private Scanner scanner;
	private Token token;
	
	LinkedList<String> tabela = new LinkedList<String>();
	LinkedList<String> tabelaVariavel = new LinkedList<String>();
    LinkedList<String> tabelaTipo = new LinkedList<String>();
	LinkedList<String> values = new LinkedList<String>();
	LinkedList<String> operators = new LinkedList<String>();
	
	String textoId;
	int index;

    int tsoma = 0;
	String T;
    String y;
    String x;
    String op;
    int aux = 0;

	public Parser(Scanner scanner) {
		this.scanner = scanner;
	}

//----------------------------------------------------------------------------------------------

    public void programa() {
		token = scanner.nextToken();
		if(token.getType() != Token.RESERVEDWORD && token.getText().compareTo("int") != 0) {
			throw new Error_Exception("SYNTAX ERROR: int Expected in line " + scanner.lineCode());
		}
		token = scanner.nextToken();
		if(token.getType() != Token.RESERVEDWORD && token.getText().compareTo("main") != 0) {
			throw new Error_Exception("SYNTAX ERROR: main Expected in line " + scanner.lineCode());
		}
		token = scanner.nextToken();
		if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("(") != 0) {
			throw new Error_Exception("SYNTAX ERROR: Abre parentese Expected in line " + scanner.lineCode());
		}
		token = scanner.nextToken();
		if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo(")") != 0) {
			throw new Error_Exception("SYNTAX ERROR: Fecha parenteses Expected in line " + scanner.lineCode());
		}
		token = scanner.nextToken();
		bloco();
	}

//----------------------------------------------------------------------------------------------
    
    public void bloco() {
        if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("{") != 0) {
            throw new Error_Exception("SYNTAX ERROR: Abre chave Expected in line " + scanner.lineCode());
        }
        token = scanner.nextToken();
        do {
            if(token.getText().compareTo("int") == 0 || token.getText().compareTo("float") == 0 || token.getText().compareTo("char") == 0){
                declaraVar();
            }else{
                comando();
            }
            token = scanner.nextToken();
        } while(token.getText().compareTo("}") != 0);
    }

//----------------------------------------------------------------------------------------------

    public void declaraVar() {
		tipo();
		id();
		token = scanner.nextToken();
		if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo(";") != 0) {
			throw new Error_Exception("SYNTAX ERROR: Ponto e virgula Expected in line " + scanner.lineCode());
		}
	}

//----------------------------------------------------------------------------------------------

	public void tipo() {
		if(token.getType() != Token.RESERVEDWORD && token.getText().compareTo("int") != 0 && token.getText().compareTo("float") != 0 && token.getText().compareTo("char") != 0) {
			throw new Error_Exception("SYNTAX ERROR: Type Expected in line " + scanner.lineCode());
		}
		
		tabela.add(token.getText());
	}

//----------------------------------------------------------------------------------------------

	public void id() {
		token = scanner.nextToken();
		if(token.getType() != Token.IDENTIFIER) {
			throw new Error_Exception("SYNTAX ERROR: Identifier Expected in line " + scanner.lineCode());
		}
		if(tabelaVariavel.contains(token.getText()) == true) {
			throw new Error_Exception("SEMANTIC ERROR: Variavel '" + token.getText() + "' repetida in line " + scanner.lineCode());
		}
		tabelaVariavel.add(token.getText());
	}

//----------------------------------------------------------------------------------------------

    public void comando() {
        if(token.getType() == Token.RESERVEDWORD && token.getText().compareTo("if") == 0) {
            token = scanner.nextToken();
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("(") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Abre parentese Expected in line " + scanner.lineCode());
            }
            expr_relacional();
    
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo(")") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Fecha parenteses Expected in line " + scanner.lineCode());
            }
            token = scanner.nextToken();
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("{") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Abre chave Expected in line " + scanner.lineCode());
            }
            token = scanner.nextToken();
            comando();
            token = scanner.nextToken();
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("}") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Fecha chave Expected in line " + scanner.lineCode());
            }
            token = scanner.nextToken();
            if (token != null && token.getType() == Token.RESERVEDWORD && token.getText().compareTo("else") == 0) {
                token = scanner.nextToken();
                if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("{") != 0) {
                    throw new Error_Exception("SYNTAX ERROR: Abre chave Expected in line " + scanner.lineCode());
                }
                token = scanner.nextToken();
                comando();
                token = scanner.nextToken();
                if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("}") != 0) {
                    throw new Error_Exception("SYNTAX ERROR: Fecha chave Expected in line " + scanner.lineCode());
                }
            }
        }
        else if(token.getType() == Token.RESERVEDWORD && token.getText().compareTo("while") == 0){
            token = scanner.nextToken();
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("(") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Abre parentese Expected in line " + scanner.lineCode());
            }
            expr_relacional();
    
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo(")") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Fecha parenteses Expected in line " + scanner.lineCode());
            }
            token = scanner.nextToken();
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("{") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Abre chave Expected in line " + scanner.lineCode());
            }
            token = scanner.nextToken();
            comando();
            token = scanner.nextToken();
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("}") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Fecha chave Expected in line " + scanner.lineCode());
            }
        }
        else {
            comando_basico();
        }
    }

//----------------------------------------------------------------------------------------------

    public void comando_basico() {
	    if(token.getType() == Token.IDENTIFIER){
            atribuicao();
		}
        if(token.getType() == Token.RESERVEDWORD && token.getText().compareTo("printf") == 0){
            _printf();
        }
        if(token.getType() == Token.RESERVEDWORD && token.getText().compareTo("scanf") == 0){
            _scanf();
        }
	}

//----------------------------------------------------------------------------------------------

    public void _printf(){
        if(token.getType() == Token.RESERVEDWORD && token.getText().compareTo("printf") == 0){
            token = scanner.nextToken();
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("(") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Abre parentese Expected in line " + scanner.lineCode());
            }
            token = scanner.nextToken();
            if(tabelaVariavel.contains(token.getText()) == false) {
                throw new Error_Exception("SEMANTIC ERROR: Variavel '" + token.getText() + "' nao declarada in line "  + scanner.lineCode());
            }
            token = scanner.nextToken();
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo(")") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Fecha parentese Expected in line " + scanner.lineCode());
            }
            token = scanner.nextToken();
            if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo(";") != 0) {
                throw new Error_Exception("SYNTAX ERROR: Ponto e virgula Expected in line " + scanner.lineCode());
            }
        }
    }

//----------------------------------------------------------------------------------------------

    public void _scanf(){
        token = scanner.nextToken();
        if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("(") != 0) {
            throw new Error_Exception("SYNTAX ERROR: Abre parentese Expected in line " + scanner.lineCode());
        }
        token = scanner.nextToken();
        if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo("&") != 0) {
            throw new Error_Exception("SYNTAX ERROR: E-commerce Expected in line " + scanner.lineCode());
        }
        token = scanner.nextToken();
        if(tabelaVariavel.contains(token.getText()) == false) {
            throw new Error_Exception("SEMANTIC ERROR: Variavel '" + token.getText() + "' nao declarada in line "  + scanner.lineCode());
        }
        token = scanner.nextToken();
        if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo(")") != 0) {
            throw new Error_Exception("SYNTAX ERROR: Fecha parentese Expected in line " + scanner.lineCode());
        }
        token = scanner.nextToken();
        if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo(";") != 0) {
            throw new Error_Exception("SYNTAX ERROR: Ponto e virgula Expected in line " + scanner.lineCode());
        }
    }

//----------------------------------------------------------------------------------------------

    public void atribuicao() {
        if(token.getType() != Token.IDENTIFIER) {
            throw new Error_Exception("SYNTAX ERROR: Identifier Expected in line " + scanner.lineCode());
        }
        textoId = token.getText();
        if(tabelaVariavel.contains(token.getText()) == false) {
            throw new Error_Exception("SEMANTIC ERROR: Variavel '" + token.getText() + "' nao declarada in line "  + scanner.lineCode());
        }
        token = scanner.nextToken();
        if(token.getType() != Token.ATTRIBUTION) {
            throw new Error_Exception("SYNTAX ERROR: Atribuidor Expected in line " + scanner.lineCode());
        }
        expr_arit();
        gerarCode();
        tabelaTipo.clear();
		if(token.getType() != Token.PUNCTUANTION && token.getText().compareTo(";") != 0) {
			throw new Error_Exception("SYNTAX ERROR: Ponto e virgula Expected in line "  + scanner.lineCode());
		}
    }

//----------------------------------------------------------------------------------------------

    public void gerarCode(){
        System.out.println("\n\n" + "--------- Código de 3 Endereços -----------" + '\n');
        while(aux <= operators.size()){
            for(int i = 0; i < operators.size(); i++){
                T = "T";
                if(operators.get(i).equals("/") || operators.get(i).equals("*")){
                    x = values.get(i);
                    op = operators.get(i);
                    values.remove(i);
                    y = values.get(i);
                    operators.remove(i);
                    tsoma++;
                    T = T+tsoma;
                    values.set(i, T);
                    System.out.println(T + " = " + x + " " + op + " " + y);
                }
            }   
        aux++;
        }
		aux = 0;
        while(aux <= operators.size()){
            for(int i = 0; i < operators.size(); i++){
                T = "T";
                if(operators.get(i).equals("+") || operators.get(i).equals("-")){
                    x = values.get(i);
                    op = operators.get(i);
                    values.remove(i);
                    y = values.get(i);
                    operators.remove(i);
                    tsoma++;
                    T = T+tsoma;
                    values.set(i, T);
                    System.out.println(T + " = " + x + " "+ op + " " + y);
                }
            }
            aux++;
        }
        if(values.size() >= 1){
            System.out.println(textoId + " = " + T);
        }
        values.clear();
    }

//----------------------------------------------------------------------------------------------

    public void expr_relacional() {
        fator();
        if(token.getType() != Token.RELACIONAL) {
            throw new Error_Exception("SYNTAX ERROR: Operador relacional Expected in line " + scanner.lineCode());
        }
        fator();
    }

//----------------------------------------------------------------------------------------------

    public void expr_arit() {
        fator();
        if(token.getType() != Token.PUNCTUANTION){
            if(token.getType() != Token.OPERATOR) {
                throw new Error_Exception("SYNTAX ERROR: Operador aritmetico Expected in line " + scanner.lineCode());
            }
            operators.add(token.getText());
            fator();

            if(token != null){
                if(token.getType() != Token.PUNCTUANTION){
                    if(token.getType() != Token.OPERATOR){
                        throw new Error_Exception("SYNTAX ERROR: Operador aritmetico Expected in line " + scanner.lineCode());
                    }
                    operators.add(token.getText());
                    expr_arit();
                }
            }
        }
    }

//----------------------------------------------------------------------------------------------

    public void fator() {
        token = scanner.nextToken();
        if(token.getType() != Token.NUMBER && token.getType() != Token.IDENTIFIER) {
            throw new Error_Exception("SYNTAX ERROR: Terminal Expected in line  " +  scanner.lineCode());
        }
        if(token.getType() == Token.IDENTIFIER && tabelaVariavel.contains(token.getText()) == false) {
            throw new Error_Exception("SYNTAX ERROR: Variavel '" + token.getText() + "' nao declarada in line " + scanner.lineCode());
        }
        if(token.getType() == Token.NUMBER || token.getType() == Token.IDENTIFIER) {
            tabelaTipo.add(token.getName(token.getType()));
            values.add(token.getText());
        }
        token = scanner.nextToken();
    }
}