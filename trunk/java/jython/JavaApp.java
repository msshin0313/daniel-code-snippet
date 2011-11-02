package jython;

public class JavaApp {

    public int a = 1;

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
