package pl.blokaj.dbms.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SystemInformation {

    /** Version of the DBMS interface */
    public static String interfaceVersion = "1.0.0";

    /** Version of the DBMS system */
    public static String version = "1.3.0";

    /** Author of the DBMS system */
    public static String author = "Bartosz Lokaj";

    /** System uptime in seconds */
    public long uptime = 0;
    @JsonIgnore
    private long startTimestamp;

    // Default constructor (needed for Jackson)
    public SystemInformation() {}

    public SystemInformation(long startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void updateUptime(long currentTimestamp) {
        this.uptime = currentTimestamp - startTimestamp;
    }

    public String getInterfaceVersion() { return interfaceVersion; }
    public void setInterfaceVersion(String interfaceVersion) { SystemInformation.interfaceVersion = interfaceVersion; }

    public String getVersion() { return version; }
    public void setVersion(String version) { SystemInformation.version = version; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { SystemInformation.author = author; }

    public long getUptime() { return uptime; }
    public void setUptime(long uptime) { this.uptime = uptime; }
}

