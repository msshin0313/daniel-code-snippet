package magicstudio.mmviewer;

/*
 * @(#)magicstudio.mmviewer.MMViewer.java 1.0 03/06/10
 */
import javax.swing.ImageIcon;
import java.util.*;
import java.io.*;
import java.util.logging.*;
import java.awt.event.KeyEvent;

class ImageQueue {
	private static Logger logger = Logger.getLogger("magicstudio.mmviewer.ImageQueue");
	
	// singleton pattern
	private ImageQueue() {
		logger.setLevel( Level.ALL );
	}
	private static ImageQueue currentInstance = null;
	public static ImageQueue getImageQueue() {
		if ( currentInstance == null ) {
			currentInstance = new ImageQueue();
		}
		return currentInstance;
	}
	
	private ArrayList fileList = new ArrayList( 500 );

	// a rather complex implementation
	/*public void init( File dir ) {
		assert dir.exists() && dir.isDirectory();
		fileList.clear();
		fileList.add( dir );
	}
	private File getNextFileFromList() {
		if ( fileList.isEmpty() ) return null;
		File file = (File)fileList.get(0);
		if ( file.isDirectory() ) {
			File[] subFiles = file.listFiles( new FileFilter() {
				public boolean accept(File f) {
					if ( f.isDirectory() ) return true;
					else return f.getName().matches(".+\\.jpg|.+\\.jpeg");
				}} );
			for ( int i=0; i<subFiles.length; i++ ) {
				fileList.add( subFiles[i] );
			}
			fileList.remove(0); // remove the current dir
			return getNextFileFromList();
		} else {
			fileList.remove(0);
			return file;
		}
	}*/

	// current image index
	private int currIndex;
	public void init( File dir ) {
		assert dir.exists() && dir.isDirectory();
		fileList.clear();
		appendList( dir );
		currIndex = -1; // means the begin of the list
	}
	
	private void appendList( File dir ) {
		assert dir.exists() && dir.isDirectory();
		File[] subFiles = dir.listFiles( new FileFilter() {
			public boolean accept(File f) {
				if ( f.isDirectory() ) {
					// does not include sub dir that ends with '~'
					if ( f.getName().endsWith("~") ) return false;
					else return true;
				} else {
					// set which type of images is acceptable
					return f.getName().toLowerCase().matches(".+\\.jpg|.+\\.jpeg");
				}
			}} );
		for ( int i=0; i<subFiles.length; i++ ) {
			if ( subFiles[i].isDirectory() ) {
				if ( GlobalSettings.getSettings().includeSubDir ) {
					appendList( subFiles[i] );
				} else {
					continue;	// skip subdirectory
				}
			} else {
				fileList.add( subFiles[i] );
			}
		}
	}
	
	private File getNextFileFromList() {
		if ( fileList.size() == 0 ) return null;
		if ( currIndex == -1 ) currIndex = 0;
		else currIndex++;
		if ( currIndex == fileList.size() ) {
			logger.info( "End of fileList. currIndex->0" );
			currIndex = 0;
		}
		return (File)fileList.get( currIndex );
	}

	public synchronized ImageIcon getNextImage() throws magicstudio.mmviewer.OutOfImageException {
		File file = getNextFileFromList();
		if ( file == null ) throw new magicstudio.mmviewer.OutOfImageException();
		assert file.isFile();
		ImageIcon img = new ImageIcon( file.toString() );
		return img;
	}
		
	private File getPrevFileFromList() throws magicstudio.mmviewer.OutOfImageException {
		if ( fileList.size() == 0 ) return null;
		if ( currIndex == -1 ) currIndex = 0;
		else currIndex--;
		if ( currIndex == -1 ) {
			logger.info( "End of fileList. currIndex->end" );
			currIndex = fileList.size() - 1;
		}
		return (File)fileList.get( currIndex );
	}

	public synchronized ImageIcon getPrevImage() throws magicstudio.mmviewer.OutOfImageException {
		File file = getPrevFileFromList();
		if ( file == null ) throw new magicstudio.mmviewer.OutOfImageException();
		assert file.isFile();
		ImageIcon img = new ImageIcon( file.toString() );
		return img;
	}
	
