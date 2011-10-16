package language;

public class Outer {
    private int id;

    /*public static class Inner {
        private int id;

        public void run() {
            Outer outer = new Outer();
            outer.id = 20;
            System.out.println(outer.id);
        }
    }

    // test if outer class can access private field of inner class
    public void run() {
        Inner inner = new Inner();
        inner.id = 10;
        System.out.println(inner.id);
    }*/

    public static void main(String[] args) {
        /*Outer outer = new Outer();
        outer.run();
        Inner inner = new Inner();
        inner.run();*/
        System.out.println("Hello, world");
    }
}
