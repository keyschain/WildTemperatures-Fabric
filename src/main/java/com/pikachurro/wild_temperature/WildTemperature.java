package com.pikachurro.wild_temperature;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WildTemperature implements ModInitializer {
	public static final String MOD_ID = "wild_temperature";
	private boolean hasLoggedFirst = false;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private float targetTemperature = 0.0f;
	private float currentTemperature = 0.0f;



	@Override
	public void onInitialize() {

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			if (!hasLoggedFirst) {
				world.getServer().getPlayerManager().getPlayerList().forEach(this::logBiomeTemperature);
				hasLoggedFirst = true;
			}

			world.getServer().getPlayerManager().getPlayerList().forEach(this::logBiomeTemperature);
		});
	}

	private void logBiomeTemperature(ServerPlayerEntity player) {

		if (!(player instanceof ServerPlayerEntity)) {
			return;
		}

		// get player position
		int playerX = (int) player.getX();
		int playerY = (int) player.getY();
		int playerZ = (int) player.getZ();
		BlockPos playerPos = new BlockPos(playerX, playerY, playerZ);

		// get world
		ServerWorld serverWorld = (ServerWorld) player.getWorld();

		// get current biome at player position
		Biome biome = serverWorld.getBiome(playerPos).value();

		// get temperature of players current biome
		targetTemperature = biome.getTemperature();

		// set the temperature in the manager if its different
		if (currentTemperature != targetTemperature) {
			// gradually update the currentTemperature towards the targetTemperature
			if (currentTemperature < targetTemperature) {
				if (targetTemperature - currentTemperature <= 0.1f) {
					currentTemperature = targetTemperature;
				} else {
					currentTemperature += 0.1f; // adjust the increment value as needed
				}
			} else {
				if (currentTemperature - targetTemperature <= 0.1f) {
					currentTemperature = targetTemperature;
				} else {
					currentTemperature -= 0.1f; // adjust the decrement value as needed
				}
			}
		} else {
			// set the currentTemperature to match the targetTemperature
			currentTemperature = targetTemperature;
		}
		TemperatureManager.setTemperature(player, currentTemperature);


		//LOGGER.info("Current Temperature for " + player.getName().getString() + ": " + currentTemperature + " at " + playerPos + " and Target Temperature " + targetTemperature);
	}
}