/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package au.id.simo.dbversion;

import au.id.simo.dbversion.common.Version;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author gsimon
 */
public class DBVersionManagerTest extends TestCase {
    private Connection connection;
    
    public DBVersionManagerTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        Class.forName("org.h2.Driver");
        //connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/dbtest","sa", "");
        connection = DriverManager.getConnection("jdbc:h2:mem:dbtest","sa", "");
        
        List<Task> list = new ArrayList<Task>();
        list.add(new SchemaTask01()); // 1.0.0
        list.add(new DataTask01()); // 1.0.0
        list.add(new TransactionTask01()); // 1.0.2
        list.add(new TransactionSchemaChangeTask()); // 2.0.0
        
        DBVersionManager dbman = new DBVersionManager();
        dbman.ensureUptoDate(list, connection);
    }

    @Override
    protected void tearDown() throws Exception {
        connection.close();
    }
    
    
    public void testGetCurrentVersion() throws Exception {
        DBVersionManager dbman = new DBVersionManager();
        assertEquals(new Version(2,0,0), dbman.getCurrentVersion(connection));
    }
}
