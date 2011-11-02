package language;

public class ClassMethod {

    public void register(Class<?> classType) {
        assert CharSequence.class.isAssignableFrom(classType);
        System.out.println(classType.getName() + " registered.");
    }

    public static void main(String[] args) {
        //System.out.println(System.class.getName());
        ClassMethod c = new ClassMethod();
        c.register(String.class);
        c.register(System.class);
    }

}
