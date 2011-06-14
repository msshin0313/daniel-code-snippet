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

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.ParserFactory;

import com.sun.xml.parser.Resolver;

/**
 * A sample SAX writer. This sample program illustrates how to
 * register a SAX DocumentHandler and receive the callbacks in
 * order to print a document that is parsed.
 *
 * @version Revision: 06 1.5 samples/sax/SAXWriter.java, samples, xml4j2, xml4j2_0_15 
 */
public class saxTwo
  extends HandlerBase 
{
  public void parseURI(String uri)
  {
    try
    {
      Parser parser = ParserFactory.makeParser();
      parser.setDocumentHandler(this);
      parser.setErrorHandler(this);
      parser.parse(Resolver.createInputSource(new File(uri)));
    }
    catch (Exception e)
    {
      System.err.println(e);
    }
  }

  /** Processing instruction. */
  public void processingInstruction(String target, String data) 
  {
    System.out.print("<?");
    System.out.print(target);
    if (data != null && data.length() > 0)
    {
      System.out.print(' ');
      System.out.print(data);
    }
    System.out.print("?>");

  } // processingInstruction(String,String)

  /** Start document. */
  public void startDocument() 
  {
    System.out.println("<?xml version=\"1.0\"?>");
  } // startDocument()

  /** Start element. */
  public void startElement(String name, AttributeList attrs) 
  {
    System.out.print("<");
    System.out.print(name);
    if (attrs != null)
    {
      int len = attrs.getLength();
      for (int i = 0; i < len; i++)
      {
        System.out.print(" ");
        System.out.print(attrs.getName(i));
        System.out.print("=\"");
        System.out.print(attrs.getValue(i));
        System.out.print("\"");
      }
    }
    System.out.print(">");
  } // startElement(String,AttributeList)

  /** Characters. */
  public void characters(char ch[], int start, int length) 
  {
    System.out.print(new String(ch, start, length));
  } // characters(char[],int,int);

  /** Ignorable whitespace. */
  public void ignorableWhitespace(char ch[], int start, int length) 
  {
    characters(ch, start, length);
  } // ignorableWhitespace(char[],int,int);

  /** End element. */
  public void endElement(String name) 
  {
    System.out.print("</");
    System.out.print(name);
    System.out.print(">");
  } // endElement(String)

  /** End document. */
  public void endDocument() 
  {
    // No need to do anything.
  } // endDocument()

  //
  // ErrorHandler methods
  //

  /** Warning. */
  public void warning(SAXParseException ex) 
  {
    System.err.println("[Warning] "+
                       getLocationString(ex)+": "+
                       ex.getMessage());
  }

  /** Error. */
  public void error(SAXParseException ex) 
  {
    System.err.println("[Error] "+
                       getLocationString(ex)+": "+
                       ex.getMessage());
  }

  /** Fatal error. */
  public void fatalError(SAXParseException ex) 
  throws SAXException 
  {
    System.err.println("[Fatal Error] "+
                       getLocationString(ex)+": "+
                       ex.getMessage());
    throw ex;
  }

  /** Returns a string of the location. */
  private String getLocationString(SAXParseException ex) 
  {
    StringBuffer str = new StringBuffer();

    String systemId = ex.getSystemId();
    if (systemId != null)
    {
      int index = systemId.lastIndexOf('/');
      if (index != -1)
        systemId = systemId.substring(index + 1);
      str.append(systemId);
    }
    str.append(':');
    str.append(ex.getLineNumber());
    str.append(':');
    str.append(ex.getColumnNumber());

    return str.toString();
  } // getLocationString(SAXParseException):String

  /** Main program entry point. */
  public static void main(String argv[]) 
  {
    if (argv.length == 0)
    {
      System.out.println("Usage:  java saxTwo uri");
      System.out.println("   where uri is the URI of your XML document.");
      System.out.println("   Sample:  java saxTwo sonnet.xml");
      System.exit(1);
    }

    saxTwo s2 = new saxTwo();
    s2.parseURI(argv[0]);
  } // main(String[])
} 

