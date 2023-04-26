package io.subtoqz.permission_switcher.command;

import io.subtoqz.permission_switcher.PermissionSwitcherMain;
import io.subtoqz.permission_switcher.exception.CommandNotPreparedException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class CommandRegisterer extends BukkitCommand {
    private CommandExecutor commandExecutor;
    private final PermissionSwitcherMain plugin = PermissionSwitcherMain.getInstance();

    public CommandRegisterer(String name, ArrayList<String> aliases, CommandExecutor commandExecutor) {
        super(name);
        this.setAliases(aliases);
        this.setExecutor(commandExecutor);
    }

    public void setExecutor(CommandExecutor commandExecutor) {
        this.commandExecutor = commandExecutor;
    }

    public void register() {
        if(commandExecutor == null) {
            throw new CommandNotPreparedException();
        }
        try {
            final Field bukkitCommandMap = plugin.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(plugin.getServer());

            commandMap.register(getName(), this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Я яйца чешу
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        return commandExecutor.onCommand(sender, this, label, args);
    }
}
