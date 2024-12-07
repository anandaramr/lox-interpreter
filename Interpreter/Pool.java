package Interpreter;

import java.util.HashMap;

public class Pool {
    private final static HashMap<String,String> pool = new HashMap<>();

    public static void setVariable(String label, String value) {
        pool.put(label, value);
    }

    public static String getVariable(String label) {
        return pool.get(label);
    }
}
