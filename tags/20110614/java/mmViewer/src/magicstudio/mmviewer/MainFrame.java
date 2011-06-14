package magicstudio.mmviewer;

/*
 * @(#)magicstudio.mmviewer.MMViewer.java 1.0 03/06/10
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.util.logging.*;

class MainFrame extends JFrame {
	private static Logger logger = Logger.getLogger("magicstudio.mmviewer.MainFrame");
	private GlobalSettings gs = GlobalSettings.getSettings();
	private JFrame frame = this;
	private JPanel pane = (JPanel)getContentPane();
	private JTextField sourceDir = new JTextField( 30 );
	private JTextField destDir = new JTextField( 30 );
	private JButton resumeButton;

	MainFrame() {
		// frame setup
		setTitle("magicstudio.mmviewer.MMViewer");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
   			 public void windowClosing(WindowEvent e) {
   			 	 ImageQueue.getImageQueue().processRenaming();
   			 	 gs.saveGeneral();
	  		 }});
		logger.setLevel( Level.ALL );
		logger.info( "magicstudio.mmviewer.MainFrame constructing..." );
		
		// source pane setup
		JPanel sourcePane = new JPanel( new FlowLayout() );
		sourceDir.setText( gs.srcDir==null?null:gs.srcDir.toString() );
		sourceDir.setEditable(false);
		JButton sourceBrowse = createBrowseButton( sourceDir );
		sourcePane.setBorder( BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder( Color.black ),
			"Source Directory:" ) );
		sourcePane.add(sourceDir);
		sourcePane.add(sourceBrowse);

		
		// destination pane setup
		JPanel destPane = new JPanel( new FlowLayout() );
		destDir.setText( gs.destDir==null?null:gs.destDir.toString() );
		destDir.setEditable(false);
		JButton destBrowse = createBrowseButton( destDir );
		destPane.setBorder( BorderFactory.createTitledBorder(
			BorderFactory.createLineBorder( Color.black ),
			"Destination Directory:" ) );
		destPane.add(destDir);
		destPane.add(destBrowse);
		

		// control pane setup
		JPanel controlPane = new JPanel( new FlowLayout() );
		// Config...
		JButton configButton = new JButton( "Config..." );
		configButton.addActionListener( configListener );
		configButton.setEnabled(false);
		// Start
		JButton startButton = new JButton( "<html><b><font color=red>START!</b></font><html>" );
		startButton.addActionListener( startListener );
		// Resume
		resumeButton = new JButton( "RESUME" );
		resumeButton.setEnabled(false);
		resumeButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				invokeImageBox();
				}});
		// About...
		JButton aboutButton = new JButton( "About..." );
        aboutButton.addActionListener(new ActionListener() {
        	public void actionPerformed( ActionEvent e ) {
        		JOptionPane.showMessageDialog(frame,
        			"Developed by Cobra@2003");
        		}});
		controlPane.add( configButton );
		controlPane.add( startButton );
		controlPane.add( resumeButton );
		controlPane.add( aboutButton );
		

		// content pane setup
		pane.setLayout( new GridLayout(0,1) );
		pane.setBorder( BorderFactory.createEmptyBorder(5,5,5,5) );
		pane.add( sourcePane );
		pane.add( destPane );
		pane.add( controlPane );
	}
	
	// create a button choosing one dir and set text to JTextField
	private JButton createBrowseButton( final JTextField text ) {
		ImageIcon imgIcon;
		java.net.URL imgURL = this.getClass().getResource("Open16.gif");
        JButton browseButton = new JButton("Browse...", new ImageIcon(imgURL));
        final JFileChooser fc = new JFileChooser( text.getText() );
		fc.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
		// browse and select directory
        browseButton.addActionListener( new ActionListener() {
        	public void actionPerformed( ActionEvent e ) {
        		if ( fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION ) {
        			text.setText( fc.getSelectedFile().toString() );
        		}
        	}} );
        return browseButton;
    }
    
    private ActionListener configListener = new ActionListener() {
    	public void actionPerformed( ActionEvent e ) {
    	}
    };
    
    private ActionListener startListener = new ActionListener() {
    	public void actionPerformed( ActionEvent e ) {
    		if ( !validateDir() ) return;
    		logger.info( "Start running..." );
    		gs.srcDir = new File( sourceDir.getText() );
    		gs.destDir = new File( destDir.getText() );
    		gs.saveDir(); // only here need to save the dir settings
    		ImageQueue.getImageQueue().processRenaming();
    		ImageQueue.getImageQueue().init( gs.srcDir );
    		invokeImageBox();
    		resumeButton.setEnabled(true);
    	}
    };
    
    private void invokeImageBox() {
    	ImageBox ib = new ImageBox();
    	ib.setVisible(true);
    }
    
    private boolean validateDir() {
    	File src = new File( sourceDir.getText() );
    	File dest = new File( destDir.getText() );
    	boolean flag = true;
    	StringBuffer sb = new StringBuffer();
    	if ( !src.exists() || src.isFile() ) {
    		sb.append( "Source Directory: " + src.toString() + "\n" );
    		flag = false;
    	}
    	if ( !dest.exists() || dest.isFile() ) {
    		sb.append( "Destination Directory: " + dest.toString() + "\n" );
    		flag = false;
    	}
    	if ( !flag ) {	// test existence validity
    		sb.append( "Does not exist or is not a valid directory" );
    		JOptionPane.showMessageDialog( frame, sb.toString(),
    			 "ERROR!", JOptionPane.ERROR_MESSAGE );
    		return false;
    	} 
    	// test whether in the same disk to move
    	if ( System.getProperty("os.name").toLowerCase().indexOf("windows") != -1
    	     && src.toString().charAt(0) != dest.toString().charAt(0) ) {
    		JOptionPane.showMessageDialog( frame,
			 "Source / Destination are not on the same disk.\nNot Support!",
			 "ERROR!", JOptionPane.ERROR_MESSAGE );
			return false;
		}
    	// test destDir duplicity
		if ( ! ImageQueue.getImageQueue().prepareDestDir( dest ) ) {
			JOptionPane.showMessageDialog( frame,
			 "Destination directory contains sub dirs\nwhich have duplicate initial letters",
			 "ERROR!", JOptionPane.ERROR_MESSAGE );
			return false;
		}
		return true;
    }
}
	