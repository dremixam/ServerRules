package com.dremixam.serverrules;

import com.dremixam.serverrules.config.ConfigManager;
import com.dremixam.serverrules.data.RulesManager;
import com.dremixam.serverrules.events.PlayerConnectionHandler;
import com.dremixam.serverrules.network.NetworkHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Serverrules implements ModInitializer {
    public static final String MOD_ID = "serverrules";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    private ConfigManager configManager;
    private RulesManager rulesManager;

    @Override
    public void onInitialize() {
        // Vérifier si on est côté serveur (pas en solo)
        if (!FabricLoader.getInstance().getEnvironmentType().name().equals("SERVER")) {
            LOGGER.info("Server Rules disabled in single-player mode");
            return;
        }

        LOGGER.info("Initializing Server Rules!");

        configManager = new ConfigManager();
        rulesManager = new RulesManager(configManager);

        // Initialize network system
        NetworkHandler.initialize(configManager, rulesManager);

        // Initialize player connection events
        PlayerConnectionHandler.initialize(configManager, rulesManager);

        LOGGER.info("Server Rules successfully initialized!");
        if (configManager.isRulesEnabled()) {
            LOGGER.info("Rules system enabled");
        }
    }
}
