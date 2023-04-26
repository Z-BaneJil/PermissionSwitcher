package io.subtoqz.permission_switcher.properties;

import io.subtoqz.permission_switcher.PermissionSwitcherMain;
import org.bukkit.ChatColor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;

public class PropertiesFile {
    private static final PermissionSwitcherMain plugin = PermissionSwitcherMain.getInstance();

    private final static Properties properties = new Properties();
    private static String _fileName;

    public static void setName(String fileName) {
        _fileName = fileName;
    }

    public static void load() {
        InputStreamReader inputStreamReader = new InputStreamReader(
                Objects.requireNonNull(plugin.getClass().getClassLoader().getResourceAsStream(_fileName + ".properties")), StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            properties.load(bufferedReader);
            inputStreamReader.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reload() {
        load();
    }

    public static String get(String key) {
        return ChatColor.translateAlternateColorCodes('&', properties.getProperty(key));
    }
}
