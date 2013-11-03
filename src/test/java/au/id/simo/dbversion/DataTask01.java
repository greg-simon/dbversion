/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.id.simo.dbversion;

import au.id.simo.dbversion.common.Version;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author gsimon
 */
public class DataTask01 extends DataTask {

    @Override
    public Version getRequiredSchemaVersion() {
        return new Version(1,0,0);
    }

    public String getComment() {
        return "added a single row of bugus test data";
    }

    public Target getTarget() {
        return null;
    }

    @Override
    public void runTransaction(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("insert into blah (col1,col2) values (100,'one hundred')");
        stmt.close();
    }
}
