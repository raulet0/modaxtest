package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.*;

public class EvalScript {
	
	// String[] formula = new String[]{"println(2*x); var y; y=2*x;"};
    static ScriptEngineManager manager = new ScriptEngineManager();
    static ScriptEngine engine = manager.getEngineByName("rhino"); // JavaScript

	public boolean evaluate1(String formula, Map<String, String> boundset) throws Exception {
		if (engine == null) { return false; }
		try {
			StringBuilder buffer = new StringBuilder();
			buffer.append("importPackage(java.util);");
			buffer.append("var todayDate = new Date();");
			buffer.append("println('Today Date Is ' + todayDate);");
			engine.eval(buffer.toString());
			engine.put("x", "10");
			engine.put("y", "5");
			System.out.println((boolean)engine.eval("x==y*2"));
			buffer = new StringBuilder();
			buffer.append("importClass(Packages.test.EvalScript);");
			buffer.append("EvalScript.triple(10);");
			String action = String.valueOf(engine.eval(buffer.toString()));
			System.out.println(action + 1);
		
		} catch (ScriptException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return false;
	}
	
	public boolean evaluate2(String formula, Map<String, String> boundset) throws Exception {
		String formula1 = formula.replace('?', '_');
		System.out.println(formula1);
		for (String key : boundset.keySet()) {
			System.out.println("key: " + key + " value: " + boundset.get(key));
			String key1 = key.replace('?', '_');
			engine.put(key1, boundset.get(key));
		}
		return (boolean)engine.eval(formula1);
	}

	public static int triple(int n) { return 3 * n; }
	
	public static void main(String[] args) throws Exception {

		EvalScript evalscript = new EvalScript();

	    List<ScriptEngineFactory> factories = manager.getEngineFactories(); 
	    for (ScriptEngineFactory factory : factories) { 
	        System.out.println("Name : " + factory.getEngineName()); 
	        System.out.println("Version : " + factory.getEngineVersion()); 
	        System.out.println("Language name : " + factory.getLanguageName()); 
	        System.out.println("Language version : " + factory.getLanguageVersion()); 
	        System.out.println("Extensions : " + factory.getExtensions()); 
	        System.out.println("Mime types : " + factory.getMimeTypes()); 
	        System.out.println("Names : " + factory.getNames()); 
	      }
	    HashMap<String, String> boundset1 = new HashMap<>();
	    boundset1.clear();
	    boundset1.put("?x", "2000");
	    boundset1.put("?y", "1000");
	    System.out.println(evalscript.evaluate2("?x==?y*2", boundset1));

	    boundset1.clear();
	    boundset1.put("?x", "2001");
	    boundset1.put("?y", "1000");
	    System.out.println(evalscript.evaluate2("?x==?y*2", boundset1));

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
