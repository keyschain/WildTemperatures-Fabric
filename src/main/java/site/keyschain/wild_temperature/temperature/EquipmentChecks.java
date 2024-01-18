package site.keyschain.wild_temperature.temperature;

import net.minecraft.item.*;
import site.keyschain.wild_temperature.enchantment.ModEnchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import site.keyschain.wild_temperature.util.ModTags;

public class EquipmentChecks {
    static boolean hasFireProtection(ServerPlayerEntity player) {
        for (ItemStack armor : player.getArmorItems()) {
            if (EnchantmentHelper.getLevel(Enchantments.FIRE_PROTECTION, armor) > 0) {
                return true;
            }
        }
        return false;
    }

    static boolean hasFrostProtection(ServerPlayerEntity player) {
        for (ItemStack armor : player.getArmorItems()) {
            if (EnchantmentHelper.getLevel(ModEnchantments.FROST_PROTECTION, armor) > 0) {
                return true;
            }
        }
        return false;
    }


    static boolean hasHeatProtectionArmor(ServerPlayerEntity player) {
        int count = 0;
        for (ItemStack armor : player.getArmorItems()) {
            if (armor.getItem() instanceof ArmorItem && armor.isIn(ModTags.Items.PROTECT_AGAINST_HEAT)) {
                count++;
            }
        }
        return count >= 3;
    }


    static boolean hasColdProtectionArmor(ServerPlayerEntity player) {
        int count = 0;
        for (ItemStack armor : player.getArmorItems()) {
            if (armor.getItem() instanceof ArmorItem && armor.isIn(ModTags.Items.PROTECT_AGAINST_COLD)) {
                count++;
            }
        }
        return count >= 3;
    }

    static boolean hasFireResistance(ServerPlayerEntity player) {
        return player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
    }
}
