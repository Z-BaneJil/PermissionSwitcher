package io.subtoqz.permission_switcher;

import com.google.gson.*;
import io.subtoqz.permission_switcher.command.CommandRegisterer;
import io.subtoqz.permission_switcher.command.PermissionCommandExecutor;
import io.subtoqz.permission_switcher.command.SwitchPermissionCommandExecutor;
import io.subtoqz.permission_switcher.json.JsonConfig;
import io.subtoqz.permission_switcher.properties.PropertiesFile;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Objects;

public class PermissionSwitcherMain extends JavaPlugin {
    private static PermissionSwitcherMain instance;
    public LuckPerms luckPerms;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        RegisteredServiceProvider<LuckPerms> luckPermsProvider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
        if (luckPermsProvider != null)
            luckPerms = luckPermsProvider.getProvider();

        getLogger().info(ChatColor.DARK_AQUA + "LuckPerms initialized");

        Objects.requireNonNull(getCommand("permissionswitcher")).setExecutor(new PermissionCommandExecutor());

        JsonConfig.load();
        JsonArray commands = JsonConfig.getCommands();

        PropertiesFile.setName(JsonConfig.get("locale").getAsString());
        PropertiesFile.load();

        int i = 0;
        for (JsonElement commandElement : commands) {
            JsonObject command = commandElement.getAsJsonObject();

            String name = command.get("name").getAsString();
            ArrayList<String> aliases = new ArrayList<>();

            JsonArray aliasesJson = command.getAsJsonArray("aliases");

            for (JsonElement alias : aliasesJson) {
                aliases.add(alias.getAsString());
            }

            new CommandRegisterer(name, aliases, new SwitchPermissionCommandExecutor(i)).register();

            i++;
        }
    }

    public static PermissionSwitcherMain getInstance() {
        return instance;
    }
}