import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class ViewImage 
{
    public static String fname;
    public static JFrame frame;
    public static JPanel pane;
    public static ImageIcon image;
    public static JLabel imageLabel = new JLabel();

	public static void main(String[] args) 
	{
        frame = new JFrame( "Simple Image Viewer" );
        pane = new JPanel();

        JButton fileButton = new JButton( "Input Filename" );
        fileButton.addActionListener( new ActionListener() {
            public void actionPerformed( ActionEvent e ) {
                fname = JOptionPane.showInputDialog( frame, "Input file name" );
                //System.out.println( fname );
                image = new ImageIcon( fname );
                imageLabel.setIcon( image );
            }} );
        
        pane.setLayout(new BorderLayout());
        pane.add( fileButton, BorderLayout.NORTH );
        pane.add( imageLabel, BorderLayout.CENTER );

        frame.getContentPane().add( pane, BorderLayout.CENTER );
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
	}
}
