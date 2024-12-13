package Parser;

import Lexer.Token;

public abstract class Expr {

    public abstract <E> E accept(Visitor<E> visit);

    public interface Visitor<E> {
        E visitBinaryExpr(BinaryExpr expr);
        E visitUnary(Unary expr);
        E visitGrouping(Grouping expr);
        E visitLiteral(Literal expr);
        E visitVariable(Variable expr);
        E visitAssign(Assign expr);
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

        public <E> E accept(Visitor<E> visitor) {
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

        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitUnary(this);
        }
    }

    public static class Grouping extends Expr {
        Grouping(Expr expression) {
            this.expression = expression;
        }

        public final Expr expression;

        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitGrouping(this);
        }
    }

    public static class Literal extends Expr {
        Literal(Object value) {
            this.value = value;
        }

        public final Object value;

        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitLiteral(this);
        }
    }

    public static class Variable extends Expr {
        Variable(Token name) {
            this.name = name;
        }

        public final Token name;

        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitVariable(this);
        }
    }

    public static class Assign extends Expr {
        Assign(Token name, Expr value) {
            this.name = name;
            this.value = value;
        }

        public final Token name;
        public final Expr value;

        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitAssign(this);
        }
    }
}