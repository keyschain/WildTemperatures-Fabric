package site.keyschain.wild_temperature.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import site.keyschain.wild_temperature.enchantment.FrostProtectionEnchantment;

public class ModEnchantments {

    public static Enchantment FROST_PROTECTION = register("frost_protection", new FrostProtectionEnchantment());
    public static final String MOD_ID = "wild_temperature";

    private static Enchantment register(String name, Enchantment enchantment) {
        return Registry.register(Registries.ENCHANTMENT, new Identifier(MOD_ID, name), enchantment);
    }

    public static void registerModEnchantments() {
        System.out.println("Registering Enchantments for " + MOD_ID);
    }
}