/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.id.simo.dbversion;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import au.id.simo.dbversion.common.Version;

/**
 *
 * @author gsimon
 */
public class TransactionTask01 extends TransactionTask {

    public Version getVersion() {
        return new Version(1,0,2);
    }

    public String getComment() {
        return "a failure of a transaction, this is still too hard";
    }

    public boolean isSchemaChange() {
        return false;
    }

    public Target getTarget() {
        return null;
    }

    public void runTransaction(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("insert into blah (col1,col2) values (1,'one')");
        stmt.executeUpdate("insert into blah (col1,col2) values (2,'two')");
        stmt.executeUpdate("insert into blah (col1,col2) values (3,'three')");
        stmt.executeUpdate("insert into blah (col1,col2) values (4,'four')");
        stmt.close();
    }

}
