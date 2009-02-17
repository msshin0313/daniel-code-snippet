import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;
import java.text.*;

/**
 * The PrintText application expands on the
 * PrintExample application in that it images
 * text on to the single page printed.
 */
public class PrintText implements Printable {

    /**
     * The text to be printed.
     */
    private static final String mText =
            "Four score and seven years ago our fathers brought forth on this "
                    + "continent a new nation, conceived in liberty and dedicated to the "
                    + "proposition that all men are created equal. Now we are engaged in "
                    + "a great civil war, testing whether that nation or any nation so "
                    + "conceived and so dedicated can long endure. We are met on a great "
                    + "battlefield of that war. We have come to dedicate a portion of "
                    + "that field as a final resting-place for those who here gave their "
                    + "lives that that nation might live. It is altogether fitting and "
                    + "proper that we should do this. But in a larger sense, we cannot "
                    + "dedicate, we cannot consecrate, we cannot hallow this ground."
                    + "The brave men, living and dead who struggled here have consecrated "
                    + "it far above our poor power to add or detract. The world will "
                    + "little note nor long remember what we say here, but it can never "
                    + "forget what they did here. It is for us the living rather to be "
                    + "dedicated here to the unfinished work which they who fought here "
                    + "have thus far so nobly advanced. It is rather for us to be here "
                    + "dedicated to the great task remaining before us--that from these "
                    + "honored dead we take increased devotion to that cause for which "
                    + "they gave the last full measure of devotion--that we here highly "
                    + "resolve that these dead shall not have died in vain, that this "
                    + "nation under God shall have a new birth of freedom, and that "
                    + "government of the people, by the people, for the people shall "
                    + "not perish from the earth.";


    /**
     * Our text in a form for which we can obtain a
     * AttributedCharacterIterator.
     */
    private static final AttributedString mStyledText = new AttributedString(mText);

    /**
     * Print a single page containing some sample text.
     */
    static public void main(String args[]) {

/* Get the representation of the current printer and
 * the current print job.
*/
        PrinterJob printerJob = PrinterJob.getPrinterJob();

/* Build a book containing pairs of page painters (Printables)
 * and PageFormats. This example has a single page containing
 * text.
 */
        Book book = new Book();
        book.append(new PrintText(), new PageFormat());

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
     * Print a page of text.
     */
    public int print(Graphics g, PageFormat format, int pageIndex) {

/* We'll assume that Jav2D is available.
 */
        Graphics2D g2d = (Graphics2D) g;

/* Move the origin from the corner of the Paper to the corner
 * of the imageable area.
 */
        g2d.translate(format.getImageableX(), format.getImageableY());

/* Set the text color.
 */
        g2d.setPaint(Color.black);

/* Use a LineBreakMeasurer instance to break our text into
 * lines that fit the imageable area of the page.
 */
        Point2D.Float pen = new Point2D.Float();
        AttributedCharacterIterator charIterator = mStyledText.getIterator();
        LineBreakMeasurer measurer = new LineBreakMeasurer(charIterator, g2d.getFontRenderContext());
        float wrappingWidth = (float) format.getImageableWidth();


        while (measurer.getPosition() < charIterator.getEndIndex()) {

            TextLayout layout = measurer.nextLayout(wrappingWidth);
            pen.y += layout.getAscent();
            float dx = layout.isLeftToRight() ? 0 : (wrappingWidth - layout.getAdvance());

            layout.draw(g2d, pen.x + dx, pen.y);
            pen.y += layout.getDescent() + layout.getLeading();

        }
        return Printable.PAGE_EXISTS;

    }
}

