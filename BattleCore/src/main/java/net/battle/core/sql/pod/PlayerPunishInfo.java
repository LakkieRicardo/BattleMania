package net.battle.core.sql.pod;

import java.sql.Date;

public class PlayerPunishInfo {

    private int id;
    private String recipientUUID;
    private String punisherUUID;
    private Date expiration;
    private boolean isActive;
    private PunishmentType type;
    private String reason;

    public PlayerPunishInfo(int id, String recipientUUID, String punisherUUID, Date expiration, boolean isActive,
            PunishmentType type, String reason) {
        this.id = id;
        this.recipientUUID = recipientUUID;
        this.punisherUUID = punisherUUID;
        this.expiration = expiration;
        this.isActive = isActive;
        this.type = type;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRecipientUUID() {
        return recipientUUID;
    }

    public void setRecipientUUID(String recipientUUID) {
        this.recipientUUID = recipientUUID;
    }

    public String getPunisherUUID() {
        return punisherUUID;
    }

    public void setPunisherUUID(String punisherUUID) {
        this.punisherUUID = punisherUUID;
    }

    public Date getExpiration() {
        return expiration;
    }

    public void setExpiration(Date expiration) {
        this.expiration = expiration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }

    public PunishmentType getType() {
        return type;
    }

    public void setType(PunishmentType type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

     @Override
     public String toString() {
         return String.format("[id=%s,recipient=%s,punisher=%s,expiration=%s,is_active=%s,type=%s,reason=%s]", id, recipientUUID, punisherUUID, expiration.toString(), isActive, reason);
     }

}