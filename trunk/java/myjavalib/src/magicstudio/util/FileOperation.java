package magicstudio.util;

import java.io.File;
import java.io.IOException;

/**
 * Date: 2004-1-10
 * Time: 19:15:53
 * Desc: A set of handful file manipulators
 * Progress: Interface definition
 */
public class FileOperation {

    /**
     * copy file using RENAME strategy to handler name conflict.
     * @param src original file being copied, should not be null
     * @param dest copy destination, if null, the file is not copied.
     * @return the new copy of the file
     */
    public static File copy(File src, File dest) throws IOException {
        assert src!=null && src.exists();
        if (dest==null) return src; // if the user wants to copy a file to null, then the file is not copied.
        //TODO: support dir copy operation
        if ( src.isDirectory() || dest.isDirectory() )
            throw new UnsupportedOperationException("Does not support directory operations");
        return copyFileToFile(src, dest, RENAME);
    }

    /**
     * move file using RENAME strategy to handle name conflict.
     * @param src original file being moved, should not be null
     * @param dest move destination, if null, the file is not copied.
     * @return the moved new file
     */
    public static File move(File src, File dest) throws IOException {
        assert src!=null && src.exists();
        if (dest==null) return src; // if the user wants to copy a file to null, then the file is not copied.
        //TODO: support dir move operation
        if ( src.isDirectory() || dest.isDirectory() )
            throw new UnsupportedOperationException("Does not support directory operations");
        return moveFileToFile(src, dest, RENAME);
    }

    // copy the file, and return the File of the new file
    // if destination file exists, using ConflictStrategy to handle the situation
    private static File copyFileToFile(File src, File dest, ConflictStrategy conflictStrategy) throws IOException {
        assert src!=null && dest!=null && conflictStrategy!=null;
        assert src.isFile() && !dest.isDirectory(); // isFile() means also exists()

        throw new UnsupportedOperationException("Not Implemented Yet.");
        //return null;
    }

    private static File moveFileToFile(File src, File dest, ConflictStrategy conflictStrategy) throws IOException {
        assert src!=null && dest!=null && conflictStrategy!=null;
        assert src.isFile() && !dest.isDirectory(); // isFile() means also exists()

        File newfile;   // the returned file
        if ( inSameDisk(src, dest) ) {
            dest = conflictStrategy.solve(dest);  // guarantees the 'dest' doesn't conflict with 'src'
            if ( !src.renameTo(dest) )
                throw new IOException("Cannot rename "+src+" to "+dest+" in the same disk.");
            newfile = src;
        } else {
            newfile = copyFileToFile(src, dest, conflictStrategy);
            if ( !src.delete() ) {
                throw new IOException("Can not delete file '" + src + "' in 'move' operation.");
            }
        }
        return newfile;
    }

    private static boolean inSameDisk(File fa, File fb) {
        assert fa!=null && fb !=null;
        if ( System.getProperty("os.name") == null || !System.getProperty("os.name").startsWith("Windows") )
            throw new UnsupportedOperationException("Only Supports MS Windows now.");
        return fa.getPath().charAt(0) == fb.getPath().charAt(0);
    }

    // Strategy pattern
    private static interface ConflictStrategy {
        /**
         * solve conflict-name files. <p>
         * 'src' should be moved/copyed to 'dest', but 'dest' already exists.
         * After invoking this method, the confliction should be solved. <p>
         * This method guarantees the conflict be solve after invocation,
         * i.e. 'dest' should not be occupied by other files, it should be available for 'src'.
         * However, it is not thread-safe.
         * @param dest the destination file that should not exist
         * @return the new dest that does not exist. null if your cannot rename 'dest' but to skip
         */
        File solve(File dest) throws IOException;
    }

    private static ConflictStrategy RENAME = new ConflictStrategy() {
        public File solve(File dest) throws IOException{
            assert dest!=null && !dest.isDirectory();
            while (dest.exists()) {

            }
            return dest;
        }
    };

/*    private static ConflictStrategy DELETE = new ConflictStrategy() {
        public void solve(File dest) throws IOException{
            assert dest!=null && !dest.isDirectory();
            if ( dest.exists() )
                if ( !dest.delete() )
                    throw new IOException("Cannot delete file '" + dest + "' in in ConflictStrategy DELETE");
        }
    };*/
}
