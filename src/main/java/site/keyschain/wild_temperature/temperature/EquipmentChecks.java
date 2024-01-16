package site.keyschain.wild_temperature.temperature;

import site.keyschain.wild_temperature.enchantment.ModEnchantments;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;

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

    static boolean hasHeatProtectionArmor(ServerPlayerEntity player){
        int count = 0;
        for (ItemStack armor : player.getArmorItems()) {
            if (armor.getItem() instanceof ArmorItem && ((ArmorItem) armor.getItem()).getMaterial() == ArmorMaterials.CHAIN) {
                count++;
            }
        }
        return count >= 3;
    }


    static boolean hasColdProtectionArmor(ServerPlayerEntity player) {
        int count = 0;
        for (ItemStack armor : player.getArmorItems()) {
            if (armor.getItem() instanceof ArmorItem && ((ArmorItem) armor.getItem()).getMaterial() == ArmorMaterials.LEATHER) {
                count++;
            }
        }
        return count >= 3;
    }

    static boolean hasFireResistance(ServerPlayerEntity player) {
        return player.hasStatusEffect(StatusEffects.FIRE_RESISTANCE);
    }
}
