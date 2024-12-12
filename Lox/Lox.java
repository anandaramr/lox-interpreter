package Lox;

import Lexer.Lexer;
import Lexer.Token;
import Parser.Parser;
import Parser.AstPrinter;
import Parser.Expr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Lox {
    static int lineCount = 0;
    static String line;
    private static boolean hadError = true;

    public static void main(String[] args) throws IOException {
        if(args.length==0) runREPL();
        else {
            try (BufferedReader input = new BufferedReader(new FileReader(args[0]))) {
                while (true) {
                    lineCount++;
                    line = input.readLine();
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
        if(tokens.isEmpty()) return;

        Parser parser = new Parser();
        Expr root = parser.parse(tokens);

//        if(hadError) return;
        new AstPrinter().print(root);
    }

    public static void error(String err) {
        System.out.println('\n' + err + (lineCount!=0 ? "\n\t " + lineCount + " |\t" + line : ""));
        hadError = true;
    }
}