package au.id.simo.dbversion.xml;

import au.id.simo.dbversion.common.Version;
import au.id.simo.dbversion.common.xml.Element;
import au.id.simo.dbversion.DefaultTarget;
import au.id.simo.dbversion.SQLTask;
import au.id.simo.dbversion.Task;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 21/11/2005
 * Time: 15:52:28
 * To change this template use File | Settings | File Templates.
 */
public class SQLTaskTagProcessor implements TagProcessor{
    private static final String TAG_NAME = "sqlTask";
    private static final String VERSION = "version";
    private static final String TARGET = "target";
    private static final String COMMENT = "comment";
    private static final String SCHEMA_CHANGE = "schemaChange";
    private static final String SQL = "sql";

    public String getTagName() {
        return TAG_NAME;
    }

    public Task createTask(Element taskRoot) {
        checkElement(taskRoot);
        SQLTask task = new SQLTask();
        String id = taskRoot.getAttribute("id");
        if(id!=null) {
            task.setIdentifier(id);
        }
        Iterator itr = taskRoot.getElementIterator();
        while(itr.hasNext()) {
            Element e = (Element) itr.next();
            if(e.getName().equals(VERSION)) {
                task.setVersion(new Version(e.getText()));
            } else if(e.getName().equals(TARGET)){
                String target = e.getText().trim();
                if(target.length()>0) {
                    task.setTarget(new DefaultTarget(target));
                }
            } else if(e.getName().equals(COMMENT)) {
                task.setComment(e.getText());
            } else if(e.getName().equals(SQL)) {
                task.setSql(e.getText());
            } else if(e.getName().equals(SCHEMA_CHANGE)) {
                task.setSchemaChange(Boolean.parseBoolean(e.getText()));
            }
        }
        return task;
    }

    private void checkElement(Element root) {
        if(root.getAttribute("id")==null) throw new IllegalArgumentException("sql task has no id attribute");
        if(!root.containsElement(VERSION)) throw new IllegalArgumentException("sql task has no version tag");
        if(!root.containsElement(COMMENT)) throw new IllegalArgumentException("sql task has no comment tag");
        if(!root.containsElement(SQL)) throw new IllegalArgumentException("sql task has no sql tag");
        if(!root.containsElement(SCHEMA_CHANGE)) throw new IllegalArgumentException("sql task has no schemaChange tag");
    }
}
