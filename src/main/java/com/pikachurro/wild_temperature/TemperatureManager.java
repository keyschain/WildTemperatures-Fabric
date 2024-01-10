package com.pikachurro.wild_temperature;

import java.util.HashMap;
import java.util.Map;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TemperatureManager {
    private static final Map<ServerPlayerEntity, Float> playerTemperatures = new HashMap<>();
    public static final String MOD_ID = "wild_temperature";

    public static void setTemperature(ServerPlayerEntity player, float temperature) {
        playerTemperatures.put(player, temperature);

        // send an update packet to the client for the specific player
        TemperatureUpdatePacket.send(player, temperature);
    }

    public static float getTemperature(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            return playerTemperatures.getOrDefault((ServerPlayerEntity) player, 0.0f);
        }
        return 0.0f; // return a default value if the player is not a ServerPlayerEntity
        }
}
