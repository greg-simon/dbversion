/*
 * Copyright (c) 2005 Your Corporation. All Rights Reserved.
 */
package au.id.simo.dbversion.xml;

import au.id.simo.dbversion.Task;
import au.id.simo.dbversion.common.xml.Element;

import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 18/11/2005
 * Time: 22:46:54
 * To change this template use File | Settings | File Templates.
 */
public class ClassTaskTagProcessor implements TagProcessor{
    protected static final String TAG_NAME = "classTask";
    private static final String CLASS_TAG_NAME = "class";

    private ClassLoader classLoader = ClassTaskTagProcessor.class.getClassLoader();

    public String getTagName() {
        return TAG_NAME;
    }

    public void setClassLoader(ClassLoader classLoader) {
        this.classLoader=classLoader;
    }

    public Task createTask(Element taskRoot) {
        Iterator itr = taskRoot.getElementIterator();
        while(itr.hasNext()) {
            Element e = (Element) itr.next();
            if(e.getName().equals(CLASS_TAG_NAME)) {
                try {
                    Class taskClass =  classLoader.loadClass(e.getText());
                    if(taskClass.isAssignableFrom(Task.class)) {
                        return (Task) taskClass.newInstance();
                    }
                } catch (ClassNotFoundException cnfe) {
                    throw new Error(cnfe);
                } catch (IllegalAccessException e1) {
                    throw new Error(e1);
                } catch (InstantiationException e1) {
                    throw new Error(e1);
                }
            }
        }
        throw new Error("Missing 'class' tag");
    }
}
