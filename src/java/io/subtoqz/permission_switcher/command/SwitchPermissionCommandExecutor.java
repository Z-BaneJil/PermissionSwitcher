package io.subtoqz.permission_switcher.command;

import io.subtoqz.permission_switcher.PermissionSwitcherMain;
import io.subtoqz.permission_switcher.json.JsonConfig;
import io.subtoqz.permission_switcher.properties.PropertiesFile;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.types.PermissionNode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SwitchPermissionCommandExecutor implements CommandExecutor {
    private final PermissionSwitcherMain plugin = PermissionSwitcherMain.getInstance();
    private final LuckPerms luckPerms = plugin.luckPerms;
    private int commandIndex;

    public SwitchPermissionCommandExecutor(int commandIndex) {
        this.commandIndex = commandIndex;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)) {
            sender.sendMessage(PropertiesFile.get("consoleNotAllowedCmd"));
            return false;
        }

        if (!player.hasPermission("permissionswitcher." + JsonConfig.getName(commandIndex))) {
            sender.sendMessage(PropertiesFile.get("accessDeniedCmd"));
            return false;
        }

        User user = luckPerms.getPlayerAdapter(Player.class).getUser(player);

        PermissionNode permissionNode = PermissionNode.builder(JsonConfig.getPermission(commandIndex)).build();

        if (player.hasPermission(permissionNode.getPermission())) {
            user.data().add(permissionNode.toBuilder().value(false).build());
            sender.sendMessage(JsonConfig.getOnRevokePermissionMessage(commandIndex));
        } else {
            user.data().add(permissionNode.toBuilder().value(true).build());
            sender.sendMessage(JsonConfig.getOnAddPermissionMessage(commandIndex));
        }
        luckPerms.getUserManager().saveUser(user);

        return true;
    }
}