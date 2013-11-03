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
public class TransactionSchemaChangeTask extends TransactionTask {

    @Override
    public void runTransaction(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("alter table blah add col3 varchar null;");
        stmt.executeUpdate("create table blah2 ( " +
                            "col1 integer null," +
                            "col2 varchar(255) null" +
                            ");");
        stmt.close();
    }

    public Version getVersion() {
        return new Version(2,0,0);
    }

    public String getComment() {
        return "testing out schema changes in a transaction";
    }

    public boolean isSchemaChange() {
        return true;
    }

    public Target getTarget() {
        return null;
    }

}
