
import Interpreter.Interpreter;
import Lexer.Lexer;
import Lexer.Token;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {
    static int lineCount = 0;

    public static void main(String[] args) throws IOException {
        if(args.length==0) runREPL();
        else {
            try (BufferedReader input = new BufferedReader(new FileReader(args[0]))) {
                while (true) {
                    lineCount++;
                    String line = input.readLine();
                    if(line==null) break;
                    run(line);
                }
            }
        }
    }

    private static void runREPL() {
        Scanner scan = new Scanner(System.in);

        while (true) {
            System.out.print("> ");
            String line = scan.nextLine();
            if(line.equals("quit")) break;

            run(line);
        }

        scan.close();
    }

    private static void run(String line) {
        Lexer lexer = new Lexer();
        List<Token> tokens = lexer.tokenize(line, lineCount);

        Interpreter inter = new Interpreter();
        inter.execute(tokens);
    }
}