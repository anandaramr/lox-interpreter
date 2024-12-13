package Lox;

import Interpreter.Interpreter;
import Lexer.Lexer;
import Lexer.Token;
import Parser.Parser;
import Parser.AstPrinter;
import Parser.Expr;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Lox {
    private static int lineCount = 0;
    private static String line;
    private static boolean hadError = true;
    private static boolean hadRuntimeError = true;

    private static final Interpreter interpreter = new Interpreter();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) runREPL();
        else {
            runFile(args[0]);
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));

        if(hadError) System.exit(65);
        if(hadRuntimeError) System.exit(70);
    }

    private static void runREPL() {
        Scanner scan = new Scanner(System.in);

        while (true) {
            hadError = false;
            hadRuntimeError = false;

            System.out.print("> ");
            String line = scan.nextLine();
            if (line.equals("quit")) break;

            run(line);
        }

        scan.close();
    }

    private static void run(String line) {
        Lexer lexer = new Lexer();
        Parser parser = new Parser();

        List<Token> tokens = lexer.tokenize(line, lineCount);
        if (tokens.isEmpty() || hadError) return;

        Expr root = parser.parse(tokens);
        if(hadError) return;
        new AstPrinter().print(root);

        interpreter.interpret(root);
    }

    public static void error(String err) {
        System.out.println('\n' + err + (lineCount != 0 ? "\n\t " + lineCount + " |\t" + line : "") + '\n');
        hadError = true;
    }

    public static void incrementLineCount() {
        lineCount++;
    }

    public static String stringify(Object value) {
        String text = value.toString();

        if (value instanceof Double) {
            if (text.endsWith(".0")) return text.substring(0, text.length() - 2);
        }

        return text;
    }

    public static void runtimeError(RuntimeError err) {
        System.out.println("\nRuntimeError: " + err.getMessage() + (lineCount!=0 ? "\n\t" + lineCount + " |\t" + line : "") + '\n');
        hadRuntimeError = true;
    }
}