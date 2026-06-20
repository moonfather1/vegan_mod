package moonfather.vegan_mod;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue _leather_multiplier = BUILDER
            .comment("How much leather to recover from undamaged armor? 0.5 means half (4 leather from undamaged tunic (8 was the input), 2 from 50% durability tunic).")
            .defineInRange("leather_multiplier", 0.51d, 0.2d, 1.0d);

    private static final ModConfigSpec.BooleanValue _leather_make_on_crafting_table = BUILDER
            .comment("Can you make our hardened fabric (leather substitute) on crafting table? If false you need drying rack (same thing but needs some time to be made).")
            .define("leather_make_on_crafting_table", true);

    private static final ModConfigSpec.ConfigValue<String> _leather_armor_color = BUILDER
            .comment("What color is the leather armor made from plant materials? You can prefix with # or 0x if you want to use hex numbers.")
            .define("leather_armor_color", "#ABAD65");

    private static final ModConfigSpec.BooleanValue _ink_accepts_blue_dye = BUILDER
            .comment("Does ink recipe accept blue dye? If false, it needs to be black dye.")
            .define("ink_accepts_blue_dye", false);

    private static final ModConfigSpec.BooleanValue _oil_bucket_always_craftable = BUILDER
            .comment("Normally, oil bucket (and fluid in-world) is accessible if Immersive Engineering or Create are present. Otherwise, you just have bottles. This setting can force the buckets craftable regardless of industry mods.")
            .define("oil_bucket_always_craftable", false);

    private static final ModConfigSpec.ConfigValue<String> _shedding = BUILDER
            .comment("What animals shed feathers or scales? And approximately how often (in seconds; so 900 (15*60) means roughly every 15 minutes (4 times every 3 game-days). Format of single entry is entity=item=time; no quotes; you can have any number of these entries, separate them with commas and optionally spaces after commas.")
            .define("shedding", "minecraft:chicken=minecraft:feather=720, minecraft:armadillo=minecraft:armadillo_scute=2100,  twilightforest:raven=twilightforest:raven_feather=60");

    private static final ModConfigSpec.IntValue _feather_litter_decay_target = BUILDER
            .comment("How long on average does feather litter last? Default 6 means 1/6 chance every minute for one leaf in litter to decay. That makes them last somewhat longer than normal but with no ticks.")
            .defineInRange("feather_litter_decay_target", 6, 1, 20);

    private static final ModConfigSpec.BooleanValue _feather_litter_enabled = BUILDER
            .comment("Is feather litter enabled? If yes, feathers on the ground became small piles (that don't tick all the time like dropped items. If not enabled, feathers wait on the ground as normal.")
            .define("feather_litter_enabled", false);

    static final ModConfigSpec SPEC = BUILDER.build();

    //-----------------------------------------//

    public static boolean leather_make_on_crafting_table()
    {
        return _leather_make_on_crafting_table.get();
    }
    public static boolean leather_make_on_drying_rack()
    {
        return ! leather_make_on_crafting_table();
    }
    public static String leather_armor_color()
    {
        return _leather_armor_color.get();
    }
    public static boolean ink_accepts_blue_dye()
    {
        return _ink_accepts_blue_dye.get();
    }
    public static boolean oil_bucket_always_craftable()
    {
        return _oil_bucket_always_craftable.get();
    }
    public static double leather_multiplier()
    {
        return _leather_multiplier.get();
    }
    public static int litterDecayTarget()
    {
        return _feather_litter_decay_target.get();
    }
    public static boolean litterEnabled()
    {
        return _feather_litter_enabled.get();
    }

    //-----------------------------------------//

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
        String[] entries = _shedding.get().split(",\\s*");
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
}
