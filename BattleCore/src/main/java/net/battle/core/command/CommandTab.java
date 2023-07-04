package net.battle.core.command;

import java.util.List;
import org.bukkit.entity.Player;

public interface CommandTab {
  String getCommandLabel();

  String[] getCommandAliases();

  List<String> getOptionsOnArgument(int paramInt, String[] paramArrayOfString, Player paramPlayer);
}