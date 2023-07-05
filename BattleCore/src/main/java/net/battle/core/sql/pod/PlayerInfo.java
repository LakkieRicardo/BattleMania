package net.battle.core.sql.pod;

public record PlayerInfo(String playerUUID, String sqlRank, int level, int ingot, int token, float ingameHours) {

    public PlayerInfo withRank(String sqlRank) {
        return new PlayerInfo(playerUUID, sqlRank, level, ingot, token, ingameHours);
    }

    public PlayerInfo withLevel(int level) {
        return new PlayerInfo(playerUUID, sqlRank, level, ingot, token, ingameHours);
    }

    public PlayerInfo withIngot(int ingot) {
        return new PlayerInfo(playerUUID, sqlRank, level, ingot, token, ingameHours);
    }

    public PlayerInfo withToken(int token) {
        return new PlayerInfo(playerUUID, sqlRank, level, ingot, token, ingameHours);
    }

    public PlayerInfo withIngameHours(float ingameHours) {
        return new PlayerInfo(playerUUID, sqlRank, level, ingot, token, ingameHours);
    }

}