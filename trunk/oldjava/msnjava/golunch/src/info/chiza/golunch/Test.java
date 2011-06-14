package info.chiza.golunch;

/**
 * @auther Daniel Zhou (danithaca@gmail.com)
 * @organization School of Information, University of Michigan
 * Date: Dec 5, 2007
 */
public class Test {
    public static void main(String[] args) {
        boolean b = "false".compareToIgnoreCase(null) > 0;
        System.out.println(b);
    }
}
