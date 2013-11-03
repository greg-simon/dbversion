package au.id.simo.dbversion.common.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Iterator;
import java.util.Stack;

/**
 * Created by IntelliJ IDEA.
 * User: Greg
 * Date: Mar 12, 2003
 * Time: 11:31:58 AM
 * A light weight xml DOM implementatino. Written before I really understood SAX.
 */
public class Sax2DomHandler extends DefaultHandler {
    protected Element root;
    private Element currele;
    protected Stack<Element> stack;

    public Sax2DomHandler() {
        stack = new Stack<Element>();
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currele = new Element(qName);
        int atts = attributes.getLength();
        for (int i = 0; i < atts; i++) {
            currele.addAttribute(attributes.getQName(i), attributes.getValue(i));
        }
        if (stack.isEmpty()) {
            root = currele;
        } else {
            stack.peek().addElement(currele);
        }
        stack.push(currele);
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        currele = currele.getParent();
        stack.pop();
    }

    public void characters(char ch[], int start, int length) throws SAXException {
        currele.appendText(new String(ch, start, length));
    }

    public void endDocument() throws SAXException {
        //printElements(root);
    }

    public Element getElementTree() {
        return root;
    }

    public void printElements(Element e) {
        System.out.print("<" + e.getName() + ">" + e.getText());
        for (Element element : e.getElementList()) {
            printElements(element);
        }
        System.out.print("</" + e.getName() + ">");
    }
}
