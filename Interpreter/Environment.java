package Interpreter;

import Lexer.Token;
import Ngi.RuntimeError;

import java.util.HashMap;

public class Environment {
    private final HashMap<String,Object> environment = new HashMap<>();
    private final HashMap<String,Boolean> constants = new HashMap<>();
    private final Environment enclosing;

    Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(Token variable, Object value, boolean isConst) {
        if(environment.containsKey(variable.lexeme)) throw new RuntimeError(variable, "Variable already defined in scope");
        environment.put(variable.lexeme, value);
        constants.put(variable.lexeme, isConst);
    }

    public Object get(Token token) {
        if(environment.containsKey(token.lexeme)) {
            return environment.get(token.lexeme);
        }

        if(enclosing!=null) { return enclosing.get(token); }

        throw new RuntimeError(token, "Undefined variable: \"" + token.lexeme + '"');
    }

    public void assign(Token variable, Object value) {
        String name = variable.lexeme;

        if(environment.containsKey(name)) {
            if(constants.get(name)) throw new RuntimeError(variable, "Cannot assign to constant variable");
            environment.put(name, value);
            return;
        }

        if(enclosing!=null) {
            enclosing.assign(variable, value);
            return;
        }

        throw new RuntimeError(variable,"Cannot assign to undefined variable \"" + name + '"');
    }
}