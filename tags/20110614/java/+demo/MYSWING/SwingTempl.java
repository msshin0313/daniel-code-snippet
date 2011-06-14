/*
 * @(#)SwingTempl.java 1.0 03/06/05
 */
//package myprojects.swingtempl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class MyFrame extends JFrame {
	private JButton button = new JButton( "Click" );
	private JTextField text = new JTextField( 20 );
	private JPanel pane = (JPanel)getContentPane();
	
	MyFrame() {
		setTitle("Swing Template");
		pane.setLayout( new GridLayout(0,1) );
		pane.add( text );
		pane.add( button );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		
		button.addActionListener( new ActionListener () {
			public void actionPerformed(ActionEvent event) {
				String s = text.getText();
				if ( s.equals("") ) s = "Click";
				((JButton)(event.getSource())).setText( s );
				Toolkit.getDefaultToolkit().beep();
			}
		});	
	}
}

public class SwingTempl {
	
	public static void main(String args[]) {
		System.out.println("Starting SwingTempl...");
		MyFrame mainFrame = new MyFrame();
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e) {
			e.printStackTrace();
		}
		mainFrame.pack();
		mainFrame.setVisible(true);	// 这里转入event-dispatch thread
	}
}
