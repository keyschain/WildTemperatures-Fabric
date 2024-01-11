package com.pikachurro.wild_temperature;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

public class TemperatureManager {
    private static final Map<ServerPlayerEntity, Float> playerTemperatures = new HashMap<>();
    public static final String MOD_ID = "wild_temperature";
    private static final float EXTREME_HEAT_THRESHOLD = 2.0f;
    private static final float EXTREME_COLD_THRESHOLD = 0.05f;
    private static final float EXTREME_HEAT_DAMAGE = 1;
    private static final float EXTREME_COLD_DAMAGE = 1;

    public static boolean isTakingTemperatureDamage = false;

    public static void setTemperature(ServerPlayerEntity player, float temperature) {
        playerTemperatures.put(player, temperature);

        // send an update packet to the client for the specific player
        TemperatureUpdatePacket.send(player, temperature);

    }

    public static float getTemperature(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity) {
            return playerTemperatures.getOrDefault((ServerPlayerEntity) player, 0.0f);
        }
        return 0.0f; // return a default value if the player is not a ServerPlayerEntity
    }

    public static void applyTemperatureDamage(ServerPlayerEntity player) {
        float temperature = getTemperature(player);

        System.out.println("Player temperature: " + temperature);


        if (temperature >= EXTREME_HEAT_THRESHOLD) {
            System.out.println("Player is experiencing extreme heat");
            if(hasFireProtection(player)) {
                System.out.println("Player has fire protection");
                isTakingTemperatureDamage = false;
                return;
            }


            if(hasHeatProtectionArmor(player) == false){
                System.out.println("Applying extreme heat damage to player");
                player.damage(player.getWorld().getDamageSources().onFire(), EXTREME_HEAT_DAMAGE);

                isTakingTemperatureDamage = true;
            } else {
                isTakingTemperatureDamage = false;
            }
        }


        if (temperature <= EXTREME_COLD_THRESHOLD) {
            System.out.println("Player is experiencing extreme cold");

            if(hasFrostProtection(player)) {
                System.out.println("Player has FROST protection");
                isTakingTemperatureDamage = false;
                return;
            }

            if(hasColdProtectionArmor(player) == false){
                System.out.println("Applying extreme cold damage to player");
                player.damage(player.getWorld().getDamageSources().freeze(), EXTREME_COLD_DAMAGE);

                isTakingTemperatureDamage = true;
            } else {
                isTakingTemperatureDamage = false;
            }
        }


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
}
