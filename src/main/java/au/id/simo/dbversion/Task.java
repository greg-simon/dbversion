package au.id.simo.dbversion;

import java.sql.Connection;
import au.id.simo.dbversion.common.Version;

import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 17/11/2005
 * Time: 17:56:01
 * To change this template use File | Settings | File Templates.
 */
public interface Task {

    /**
     * The implemetation of this method decides if this version is to be run or not.
     * @param target the target that has been specified. Is often null.
     * @param version the version of the schema the database is to be upgraded to
     * @param hasAlreadyRun true if this task has already been run, false if it hasn't.
     * @return true if this task is to be run given the paramters, false of not.
     */
    public boolean isRun(Target target, Version version, boolean hasAlreadyRun);
    
    /**
     * returns the version this task belongs to
     */
    public Version getVersion();

    /**
     *
     * @return true if this task is to be recorded in the database as run, false if not.
     * @throws SQLException if there was a problem executing.
     */
    public boolean runTask(Connection con) throws SQLException;

    /**
     * @return a human readable comment that is also discriptive, yet under about 100 chars in length.
     * for example: "Create Table blah" or "add foo col to blah"
     */
    public String getComment();

    /**
     * This String is used to identify a particular task. So it must be unique.
     *
     * @return a string that is unique to this task among all tasks in a given project.
     * The full class name can do the job, or another contrived string may
     */
    public String getIdentifier();

    /**
     * Schema changes are sort to run before any data change.
     * @return schema changes are handled diffrently from mere data entry.
     */
    public boolean isSchemaChange();

    /**
     * May return null of this Task is run for all targets.
     * @return The target
     */
    public Target getTarget();
}
