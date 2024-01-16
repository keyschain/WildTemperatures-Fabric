package site.keyschain.wild_temperature.networking;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import site.keyschain.wild_temperature.temperature.TemperatureManager;

import java.util.UUID;

import static site.keyschain.wild_temperature.WildTemperature.MOD_ID;


public class TemperatureUpdatePacket {
    public static final Identifier ID = new Identifier(MOD_ID, "temperature_update");

    private final UUID playerUuid;
    private final float temperature;

    public TemperatureUpdatePacket(UUID playerUuid, float temperature) {
        this.playerUuid = playerUuid;
        this.temperature = temperature;
    }

    public static void write(PacketByteBuf buf, TemperatureUpdatePacket packet) {
        buf.writeUuid(packet.playerUuid);
        buf.writeFloat(packet.temperature);
    }

    public static void send(ServerPlayerEntity player) {
        float temperature = TemperatureManager.playerTemperature;
        TemperatureUpdatePacket packet = new TemperatureUpdatePacket(player.getUuid(), temperature);
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        TemperatureUpdatePacket.write(buf, packet);
        ServerPlayNetworking.send(player, ID, buf);
    }
}
