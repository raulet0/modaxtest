package test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.*;

/*
---> String[] formula = new String[]{"println(2*x); var y; y=2*x;"};
engine.eval("print('Hello, World')");
engine.eval("importPackage(javax.swing);var op=JOptionPane.showMessageDialog(null, 'Hello!');");
engine.eval("importPackage(java.util);var todayDate = new Date();println('Today Date Is ' + todayDate);");
*/

public class EvalScript {
	private static ScriptEngineManager manager = new ScriptEngineManager();
	private static ScriptEngine engine = manager.getEngineByName("JavaScript");
	private static Compilable engineCompiled = null;
	private static EvalScript evalscript = new EvalScript();
	private static boolean trace = true;
	private static String lib7 = "importClass(Packages.test.EvalScript);";
	private static String lib8 = "var EvalScript = Java.type('test.EvalScript');";
	private static String library = lib7;

	private static void traceln(String message) {
		if (trace) { System.out.println(message); }
	}
    
    public static void printAvailableEngines() {
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
	}
	
	public static int triple(int n) { return 3 * n; }
	
	public boolean evalBoolean(String formula, Map<String, String> boundset) {
		traceln(formula + " with " + boundset);
		try {
			String formula1 = formula.replace('?', '_');
			for (String key : boundset.keySet()) {
				String key1 = key.replace('?', '_');
				engine.put(key1, boundset.get(key));
			}
			StringBuilder buffer = new StringBuilder();
			buffer.append(library);
			buffer.append(formula1);
			buffer.append(';');
			traceln("-----> " + buffer);
			return (boolean)engine.eval(buffer.toString());
		} catch (ScriptException e) {
			traceln("Error: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return false;
	}

	public String evalString(String formula, Map<String, String> boundset) {
		traceln(formula + " with " + boundset);
		try {
			String formula1 = formula;
			for (String key : boundset.keySet()) {
				formula1 = formula1.replace(key, boundset.get(key));
			}
			String buffer = library + formula1 + ";";
			traceln("-----> " + buffer);
			return String.valueOf(engine.eval(buffer));
		} catch (ScriptException e) {
			traceln("Error: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return "error";
	}

	public String evalStringCompiled(String formula, Map<String, String> boundset) {
		traceln(formula + " with " + boundset + " and compiled");
		try {
			String formula1 = formula;
			for (String key : boundset.keySet()) {
				formula1 = formula1.replace(key, boundset.get(key));
			}
			String buffer = library + formula1 + ";";
			traceln("-----> " + buffer);
			CompiledScript compiledscript = engineCompiled.compile(buffer);
			return String.valueOf(compiledscript.eval());
		} catch (ScriptException e) {
			traceln("Error: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
		return "error";
	}

	public void evalBooleanTest(int max, String formula, Map<String, String> boundset) throws ScriptException {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
        	evalscript.evalBoolean(formula, boundset);
        }
        long dt = System.currentTimeMillis() - t1;
        System.out.println(dt + " ms, soit " + dt*1000/max + " us/eval");
	}

	public void evalStringTest(int max, String formula, Map<String, String> boundset) throws ScriptException {
        long t1 = System.currentTimeMillis();
        for (int i = 0; i < max; i++) {
        	evalscript.evalString(formula, boundset);
        }
        long dt = System.currentTimeMillis() - t1;
        System.out.println(dt + " ms, soit " + dt*1000/max + " us/eval");
	}

	public void evalStringTestCompiled(int max, String formula, Map<String, String> boundset) throws ScriptException {
		traceln(formula + " with " + boundset + " and compiled");
		String buffer = library + formula.replace('?', '_') + ";";
		traceln("-----> " + buffer);
		String result;
		long t1 = System.currentTimeMillis();
		try {
			Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE); 
			CompiledScript compiledscript = engineCompiled.compile(buffer);
			for (int i = 0; i < max; i++) {
				bindings.clear(); 
				for (String key : boundset.keySet()) {
					String key1 = key.replace('?', '_');
					bindings.put(key1, boundset.get(key));
				}
				traceln(bindings.keySet().toString() + bindings.values().toString());
				result = String.valueOf(compiledscript.eval(bindings));
				traceln(result);
			}
		} catch (ScriptException e) {
			traceln("Error: " + e.getMessage());
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
        long dt = System.currentTimeMillis() - t1;
        System.out.println(dt + " ms, soit " + dt*1000/max + " us/eval");
	}

	public static void main(String[] args) throws Exception {
		System.out.println(Runtime.class.getPackage().getImplementationVersion());
		printAvailableEngines();
		String enginename = engine.getFactory().getEngineName().toLowerCase();
		if (enginename.contains("rhino")) {
			library = lib7;
			System.out.println("RHINO, compilable:" + (engine instanceof Compilable));
		} else {
			library = lib8; // supposed nashorn
			System.out.println("NASHORN, compilable:" + (engine instanceof Compilable));
		}
		engineCompiled = (Compilable)engine;
		HashMap<String, String> bs1 = new HashMap<>();
	    bs1.put("?x", "2000");
	    bs1.put("?y", "1000.0");
	    bs1.put("?z", "dbms");
	    HashMap<String, String> bs2 = new HashMap<>();
	    bs2.put("?x", "2001");
	    bs2.put("?y", "1000.0");
	    bs2.put("?z", "dbms");
	    HashMap<String, String> bs3 = new HashMap<>();
	    bs3.put("?x", "6000");
	    bs3.put("?y", "1000.0");
	    bs3.put("?z", "dbms");
	    HashMap<String, String> bs4 = new HashMap<>();
	    bs4.put("?x", "toto");
	    bs4.put("?y", "1000.0");
	    bs4.put("?z", "dbms");
	    trace = true;
	    System.out.println(evalscript.evalBoolean("?x==?y*2", bs1));
	    System.out.println(evalscript.evalBoolean("?x==?y*2", bs2));
	    System.out.println(evalscript.evalString("?x+?y*2", bs1));
	    System.out.println(evalscript.evalBoolean("?x==EvalScript.triple(?y*2)", bs3));
	    System.out.println(evalscript.evalBoolean("?x==EvalScript.triple1(?y*2)", bs3));
	    System.out.println(evalscript.evalString("?x+EvalScript.triple(?y*2)", bs1));
	    System.out.println(evalscript.evalString("?x+EvalScript.triple(?y*2)", bs4));
	    System.out.println(evalscript.evalString("?x==?y*2", bs1));
	    System.out.println(evalscript.evalStringCompiled("?x==?y*2", bs1));
	    trace = false;
	    evalscript.evalBooleanTest(1000, "?x==?y*2", bs1);
	    evalscript.evalStringTest(1000, "?x==?y*2", bs1);
	    evalscript.evalStringTest(1000, "?x+?y*2", bs1);
	    evalscript.evalStringTestCompiled(1000, "?x-?y*2", bs1);
	}
}
