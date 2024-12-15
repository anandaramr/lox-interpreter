package Parser;

import Lox.Lox;

import java.util.List;

public class AstPrinter implements Expr.Visitor<String>, Stmt.Visitor<String> {

    public void print(List<Stmt> stmts) {
        for(Stmt stmt: stmts) {
            System.out.println(stmt.accept(this));
        }
    }

    @Override
    public String visitBinaryExpr(Expr.BinaryExpr expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitUnary(Expr.Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.operand);
    }

    @Override
    public String visitGrouping(Expr.Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteral(Expr.Literal expr) {
        return Lox.stringify(expr.value);
    }

    @Override
    public String visitVariable(Expr.Variable expr) {
        return "";
    }

    @Override
    public String visitAssign(Expr.Assign expr) {
        return "";
    }

    private String parenthesize(String prefix, Expr ...expressions) {

        StringBuilder builder = new StringBuilder();
        builder.append("( ").append(prefix);

        for(Expr expr: expressions) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();

    }

    @Override
    public String visitExpression(Stmt.Expression expr) {
        return parenthesize("expr", expr.expression);
    }

    @Override
    public String visitPrint(Stmt.Print expr) {
        return parenthesize("print", expr.expression);
    }

    @Override
    public String visitVarDeclaration(Stmt.VarDeclaration expr) {
        return "";
    }

    @Override
    public String visitBlock(Stmt.Block expr) {
        return "";
    }
}