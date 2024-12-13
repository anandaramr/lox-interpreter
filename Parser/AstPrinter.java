package Parser;

import Lox.Lox;

public class AstPrinter implements Expr.Visitor<String> {
    int level = 0;

    public void print(Expr expr) {
        System.out.println(expr.accept(this));
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
    public String visitLiteral(Expr.Literal expr) {
        return Lox.stringify(expr.value);
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
}