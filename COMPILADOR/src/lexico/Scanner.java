package src.lexico;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import src.principal.Error_Exception;

public class Scanner {

    private static int lines = 1;

    private char[] conteudo;
    private int estado; 
    private int posicao;

    public Scanner(String filename){
        try {
            System.out.println('\n' + "----------------- DEBUG ---------------------" + '\n');
            String txtConteudo;
            txtConteudo = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
            System.out.println(txtConteudo);
            conteudo = txtConteudo.toCharArray();
            posicao = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public int lineCode(){
        return lines++;
    }

    public Token nextToken(){ 
        Token token;
        char currentChar;
        String termo = "";
        estado = 0;
        while(true){
            if(ehEOF()){
                return null;
            }
            currentChar = nextChar();
            switch(estado){
                case 0:
                    if(currentChar == '\n'){
                        lineCode();
                    }
                    if(ehCaracter(currentChar)){
                        termo += currentChar;
                        estado = 1;
                    }
                    else if(ehDigito(currentChar)){
                        termo += currentChar;
                        estado = 3;
                    }
                    else if(ehEspaco(currentChar)){
                        estado = 0;
                    }
                    else if(ehAtribuicao(currentChar)){
                        termo += currentChar;
                        estado = 5;
                    }
                    else if(ehRelacional(currentChar)){
                        termo += currentChar;
                        estado = 8;
                    }
                    else if(ehDiferente(currentChar)){
                        termo += currentChar;
                        estado = 10;
                    }
                    else if(ehOperador(currentChar)){
                        termo += currentChar;
                        estado = 11;
                    }
                    else if(ehPontuacao(currentChar)){
                        termo += currentChar;
                        estado = 12;
                    }
                    else{
                        throw new Error_Exception("LEXICAL ERROR: Unrecognized SYMBOL " + currentChar + " in line " + lineCode());
                    }
                break;

                case 1:
                    if(ehCaracter(currentChar) || ehDigito(currentChar)){
                        estado = 1;
                        termo += currentChar;
                    }else if(ehEspaco(currentChar) || ehOperador(currentChar)){
                        estado = 2;
                    }
                    else{
                        throw new Error_Exception("LEXICAL ERROR: Malformed Indetifier " + currentChar + " in line " + lineCode() );
                    }
                break;

                case 2:
                    token = new Token();
                    if(reservedWord(termo)){
                        token.setType(Token.RESERVEDWORD);
                        token.setText(termo);
                    }else{
                        token.setType(Token.IDENTIFIER);
                        token.setText(termo);
                    }
                    back();
                    return token;
                case 3:
                    if(ehDigito(currentChar)){
                        estado = 3;
                        termo += currentChar;
                    }
                    else if(!ehCaracter(currentChar)){
                        estado = 4;
                    }
                    else{
                        throw new Error_Exception("LEXICAL ERROR: Unrecognized Number "  + currentChar + " in line " + lineCode());
                    }
                break;

                case 4:
                    token = new Token();
                    token.setType(Token.NUMBER);
                    token.setText(termo);
                    back();
                    return token;

                case 5:
                    if(ehDigito(currentChar) || ehCaracter(currentChar) || ehEspaco(currentChar)){
                        estado = 6;
                    }
                    else if(ehAtribuicao(currentChar)){
                        termo += currentChar;
                        estado = 7;
                    }
                    else{
                        throw new Error_Exception("LEXICAL ERROR: Unrecognized Attibuition "  + currentChar + " in line " + lineCode());
                    }
                break;

                case 6:
                    token = new Token();
                    token.setType(Token.ATTRIBUTION);
                    token.setText(termo);
                    back();
                    return token;

                case 7:
                    token = new Token();
                    token.setType(Token.RELACIONAL);
                    token.setText(termo);
                    back();
                    return token;

                case 8:
                    if(ehCaracter(currentChar) || ehDigito(currentChar) || ehEspaco(currentChar)){
                        estado = 9;
                    }
                    else if(ehAtribuicao(currentChar)){
                        termo += currentChar;
                        estado = 9;
                    }
                    else{
                        throw new Error_Exception("LEXICAL ERROR: Unrecognized Relacional "  + currentChar + " in line " + lineCode());
                    }
                break;

                case 9:
                    token = new Token();
                    token.setType(Token.RELACIONAL);
                    token.setText(termo);
                    back();
                    return token;

                case 10:
                    if(ehAtribuicao(currentChar)){
                        termo += currentChar;
                        estado = 9;
                    }
                    else{
                        throw new Error_Exception("LEXICAL ERROR: Malformed Relacional "  + currentChar + " in line " + lineCode());
                    }
                break;

                case 11:
                    token = new Token();
                    token.setType(Token.OPERATOR);
                    token.setText(termo);
                    back();
                    return token;

                case 12:
                    token = new Token();
                    token.setType(Token.PUNCTUANTION);
                    token.setText(termo);
                    back();
                    return token;

            }
        }
    }

    private boolean reservedWord(String word){
        return (word.equals("int") || word.equals("main") || word.equals("if") || 
        word.equals("else") || word.equals("printf") || word.equals("scanf") || 
        word.equals("float") || word.equals("char") || word.equals("while"));
    }

    private boolean ehDigito(char c){
        return c >= '0' && c <= '9';
    }

    private boolean ehCaracter(char c){
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
    }

    private boolean ehOperador(char c){
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^' || c == '%';
    }

    private boolean ehPontuacao(char c){
        return c == ';' || c == '&' || c == '(' || c == ')' || c == '{' || c == '}';
    }

    private boolean ehAtribuicao(char c){
        return c == '=';
    }

    private boolean ehDiferente(char c){
        return  c == '!';
    }

    private boolean ehRelacional(char c){
        return c == '>' || c == '<';
    }

    private boolean ehEspaco(char c){
        return c == ' ' || c == '\n' || c == '\t' || c == '\r';
    }

    private char nextChar(){ //ler prox caractere
        return conteudo[posicao++];
    }

    private boolean ehEOF(){ //verificar o fim do vetor
        return posicao == conteudo.length;
    }

    private void back(){
        posicao--;
    }
}
