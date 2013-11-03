package au.id.simo.dbversion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 22/11/2005
 * Time: 14:23:57
 * To change this template use File | Settings | File Templates.
 */
public abstract class SchemaTask extends AbstractTask {
    
    public boolean isSchemaChange() {
        return true;
    }

    /**
     *
     * @return null by default because most schema changes apply across all targets
     */
    public Target getTarget() {
        return null;
    }
    
    public boolean runTask(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.execute(getSql());
        return true;
    }
    
    public abstract String getSql();
}
