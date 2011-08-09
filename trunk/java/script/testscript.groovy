package script

import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptEngineFactory

//testEngines();
testPHP()

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
    engine.eval("print('Hello, World')");
}

def testPHP() {
    ScriptEngineManager factory = new ScriptEngineManager();
    ScriptEngine engine = factory.getEngineByName("php");
    //engine.put('$a', '123');
    engine.eval('<?php $a=1; $a++; echo $a;');
    //println engine.get('$a');
}

def testGroovy() {
    ScriptEngineManager factory = new ScriptEngineManager();
    ScriptEngine engine = factory.getEngineByName("Groovy");
    engine.eval("println 'Hello world from groovy'");
}
