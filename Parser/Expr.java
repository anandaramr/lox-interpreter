package Parser;

import Lexer.Token;

public abstract class Expr {

    public abstract <R> R accept(Visitor<R> visit);

    public interface Visitor<E> {
        E visitBinaryExpr(BinaryExpr expr);
        E visitUnary(Unary expr);
        E visitLiteral(Literal expr);
    }

    public static class BinaryExpr extends Expr {
        BinaryExpr(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }

        public final Expr left;
        public final Token operator;
        public final Expr right;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitBinaryExpr(this);
        }
    }

    public static class Unary extends Expr {
        Unary(Token operator, Expr operand) {
            this.operator = operator;
            this.operand = operand;
        }

        public final Token operator;
        public final Expr operand;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitUnary(this);
        }
    }

    public static class Literal extends Expr {
        Literal(Object value) {
            this.value = value;
        }

        public final Object value;

        public <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteral(this);
        }
    }
}