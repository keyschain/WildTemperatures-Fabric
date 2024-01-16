package site.keyschain.wild_temperature.temperature;

import static site.keyschain.wild_temperature.WildTemperature.CONFIG;
import static site.keyschain.wild_temperature.WildTemperature.LOGGER;
import static site.keyschain.wild_temperature.temperature.EnvironmentChecks.*;
import site.keyschain.wild_temperature.networking.TemperatureUpdatePacket;

import net.minecraft.server.network.ServerPlayerEntity;

public class TemperatureManager {
    public static float playerTemperature;
    public static float transitionTimeTicks = CONFIG.transitionTimeTicks();
    public static boolean doDebugLogs = CONFIG.doDebugLogs();

    private static float getBiomeTemperature(ServerPlayerEntity player) {
        // get current biome temperature from player location
        float biomeTemperature = getBiome(getWorld(player), getPlayerPos(player)).getTemperature();

        return biomeTemperature;
    }

    public static void updatePlayerTemperature(ServerPlayerEntity player) {
        float biomeTemperature = getBiomeTemperature(player);
        float targetTemperature;
        float temperatureChangePerTick;

        if (isRaining(player)) {
            if (biomeTemperature >= 0.6f) {
                targetTemperature = 0.2f;
            } else if (biomeTemperature > 0.05f) {
                targetTemperature = 0.0f;
            } else {
                targetTemperature = -0.9f;
            }
            if (playerTemperature != targetTemperature) {
                temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
                playerTemperature += temperatureChangePerTick;
                if (doDebugLogs) {
                    LOGGER.info("Player Temperature: " + playerTemperature);
                }
                TemperatureUpdatePacket.send(player);
            }
        } else if (isInWater(player) && biomeTemperature >= 0.6f) {
            targetTemperature = 0.6f;
            if (playerTemperature != targetTemperature) {
                temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
                playerTemperature += temperatureChangePerTick;
                if (doDebugLogs) {
                    LOGGER.info("Player Temperature: " + playerTemperature);
                }
                TemperatureUpdatePacket.send(player);
            }
        } else if (isNight(player)) {
            if (isInDesert(player)) {
                targetTemperature = 0.0f;
            } else if (biomeTemperature >= 0.6f) {
                targetTemperature = 0.3f;
            } else {
                targetTemperature = biomeTemperature;
            }
            if (playerTemperature != targetTemperature) {
                temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
                playerTemperature += temperatureChangePerTick;
                if (doDebugLogs) {
                    LOGGER.info("Player Temperature: " + playerTemperature);
                }
                TemperatureUpdatePacket.send(player);
            }
        } else if (biomeTemperature != playerTemperature) {
            targetTemperature = biomeTemperature;
            temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
            playerTemperature += temperatureChangePerTick;
            if (doDebugLogs) {
                LOGGER.info("Player Temperature: " + playerTemperature);
            }
            TemperatureUpdatePacket.send(player);
        }
    }
}
