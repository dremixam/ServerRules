package com.dremixam.serverrules.config;

import net.fabricmc.loader.api.FabricLoader;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class ConfigManager {
    private static final Path CONFIG_DIR = FabricLoader.getInstance().getConfigDir().resolve("serverrules");
    private static final File CONFIG_FILE = CONFIG_DIR.resolve("config.yml").toFile();

    private Map<String, Object> config;

    public ConfigManager() {
        createConfigDirectory();
        loadConfig();
    }

    private void createConfigDirectory() {
        try {
            Files.createDirectories(CONFIG_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadConfig() {
        Yaml yaml = new Yaml();
        Map<String, Object> loadedConfig = null;
        if (CONFIG_FILE.exists()) {
            try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
                loadedConfig = yaml.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (loadedConfig == null) {
            loadedConfig = new java.util.LinkedHashMap<>();
        }
        Map<String, Object> defaultConfig = getDefaultConfigMap();
        for (String key : defaultConfig.keySet()) {
            if (!loadedConfig.containsKey(key)) {
                loadedConfig.put(key, defaultConfig.get(key));
            } else if (defaultConfig.get(key) instanceof Map) {
                Map<String, Object> subDefault = (Map<String, Object>) defaultConfig.get(key);
                Map<String, Object> subLoaded = (Map<String, Object>) loadedConfig.get(key);
                if (subLoaded == null) {
                    loadedConfig.put(key, subDefault);
                } else {
                    for (String subKey : subDefault.keySet()) {
                        if (!subLoaded.containsKey(subKey)) {
                            subLoaded.put(subKey, subDefault.get(subKey));
                        }
                    }
                }
            }
        }
        config = loadedConfig;
        saveConfig();
    }

    private void saveConfig() {
        Yaml yaml = new Yaml();
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            yaml.dump(config, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Map<String, Object> getDefaultConfigMap() {
        String defaultConfig = """
                rules:
                  enabled: true
                  title: "§6§lServer Rules"
                  content: |
                    §b§lWelcome to our community server!
                    
                    §fBy playing on this server, you agree to follow these rules:
                    
                    §a1. §fRespect other players and their builds
                    §a2. §fNo griefing, stealing or intentional destruction
                    §a3. §fNo offensive language or harassment
                    §a4. §fRespect protected areas and private properties
                    §a5. §fNo cheating, hacking or bug exploitation
                    §a6. §fListen to and respect moderators and administrators
                    
                    §c§lBreaking these rules may result in a warning,
                    §c§ltemporary suspension or permanent ban.
                    
                    §e§lThank you for helping maintain a friendly community!
                  accept_button: "I Accept"
                  decline_button: "I Decline"
                  checkbox_text: "I understand and accept the rules"
                  declined_message: "You must accept the rules to play on this server."
                  accepted_message: "You have accepted the server rules. Welcome!"
                  sending_error_message: "Error sending rules: %error%"
                """;
        Yaml yaml = new Yaml();
        return yaml.load(new StringReader(defaultConfig));
    }

    public String getString(String key) {
        Map<String, Object> rules = (Map<String, Object>) config.get("rules");
        Object value = rules.get(key);
        return value instanceof String ? (String) value : "";
    }

    public boolean isRulesEnabled() {
        Map<String, Object> rules = (Map<String, Object>) config.get("rules");
        return (Boolean) rules.get("enabled");
    }

    public Path getConfigDirectory() {
        return CONFIG_DIR;
    }
}
