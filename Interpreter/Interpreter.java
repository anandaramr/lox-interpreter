package Interpreter;

import Lexer.Token;
import Lexer.TokenType;
import Ngi.Ngi;
import Ngi.RuntimeError;
import Parser.Expr;
import Parser.Stmt;

import java.util.List;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    Environment environment = new Environment(null);

    public void interpret(List<Stmt> statements) {
        try {
            for(Stmt statement: statements) {
                execute(statement);
            }
        } catch (RuntimeError err) {
            Ngi.runtimeError(err);
        }
    }

    private void execute(Stmt statement) {
        statement.accept(this);
    }

    @Override
    public Void visitExpression(Stmt.Expression expr) {
        evaluate(expr.expression);
        return null;
    }

    @Override
    public Void visitPrint(Stmt.Print expr) {
        Object value = evaluate(expr.expression);
        System.out.println(Ngi.stringify(value));
        return null;
    }

    @Override
    public Void visitVarDeclaration(Stmt.VarDeclaration expr) {
        Object value = null;
        if(expr.initializer!=null) {
            value = evaluate(expr.initializer);
        }

        environment.define(expr.name, value, expr.isConst);
        return null;
    }

    @Override
    public Object visitBinaryExpr(Expr.BinaryExpr expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case PLUS -> {
                if(left instanceof Double && right instanceof Double) return (double)left + (double)right;
                if(left instanceof String && right instanceof String) return (String)left + right;

                throw new RuntimeError(expr.operator, "Both operands in addition should be either strings or numbers " +
                        "(left: " + left + " right: " + right + ")");
            }
            case MINUS -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left - (double)right;
            }
            case STAR -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left * (double)right;
            }
            case FORSLASH -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left / (double)right;
            }
            case LESSERTHAN -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            }
            case LESSERTHANEQU -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
            }
            case GREATERTHAN -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            }
            case GREATERTHANEQU -> {
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            }
            case BANGEQUAL -> {
                return !isEqual(left,right);
            }
            case DEQUAL -> {
                return isEqual(left,right);
            }
        }

        return null;
    }

    @Override
    public Object visitLogicalExpr(Expr.LogicalExpr expr) {
        Object left = evaluate(expr.left);

        if(expr.operator.type==TokenType.OR) {
            if(isTruthy(left)) return left;
        }

        if(expr.operator.type==TokenType.AND) {
            if(!isTruthy(left)) return left;
        }

        return evaluate(expr.right);
    }

    @Override
    public Object visitUnary(Expr.Unary expr) {
        Object operand = evaluate(expr.operand);

        switch (expr.operator.type) {
            case MINUS -> {
                checkNumberOperands(expr.operator, operand);
                return - (double)operand;
            }
            case BANG -> {
                checkBooleanOperand(expr.operator, operand);
                return !isTruthy(operand);
            }
        }

        return null;
    }

    @Override
    public Object visitGrouping(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteral(Expr.Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitVariable(Expr.Variable expr) {
        return environment.get(expr.name);
    }

    @Override
    public Object visitAssign(Expr.Assign expr) {
        Token variable = expr.name;
        Object value = evaluate(expr.value);
        environment.assign(variable, value);
        return value;
    }

    @Override
    public Object visitTernaryExpr(Expr.TernaryExpr expr) {
        Object condition = evaluate(expr.condition);

        if(isTruthy(condition)) {
            return evaluate(expr.left);
        } else {
            return evaluate(expr.right);
        }
    }

    @Override
    public Void visitBlock(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }

    @Override
    public Void visitIf(Stmt.If stmt) {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }

        return null;
    }

    private void executeBlock(List<Stmt> statements, Environment environment) {
        Environment previous = this.environment;

        try {
            this.environment = environment;
            for (Stmt statement: statements) { execute(statement); }
        } finally {
            this.environment = previous;
        }
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isTruthy(Object object) {
        if(object==null) return false;
        if(object instanceof Boolean) return (boolean) object;
        if(object instanceof String) return !((String) object).isEmpty();

        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if(a==null && b==null) return true;
        if(a==null) return false;

        return a.equals(b);
    }

    private void checkNumberOperands(Token operator, Object ...operands) {
        for(Object operand: operands) {
            if(!(operand instanceof Double)) throw new RuntimeError(operator, "Operand must be a number");
        }
    }

    private void checkBooleanOperand(Token operator, Object operand) {
        if(operand instanceof Boolean) return;
        throw new RuntimeError(operator, "Operand must be of type boolean");
    }
}