package Interpreter;

import Lexer.Token;
import Lox.RuntimeError;

import java.util.HashMap;

public class Environment {
    private final HashMap<String,Object> environment = new HashMap<>();
    private final HashMap<String,Boolean> constants = new HashMap<>();

    public void define(String name, Object value, boolean isConst) {
        environment.put(name, value);
        constants.put(name, isConst);
    }

    public Object get(Token token) {
        if(environment.containsKey(token.lexeme)) {
            return environment.get(token.lexeme);
        }

        throw new RuntimeError(token, "Undefined variable: \"" + token.lexeme + '"');
    }

    public void assign(Token variable, Object value) {
        String name = variable.lexeme;

        if(environment.containsKey(name)) {
            if(constants.get(name)) throw new RuntimeError(variable, "Cannot assign to constant variable");
            environment.put(name, value);
            return;
        }

        throw new RuntimeError(variable,"Cannot assign to undefined variable \"" + name + '"');
    }
}