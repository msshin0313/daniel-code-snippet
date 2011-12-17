package jython;

public class JavaApp {

    public int a = 1;

    private int a1;
    private int a2;

    public JavaApp() {
        a1 = 10;
        a2 = 10;
    }

    public JavaApp(int a1) {
        this.a1 = a1;
        this.a2 = 9;
    }

    public JavaApp(int a1, int a2) {
        this.a1 = a1;
        this.a2 = a2;
    }

    public JavaApp(String s) {
        a1 = 99;
        a2 = 99;
    }

    public void printme() {
        System.out.println("Numbers: " + a1 + ' ' + a2);
    }

    public void printme(String s) {
        System.out.println("String: " + s);
    }

    protected String b = "hello";

    public void reflectClass(Class theClass) {
        System.out.println(theClass.getName());
        /*System.out.println(theClass.toString());
        System.out.println(theClass.getSimpleName());
        System.out.println(theClass.getCanonicalName());
        for (Class c : theClass.getClasses()) {
            System.out.println(c.getSimpleName());
        }
        System.out.println(theClass.getSuperclass().getName());*/

    }
}
