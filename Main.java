
import Interpreter.Interpreter;
import Lexer.Lexer;
import Lexer.Token;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String args[]) {
        Scanner scan = new Scanner(System.in);

        while (true) { 
            System.out.print("> ");
            String line = scan.nextLine();

            if(line.equals("quit")) break;

            Lexer lexer = new Lexer();
            List<Token> tokens = lexer.tokenize(line);
            
            Interpreter inter = new Interpreter();
            inter.execute(tokens);
        }

        scan.close();
    }
}