package magicstudio.mmviewer;

/*
 * @(#)magicstudio.mmviewer.MMViewer.java 1.0 03/06/10
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.util.logging.*;

class ImageBox extends JFrame {
	private GlobalSettings gs = GlobalSettings.getSettings();
	private static Logger logger = Logger.getLogger("magicstudio.mmviewer.ImageBox");
	private ImageBox frame = this;
	private JPanel pane = (JPanel)getContentPane();
	private JLabel imgLabel = new JLabel();
	private JScrollPane scrollPane = new JScrollPane();
	private ImageQueue imageQueue = ImageQueue.getImageQueue();
	private String caption;
	private Font captionFont = new Font( null, Font.PLAIN, 20 );
	
	ImageBox() {
		logger.setLevel( Level.ALL );
		if ( gs.fullScreen && 
		  GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().isFullScreenSupported() ) {
			logger.info( "Full-screen mode supported." );
			setUndecorated(true);
			setResizable(false);
		} else {
			setTitle("magicstudio.mmviewer.ImageBox");
    		setSize(400,300); // init size
    		setLocationRelativeTo(null);
    	}
    	setDefaultCloseOperation( DISPOSE_ON_CLOSE );
    	setGlassPane( new JComponent() {
    		public void paintComponent( Graphics g ) {
    			caption = imageQueue.getCurrImgCaption();
    			if ( caption != null ) {
	    			g.setColor( Color.red );
	    			g.setFont( captionFont );
	    			g.drawString( caption, 10, 20 );
	    		}
    		}} );
    	
    	imgLabel.setVerticalAlignment( JLabel.CENTER );
		imgLabel.setHorizontalAlignment( JLabel.CENTER );

		scrollPane.setViewportView( imgLabel );
		//scrollPane.getHorizontalScrollBar().setUnitIncrement(10);
		//scrollPane.getVerticalScrollBar().setUnitIncrement(10);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_NEVER );
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
		pane.add( scrollPane );
		
		// setup glass pane
		getGlassPane().addKeyListener( keyListener );		
		addWindowListener(new WindowAdapter() {
   			 public void windowActivated(WindowEvent e) {
   			 	 // set focus to receive key events
   			 	 getGlassPane().setVisible(true);
	   		     getGlassPane().requestFocusInWindow();
	   		     // set full-screen or maxsize window
	   		     GraphicsDevice gd = GraphicsEnvironment.
	   		     	getLocalGraphicsEnvironment().getDefaultScreenDevice();
	   		     if ( gs.fullScreen && gd.isFullScreenSupported() ) {
	   		     	// set full screen mode
	   		     	gd.setFullScreenWindow( frame );
	   		     } else {
	   		     	setExtendedState( MAXIMIZED_BOTH );
	   		     }
	   		     showNextImage();
	  		 }});
	}
	
	private void showNextImage() {
		try {
			showImage( imageQueue.getNextImage() );
		} catch (magicstudio.mmviewer.OutOfImageException e) {
			//JOptionPane.showMessageDialog( frame, "The End!" );
		}
	}
	
	private void showPrevImage() {
		try {
			showImage( imageQueue.getPrevImage() );
		} catch (magicstudio.mmviewer.OutOfImageException e) {
			//JOptionPane.showMessageDialog( frame, "The End!" );
		}
	}
	
	private Rectangle locationRect = new Rectangle(0,0,1,1);
	private void showImage(ImageIcon img) {
		imgLabel.setIcon( img );
		imgLabel.scrollRectToVisible( locationRect );
		logger.info( img.toString() );
	}
	
	// handle keyevents
	private KeyListener keyListener = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			Rectangle rect = scrollPane.getViewport().getViewRect();
			switch ( e.getKeyCode() ) {
			case KeyEvent.VK_UP:
				//System.out.println( rect.x );
				rect.y -= gs.unitIncrement;
				imgLabel.scrollRectToVisible( rect );
				break;
			case KeyEvent.VK_DOWN:
				//System.out.println( rect.y );
				rect.y += gs.unitIncrement;
				imgLabel.scrollRectToVisible( rect );
				break;
			case KeyEvent.VK_LEFT:
				rect.x -= gs.unitIncrement;
				imgLabel.scrollRectToVisible( rect );
				//System.out.println( rect.x+rect.width );
				break;
			case KeyEvent.VK_RIGHT:
				//System.out.println( rect.y+rect.height );
				rect.x += gs.unitIncrement;
				imgLabel.scrollRectToVisible( rect );
				break;
			case KeyEvent.VK_PAGE_UP:
				showPrevImage();
				break;
			case KeyEvent.VK_PAGE_DOWN:
				showNextImage();
				break;
			case KeyEvent.VK_DELETE:
				e.setKeyChar( '\\' );
				// map DELETE to '\'
				keyTyped( e );
				break;
			case KeyEvent.VK_ESCAPE:
				frame.dispose();
			}
		}
		public void keyTyped( KeyEvent e ) {
			char c = e.getKeyChar();
			if ( Character.isLetter( c ) || c==KeyEvent.VK_SPACE ||
				 c==KeyEvent.VK_BACK_SLASH ) {
				if ( imageQueue.mapToDestDir(c) == true ) {
					imageQueue.renameCurrentImage();
					Toolkit.getDefaultToolkit().beep();
					showNextImage();
				}
			} else if ( c=='=' ) {
				imageQueue.removeCurrRename();
				getGlassPane().repaint();
			}
		}
	};
	
	public static void main( String[] args ) {
    	ImageBox ib = new ImageBox();
    	ib.imageQueue.init( new java.io.File("C:\\test") );
    	ib.setVisible(true);
    }
}