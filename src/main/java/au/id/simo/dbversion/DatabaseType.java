/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.id.simo.dbversion;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 *
 * @author gsimon
 */
public enum DatabaseType {
    SQL_SERVER,
    ORACLE,
    H2;
    
    /**
     * Supported database types are:
     * <ul>
     * <li>ORACLE</li>
     * <li>SQL SERVER</li>
     * <li>H2</li>
     * </ul>
     * @param con
     * @return
     * @throws java.sql.SQLException
     */
    public static DatabaseType getDatabaseType(Connection con) throws SQLException {
        DatabaseMetaData dbmd = con.getMetaData();
        String type = dbmd.getDatabaseProductName();
        if("H2".equalsIgnoreCase(type)) {
            return DatabaseType.H2;
        } else if("ORACLE".equalsIgnoreCase(type)) {
            return DatabaseType.ORACLE;
        } else if("Microsoft SQL Server".equalsIgnoreCase(type)) {
            return DatabaseType.SQL_SERVER;
        }
        throw new SQLException("Unknown database type: " + type);
    }
}
