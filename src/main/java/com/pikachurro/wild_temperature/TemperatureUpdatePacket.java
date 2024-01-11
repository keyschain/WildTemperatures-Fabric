package com.pikachurro.wild_temperature;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class TemperatureUpdatePacket {
    public static final String MOD_ID = "wild_temperature";
    public static final Identifier ID = new Identifier(MOD_ID, "temperature_update");

    private final UUID playerUuid;
    private final float temperature;

    public TemperatureUpdatePacket(UUID playerUuid, float temperature) {
        this.playerUuid = playerUuid;
        this.temperature = temperature;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public float getTemperature() {
        return temperature;
    }

    public static void write(PacketByteBuf buf, TemperatureUpdatePacket packet) {
        buf.writeUuid(packet.playerUuid);
        buf.writeFloat(packet.temperature);
    }

    public static TemperatureUpdatePacket fromPacketByteBuf(PacketByteBuf buf) {
        UUID playerUuid = buf.readUuid();
        float temperature = buf.readFloat();
        return new TemperatureUpdatePacket(playerUuid, temperature);
    }

    public static void send(ServerPlayerEntity player, float temperature) {
        TemperatureUpdatePacket packet = new TemperatureUpdatePacket(player.getUuid(), temperature);
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        TemperatureUpdatePacket.write(buf, packet);
        ServerPlayNetworking.send(player, ID, buf);
    }
}