	// store current destination dir, set by mapToDestDir()
	private File currDestDir;
	// key-dir map
	private File[] destDirs = new File[ 27 ];
	/**
	 * Pre   : dir-dest dir, destDirs
	 * Post  : if dir doesn't contain dup subdirs, return true and set destDirs
	 		   otherwise return false and destDirs contains useless infos
	 */
	public boolean prepareDestDir( File dir ) {
		assert dir != null && dir.isDirectory();
		for ( int i=0; i<27; i++ ) {
			destDirs[i] = null;
		}
		File[] subDir = dir.listFiles( new FileFilter() {
			public boolean accept( File f ) {
				return ( f.isDirectory() && Character.isLetter(f.getName().charAt(0)) );
			}} );
		for ( int i=0; i<subDir.length; i++ ) {
			char c = subDir[i].getName().toLowerCase().charAt(0);
			if ( destDirs[c-'a'] == null ) {
				destDirs[c-'a'] = subDir[i];
			} else {
				return false;
			}
		}
		destDirs[26] = dir;
		return true;
	}

	public boolean mapToDestDir( char key ) {
		assert Character.isLetter( key ) || key==' '
		       || key=='\\';
		key = Character.toLowerCase( key );
		if ( key == ' ' ) {
			currDestDir = destDirs[26];
			return true;
		} else if ( key=='\\' ) {
			currDestDir = null;
			return true;
		} else if ( destDirs[ key-'a' ] == null ) {
			return false;
		} else {
			currDestDir = destDirs[ key-'a' ];
			return true;
		}
	}
	
	private Map renameMap = new HashMap( 300 );
	/**
	 * Pre   : currIndex, currDestDir, renameMap
	 * Post  : rename the file of currIndex to currDestDir, add to renameMap
	 */
	public void renameCurrentImage() {
		// the put() replace old value, if any
		renameMap.put( fileList.get(currIndex), currDestDir );
		if (currDestDir==null) {
			logger.info( fileList.get(currIndex).toString() + " => DELETED!" );
		} else {
			logger.info( fileList.get(currIndex).toString() + " => " + currDestDir.toString() );
		}
	}
	
	public void removeCurrRename() {
		renameMap.remove( fileList.get(currIndex) );
	}
	
	// be careful of same name and move between disks
	// files are moved. so the original file is inaccessible
	// should be invoked at "START!", and "app exit"
	public void processRenaming() {
		Iterator iter = renameMap.keySet().iterator();
		while ( iter.hasNext() ) {
			File origin = (File)iter.next();
			// deleting operation
			if ( renameMap.get(origin) == null ) {
				if ( ! origin.delete() ) {
					logger.warning( "Delete "+origin.toString()+" ERROR!" );
				}
				continue;
			}
			
			File newfile = new File( renameMap.get(origin).toString() +
				System.getProperty("file.separator") + origin.getName() );
			// origin and newfile maybe the same
			if ( origin.equals( newfile ) ) continue;
			
			// generate new unique filename to avoid duplicate
			String newfileName = newfile.toString();
			int count = 0;
			while ( newfile.exists() ) {
				count++;
				String surfix = "_"+count;
				int dotPos = newfileName.lastIndexOf('.');
				if ( dotPos == -1 ) { // there isn't a file extention name
					newfile = new File( newfileName+surfix );
				} else {
					StringBuffer sb = new StringBuffer( newfileName );
					sb.insert( dotPos, surfix );
					newfile = new File( sb.toString() );
				}
			} // newfile is the unique dest file
			if ( ! origin.renameTo( newfile ) ) {
				logger.warning( "Renaming "+origin.toString()+
				  " to "+newfile.toString()+" ERROR!" );
			}
		}
		renameMap.clear();
	}
	
	public String getCurrImgCaption() {
		if ( currIndex<0 || currIndex>fileList.size()-1 ) {
			return null;
		}
		File dest = (File)renameMap.get( fileList.get(currIndex) );
		if ( dest==null ) {
			if ( renameMap.containsKey( fileList.get(currIndex) ) ) {
				return "DELETED!";
			} else {
				return null;
			}
		} else {
			if ( dest == destDirs[26] ) return "/";
			else return "/"+dest.getName();
		}
	}
}