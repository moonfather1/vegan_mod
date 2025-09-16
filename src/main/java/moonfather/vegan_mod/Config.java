package moonfather.vegan_mod;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.DoubleValue _leather_multiplier = BUILDER
            .comment("How much leather to recover from undamaged armor? 0.5 means half (4 leather from undamaged tunic (8 was the input), 2 from 50% durability tunic.")
            .defineInRange("leather_multiplier", 0.51d, 0.2d, 1.0d);

    private static final ModConfigSpec.BooleanValue _leather_make_on_crafting_table = BUILDER
            .comment("Can you make our hardened fabric (leather substitute) on crafting table? If false you need drying rack (same thing but needs some time to be made).")
            .define("leather_make_on_crafting_table", true);

    private static final ModConfigSpec.ConfigValue<String> _leather_armor_color = BUILDER
            .comment("What color is the leather armor made from plant materials? You cah prefix with # or 0x if you want to use hex numbers.")
            .define("leather_armor_color", "#ABAD65");

    private static final ModConfigSpec.BooleanValue _ink_accepts_blue_dye = BUILDER
            .comment("Does ink recipe accept blue dye? If false, it needs to be black dye.")
            .define("ink_accepts_blue_dye", false);

    static final ModConfigSpec SPEC = BUILDER.build();

    //-----------------------------------------//

    public static double leather_multiplier()
    {
        return _leather_multiplier.get();
    }
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
}
