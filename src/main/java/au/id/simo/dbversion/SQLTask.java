package au.id.simo.dbversion;

import au.id.simo.dbversion.common.Version;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 18/11/2005
 * Time: 11:36:52
 * To change this template use File | Settings | File Templates.
 */
public class SQLTask extends AbstractTask {
    private Version version;
    private Target target;
    private String comment;
    private String identifier;
    private String sql;
    private boolean schemaChange;

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    public boolean isSchemaChange() {
        return schemaChange;
    }

    public void setSchemaChange(boolean bool) {
        this.schemaChange = bool;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Version getVersion() {
        return version;
    }

    public void setVersion(Version version) {
        this.version = version;
    }

    public String getSql() {
        return sql;
    }
    
    public boolean runTask(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.execute(getSql());
        return true;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}
