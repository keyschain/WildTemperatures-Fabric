package com.pikachurro.wild_temperature;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WildTemperature implements ModInitializer {
	public static final String MOD_ID = "wild_temperature";
	private boolean hasLogged = false;
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			// log the temperature every 30 seconds to console
			if (!hasLogged && world.getTime() % (30 * 20) == 0) {  // 20 ticks per second, 30 seconds

				// log biome temperature for all online players
				world.getServer().getPlayerManager().getPlayerList().forEach(this::logBiomeTemperature);
				hasLogged = true;  // set the flag to avoid logging multiple times in one tick

			} else if (world.getTime() % (30 * 20) != 0) {
				hasLogged = false;  // reset the flag if the condition is not met
			}
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
		float currentPlayerTemperature = biome.getTemperature();

		// set the temperature in the manager
		TemperatureManager.setTemperature(player, currentPlayerTemperature);

		// output string
		LOGGER.info("Biome Temperature for " + player.getName().getString() + ": " + currentPlayerTemperature + " at " + playerPos);
	}


}