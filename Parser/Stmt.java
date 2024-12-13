package Parser;

import Lexer.Token;

public abstract class Stmt {

    public abstract <E> E accept(Visitor<E> visitor);

    public interface Visitor<E> {
        E visitExpression(Expression expr);
        E visitPrint(Print expr);
        E visitVarDeclaration(VarDeclaration expr);
    }

    public static class Expression extends Stmt {
        public final Expr expression;

        Expression(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitExpression(this);
        }
    }

    public static class Print extends Stmt {
        public final Expr expression;

        Print(Expr expression) {
            this.expression = expression;
        }

        @Override
        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitPrint(this);
        }
    }

    public static class VarDeclaration extends Stmt {
        public final Token name;
        public final Expr initializer;
        public final boolean isConst;

        VarDeclaration(Token name, Expr initializer, boolean isConst) {
            this.name = name;
            this.initializer = initializer;
            this.isConst = isConst;
        }

        @Override
        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitVarDeclaration(this);
        }
    }
}