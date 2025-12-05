package pl.blokaj.dbms.model;

public class SystemInformation {

    /** Version of the DBMS interface */
    public String interfaceVersion;

    /** Version of the DBMS system */
    public String version;

    /** Author of the DBMS system */
    public String author;

    /** System uptime in seconds */
    public long uptime;

    // Default constructor (needed for Jackson)
    public SystemInformation() {}

    // Optional: convenience constructor
    public SystemInformation(String interfaceVersion, String version, String author, long uptime) {
        this.interfaceVersion = interfaceVersion;
        this.version = version;
        this.author = author;
        this.uptime = uptime;
    }

    // Optional: getters and setters if you prefer
    public String getInterfaceVersion() { return interfaceVersion; }
    public void setInterfaceVersion(String interfaceVersion) { this.interfaceVersion = interfaceVersion; }

    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public long getUptime() { return uptime; }
    public void setUptime(long uptime) { this.uptime = uptime; }
}

