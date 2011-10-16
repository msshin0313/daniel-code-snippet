package script

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptEngineFactory

//testEngines();
testPHP()
//testJS()

def testEngines() {
    ScriptEngineManager manager = new ScriptEngineManager();
    List<ScriptEngineFactory> factories = manager.getEngineFactories();
    for (ScriptEngineFactory factory : factories) {
        println factory.getEngineName();
        println factory.getLanguageName();
    }
}

def testJS() {
    // create a script engine manager
    ScriptEngineManager factory = new ScriptEngineManager();
    // create a JavaScript engine
    ScriptEngine engine = factory.getEngineByName("JavaScript");
    // evaluate JavaScript code from String
    engine.eval("println('Hello, World');");

    List<String> namesList = new ArrayList<String>();
    namesList.add("Jill");
    namesList.add("Bob");
    namesList.add("Laureen");
    namesList.add("Ed");

    engine.put("namesListKey", namesList);
    System.out.println("Executing in script environment...");
    try {
        engine.eval("var x;" +
                "var names = namesListKey.toArray();" +
                "for(x in names) {" +
                "  println(names[x]);" +
                "}" +
                "namesListKey.add(\"Dana\");");
    } catch (ScriptException ex) {
        ex.printStackTrace();
    }
    System.out.println("Executing in Java environment...");
    for (String name: namesList) {
        System.out.println(name);
    }
}

def testPHP() {
    ScriptEngineManager factory = new ScriptEngineManager();
    ScriptEngine engine = factory.getEngineByName("php");
    if (engine == null) throw new RuntimeException("Can't find PHP engine");
    //engine.eval('<?php $a=1; $a++; echo $a;');
    //engine.eval('<?php echo $a++;');
    engine.put('a', 123);
    engine.eval('<?php echo $a;')
    println engine.get('a');
}

def testGroovy() {
    ScriptEngineManager factory = new ScriptEngineManager();
    ScriptEngine engine = factory.getEngineByName("Groovy");
    engine.eval("println 'Hello world from groovy'");
}
