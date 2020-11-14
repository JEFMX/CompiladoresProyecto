import java.lang.*;
import java.io.*;
import java.util.*;


public class Main{
	public static void main(String[] args){
       	          try{
                         Yylex lexer=new Yylex(new BufferedReader(new FileReader(new File(args[0]))));
                         lexer.yylex();
                 }catch(IOException e){
                         System.out.println("Ocurrio un error al intentar acceder a la consola");
		}
	}
}

