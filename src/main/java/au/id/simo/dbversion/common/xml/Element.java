package au.id.simo.dbversion.common.xml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: Greg
 * Date: Mar 12, 2003
 * Time: 11:39:08 AM
 * To change this template use Options | File Templates.
 */
public class Element {
    private Map<String,String> att;
    private List<Element> elements;
    private StringBuffer buffer;
    private String name;
    private Element parent;

    public Element() {
        att = new TreeMap<String,String>();
        elements = new ArrayList<Element>();
        buffer = new StringBuffer();
    }

    public Element(String name) {
        this();
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void appendText(String text) {
        buffer.append(text);
    }

    public String getText() {
        return buffer.toString();
    }

    public void addElement(Element e) {
        e.parent = this;
        elements.add(e);
    }

    public List<Element> getElementList() {
        return elements;
    }

    public Iterator getElementIterator() {
        return elements.iterator();
    }

    public void addAttribute(String attb, String value) {
        att.put(attb, value);
    }

    public String getAttribute(String name) {
        return att.get(name);
    }

    public Set<String> getAttributeKeys() {
        return att.keySet();
    }

    public Iterator getAttributeIterator() {
        return att.values().iterator();
    }

    public Map<String,String> getAttributeMap() {
        return att;
    }

    public Element getParent() {
        return parent;
    }

    public boolean hasNonWhiteSpaceText() {
        if(buffer.length()>0) {
            return !"".equals(getText().trim());
        } else {
            return false;
        }
    }

    public boolean containsElement(String name) {
        for (Element element : elements) {
            if (element.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}

