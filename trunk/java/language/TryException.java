package language;

public class TryException {
    public static void main(String[] args) {
        try {
            throw new Exception("What's the matter?");
        } catch (Exception e) {
            try {
                throw new Exception("This is my new message.", e);
            } catch (Exception e1) {
                System.out.println(e1.getMessage());
                e1.printStackTrace();
            }
        }
    }

}
