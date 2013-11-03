/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.id.simo.dbversion;

import au.id.simo.dbversion.common.Version;

/**
 *
 * @author gsimon
 */
public class SchemaTask01 extends SchemaTask {

    public String getSql() {
        return "create table blah ( " +
                "col1 integer null," +
                "col2 varchar(255) null" +
                ");";
    }

    public Version getVersion() {
        return new Version(1,0,0);
    }

    public String getComment() {
        return "initial test schema setup";
    }

}
