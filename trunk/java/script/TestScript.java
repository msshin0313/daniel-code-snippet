package script;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class TestScript {
    public static void main(String[] args) throws ScriptException {
        // this is to avoid Groovy libraries.
        ScriptEngineManager factory = new ScriptEngineManager();
        ScriptEngine engine = factory.getEngineByName("PHP");
        engine.eval("echo 'Hello, world';");
    }
}
