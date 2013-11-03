package au.id.simo.dbversion.xml;

import org.xml.sax.SAXException;
import au.id.simo.dbversion.common.xml.Sax2DomHandler;
import au.id.simo.dbversion.common.xml.Element;
import au.id.simo.dbversion.TaskListBuilder;
import au.id.simo.dbversion.Task;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 18/11/2005
 * Time: 11:16:05
 */
public class XMLTaskListBuilder implements TaskListBuilder {
    private static final String LOAD_TAG = "loadTagProcessor";

    private Map<String,TagProcessor> tagProccessorMap;
    private List<Task> taskList;

    public XMLTaskListBuilder() {
        tagProccessorMap = new TreeMap<String,TagProcessor>();
        taskList = new LinkedList<Task>();

        //add the built in TagProcessors
        addTagProcessor(new ClassTaskTagProcessor());
        addTagProcessor(new SQLTaskTagProcessor());
    }

    /**
     * Sets the classloader for the ClassTaskTagProccessor that
     * is always built in. If not set the the ClassLoader that loaded
     * ClassTaskTagProccessor will be used by default.
     * @param classLoader
     */
    public void setClassTaskClassLoader(ClassLoader classLoader) {
        ClassTaskTagProcessor tp = (ClassTaskTagProcessor)getTagProcessor(ClassTaskTagProcessor.TAG_NAME);
        tp.setClassLoader(classLoader);
    }

    /**
     * add a tp to handle a given tag
     * @param tp
     * @return the previous TP that matched the tagname, null if nothing.
     */
    public TagProcessor addTagProcessor(TagProcessor tp) {
        return tagProccessorMap.put(tp.getTagName(),tp);
    }

    protected TagProcessor getTagProcessor(String name) {
        return (TagProcessor) tagProccessorMap.get(name);
    }

    public void loadTaskList(InputStream in) throws IOException {
        Element docroot = parseXML(in);
        Iterator itr =docroot.getElementIterator();
        while(itr.hasNext()) {
            Element taskElement = (Element)itr.next();
            if(LOAD_TAG.equals(taskElement.getName())) {
                loadTagProccessor(taskElement);
                continue;
            }
            TagProcessor tp = getTagProcessor(taskElement.getName());
            if(tp==null) {
                //todo write code to report error or something
                System.err.println("Unknown Task Tag: "+taskElement.getName());
            } else {
                try {
                    Task task = tp.createTask(taskElement);
                    taskList.add(task);
                } catch (Throwable t) {
                    //todo report on error somehow
                    System.err.println("Unable to load Task: "+t.getMessage());
                }
            }
        }
    }

    private void loadTagProccessor(Element taskElement) {
        try {
            Class tpClass =  this.getClass().getClassLoader().loadClass(taskElement.getText().trim());
            if(tpClass.isAssignableFrom(TagProcessor.class)) {
                addTagProcessor( (TagProcessor) tpClass.newInstance());
            }
        } catch (Throwable t) {
            //todo report on error somehow
            System.err.println("Unable to load TagProcessor: "+t.getClass().getName()+": "+t.getMessage());
        }
    }

    private Element parseXML(InputStream in) throws IOException {
        try {
            SAXParser sp = SAXParserFactory.newInstance().newSAXParser();
            Sax2DomHandler s2dh = new Sax2DomHandler();
            sp.parse(in,s2dh);
            return s2dh.getElementTree();
        } catch (ParserConfigurationException e) {
            throw new IOException(e.getMessage());
        } catch (SAXException e) {
            throw new IOException(e.getMessage());
        }
    }

    public List<Task> getTaskList() {
        return taskList;
    }
}
