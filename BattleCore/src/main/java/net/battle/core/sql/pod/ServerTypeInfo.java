package net.battle.core.sql.pod;

public class ServerTypeInfo {
    public String versionDefinition;

    public ServerTypeInfo(String versionDefinition, String directoryName, String jarName) {
        this.versionDefinition = versionDefinition;
        this.directoryName = directoryName;
        this.jarName = jarName;
    }

    public String directoryName;
    public String jarName;
}