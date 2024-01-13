package com.pikachurro.wild_temperature;

import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import java.util.Objects;

import static com.pikachurro.wild_temperature.WildTemperature.CONFIG;
import static com.pikachurro.wild_temperature.WildTemperature.LOGGER;


public class RefactoredTemperatureManager {
    static float playerTemperature;
    public static float transitionTimeTicks = CONFIG.transitionTimeTicks();
    public static BlockPos getPlayerPos(ServerPlayerEntity player) {
        // get current positon from player
        int playerX = (int) player.getX();
        int playerY = (int) player.getY();
        int playerZ = (int) player.getZ();
        BlockPos playerPos = new BlockPos(playerX, playerY, playerZ);

        return playerPos;
    }
    static ServerWorld getWorld(ServerPlayerEntity player) {
        // get players current world
        ServerWorld serverWorld = (ServerWorld) player.getWorld();
        return serverWorld;
    }

    public static Biome getBiome(ServerWorld serverWorld, BlockPos playerPos) {
        //get current biome at player position
        Biome biome = serverWorld.getBiome(playerPos).value();
        return biome;
    }

    private static float getBiomeTemperature(ServerPlayerEntity player) {
        // get current biome temperature from player location
        float biomeTemperature = getBiome(getWorld(player), getPlayerPos(player)).getTemperature();

        return biomeTemperature;
    }

    public static void updatePlayerTemperature(ServerPlayerEntity player) {
        float biomeTemperature = getBiomeTemperature(player);

        if (isRaining(player) && biomeTemperature >= 0.6f) {
            if (playerTemperature != 0.2f) {
                float targetTemperature = 0.2f;
                float temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
                playerTemperature += temperatureChangePerTick;

                LOGGER.info("Player Temperature: " + playerTemperature);
                TemperatureUpdatePacket.send(player); // send the temperature update to the client
            }
        } else if (isRaining(player) && biomeTemperature > 0.05f) {
            if (playerTemperature != 0.0f) {
                float targetTemperature = 0.0f;
                float temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
                playerTemperature += temperatureChangePerTick;

                LOGGER.info("Player Temperature: " + playerTemperature);
                TemperatureUpdatePacket.send(player); // send the temperature update to the client
            }
        } else if (isRaining(player) && biomeTemperature < 0.05f) {
            if (playerTemperature != -0.9f) {
                float targetTemperature = -0.9f;
                float temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
                playerTemperature += temperatureChangePerTick;

                LOGGER.info("Player Temperature: " + playerTemperature);
                TemperatureUpdatePacket.send(player); // send the temperature update to the client
            }
        } else if (isNight(player) && isInDesert(player)) {
            if (playerTemperature != 0.0f) {
                float targetTemperature = 0.0f;
                float temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
                playerTemperature += temperatureChangePerTick;

                LOGGER.info("Player Temperature: " + playerTemperature);
                TemperatureUpdatePacket.send(player); // send the temperature update to the client
            }
        } else if (isNight(player) && !isInDesert(player) && biomeTemperature >= 0.6f) {
            if (playerTemperature != 0.3f) {
                float targetTemperature = 0.3f;
                float temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
                playerTemperature += temperatureChangePerTick;

                LOGGER.info("Player Temperature: " + playerTemperature);
                TemperatureUpdatePacket.send(player); // send the temperature update to the client
            }
        } else if (!isNight(player) && biomeTemperature != playerTemperature) {
            float targetTemperature = biomeTemperature;
            float temperatureChangePerTick = (targetTemperature - playerTemperature) / transitionTimeTicks;
            playerTemperature += temperatureChangePerTick;

            LOGGER.info("Player Temperature: " + playerTemperature);
            TemperatureUpdatePacket.send(player); // send the temperature update to the client
        }}


    public static boolean isNight(ServerPlayerEntity player) {
        long timeOfDay = player.getEntityWorld().getTimeOfDay();
        return timeOfDay >= 13000 && timeOfDay <= 22000;
    }

    public static boolean isInWater(ServerPlayerEntity player) {
        // get the block state at the player's position
        BlockState blockState = player.getEntityWorld().getBlockState(player.getBlockPos());

        // check if the fluid state of the block state is in the water tag
        boolean isInWater = blockState.getFluidState().isIn(FluidTags.WATER);

        return isInWater;
    }

    // this is a terrifying method but it works...
    public static boolean isInDesert(ServerPlayerEntity player) {
        BlockPos playerPos = getPlayerPos(player);
        ServerWorld serverWorld = getWorld(player);
        RegistryKey biome = serverWorld.getBiome(playerPos).getKey().get();

        if(Objects.equals(String.valueOf(biome), "ResourceKey[minecraft:worldgen/biome / minecraft:desert]")) {
            return true;
        }
        return false;
    }

    public static boolean isRaining(ServerPlayerEntity player) {
        ServerWorld serverWorld = getWorld(player);
        return serverWorld.isRaining();
    }
}
