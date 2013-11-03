/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.id.simo.dbversion;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author gsimon
 */
public abstract class TransactionTask extends AbstractTask {

    public boolean runTask(Connection con) throws SQLException {
        boolean autoCommitStatus = true;
        try {
             autoCommitStatus = con.getAutoCommit();
            con.setAutoCommit(false);
            runTransaction(con);
            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(autoCommitStatus);
        }
        return true;
    }
    
    public abstract void runTransaction(Connection con) throws SQLException;
}
