package com.pikachurro.wild_temperature;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class TemperatureDamageManager {
    public static boolean isTakingTemperatureDamage = false;

    private static final float EXTREME_HEAT_THRESHOLD = 1.98f;
    private static final float EXTREME_COLD_THRESHOLD = 0.07f;

    private static final float EXTREME_HEAT_DAMAGE = 1;
    private static final float EXTREME_COLD_DAMAGE = 1;

    public static void applyTemperatureDamage(ServerPlayerEntity player) {
        float temperature = RefactoredTemperatureManager.playerTemperature;

        boolean extremeHeat = temperature >= EXTREME_HEAT_THRESHOLD;
        boolean extremeCold = temperature <= EXTREME_COLD_THRESHOLD;

        if (extremeHeat) {
            doExtremeHeatDamage(player);
            return;
        }

        if (extremeCold) {
            doExtremeColdDamage(player);
            return;
        }

        isTakingTemperatureDamage = false;
    }

    private static void doExtremeHeatDamage(ServerPlayerEntity player) {

        if (RefactoredTemperatureManager.isInWater(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        if (hasFireProtection(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        if (hasHeatProtectionArmor(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        player.damage(player.getWorld().getDamageSources().onFire(), EXTREME_HEAT_DAMAGE);
        isTakingTemperatureDamage = true;
    }

    private static void doExtremeColdDamage(ServerPlayerEntity player) {

        if (hasFrostProtection(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        if (hasColdProtectionArmor(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        player.damage(player.getWorld().getDamageSources().freeze(), EXTREME_COLD_DAMAGE);
        isTakingTemperatureDamage = true;
    }

    private static boolean hasFireProtection(ServerPlayerEntity player) {
        for (ItemStack armor : player.getArmorItems()) {
            if (EnchantmentHelper.getLevel(Enchantments.FIRE_PROTECTION, armor) > 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasFrostProtection(ServerPlayerEntity player) {
        for (ItemStack armor : player.getArmorItems()) {
            if (EnchantmentHelper.getLevel(ModEnchantments.FROST_PROTECTION, armor) > 0) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasHeatProtectionArmor(ServerPlayerEntity player){
        int count = 0;
        for (ItemStack armor : player.getArmorItems()) {
            if (armor.getItem() instanceof ArmorItem && ((ArmorItem) armor.getItem()).getMaterial() == ArmorMaterials.CHAIN) {
                count++;
            }
        }
        return count >= 3;
    }


    private static boolean hasColdProtectionArmor(ServerPlayerEntity player) {
        int count = 0;
        for (ItemStack armor : player.getArmorItems()) {
            if (armor.getItem() instanceof ArmorItem && ((ArmorItem) armor.getItem()).getMaterial() == ArmorMaterials.LEATHER) {
                count++;
            }
        }
        return count >= 3;
    }


}