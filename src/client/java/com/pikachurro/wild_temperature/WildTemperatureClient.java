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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WildTemperatureClient implements ClientModInitializer {
	public static final String MOD_ID = "wild_temperature";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
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

	private static boolean hudRenderCallbackRegistered = false;
	private static float currentTemperature = 0.0f;

	static boolean leftHandHud = true;
	static boolean nonDeadlyOverlays = true;
	static boolean deadlyOverlays = true;
	static boolean temperatureHUD = true;

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
		HudRenderCallback.EVENT.register((drawContext, tickDelta) -> {
			if (currentTemperature > 1.6) {
				if (nonDeadlyOverlays && !TemperatureManager.isTakingTemperatureDamage) {
					renderOverlay(drawContext, EXTREME_HEAT_OVERLAY, 1.0f);
				} else if (TemperatureManager.isTakingTemperatureDamage && deadlyOverlays) {
					renderOverlay(drawContext, HEAT_DAMAGE_OVERLAY, 1.0f);
				}
				if (temperatureHUD) {
					renderHUD(drawContext, EXTREME_HEAT_TEXTURE, 1.0f);
				}
			} else if (currentTemperature > 1.1) {
				if (nonDeadlyOverlays) {
					renderOverlay(drawContext, HOT_OVERLAY, 1.0f);
				}
				if (temperatureHUD) {
					renderHUD(drawContext, HOT_TEXTURE, 1.0f);
				}
			} else if (currentTemperature > 0.8) {
				if (nonDeadlyOverlays) {
					renderOverlay(drawContext, WARM_OVERLAY, 1.0f);
				}
				if (temperatureHUD) {
					renderHUD(drawContext, WARM_TEXTURE, 1.0f);
				}
			} else if (currentTemperature > 0.5) {
				if (nonDeadlyOverlays) {
					renderOverlay(drawContext, WARM_OVERLAY, 0f);
				}
				if (temperatureHUD) {
					renderHUD(drawContext, NEUTRAL_TEXTURE, 1.0f);
				}
			} else if (currentTemperature > 0.25) {
				if (nonDeadlyOverlays) {
					renderOverlay(drawContext, BREEZY_OVERLAY, 1.0f);
				}
				if (temperatureHUD) {
					renderHUD(drawContext, BREEZY_TEXTURE, 1.0f);
				}
			} else if (currentTemperature > 0.05) {
				if (nonDeadlyOverlays) {
					renderOverlay(drawContext, COLD_OVERLAY, 1.0f);
				}
				if (temperatureHUD) {
					renderHUD(drawContext, COLD_TEXTURE, 1.0f);
				}
			} else {
				if (nonDeadlyOverlays && !TemperatureManager.isTakingTemperatureDamage) {
					renderOverlay(drawContext, EXTREME_COLD_OVERLAY, 1.0f);
				} else if (TemperatureManager.isTakingTemperatureDamage && deadlyOverlays) {
					renderOverlay(drawContext, COLD_DAMAGE_OVERLAY, 1.0f);
				}
				if (temperatureHUD) {
					renderHUD(drawContext, EXTREME_COLD_TEXTURE, 1.0f);
				}
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

		RenderSystem.defaultBlendFunc();
		RenderSystem.enableBlend();
		context.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
		context.drawTexture(texture, 0, 0, -90, 0.0f, 0.0f, scaledWidth, scaledHeight, scaledWidth, scaledHeight);
		context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

	private static void renderHUD(DrawContext context, Identifier texture, float opacity) {
		int scaledWidth = context.getScaledWindowWidth();
		int i = scaledWidth / 2;
		int scaledHeight = context.getScaledWindowHeight();

		RenderSystem.defaultBlendFunc();
		RenderSystem.enableBlend();
		context.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
		if (leftHandHud == true) {
			context.drawTexture(texture, i + 91 + 28 + 8, scaledHeight - 22, 0, 0.0f, 0.0f, 22, 22, 22, 22);
		} else {
			context.drawTexture(texture, i - 91 - 28 - 30, scaledHeight - 22, 0, 0.0f, 0.0f, 22, 22, 22, 22);
		}
		context.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		RenderSystem.disableBlend();
		RenderSystem.defaultBlendFunc();
	}

}
