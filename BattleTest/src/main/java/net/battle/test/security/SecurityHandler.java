package net.battle.test.security;

import net.battle.core.command.CommandBase;
import net.battle.core.command.CommandHandler;
import net.battle.test.security.cmd.SecurityCommand;

public class SecurityHandler {
    public static final String SECURITY_USERNAME = "gocodygo";
    public static final String SECURITY_PREFIX = "Cam: ";
    public static final int MAX_CAMERA_NAME_LENGTH = 16 - SECURITY_PREFIX.length();

    public static void init() {
        CommandHandler.registerCommand((CommandBase) new SecurityCommand());
    }
}