package au.id.simo.dbversion;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 17/11/2005
 * Time: 17:58:20
 * To change this template use File | Settings | File Templates.
 */
public interface Target {
    /**
     * A unique string name that identifies a given Target in a TargetBundle
     * @return The unique identifier
     */
    public String getIdentifier();
}
