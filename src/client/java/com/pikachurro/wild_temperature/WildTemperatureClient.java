package com.pikachurro.wild_temperature;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class WildTemperatureClient implements ClientModInitializer {
	public static final String MOD_ID = "wild_temperature";
	private static final Identifier EXTREME_COLD_TEXTURE = new Identifier(MOD_ID, "textures/gui/extreme_cold.png");
	private static final Identifier COLD_TEXTURE = new Identifier(MOD_ID, "textures/gui/cold.png");
	private static final Identifier BREEZY_TEXTURE = new Identifier(MOD_ID, "textures/gui/breezy.png");
	private static final Identifier NEUTRAL_TEXTURE = new Identifier(MOD_ID, "textures/gui/neutral.png");
	private static final Identifier WARM_TEXTURE = new Identifier(MOD_ID, "textures/gui/warm.png");
	private static final Identifier HEAT_TEXTURE = new Identifier(MOD_ID, "textures/gui/hot.png");
	private static final Identifier EXTREME_HEAT_TEXTURE = new Identifier(MOD_ID, "textures/gui/extreme_heat.png");

	private static final TemperatureHUD temperatureHUD = new TemperatureHUD();

	@Override
	public void onInitializeClient() {
		HudRenderCallback.EVENT.register((matrices, tickDelta) -> renderTemperatureHUD(matrices));
	}
}