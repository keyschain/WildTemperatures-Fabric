package site.keyschain.wild_temperature.util;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import static site.keyschain.wild_temperature.WildTemperature.MOD_ID;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> PROTECT_AGAINST_COLD =
                createTag("protect_against_cold");
        public static final TagKey<Item> PROTECT_AGAINST_HEAT =
                createTag("protect_against_heat");
        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(MOD_ID, name));
        }
    }
}
