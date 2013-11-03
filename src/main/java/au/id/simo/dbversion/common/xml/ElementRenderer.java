/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package au.id.simo.dbversion.common.xml;

import java.io.PrintStream;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 18/11/2005
 * Time: 20:58:39
 * Class to specificly render and Element in nicly formatted xml. 
 */
public class ElementRenderer {
    private static final String STD_HEADER = "<?xml version=\"1.0\"?>";
    private Element elementRoot;
    private boolean humanReadable;
    private String padString;

    public ElementRenderer(Element root) {
        this.elementRoot = root;
        this.humanReadable=true;
        this.padString="\t";
    }

    public Element getElementRoot() {
        return elementRoot;
    }

    public void setElementRoot(Element elementRoot) {
        this.elementRoot = elementRoot;
    }

    public boolean isHumanReadable() {
        return humanReadable;
    }

    public void setHumanReadable(boolean humanReadable) {
        this.humanReadable = humanReadable;
    }

    public String getPadString() {
        return padString;
    }

    public void setPadString(String padString) {
        this.padString = padString;
    }

    public void renderElement(PrintStream output) {
        printHeader(output);
        renderElement(elementRoot, output, 0);
    }

    private void printHeader(PrintStream out) {
        out.println(STD_HEADER);
    }

    /**
     * Recursive function to render the Element tree passed to it.
     * @param element the element to print
     * @param out the PrintStream the xml will go to
     * @param depth the number of spaces to indent the tag
     */
    private void renderElement(Element element, PrintStream out, int depth) {
        renderIndentPadding(out,depth);
        out.print("<");
        out.print(element.getName());
        Set<String> attSet = element.getAttributeKeys();
        if(!attSet.isEmpty()) {
            out.print(' ');
            for (String key : attSet) {
                renderAttribute(out, key, element.getAttribute(key));
            }
        }
        out.print('>');
        if(element.hasNonWhiteSpaceText()) {
            out.print(element.getText());
        } else {
            if(isHumanReadable()) {
                out.println();
            }
        }
        Iterator itr = element.getElementIterator();
        while(itr.hasNext()) {
            renderElement((Element) itr.next(),out,depth+1);
        }
        if(!element.hasNonWhiteSpaceText()) {
            renderIndentPadding(out,depth);
        }
        out.print("</");
        out.print(element.getName());
        out.print('>');
        if(isHumanReadable()) {
            out.println();
        }
    }

    private void renderIndentPadding(PrintStream out, int depth) {
        for(int i=0;i<depth&&isHumanReadable();i++) {
            out.print(padString);
        }
    }

    /**
     * this method renders the name value pair in the format of:
     * name="value" (with a following space)
     */
    private void renderAttribute(PrintStream out, String name, String value) {
        out.print(name);
        out.print('=');
        out.print('"');
        out.print(value);
        out.print('"');
        out.print(' ');
    }
}
