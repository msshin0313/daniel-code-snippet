package magicstudio.mmviewer;

/*
 * @(#)magicstudio.mmviewer.MMViewer.java 1.0 03/06/10
 * this class is used to store app-wide vars
 */
import javax.swing.*;

public class MMViewer {
	
	public static void main(String args[]) {
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch(Exception e) {
			e.printStackTrace();
		}
		// to enable full screen support, the following statement is invalid
		//JFrame.setDefaultLookAndFeelDecorated(true);
		//JDialog.setDefaultLookAndFeelDecorated(true);
		MainFrame frame = new MainFrame();
		frame.pack();
		frame.setVisible(true);
	}
}

class OutOfImageException extends Exception {}