package au.id.simo.dbversion;

import au.id.simo.dbversion.common.Version;


/**
 * Created by IntelliJ IDEA.
 * User: gsimon
 * Date: 22/11/2005
 * Time: 14:33:59
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractTask implements Task {

    public boolean isRun(Target target, Version version, boolean hasAlreadyRun) {
        if((target!=null&&getTarget()!=null) && target.equals(getTarget())) return false;
        if(version.older(getVersion())) return false;
        return !hasAlreadyRun;
    }

    public String getIdentifier() {
        return getClass().getName();
    }
}
