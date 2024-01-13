package com.pikachurro.wild_temperature;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.pikachurro.wild_temperature.WildTemperaturesConfig;
public class WildTemperature implements ModInitializer {
	public static final String MOD_ID = "wild_temperature";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	Enchantment frostProtectionEnchantment = ModEnchantments.FROST_PROTECTION;
	public static final WildTemperaturesConfig CONFIG = WildTemperaturesConfig.createAndLoad();
	@Override
	public void onInitialize() {

		ModEnchantments.registerModEnchantments();
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			world.getServer().getPlayerManager().getPlayerList().forEach(player -> {
				RefactoredTemperatureManager.updatePlayerTemperature(player);
				applyFrostProtection(player);
				TemperatureDamageManager.applyTemperatureDamage(player);
			});
		});
	}

	private void applyFrostProtection(ServerPlayerEntity player) {
		// get the player's equipped armor items
		Iterable<ItemStack> armorItems = player.getInventory().armor;

		// check each armor item for the Frost Protection enchantment
		for (ItemStack armorItem : armorItems) {
			if (EnchantmentHelper.getLevel(frostProtectionEnchantment, armorItem) > 0) {
				// apply the frost protection effect
				if (isInExtremeColdBiome(player)) {
					player.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 20, 0, false, false));
				}
				break; // stop checking other armor items if one has the enchantment
			}
		}
	}

	private boolean isInExtremeColdBiome(ServerPlayerEntity player) {
		// check if the biome temperature is below a certain threshold (e.g., 0.2f)
		return RefactoredTemperatureManager.playerTemperature < 0.2f;
	}
}
