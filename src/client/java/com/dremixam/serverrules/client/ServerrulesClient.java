package com.dremixam.serverrules.client;

import com.dremixam.serverrules.client.network.ClientNetworkHandler;
import net.fabricmc.api.ClientModInitializer;

public class ServerrulesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientNetworkHandler.initialize();
    }
}
