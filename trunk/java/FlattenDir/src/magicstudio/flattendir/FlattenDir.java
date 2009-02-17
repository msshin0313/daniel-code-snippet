package magicstudio.flattendir;

/*
 * @(#)magicstudio.flattendir.FlattenDir.java v2.0 03/06/08
 * Usage: Flatten selected directory. e.g: \root\dir1\file1 => \root\dir1_file1
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import magicstudio.util.DirTraversal;

public class FlattenDir {
    // used to replace the directory separator.
    // TODO: dynamicly ask user to assign one.
    private static char separator = '_';
    private static char sysSeparator = System.getProperty("file.separator").charAt(0);

    public static void main(String args[]) {
        assert System.getProperty("file.separator").length() == 1; // guarantee it's a char
        /*try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch(Exception e) {
            e.printStackTrace();
        }*/
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        MainFrame frame = new MainFrame();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // needs Windows environment in this version
        if (System.getProperty("os.name").toLowerCase().indexOf("windows") == -1) {
            JOptionPane.showMessageDialog(frame,
                    "Your OS is: " + System.getProperty("os.name") +
                    "\nPlease run under Windows",
                    "System Requirment", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        frame.pack();
        frame.setVisible(true);
    }

    private static class MainFrame extends JFrame {
        private JFrame frame = this;
        private JPanel pane = (JPanel) getContentPane();
        private JTextField fileText = new JTextField(30);
        private int prefixLength;
        private String prefix;
        private static int count = 0;	// renaming conflicts count

        MainFrame() {
            setTitle("FlattenDir");
            pane.setLayout(new GridLayout(0, 1));

            // setup upper pane
            JPanel upperPane = new JPanel(new FlowLayout());
            java.net.URL imgURL = this.getClass().getResource("Open16.gif");
            JButton browseButton = new JButton("Browse...", new ImageIcon(imgURL));
            fileText.addActionListener(startAction); // 'return' button fire this event

            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            // browse and select directory
            browseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (fc.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
                        fileText.setText(fc.getSelectedFile().toString());
                    }
                }
            });
            upperPane.add(fileText);
            upperPane.add(browseButton);
            pane.add(upperPane);

            // setup lower pane
            JPanel lowerPane = new JPanel(new FlowLayout());
            JButton aboutButton = new JButton("About");
            // show about infomation
            aboutButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(frame,
                            "Developed by Cobra@2003\nUsage: select directory to be flatten.");
                }
            });
            JButton startButton = new JButton("Start");
            startButton.addActionListener(startAction);
            lowerPane.add(aboutButton);
            lowerPane.add(startButton);

            pane.add(lowerPane);
        }

        // invoke operation here:
        private ActionListener startAction = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File file = new File(fileText.getText());
                //System.err.println( file.toString() );
                if (!file.exists() || file.isFile()) {
                    JOptionPane.showMessageDialog(frame,
                            file.toString() + "\ndoes not exists or\nis not a valid directory!",
                            "Error!", JOptionPane.ERROR_MESSAGE);
                } else {
                    // list content summary of the dir
                    File[] subfiles = file.listFiles();
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < 10 && i < subfiles.length; i++) {
                        sb.append("-- " + subfiles[i].toString() + "\n");
                    }
                    if (subfiles.length > 10) sb.append("......\n");
                    if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(frame,
                            file.toString() + " =>\n" +
                            sb.toString() +
                            "Do you wish to continue?",
                            "Continue?",
                            JOptionPane.YES_NO_OPTION)) {
                        prefix = file.toString() + FlattenDir.sysSeparator;
                        prefixLength = prefix.toString().length();
                        // start recursive method call
                        DirAccess da = new DirAccess(file);
                        da.start();
                    }
                }
            }
        };

        private class DirAccess extends DirTraversal {
            public DirAccess(File file) { super(file); }

            public void run() {
                super.run();
                JOptionPane.showMessageDialog(frame, "Operation Successful!");
            }

            protected void enterDir(File file) {
                // no operation
            }

            protected void leaveDir(File dir) {
                if (currentDepth!=1) {
                    System.err.println("Delete " + dir);
                    boolean flag = dir.delete();
                    if (!flag) throw new RuntimeException();
                }
            }

            protected void handleFile(File file) {
                File dir = file.getParentFile();
                if (prefix.equals(dir.toString() + FlattenDir.sysSeparator)) return;
                String filename = file.toString().substring(prefixLength);
                // replace '\' in Windows or '/' in Linux to 'separator'
                filename = filename.replace(FlattenDir.sysSeparator, FlattenDir.separator);
                File newFile = new File(prefix + filename);
                while (newFile.exists()) {
                    count++;
                    // generate new unique filename to avoid duplicate
                    String surfix = "" + FlattenDir.separator + count;
                    int dotPos = filename.lastIndexOf('.');
                    if (dotPos == -1) { // there isn't a file extention name
                        newFile = new File(prefix + filename + surfix);
                    } else {
                        StringBuffer sb = new StringBuffer(filename);
                        sb.insert(dotPos, surfix);
                        newFile = new File(prefix + sb.toString());
                    }
                } // unique newFile garanteed
                System.err.println(newFile.toString());
                boolean flag = file.renameTo(newFile);
                if (!flag) throw new RuntimeException();
                }
            }

    } // end of JFrame
}
