package com.pikachurro.wild_temperature;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

import static com.pikachurro.wild_temperature.WildTemperature.CONFIG;

public class TemperatureDamageManager {
    public static boolean isTakingTemperatureDamage = false;
    public static float EXTREME_HEAT_THRESHOLD = CONFIG.EXTREME_HEAT_THRESHOLD();
    public static final float EXTREME_COLD_THRESHOLD = CONFIG.EXTREME_COLD_THRESHOLD();
    public static final float EXTREME_HEAT_DAMAGE = CONFIG.EXTREME_HEAT_DAMAGE();
    public static final float EXTREME_COLD_DAMAGE = CONFIG.EXTREME_COLD_DAMAGE();

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

        // is in water
        if (RefactoredTemperatureManager.isInWater(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        // enchanted armor with fire protection
        if (hasFireProtection(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        // armor like chainmail
        if (hasHeatProtectionArmor(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        // potion of fire resistance
        if (hasFireResistance(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        player.damage(player.getWorld().getDamageSources().onFire(), EXTREME_HEAT_DAMAGE);
        isTakingTemperatureDamage = true;
    }

    private static void doExtremeColdDamage(ServerPlayerEntity player) {

        // enchanted armor with frost protection
        if (hasFrostProtection(player)) {
            isTakingTemperatureDamage = false;
            return;
        }

        // armor like leather
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

    private static boolean hasFireResistance(ServerPlayerEntity player) {
        return player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
    }

}