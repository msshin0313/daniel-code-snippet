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
import java.io.Reader;
import java.io.StringReader;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.apache.xerces.parsers.*;

/**
 * A sample DOM writer. This sample program illustrates how to
 * traverse a DOM tree.
 */

public class parseString
{
  public void parseAndPrint(InputSource xmlSource)
  {
    Document doc = null;

    try
    {
      DOMParser parser = new DOMParser();
      parser.parse(xmlSource);
      doc = parser.getDocument();
    }
    catch (Exception e)
    {
      System.err.println("Sorry, an error occurred: " + e);
    }

    // We've parsed the document now, so let's print it.  

    if (doc != null)
      printDOMTree(doc);
  }

  /** Prints the specified node, recursively. */
  public void printDOMTree(Node node) 
  {
    int type = node.getNodeType();
    switch (type)
    {
      // print the document element
      case Node.DOCUMENT_NODE: 
        {
          System.out.println("<?xml version=\"1.0\" ?>");
          printDOMTree(((Document)node).getDocumentElement());
          break;
        }

        // print element with attributes
      case Node.ELEMENT_NODE: 
        {
          System.out.print("<");
          System.out.print(node.getNodeName());
          NamedNodeMap attrs = node.getAttributes();
          for (int i = 0; i < attrs.getLength(); i++)
          {
            Node attr = attrs.item(i);
            System.out.print(" " + attr.getNodeName() + 
                             "=\"" + attr.getNodeValue() + 
                             "\"");
          }
          System.out.println(">");

          NodeList children = node.getChildNodes();
          if (children != null)
          {
            int len = children.getLength();
            for (int i = 0; i < len; i++)
              printDOMTree(children.item(i));
          }

          break;
        }

        // handle entity reference nodes
      case Node.ENTITY_REFERENCE_NODE: 
        {
          System.out.print("&");
          System.out.print(node.getNodeName());
          System.out.print(";");
          break;
        }

        // print cdata sections
      case Node.CDATA_SECTION_NODE: 
        {
          System.out.print("<![CDATA[");
          System.out.print(node.getNodeValue());
          System.out.print("]]>");
          break;
        }

        // print text
      case Node.TEXT_NODE: 
        {
          System.out.print(node.getNodeValue());
          break;
        }

        // print processing instruction
      case Node.PROCESSING_INSTRUCTION_NODE: 
        {
          System.out.print("<?");
          System.out.print(node.getNodeName());
          String data = node.getNodeValue();
          {
            System.out.print(" ");
            System.out.print(data);
          }
          System.out.print("?>");
          break;
        }
    }

    if (type == Node.ELEMENT_NODE)
    {
      System.out.println();
      System.out.print("</");
      System.out.print(node.getNodeName());
      System.out.print('>');
    }
  } // printDOMTree(Node)

  /** Main program entry point. */
  public static void main(String argv[]) 
  {
    parseString ps = new parseString();
    StringReader sr = new StringReader("<?xml version=\"1.0\"?><a>Alpha<b>Bravo</b><c>Charlie</c></a>");
    InputSource iSrc = new InputSource(sr);
    ps.parseAndPrint(iSrc);
  }
}

