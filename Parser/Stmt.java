package Parser;

import Lexer.Token;

import java.util.List;

public abstract class Stmt {

    public abstract <E> E accept(Visitor<E> visitor);

    public interface Visitor<E> {
        E visitExpression(Expression expr);
        E visitPrint(Print expr);
        E visitVarDeclaration(VarDeclaration expr);
        E visitBlock(Block expr);
        E visitIf(If expr);
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

    public static class Block extends Stmt {
        Block(List<Stmt> statements) {
            this.statements = statements;
        }

        public final List<Stmt> statements;

        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitBlock(this);
        }
    }

    public static class If extends Stmt {
        If(Expr condition, Stmt thenBranch, Stmt elseBranch) {
            this.condition = condition;
            this.thenBranch = thenBranch;
            this.elseBranch = elseBranch;
        }

        public final Expr condition;
        public final Stmt thenBranch;
        public final Stmt elseBranch;

        public <E> E accept(Visitor<E> visitor) {
            return visitor.visitIf(this);
        }
    }
}