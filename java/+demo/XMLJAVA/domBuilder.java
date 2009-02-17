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
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.apache.xerces.parsers.*;

/**
 * A sample DOM writer. This sample program illustrates how to
 * traverse a DOM tree.
 */

public class domBuilder
{
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
    if (argv.length == 1 && argv[0].equals("-help"))
    {
      System.out.println("Usage:  java domBuilder");
      System.out.println("   This code builds a DOM tree, then prints it.");
      System.exit(1);
    }

    try
    {
      Document doc = (Document)Class.
                     forName("org.apache.xerces.dom.DocumentImpl").
                     newInstance();

      Element root = doc.createElement("sonnet");
      root.setAttribute("type", "Shakespearean");

      Element author = doc.createElement("author");

      Element lastName = doc.createElement("last-name");
      lastName.appendChild(doc.createTextNode("Shakespeare"));
      author.appendChild(lastName);

      Element firstName = doc.createElement("first-name");
      firstName.appendChild(doc.createTextNode("William"));
      author.appendChild(firstName);

      Element nationality = doc.createElement("nationality");
      nationality.appendChild(doc.createTextNode("British"));
      author.appendChild(nationality);

      Element yearOfBirth = doc.createElement("year-of-birth");
      yearOfBirth.appendChild(doc.createTextNode("1564"));
      author.appendChild(yearOfBirth);

      Element yearOfDeath = doc.createElement("year-of-death");
      yearOfDeath.appendChild(doc.createTextNode("1616"));
      author.appendChild(yearOfDeath);

      root.appendChild(author);

      Element title = doc.createElement("title");
      title.appendChild(doc.createTextNode("Sonnet 130"));
      root.appendChild(title);

      Element lines = doc.createElement("lines");

      Element line01 = doc.createElement("line");
      line01.appendChild(doc.createTextNode("My mistress' eyes are nothing like the sun,"));
      lines.appendChild(line01);

      Element line02 = doc.createElement("line");
      line02.appendChild(doc.createTextNode("Coral is far more red than her lips red."));
      lines.appendChild(line02);

      Element line03 = doc.createElement("line");
      line03.appendChild(doc.createTextNode("If snow be white, why then her breasts are dun,"));
      lines.appendChild(line03);

      Element line04 = doc.createElement("line");
      line04.appendChild(doc.createTextNode("If hairs be wires, black wires grow on her head."));
      lines.appendChild(line04);

      Element line05 = doc.createElement("line");
      line05.appendChild(doc.createTextNode("I have seen roses damasked, red and white,"));
      lines.appendChild(line05);

      Element line06 = doc.createElement("line");
      line06.appendChild(doc.createTextNode("But no such roses see I in her cheeks."));
      lines.appendChild(line06);

      Element line07 = doc.createElement("line");
      line07.appendChild(doc.createTextNode("And in some perfumes is there more delight"));
      lines.appendChild(line07);

      Element line08 = doc.createElement("line");
      line08.appendChild(doc.createTextNode("Than in the breath that from my mistress reeks."));
      lines.appendChild(line08);

      Element line09 = doc.createElement("line");
      line09.appendChild(doc.createTextNode("I love to hear her speak, yet well I know"));
      lines.appendChild(line09);

      Element line10 = doc.createElement("line");
      line10.appendChild(doc.createTextNode("That music hath a far more pleasing sound."));
      lines.appendChild(line10);

      Element line11 = doc.createElement("line");
      line11.appendChild(doc.createTextNode("I grant I never saw a goddess go,"));
      lines.appendChild(line11);

      Element line12 = doc.createElement("line");
      line12.appendChild(doc.createTextNode("My mistress when she walks, treads on the ground."));
      lines.appendChild(line12);

      Element line13 = doc.createElement("line");
      line13.appendChild(doc.createTextNode("And yet, by Heaven, I think my love as rare"));
      lines.appendChild(line13);

      Element line14 = doc.createElement("line");
      line14.appendChild(doc.createTextNode("As any she belied with false compare."));
      lines.appendChild(line14);

      root.appendChild(lines);

      doc.appendChild(root);

      domBuilder db = new domBuilder();
      db.printDOMTree(doc);
    }
    catch (Exception e)
    {
      System.err.println(e);
    }
  }
}

