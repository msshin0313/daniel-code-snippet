import java.awt.*;
import java.awt.print.*;

/**
 * This example shows how to use the PrinterJob
 * and Book classes.
 */

public class PrintBlank implements Printable {

    /**
     * Print a single, blank page.
     */
    static public void main(String args[]) {

/* Get the representation of the current printer and
* the current print job.
*/
        PrinterJob printerJob = PrinterJob.getPrinterJob();

/* Build a book containing pairs of page painters (Printables)
 * and PageFormats. This example has a single page.
 */
        Book book = new Book();
        book.append(new PrintBlank(), new PageFormat());

/* Set the object to be printed (the Book) into the PrinterJob.
 * Doing this before bringing up the print dialog allows the
 * print dialog to correctly display the page range to be printed
 * and to dissallow any print settings not appropriate for the
 * pages to be printed.
 */
        printerJob.setPageable(book);

/* Show the print dialog to the user. This is an optional step
* and need not be done if the application wants to perform
* 'quiet' printing. If the user cancels the print dialog then false
* is returned. If true is returned we go ahead and print.
*/
        boolean doPrint = printerJob.printDialog();
        if (doPrint) {


            try {

                printerJob.print();

            } catch (PrinterException exception) {

                System.err.println("Printing error: " + exception);

            }

        }

    }

    /**
     * Print a blank page.
     */
    public int print(Graphics g, PageFormat format, int pageIndex) {

/* Do the page drawing here. This example does not do any
 * drawing and therefore blank pages are generated:
 * "This Page Intentionally Left Blank"
 */
        return Printable.PAGE_EXISTS;
    }

}

