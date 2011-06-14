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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.xml.parser.Parser;
import com.sun.xml.tree.XmlDocumentBuilder;

/**
 * A sample DOM writer. This sample program illustrates how to
 * traverse a DOM tree.
 */

public class domTwo
{
  public void parseAndPrint(String uri)
  {
    Document doc = null;

    try
    {
      XmlDocumentBuilder builder = new XmlDocumentBuilder();
      Parser parser = new com.sun.xml.parser.Parser();
      parser.setDocumentHandler(builder);
      builder.setParser(parser);
      builder.setDisableNamespaces(false);
      parser.parse(uri);
      doc = builder.getDocument();
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
          System.out.print(">");

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
      System.out.print("</");
      System.out.print(node.getNodeName());
      System.out.print('>');
    }
  } // printDOMTree(Node)

  /** Main program entry point. */
  public static void main(String argv[]) 
  {
    if (argv.length == 0)
    {
      System.out.println("Usage:  java domTwo uri");
      System.out.println("   where uri is the URI of the XML document you want to print.");
      System.out.println("   Sample:  java domTwo sonnet.xml");
      System.exit(1);
    }

    domTwo d2 = new domTwo();
    d2.parseAndPrint(argv[0]);
  }
}

