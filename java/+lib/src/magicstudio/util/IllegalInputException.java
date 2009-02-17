package magicstudio.util;

/**
 * Date: 2004-1-10
 * Time: 18:45:23
 * Desc: To indicate a false input of user's
 * Progess: Done
 */
public class IllegalInputException extends Exception {
    public IllegalInputException( String message ) {
        super(message);
    }
}
