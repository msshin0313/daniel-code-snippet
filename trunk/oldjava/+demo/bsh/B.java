import bsh.EvalError;

public class B extends A {
    public void print() {
        System.out.println("In class B.");
    }

    public static void main(String[] args) throws EvalError {
        B app = new B();
        app.run();
    }
}
