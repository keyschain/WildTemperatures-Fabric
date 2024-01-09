package com.pikachurro.wild_temperature;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.Identifier;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;

import java.util.UUID;


public class WildTemperatureClient implements ClientModInitializer {
	public static final String MOD_ID = "wild_temperature";

	private static final Identifier EXTREME_COLD_TEXTURE = new Identifier(MOD_ID, "textures/gui/extreme_cold.png");
	private static final Identifier COLD_TEXTURE = new Identifier(MOD_ID, "textures/gui/cold.png");
	private static final Identifier BREEZY_TEXTURE = new Identifier(MOD_ID, "textures/gui/breezy.png");
	private static final Identifier NEUTRAL_TEXTURE = new Identifier(MOD_ID, "textures/gui/neutral.png");
	private static final Identifier WARM_TEXTURE = new Identifier(MOD_ID, "textures/gui/warm.png");
	private static final Identifier HOT_TEXTURE = new Identifier(MOD_ID, "textures/gui/hot.png");
	private static final Identifier EXTREME_HEAT_TEXTURE = new Identifier(MOD_ID, "textures/gui/extreme_heat.png");

	private static final Identifier BREEZY_OVERLAY = new Identifier(MOD_ID, "textures/gui/breezy_overlay.png");
	private static final Identifier COLD_OVERLAY = new Identifier(MOD_ID, "textures/gui/cold_overlay.png");
	private static final Identifier EXTREME_COLD_OVERLAY = new Identifier(MOD_ID, "textures/gui/extreme_cold_overlay.png");
	private static final Identifier COLD_DAMAGE_OVERLAY = new Identifier(MOD_ID, "textures/gui/cold_damage_overlay.png");

	private static final Identifier WARM_OVERLAY = new Identifier(MOD_ID, "textures/gui/warm_overlay.png");
	private static final Identifier HOT_OVERLAY = new Identifier(MOD_ID, "textures/gui/hot_overlay.png");
	private static final Identifier EXTREME_HEAT_OVERLAY = new Identifier(MOD_ID, "textures/gui/extreme_heat_overlay.png");
	private static final Identifier HEAT_DAMAGE_OVERLAY = new Identifier(MOD_ID, "textures/gui/heat_damage_overlay.png");

	public static final Identifier UPDATE_PACKET_ID = new Identifier(MOD_ID, "update_temperature");


    @Override
	public void onInitializeClient() {
		initClient();
		HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
			// renderHUD(drawContext, NEUTRAL_TEXTURE, 1.0f);
			// renderOverlay(drawContext, HEAT_DAMAGE_OVERLAY, 1.0f);
		});
	}

	public static void initClient() {
		// register a packet handler for the temperature update packet
		ClientPlayNetworking.registerGlobalReceiver(TemperatureUpdatePacket.ID, (client, handler, buf, responseSender) -> {
			UUID playerUuid = buf.readUuid();
			float temperature = buf.readFloat();
			// handle the received temperature update on the client
			updateTemperatureHUD(playerUuid, temperature);
		});
	}

	private static void updateTemperatureHUD(UUID playerUuid, float temperature) {
		MinecraftClient.getInstance().execute(() -> {
			ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
			if (clientPlayerEntity != null) {
				// Perform actions on the client player entity

				// Extreme Cold
				if (temperature <= 0.05){
					HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
						renderHUD(drawContext, EXTREME_COLD_TEXTURE, 1.0f);
						// renderOverlay(drawContext, HEAT_DAMAGE_OVERLAY, 1.0f);
					});
				}
				// Cold
				else if (temperature > 0.05 && temperature <= 0.25) {
					HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
						renderHUD(drawContext, COLD_TEXTURE, 1.0f);
						// renderOverlay(drawContext, HEAT_DAMAGE_OVERLAY, 1.0f);
					});
				}
				// Breezy
				else if (temperature > 0.25 && temperature <= 0.5) {
					HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
						renderHUD(drawContext, BREEZY_TEXTURE, 1.0f);
						// renderOverlay(drawContext, HEAT_DAMAGE_OVERLAY, 1.0f);
					});
				}
				// Neutral
				else if (temperature > 0.5 && temperature <= 0.7) {
					HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
						renderHUD(drawContext, NEUTRAL_TEXTURE, 1.0f);
						// renderOverlay(drawContext, HEAT_DAMAGE_OVERLAY, 1.0f);
					});
				}
				// Warm
				else if (temperature > 0.7 && temperature <= 1.0) {
					HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
						renderHUD(drawContext, WARM_TEXTURE, 1.0f);
						// renderOverlay(drawContext, HEAT_DAMAGE_OVERLAY, 1.0f);
					});
				}
				// Hot
				else if (temperature > 1.0 && temperature <= 1.6) {
					HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
						renderHUD(drawContext, HOT_TEXTURE, 1.0f);
						// renderOverlay(drawContext, HEAT_DAMAGE_OVERLAY, 1.0f);
					});
				}
				// Extreme Heat
				else if (temperature > 1.6) {
					HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
						renderHUD(drawContext, EXTREME_HEAT_TEXTURE, 1.0f);
						// renderOverlay(drawContext, HEAT_DAMAGE_OVERLAY, 1.0f);
					});
				}

			}
			});


	}

	private void renderOverlay(DrawContext context, Identifier texture, float opacity) {
		int scaledWidth = context.getScaledWindowWidth();
		int scaledHeight = context.getScaledWindowHeight();

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		context.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
		context.drawTexture(texture, 0, 0, -90, 0.0f, 0.0f, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	private static void renderHUD(DrawContext context, Identifier texture, float opacity) {
		int scaledWidth = context.getScaledWindowWidth();
		int i = scaledWidth / 2;
		int scaledHeight = context.getScaledWindowHeight();

		boolean leftHud = false;

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		context.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
		if (leftHud == true) {
			context.drawTexture(texture, i + 91 + 28 + 8, scaledHeight - 22, 0, 0.0f, 0.0f, 22, 22, 22, 22);
		} else {
			context.drawTexture(texture, i - 91 - 28 - 8, scaledHeight - 22, 0, 0.0f, 0.0f, 22, 22, 22, 22);
		}
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
	}
}
