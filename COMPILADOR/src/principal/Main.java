package src.principal;

import src.lexico.*;
import src.sintatico.*;

public class Main {
    public   static void main(String[] args){
        try{
            Scanner scan = new Scanner("code.txt");
            Parser parser = new Parser(scan);

            parser.programa();
			System.out.println("\nCompilation Successful!");
            System.out.println();
        }
        catch (Error_Exception e) {
           System.out.println("\nCompilation Fail! " + '\n' + e.getMessage() + "\n");
        }
        catch (Exception e) {
            System.out.println("Generical Error:");
            e.printStackTrace();
        }
    }
}   
