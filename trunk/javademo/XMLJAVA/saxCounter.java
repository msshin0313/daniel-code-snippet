/*
 * (C) Copyright IBM Corp. 1999  All rights reserved.
 *
 * US Government Users Restricted Rights Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 *
 * The program is provided "as is" without any warranty express or
 * implied, including the warranty of non-infringement and the implied
 * warranties of merchantibility and fitness for a particular purpose.
 * IBM will not be liable for any damages suffered by you as a result
 * of using the Program. In no event will IBM be liable for any
 * special, indirect or consequential damages or lost profits even if
 * IBM has been advised of the possibility of their occurrence. IBM
 * will not be liable for any third party claims against you.
 */

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.ParserFactory;

import org.apache.xerces.parsers.SAXParser;

/**
 * A sample SAX writer. This sample program illustrates how to
 * register a SAX DocumentHandler and receive the callbacks in
 * order to print a document that is parsed.
 *
 * @version Revision: 06 1.5 samples/sax/SAXWriter.java, samples, xml4j2, xml4j2_0_15 
 */
public class saxCounter
  extends HandlerBase 
{

  int startDocumentEvents = 0;
  int endDocumentEvents = 0;
  int startElementEvents = 0;
  int endElementEvents = 0;
  int processingInstructionEvents = 0;
  int characterEvents = 0;
  int ignorableWhitespaceEvents = 0;
  int warningEvents = 0;
  int errorEvents = 0;
  int fatalErrorEvents = 0;

  public void parseURI(String uri)
  {
    SAXParser parser = new SAXParser();
    parser.setDocumentHandler(this);
    parser.setErrorHandler(this);
    try
    {
      parser.parse(uri);
    }
    catch (Exception e)
    {
      System.err.println(e);
    }

    System.out.println("Document Statistics for " + uri + ":");
    System.out.println("====================================");
    System.out.println("DocumentHandler Events:");
    System.out.println("  startDocument           " + 
                       startDocumentEvents);
    System.out.println("  endDocument             " + 
                       endDocumentEvents);
    System.out.println("  startElement            " +
                       startElementEvents);
    System.out.println("  endElement              " +
                       endElementEvents);
    System.out.println("  processingInstruction   " +
                       processingInstructionEvents);
    System.out.println("  character               " + 
                       characterEvents);
    System.out.println("  ignorableWhitespace     " + 
                       ignorableWhitespaceEvents);
    System.out.println("ErrorHandler Events:");
    System.out.println("  warning                 " + 
                       warningEvents);
    System.out.println("  error                   " + 
                       errorEvents);
    System.out.println("  fatalError              " + 
                       fatalErrorEvents);
		System.out.println("                          ----------");
    int totalEvents = startDocumentEvents + endDocumentEvents + 
                      startElementEvents + endElementEvents + 
                      processingInstructionEvents + 
                      characterEvents + ignorableWhitespaceEvents + 
                      warningEvents + errorEvents + fatalErrorEvents;
    System.out.println("Total:                    " + 
                       totalEvents + " Events");
  }

  /** Processing instruction. */
  public void processingInstruction(String target, String data) 
  {
    processingInstructionEvents++;
  } // processingInstruction(String,String)

  /** Start document. */
  public void startDocument() 
  {
    startDocumentEvents++;
  } // startDocument()

  /** Start element. */
  public void startElement(String name, AttributeList attrs) 
  {
    startElementEvents++;
  } // startElement(String,AttributeList)

  /** Characters. */
  public void characters(char ch[], int start, int length) 
  {
    characterEvents++;
  } // characters(char[],int,int);

  /** Ignorable whitespace. */
  public void ignorableWhitespace(char ch[], int start, int length) 
  {
    ignorableWhitespaceEvents++;
  } // ignorableWhitespace(char[],int,int);

  /** End element. */
  public void endElement(String name) 
  {
    endElementEvents++;
  } // endElement(String)

  /** End document. */
  public void endDocument() 
  {
    endDocumentEvents++;
  } // endDocument()

  //
  // ErrorHandler methods
  //

  /** Warning. */
  public void warning(SAXParseException ex) 
  {
    warningEvents++;
  }

  /** Error. */
  public void error(SAXParseException ex) 
  {
    errorEvents++;
  }

  /** Fatal error. */
  public void fatalError(SAXParseException ex) 
  throws SAXException 
  {
    fatalErrorEvents++;
    throw ex;
  }

  /** Main program entry point. */
  public static void main(String argv[]) 
  {
    if (argv.length == 0)
    {
      System.out.println("Usage:  java saxCounter uri");
      System.out.println("   where uri is the URI of your XML document.");
      System.out.println("   Sample:  java saxCounter sonnet.xml");
      System.exit(1);
    }

    saxCounter sc = new saxCounter();
    sc.parseURI(argv[0]);
  } // main(String[])
} 

