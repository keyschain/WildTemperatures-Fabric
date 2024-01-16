package site.keyschain.wild_temperature.enchantment;

import static site.keyschain.wild_temperature.temperature.EnvironmentChecks.isInExtremeColdBiome;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;


public class EnchantmentFunctionality {
    static Enchantment frostProtectionEnchantment = ModEnchantments.FROST_PROTECTION;
    public static void applyFrostProtection(ServerPlayerEntity player) {
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
}
