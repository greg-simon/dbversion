package au.id.simo.dbversion;

import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Iterator;
import au.id.simo.dbversion.common.Version;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 18/11/2005
 * Time: 10:43:32
 * This class is the main entry point for this package. While it dosent have a main method of its own,
 * (thats the resposability of the application is used in) it does provide relative simple methods to
 * upgrade the database.
 */
public class DBVersionManager {
    private static final String DEFAULT_TABLE = "DBVersionTasks";
    private String schema;
    private String tableName;

    public String getTableName() {
        if(tableName==null) {
            return DEFAULT_TABLE;
        }
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }
    
    public String getTableNameWithSchema() {
        if(getSchema() == null) {
            return getTableName();
        } else {
            return getSchema()+"."+getTableName();
        }
    }
    
    public void ensureUptoDate(List<Task> tasklist, Connection con) throws SQLException {
        Version largestVersion = getLargestVersionInList(tasklist);
        executeUpgrade(null,largestVersion,tasklist,con);
    }
    
    public void ensureUptoDate(Target target, List<Task> tasklist, Connection con) throws SQLException {
        Version largestVersion = getLargestVersionInList(tasklist);
        executeUpgrade(target,largestVersion,tasklist,con);
    }
    
    /**
     * 
     * @param con
     * @return the version the current schema of the database is.
     * @throws java.sql.SQLException
     */
    public Version getCurrentVersion(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select distinct version from "+ getTableNameWithSchema());
        List<Version> versionList = new ArrayList<Version>();
        while(rs.next()) {
            Version tempVersion = new Version(rs.getString(1));
            versionList.add(tempVersion);
        }
        rs.close();
        stmt.close();
        
        Collections.sort(versionList);
        if(versionList.size()>0) {
            return versionList.get(versionList.size()-1);
        } else {
            return new Version(0,0,0); // 0 means there are no versions.
        }
    }
    
    private Version getLargestVersionInList(List<Task> tasklist) {
        Version largestVersion = new Version(0,0,0);
        for(Task task:tasklist) {
            Version taskVer = task.getVersion();
            if(taskVer!=null && largestVersion.compareTo(taskVer)<0) {
                largestVersion = taskVer;
            }
        }
        return largestVersion;
    }

    public void executeUpgrade(Version targetVersion, List<Task> taskList, Connection con) throws SQLException {
        executeUpgrade(null,targetVersion,taskList,con);
    }
    
    public void executeUpgrade(Target target, Version version, List<Task> taskList, Connection con) throws SQLException {
        createVersionTableIfNeeded(con);
        filterTasksNotToRun(target,version,taskList,con);
        runTasks(taskList, version, con);
    }

    private void runTasks(List<Task> list, Version targetVersion, Connection con) throws SQLException {
        for(Task task:list) {
            if(task.runTask(con)) {
                // save the task in the database
                saveTask(task, con);
            }
        }
    }

    private void filterTasksNotToRun(Target target, Version schemaVersion, List<Task> taskList, Connection con) throws SQLException {
        List<String> hasRunList = getTasksAlreadyRun(con);
        Iterator<Task> itr = taskList.iterator();
        while(itr.hasNext()) {
            Task task = itr.next();
            boolean hasRun = hasRunList.contains(task.getIdentifier());
            if(!task.isRun(target,schemaVersion,hasRun)) {
                itr.remove();
            }
        }
    }
    
    //=======================================
    // SQL stuff follows
    //=======================================

    /**
     *
     * @return a list of String id's that have already been run agaist the database and have been stored.
     */
    public List<String> getTasksAlreadyRun(Connection con) throws SQLException {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select id from "+getTableNameWithSchema());
        List<String> list = new LinkedList<String>();
        while(rs.next()) {
            list.add(rs.getString(1));
        }
        rs.close();
        stmt.close();
        return list;
    }
    
    private boolean versionTableExists(Connection con) throws SQLException {
        DatabaseMetaData metaData = con.getMetaData();
        String [] tableTypes = {"TABLE"};
        ResultSet rs = metaData.getTables(null,getSchema(),getTableName(),tableTypes);
        boolean containsTable = rs.next(); // true if we get at least one result
        rs.close();
        if(containsTable==false) {
            rs = metaData.getTables(null,getSchema(),getTableName().toUpperCase(),tableTypes);
            containsTable = rs.next(); // true if we get at least one result
            rs.close();
        }
        return containsTable;
    }

    private void createVersionTableIfNeeded(Connection con) throws SQLException {
        if(!versionTableExists(con)) {
            Statement stmt = con.createStatement();
            // check if the required schema exists
            if(getSchema()!=null) {
                boolean schemaExists = false;
                DatabaseMetaData dbmd = con.getMetaData();
                ResultSet rs = dbmd.getSchemas();
                while(rs.next()) {
                    if(getSchema().equalsIgnoreCase(rs.getString(1))) {
                        schemaExists = true;
                    }
                }
                rs.close();
                if(!schemaExists) {
                    stmt.execute("create schema "+getSchema());
                }
            }
            DatabaseType type = DatabaseType.getDatabaseType(con);
            String sql = createTableSQL(type);
            stmt.execute(sql);
            stmt.close();
        }
    }
    
    /**
     * @returns  the sql required to create a new versioning table.
     * 
     * For example: taskA is to be run at version 1.1 however it is missing 
     * when a db is upgraded to 1.3. Therefore it will be run and saved in 
     * the database with 1.3 assigned to it.
     */
    private String createTableSQL(DatabaseType type) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("create table " + getTableNameWithSchema() + " (");
        sb.append("  id varchar(255) not null,");
        sb.append("  target varchar(255) null,");
        sb.append("  comment_text varchar(255) null,");
        sb.append("  version varchar(255) not null,");
        // the up_time datatype is tricky on sql server
        if(DatabaseType.SQL_SERVER.equals(type)) {
            sb.append("  up_time datetime not null,");
        } else {
            sb.append("  up_time timestamp not null,");
        }
        sb.append("  constraint \"PK_"+getTableName().toUpperCase()+"\" primary key (\"ID\")");
        sb.append(") ");
        return sb.toString();
    }

    private void saveTask(Task task, Connection con) throws SQLException {
        String sql = "insert into "+getTableNameWithSchema()+" (id,target,comment_text,version,up_time) values (?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1,task.getIdentifier());
        
        if(task.getTarget()==null) {
            ps.setNull(2,Types.VARCHAR);
        } else {
            ps.setString(2,task.getTarget().getIdentifier());
        }
        
        if(task.getComment()==null) {
            ps.setNull(3,Types.VARCHAR);
        } else {
            ps.setString(3,task.getComment());
        }
        
        Version v = task.getVersion();
        if(v==null) {
            v = new Version(0,0,0);
        }
        ps.setString(4,v.toString());
        ps.setTimestamp(5,new Timestamp(System.currentTimeMillis()));
        ps.execute();
        ps.close();
        if(!con.getAutoCommit()) {
            con.commit();
        }
    }
}
