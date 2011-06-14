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

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.xerces.parsers.DOMParser;

/**
 * domCounter.java 
 * This code creates a DOM parser, parses a document, then 
 * prints statistics about the number and type of nodes 
 * found in the document. 
 */

public class domCounter
{
  int documentNodes = 0;
  int elementNodes = 0;
  int entityReferenceNodes = 0;
  int cdataSections = 0;
  int textNodes = 0;
  int processingInstructions = 0;

  public void parseAndCount(String uri)
  {
    Document doc = null;

    try
    {
      DOMParser parser = new DOMParser();
      parser.parse(uri);
      doc = parser.getDocument();
    }
    catch (Exception e)
    {
      System.err.println("Sorry, an error occurred: " + e);
    }

    // We've parsed the document now, so let's scan the DOM tree and
    // print the statistics.

    if (doc != null)
    {
      scanDOMTree(doc);
      System.out.println("Document Statistics for " + uri + ":");
      System.out.println("====================================");
      System.out.println("Document Nodes:           " + documentNodes);
      System.out.println("Element Nodes:            " + elementNodes);
      System.out.println("Entity Reference Nodes:   " + entityReferenceNodes);
      System.out.println("CDATA Sections:           " + cdataSections);
      System.out.println("Text Nodes:               " + textNodes);
      System.out.println("Processing Instructions:  " + processingInstructions);
      System.out.println("                          ----------");
      int totalNodes = documentNodes + elementNodes + entityReferenceNodes + 
                       cdataSections + textNodes + processingInstructions;
      System.out.println("Total:                    " + totalNodes + " Nodes");
    }
  }

  /** Scans the DOM tree and counts the different types of nodes. */
  public void scanDOMTree(Node node) 
  {
    int type = node.getNodeType();
    switch (type)
    {
      case Node.DOCUMENT_NODE: 
        documentNodes++;
        scanDOMTree(((Document)node).getDocumentElement());
        break;

      case Node.ELEMENT_NODE: 
        elementNodes++;
        NodeList children = node.getChildNodes();
        if (children != null)
        {
          int len = children.getLength();
          for (int i = 0; i < len; i++)
            scanDOMTree(children.item(i));
        }
        break;

      case Node.ENTITY_REFERENCE_NODE: 
        entityReferenceNodes++;
        break;

      case Node.CDATA_SECTION_NODE: 
        cdataSections++;
        break;

      case Node.TEXT_NODE: 
        textNodes++;
        break;

      case Node.PROCESSING_INSTRUCTION_NODE: 
        processingInstructions++;
        break;
    }
  } // scanDOMTree(Node)

  /** Main program entry point. */
  public static void main(String argv[]) 
  {
    if (argv.length == 0)
    {
      System.out.println("Usage:  java domCounter uri");
      System.out.println("   where uri is the URI of your XML document.");
      System.out.println("   Sample:  java domCounter sonnet.xml");
      System.exit(1);
    }

    domCounter dc = new domCounter();
    dc.parseAndCount(argv[0]);
  }
}
