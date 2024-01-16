package site.keyschain.wild_temperature;

import site.keyschain.wild_temperature.config.WildTemperaturesConfig;
import site.keyschain.wild_temperature.enchantment.ModEnchantments;
import static site.keyschain.wild_temperature.enchantment.EnchantmentFunctionality.applyFrostProtection;
import static site.keyschain.wild_temperature.temperature.TemperatureDamageManager.applyTemperatureDamage;
import static site.keyschain.wild_temperature.temperature.TemperatureManager.updatePlayerTemperature;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WildTemperature implements ModInitializer {
	public static final String MOD_ID = "wild_temperature";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final WildTemperaturesConfig CONFIG = WildTemperaturesConfig.createAndLoad();
	@Override
	public void onInitialize() {

		ModEnchantments.registerModEnchantments();
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			world.getServer().getPlayerManager().getPlayerList().forEach(player -> {
				updatePlayerTemperature(player);
				applyFrostProtection(player);
				applyTemperatureDamage(player);
			});
		});
	}
}
