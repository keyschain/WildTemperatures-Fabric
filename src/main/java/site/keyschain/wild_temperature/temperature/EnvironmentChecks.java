package site.keyschain.wild_temperature.temperature;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;

import java.util.Objects;

public class EnvironmentChecks {
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

    public static boolean isInExtremeColdBiome(ServerPlayerEntity player) {
        // check if the biome temperature is below a certain threshold (e.g., 0.2f)
        return TemperatureManager.playerTemperature < 0.2f;
    }

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
