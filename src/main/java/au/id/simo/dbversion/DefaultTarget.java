package au.id.simo.dbversion;

/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 17/11/2005
 * Time: 18:27:25
 * Default implementation of the target interface.
 */
public class DefaultTarget implements Target{
    private String identifier;

    public DefaultTarget(){}

    public DefaultTarget(String identifier){
        this.identifier = identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String toString() {
        return identifier;
    }
}
