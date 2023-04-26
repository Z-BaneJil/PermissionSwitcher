package io.subtoqz.permission_switcher.json;

import com.google.gson.*;
import io.subtoqz.permission_switcher.PermissionSwitcherMain;
import io.subtoqz.permission_switcher.properties.PropertiesFile;
import org.bukkit.ChatColor;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class JsonConfig {
    private static final PermissionSwitcherMain plugin = PermissionSwitcherMain.getInstance();
    private static final ArrayList<String> onAddPermissionMessages = new ArrayList<>();
    private static final ArrayList<String> commandNames = new ArrayList<>();
    private static final ArrayList<String> permissions = new ArrayList<>();
    private static final ArrayList<String> onRevokePermissionMessages = new ArrayList<>();
    private static final ArrayList<Boolean> savesToDB = new ArrayList<>();
    private static JsonObject rootJsonObject = getRootJsonObject();

    public static JsonObject getRootJsonObject() {
        File configFile = new File(plugin.getDataFolder() + File.separator + "config.yml");
        Yaml yaml = new Yaml();

        Object loadedYaml;
        try {
            loadedYaml = yaml.load(new FileReader(configFile, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(loadedYaml, LinkedHashMap.class);

        //Если нужно отобразить конфиг ввиде JSON
        //plugin.getServer().getConsoleSender().sendMessage(ChatColor.RED + "\n" + json);

        JsonParser jsonParser = new JsonParser();
        JsonObject _rootJsonObject = (JsonObject) jsonParser.parse(json);

        JsonArray commands = _rootJsonObject.getAsJsonArray("commands");

        permissions.clear();
        commandNames.clear();
        onAddPermissionMessages.clear();
        onRevokePermissionMessages.clear();
        savesToDB.clear();
        for (JsonElement commandElement : commands) {
            JsonObject command = commandElement.getAsJsonObject();

            commandNames.add(command.get("name").getAsString());
            permissions.add(command.get("permission").getAsString());
            savesToDB.add(command.get("saveToDB").getAsBoolean());
            onAddPermissionMessages.add(ChatColor.translateAlternateColorCodes('&',  command.get("onAddPermissionMessage").getAsString()));
            onRevokePermissionMessages.add(ChatColor.translateAlternateColorCodes('&', command.get("onRevokePermissionMessage").getAsString()));
        }

        return _rootJsonObject;
    }

    public static JsonArray getCommands() {
        return rootJsonObject.getAsJsonArray("commands");
    }

    public static String getName(int index) {
        return commandNames.get(index);
    }

    public static String getPermission(int index) {
        return permissions.get(index);
    }

    public static boolean getSaveToDB(int index) {
        return savesToDB.get(index);
    }

    public static String getOnAddPermissionMessage(int index) {
        return onAddPermissionMessages.get(index);
    }

    public static String getOnRevokePermissionMessage(int index) {
        return onRevokePermissionMessages.get(index);
    }

    public static void reload() {
        rootJsonObject = getRootJsonObject();
        PropertiesFile.setName(JsonConfig.get("locale").getAsString());
        PropertiesFile.reload();
    }

    public static void load() {
        reload();
    }

    public static JsonElement get(String key) {
        return rootJsonObject.get(key);
    }
}