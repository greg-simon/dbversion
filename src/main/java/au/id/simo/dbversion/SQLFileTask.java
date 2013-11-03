/*
 * InitialCreateTask.java
 *
 * Created on 22 September 2007, 13:23
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package au.id.simo.dbversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import au.id.simo.dbversion.common.Version;

/**
 * Useful for inital database set up as it defaults its version to 1
 * @author gsimon
 */
public class SQLFileTask extends SQLTask {
    
    /** Creates a new instance of InitialCreateTask */
    public SQLFileTask(File sqlFile) throws IOException {
        long fileLength = sqlFile.length();
        Reader in = new FileReader(sqlFile);
        char[] buf = new char[(int)fileLength];
        int read = in.read(buf);
        if(read != fileLength) {
            throw new IOException("Expecting "+fileLength+" but "+read+" was read.");
        }
        setIdentifier(this.getClass().getName()+":"+sqlFile.getName());
        setSql(new String(buf));
        setVersion(new Version(1,0,0));
    }
    
}
