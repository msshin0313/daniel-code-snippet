package magicstudio.util;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;
import java.util.Arrays;

/**
 * Date: 2004-2-28
 * Time: 12:02:40
 * Desc: a framework for directory traversal
 * Progress: beta
 */
public abstract class DirTraversal extends Thread {

    abstract protected void enterDir( File dir );
    abstract protected void leaveDir( File dir );
    abstract protected void handleFile( File file );

    private int maxDepth = Integer.MAX_VALUE;
    protected int currentDepth = 0;
    protected File rootDir;
    private Comparator<File> defaultComparator = null;
    private FileFilter defaultFileFilter = null;

    public void setMaxDepth( int depth ) {
        if (depth < 0) throw new IllegalArgumentException("maxDepth must be greater than 0");
        maxDepth = depth;
    }

    public void setComparator(Comparator<File> c) {
        defaultComparator = c;
    }

    public void setFileFilter( FileFilter ff ) {
        defaultFileFilter = ff;
    }

    public DirTraversal(File root) {
        if ( root==null || !root.isDirectory() )
            throw new IllegalArgumentException("rootDir should be an existed directory");
        rootDir = root;
    }

    public void run() {
        traverseDir( rootDir );
    }

    private void traverseDir(File dir) {
        assert dir.isDirectory();
        enterDir( dir ); // do sth before the dir is expanded.
        if (currentDepth == maxDepth) return;
        File[] entries = dir.listFiles( defaultFileFilter ); // if null, all the sub files will be returned
        Arrays.sort(entries, defaultComparator);
        // the dir has been expanded. handle every entry in the sub directory
        currentDepth++;
        for (int i = 0; i < entries.length; i++) {
            if (entries[i].isFile()) {
                handleFile( entries[i] );
            } else {
                traverseDir(entries[i]); // recursive
            }
        }
        leaveDir( dir ); // Do sth after all entries of the sub directory have been accessed.
        currentDepth--;
    }

    public static Comparator<File> SORT_BY_NAME = new Comparator<File>() {
        public int compare( File f1, File f2) {
            return f1.compareTo(f2);
        }
    };

    public static Comparator<File> SORT_BY_REVERSE_NAME = new Comparator<File>() {
        public int compare( File f1, File f2) {
            return -f1.compareTo(f2);
        }
    };

    // not truly a random algorithm
    public static Comparator<File> SORT_BY_PSEUDO_RANDOM = new Comparator<File>() {
        public int compare(File f1, File f2) {
            return f1.hashCode() - f2.hashCode();
        }
    };

    public static class PrintDir extends DirTraversal {
        public PrintDir(File root) { super(root); }

        protected void enterDir(File dir) {
            System.out.println(prefix()+'+'+dir.getName());
        }

        protected void leaveDir(File dir) {
            //System.out.println(prefix()+'!end');
        }

        protected void handleFile(File file) {
            System.out.println(prefix()+'-'+file.getName());
        }

        private String prefix() {
            StringBuffer sb = new StringBuffer();
            for (int i=0; i<currentDepth; i++) {
                sb.append(" ");
            }
            return sb.toString();
        }
    }

    ///////////////////////////////// test ///////////////////////////////
    public static void main(String[] args) {
        PrintDir pd = new PrintDir(new File("c:\\java\\apache-ant-1.5.3-1"));
        pd.setMaxDepth(1);
        pd.setComparator(SORT_BY_REVERSE_NAME);
        pd.start();
        //System.out.println("test threading");
    }
}
