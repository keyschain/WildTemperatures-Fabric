package com.pikachurro.wild_temperature;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TemperatureUpdatePacket {
    public static final String MOD_ID = "wild_temperature";
    public static final Identifier ID = new Identifier(MOD_ID, "temperature_update");
//    private final float biomeTemperature;

//    public TemperatureUpdatePacket(float biomeTemperature) {
//        this.biomeTemperature = biomeTemperature;
//    }
//
//    public static void write(PacketByteBuf buf, TemperatureUpdatePacket packet) {
//        buf.writeFloat(packet.biomeTemperature);
//    }
//
//    public static TemperatureUpdatePacket read(PacketByteBuf buf) {
//        return new TemperatureUpdatePacket(buf.readFloat());
//    }

    public static void send(ServerPlayerEntity player, float biomeTemperature) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeUuid(player.getUuid());  // write player UUID
        buf.writeFloat(biomeTemperature);
        ServerPlayNetworking.send(player, ID, buf);
    }
}
