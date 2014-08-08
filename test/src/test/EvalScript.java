package test;

import java.util.Map;

import javax.script.*;

public class EvalScript {
	
	// String[] formula = new String[]{"?x","==","?y"};
	// String[] formula = new String[]{"println(2*x); var y; y=2*x;"};
	String[] formula = new String[]{"x==y"};
    static ScriptEngineManager factory = new ScriptEngineManager();
    static ScriptEngine engine = factory.getEngineByName("JavaScript");

	public boolean evaluate(Map<String, String> boundset) throws Exception {
		// String fte = Arrays.deepToString(formula);
		try {
			StringBuilder buffer = new StringBuilder();
			buffer.append("importPackage(java.util);");
			buffer.append("var todayDate = new Date();");
			buffer.append("println('Today Date Is ' + todayDate);");
			engine.eval(buffer.toString());
			engine.put("x", 11);
			engine.put("y", 11);
			System.out.println((boolean)engine.eval("x==y"));
			buffer = new StringBuilder();

			// http://stackoverflow.com/questions/3180188/import-a-class-in-scripting-java-javax-script
			buffer.append("importClass(Packages.test.EvalScript);");
			buffer.append("triple(10)");
			engine.eval(buffer.toString());
			
		
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return false;
	}
	
	public static int triple(int n) { return 3 * n; }
	
	public static void main(String[] args) throws Exception {

		EvalScript evalscript = new EvalScript();
		evalscript.evaluate(null);

		// engine.eval("importPackage(javax.swing);var op=JOptionPane.showMessageDialog(null, 'Hello!');");

		/*
		// create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");
    	engine.eval("print('Hello, World')");

        int max= 10000;
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
        */
    }
}
