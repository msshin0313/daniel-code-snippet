import bsh.EvalError;
import bsh.Interpreter;

public class A {
    /*public void print() {
        System.out.println("In class A.");
    }*/

    public void run() throws EvalError {
        Interpreter i = new Interpreter();
        i.set("app", this);
        i.eval("app.print()");
    }

    public class Inner {

    }
}
