package moonfather.vegan_mod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.Map;

public class OptionsCommon
{
    public static double leather_multiplier()
    {
        double result = backend.getModOptions().leather_multiplier_value;
        result = Math.max(result, 0.1);
        result = Math.min(result, 1.0);
        return result;
    }
    public static boolean leather_make_on_crafting_table() { return backend.getModOptions().leather_make_on_crafting_table_value; }
    public static boolean leather_make_on_drying_rack()
    {
        return ! leather_make_on_crafting_table();
    }
    public static String leather_armor_color() { return  backend.getModOptions().leather_armor_color_value; }
    public static boolean ink_accepts_blue_dye()
    {
        return backend.getModOptions().ink_accepts_blue_dye_value;
    }
    public static String shedding() { return  backend.getModOptions().shedding_value; }
    public static boolean feather_litter_enabled() { return backend.getModOptions().feather_litter_enabled_value; }
    public static int feather_litter_decay_target()
    {
        int result = backend.getModOptions().feather_litter_decay_target_value;
        result = Math.max(result, 1);
        result = Math.min(result, 25);
        return result;
    }



    ///////////////////

    public static boolean doesEntityShed(Entity entity)
    {
        initializeSheddingIfNeeded();
        if (entity instanceof ItemEntity) { return false; }
        return sheddingResults.containsKey(entity.getType());
    }
    public static Item getEntityShedItem(Entity entity)
    {
        initializeSheddingIfNeeded();
        return sheddingResults.get(entity.getType());
    }
    public static int getEntityShedIntervalInSeconds(Entity entity)
    {
        initializeSheddingIfNeeded();
        return sheddingTime.get(entity.getType());
    }
    private static void initializeSheddingIfNeeded()
    {
        if (sheddingInitialized) { return; }
        sheddingInitialized = true;
        String[] entries = backend.getModOptions().shedding_value.split(",\\s*");
        for (String entry : entries)
        {
            String[] parts = entry.split("\\s*=\\s*");
            if (parts.length != 3)
            {
                VeganMod.LOGGER.warn("Invalid entry in config file of mod \"I don't want to kill cows\". (bad format)");
                continue;
            }
            if (! BuiltInRegistries.ENTITY_TYPE.containsKey(ResourceLocation.parse(parts[0])))
            {
                VeganMod.LOGGER.warn("Invalid entry in config file of mod \"I don't want to kill cows\". (entity not present in game)");
                continue;
            }
            if (! BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(parts[1])))
            {
                VeganMod.LOGGER.warn("Invalid entry in config file of mod \"I don't want to kill cows\". (item not present in game)");
                continue;
            }
            EntityType<?> key = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(parts[0]));
            sheddingResults.put(key, BuiltInRegistries.ITEM.get(ResourceLocation.parse(parts[1])));
            sheddingTime.put(key, parseInt(parts[2], 900));
        }
    }
    private static final Map<EntityType<?>, Item> sheddingResults = new HashMap<>();
    private static final Map<EntityType<?>, Integer> sheddingTime = new HashMap<>();
    private static boolean sheddingInitialized = false;
    private static int parseInt(String input, int def) { try { return Integer.parseInt(input); } catch (NumberFormatException ex) { return def; } }

    ///////////////////////////////////////

    private static ConfigBackend backend = null;
    public static void initialize() { backend = new ConfigBackend();  sheddingInitialized = false; }
}
