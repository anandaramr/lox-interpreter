package Lox;

import Interpreter.Interpreter;
import Lexer.Lexer;
import Lexer.Token;
import Parser.Parser;
import Parser.Stmt;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;

public class Lox {
    private static String source = null;
    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;

    private static final Interpreter interpreter = new Interpreter();

    public static void main(String[] args) throws IOException {
        if (args.length == 0) runREPL();
        else {
            runFile(args[0]);
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        source = new String(bytes, Charset.defaultCharset());
        run(source);

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

        List<Token> tokens = lexer.tokenize(line);
        if (tokens.isEmpty() || hadError) return;

        List<Stmt> statements = parser.parse(tokens);
        if(hadError) return;
//        new AstPrinter().print(statements);

        interpreter.interpret(statements);
    }

    public static void error(String err, int line) {
        if(source==null) {
            System.out.println("\n " + err + '\n');
        } else {
            System.out.println("\n " + err + "\n\t" + line + " |\t" + getLine(line) + '\n');
        }
        hadError = true;
    }

    private static String getLine(int line) {
        int currLine = 1;
        int startPtr = 0;
        int length = source.length();
        while(currLine<line) {
            if(startPtr==length) return null;
            if(source.charAt(startPtr++)=='\n') currLine++;
        }

        int endPtr = startPtr;
        while(endPtr<length && source.charAt(endPtr)!='\n') endPtr++;
        return source.substring(startPtr, endPtr);
    }

    public static String stringify(Object value) {
        if(value==null) return "null";

        String text = value.toString();
        if (value instanceof Double) {
            if (text.endsWith(".0")) return text.substring(0, text.length() - 2);
        }

        return text;
    }

    public static void runtimeError(RuntimeError err) {
        int line = err.token.line;

        if(source==null) {
            System.out.println("\n RuntimeError: " + err.getMessage() + '\n');
        } else {
            System.out.println('\n' + err.getMessage() + "\n\t" + line + " |\t" + getLine(line) + '\n');
        }
        hadError = true;
    }
}