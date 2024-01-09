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

	private static boolean hudRenderCallbackRegistered = false;
	private static float currentTemperature = 0.0f;

	@Override
	public void onInitializeClient() {
		initClient();
		registerHudRenderCallback();
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

	private static void registerHudRenderCallback() {

		HudRenderCallback.EVENT.register((DrawContext drawContext, float tickDelta) -> {
			if (currentTemperature > 1.6) {
				renderOverlay(drawContext, EXTREME_HEAT_OVERLAY, 0.5f);
				renderHUD(drawContext, EXTREME_HEAT_TEXTURE, 1.0f);
			} else if (currentTemperature > 1.0 && currentTemperature <= 1.6) {
				renderOverlay(drawContext, HOT_OVERLAY, 0.5f);
				renderHUD(drawContext, HOT_TEXTURE, 1.0f);
			} else if (currentTemperature > 0.7 && currentTemperature <= 1.0) {
				renderOverlay(drawContext, WARM_OVERLAY, 0.5f);
				renderHUD(drawContext, WARM_TEXTURE, 1.0f);
			} else if (currentTemperature > 0.5 && currentTemperature <= 0.7) {
				renderOverlay(drawContext, WARM_OVERLAY, 0f);
				renderHUD(drawContext, NEUTRAL_TEXTURE, 1.0f);
			}
		});
		hudRenderCallbackRegistered = true;

	}
	private static void updateTemperatureHUD(UUID playerUuid, float temperature) {
		MinecraftClient.getInstance().execute(() -> {

			ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;

			if (clientPlayerEntity != null) {
				// perform actions on the client player entity

				currentTemperature = temperature;

				if (!hudRenderCallbackRegistered) {
					registerHudRenderCallback();
				}

			}
			});


	}

	private static void renderOverlay(DrawContext context, Identifier texture, float opacity) {
		int scaledWidth = context.getScaledWindowWidth();
		int scaledHeight = context.getScaledWindowHeight();

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		context.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
		context.drawTexture(texture, 0, 0, -90, 0.0f, 0.0f, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableBlend();
	}

	private static void renderHUD(DrawContext context, Identifier texture, float opacity) {
		int scaledWidth = context.getScaledWindowWidth();
		int i = scaledWidth / 2;
		int scaledHeight = context.getScaledWindowHeight();

		boolean leftHandHud = false;

		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		context.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
		if (leftHandHud == true) {
			context.drawTexture(texture, i + 91 + 28 + 8, scaledHeight - 22, 0, 0.0f, 0.0f, 22, 22, 22, 22);
		} else {
			context.drawTexture(texture, i - 91 - 28 - 24, scaledHeight - 22, 0, 0.0f, 0.0f, 22, 22, 22, 22);
		}
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.defaultBlendFunc();
		RenderSystem.enableBlend();
	}

}
