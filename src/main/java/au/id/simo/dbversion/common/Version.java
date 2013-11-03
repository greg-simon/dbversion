package au.id.simo.dbversion.common;

import java.util.StringTokenizer;

/**
 * User: gsimon
 * Date: 17/11/2005
 * Time: 17:56:56
 * IMplements the idea of a version in a single object.
 */
public class Version implements Comparable {
    private int major;
    private int minor;
    private int patch;

    public Version() {}

    public Version(String version) {
        StringTokenizer tok = new StringTokenizer(version,".");
        major = Integer.parseInt(tok.nextToken());
        minor = Integer.parseInt(tok.nextToken());
        patch = Integer.parseInt(tok.nextToken());
    }

    public Version(int major, int minor, int patch) {
        this.patch = patch;
        this.minor = minor;
        this.major = major;
    }

    public int getMajor() {
        return major;
    }

    public void setMajor(int major) {
        this.major = major;
    }

    public int getMinor() {
        return minor;
    }

    public void setMinor(int minor) {
        this.minor = minor;
    }

    public int getPatch() {
        return patch;
    }

    public void setPatch(int patch) {
        this.patch = patch;
    }

    public int compareTo(Object o) {
        Version v = (Version) o;
        if(compareMajorTo(v)>0) {
            return -1;
        } else if(compareMajorTo(v)<0) {
            return 1;
        } else {
            if(compareMinorTo(v)>0) {
                return -1;
            } else if(compareMinorTo(v)<0) {
                return 1;
            } else {
                if(comparePatchTo(v)>0) {
                    return -1;
                } else if(comparePatchTo(v)<0) {
                    return 1;
                } else {
                    return 0;
                }
            }
        }
    }

    public int compareMajorTo(Version v) {
        if(v.getMajor()>this.getMajor()) {
            return 1;
        } else if(v.getMajor()==this.getMajor()) {
            return 0;
        } else {
            return -1;
        }
    }

    public int compareMinorTo(Version v) {
        if(v.getMinor()>this.getMinor()) {
            return 1;
        } else if(v.getMinor()==this.getMinor()) {
            return 0;
        } else {
            return -1;
        }
    }

    public int comparePatchTo(Version v) {
        if(v.getPatch()>this.getPatch()) {
            return 1;
        } else if(v.getPatch()==this.getPatch()) {
            return 0;
        } else {
            return -1;
        }
    }

    public boolean newer(Version version) {
        return this.compareTo(version)>0;
    }

    public boolean newerOrEqualTo(Version version) {
        return this.compareTo(version)>=0;
    }

    public boolean older(Version version) {
        return this.compareTo(version)<0;
    }

    public boolean olderOrEqualTo(Version version) {
        return this.compareTo(version)<=0;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj==null) return false;
        if(!(obj instanceof Version)) return false;
        Version v = (Version) obj;
        return compareTo(v)==0;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.major;
        hash = 71 * hash + this.minor;
        hash = 71 * hash + this.patch;
        return hash;
    }

    @Override
    public String toString() {
        return major+"."+minor+"."+patch;
    }
}
