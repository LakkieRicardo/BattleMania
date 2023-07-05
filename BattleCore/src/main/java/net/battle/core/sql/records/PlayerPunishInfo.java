package net.battle.core.sql.records;

import java.sql.Date;

public record PlayerPunishInfo(int id, String recipientUUID, String punisherUUID, Date expiration, boolean isActive, PunishType type, String reason) { }