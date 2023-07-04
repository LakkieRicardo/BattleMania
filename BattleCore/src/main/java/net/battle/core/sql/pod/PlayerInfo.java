package net.battle.core.sql.pod;

public class PlayerInfo {
    private String pl;
    private String sqlRank;
    private int level;

    public PlayerInfo(String pl, int level, String rank, int ingot, int token, float time) {
        this.pl = pl;
        this.level = level;
        this.sqlRank = rank;
        this.ingot = ingot;
        this.token = token;
        this.time = time;
    }

    private int ingot;
    private int token;
    private float time;

    public PlayerInfo() {
    }

    public String getPlayerUUID() {
        return this.pl;
    }

    public void setPlayer(String pl) {
        this.pl = pl;
    }

    public String getSqlRank() {
        return this.sqlRank;
    }

    public void setSqlRank(String sqlRank) {
        this.sqlRank = sqlRank;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getIngot() {
        return this.ingot;
    }

    public void setIngot(int ingot) {
        this.ingot = ingot;
    }

    public int getToken() {
        return this.token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public float getTime() {
        return this.time;
    }

    public void setTime(float time) {
        this.time = time;
    }
}