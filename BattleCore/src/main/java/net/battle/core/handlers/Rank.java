package net.battle.core.handlers;

public enum Rank {
    OPERATOR("op", "§c§lOPERATOR"),
    DEVELOPER("dev", "§c§lDEV"),
    MODERATOR("mod", "§e§lMODERATOR"),
    BUILDER("builder", "§e§lBUILDER"),
    HELPER("helper", "§7§lHELPER"),
    PLAYER("player", ""),
    EXPLORER("explorer", "§3§lEXPLORER"),
    EMPEROR("emperor", "§a§lEMPEROR"),
    KING("king", "§6§lKING"),
    OWNER("owner", "§c§lOWNER"),
    EXPMOD("expmod", "§e§lEXP.MOD"),
    FAKEOPERATOR("f_op", "§c§lOPERATOR"),
    FAKEOWNER("f_owner", "§c§lOWNER");

    private String SQLName;
    private String gameName;

    Rank(String sqlName, String gameName) {
        this.SQLName = sqlName;
        this.gameName = gameName;
    }

    public String getSQLName() {
        return this.SQLName;
    }

    public String getGameName() {
        return this.gameName;
    }
}