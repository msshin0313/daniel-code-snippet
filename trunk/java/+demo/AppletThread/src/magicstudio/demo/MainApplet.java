package magicstudio.demo;

import javax.swing.*;
import java.awt.print.PrinterJob;

public class MainApplet extends JApplet {
    public void init() {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.printDialog();
    }
}
