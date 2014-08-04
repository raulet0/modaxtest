package test;

import javax.script.*;

public class EvalScript {
    public static void main(String[] args) throws Exception {
        // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
    	// engine.eval("print('Hello, World')");

        int max= 50000;
        long t1 = System.currentTimeMillis();
        // evaluate JavaScript code from String
        for (int i = 1; i < max; i++) {
        	engine.eval("1 == 1");
        }
        long dt = System.currentTimeMillis() - t1;
        System.out.println("\n" + dt + " ms, soit " + dt*1000/max + " us/eval");
        
        boolean test;
        int i1 = 1;
        int i2 = 1;
        t1 = System.currentTimeMillis();
        for (int i = 1; i < max; i++) {
        	test = (i1 == i2);
        }
        dt = System.currentTimeMillis() - t1;
        System.out.println("\n" + dt + " ms, soit " + dt*1000/max + " us/eval");
        
    }
}
