package io.subtoqz.permission_switcher.command;

import io.subtoqz.permission_switcher.PermissionSwitcherMain;
import io.subtoqz.permission_switcher.json.JsonConfig;
import io.subtoqz.permission_switcher.properties.PropertiesFile;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;

public class PermissionCommandExecutor implements CommandExecutor, TabCompleter {
    private static final PermissionSwitcherMain plugin = PermissionSwitcherMain.getInstance();

    String[] tabCompleter0 = {
            "reload"
    };

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        if (!commandSender.hasPermission("permissionswitcher.reload")) {
            commandSender.sendMessage(PropertiesFile.get("accessDeniedCmd"));
            return true;
        }

        if (args.length == 0) {
            commandSender.sendMessage(ChatColor.WHITE + "/permissionswitcher reload");
            return true;
        }

        if (!args[0].equalsIgnoreCase("reload")) {
            commandSender.sendMessage(ChatColor.WHITE + "/permissionswitcher reload");
            return true;
        }

        JsonConfig.reload();
        commandSender.sendMessage(PropertiesFile.get("reloadEchoCmd"));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("permissionswitcher.reload")) {
                return Arrays.stream(tabCompleter0).toList();
            }
        }
        return null;
    }
}