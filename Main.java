import java.io.*;

public class Main {
    public static void main(String args[]){
        try{            
            File f = new File(args[0]);
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            Yylex lexer = new Yylex(br);
            //ParserPru parser = new ParserPru(lexer);
            ParserSem parser = new ParserSem(lexer);
            parser.init();
            br.close();
        }catch(IOException e){
            System.out.println("Error al abrir el archivo");
        }
        
    }
}
