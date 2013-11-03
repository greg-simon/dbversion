package au.id.simo.dbversion.xml;

import au.id.simo.dbversion.Task;
import au.id.simo.dbversion.common.xml.Element;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 18/11/2005
 * Time: 17:27:47
 * apon detection of a tag that is unknown, a search will be made for an implementation of this class
 * and the element tree will be passed to it for processing. It is expected this class will be implemented
 * in the project this package is used in.
 */
public interface TagProcessor {
    /**
     * @return the name of the tag that identifies this custom xml task.
     */
    public String getTagName();

    /**
     * This method is called by the xml paser when a custom tag is detected.
     * @param taskRoot
     */
    public Task createTask(Element taskRoot);
}
