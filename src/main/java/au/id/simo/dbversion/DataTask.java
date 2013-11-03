package au.id.simo.dbversion;

import au.id.simo.dbversion.common.Version;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 22/11/2005
 * Time: 14:07:44
 * To change this template use File | Settings | File Templates.
 */
public abstract class DataTask extends TransactionTask {
    
    public Version getVersion() {
        return getRequiredSchemaVersion();
    }
    
    public abstract Version getRequiredSchemaVersion();
    
    public boolean isSchemaChange() {
        return false;
    }
}
