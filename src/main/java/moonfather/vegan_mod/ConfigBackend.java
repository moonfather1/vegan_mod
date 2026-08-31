package moonfather.vegan_mod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ConfigBackend
{
    public static class Options
    {
        public double leather_multiplier_value;
        public boolean leather_make_on_crafting_table_value;
        public String leather_armor_color_value;
        public boolean ink_accepts_blue_dye_value;
        public String shedding_value;
        public int feather_litter_decay_target_value;
        public boolean feather_litter_enabled_value;
    }
    private static class OptionsWithComments extends Options
    {
        public String leather_multiplier_info;
        public String leather_make_on_crafting_table_info;
        public String leather_armor_color_info;
        public String ink_accepts_blue_dye_info;
        public String shedding_info;
        public String feather_litter_decay_target_info;
        public String feather_litter_enabled_info;

        private OptionsWithComments() {}
        private static OptionsWithComments create()
        {
            OptionsWithComments defaults = new OptionsWithComments();
            defaults.leather_multiplier_info = "How many leather pieces to recover for each input piece. Default is 0.5 meaning 4 pieces recovered from undamaged leather tunic, 2 from boots. Item damage will scale that down, this is a base factor for undamaged item.";
            defaults.leather_multiplier_value = 0.51d;
            defaults.leather_make_on_crafting_table_info = "Do we make the leather on crafting table or on a drying rack? Later takes the same materials plus it takes some time.";
            defaults.leather_make_on_crafting_table_value = true;
            defaults.leather_armor_color_info = "What color is the leather armor made from plant materials? You can prefix with # or 0x if you want to use hex numbers.";
            defaults.leather_armor_color_value = "#ABAD65";

            defaults.ink_accepts_blue_dye_info  = "Optionally you can have the ink recipe accept blue dye.";
            defaults.ink_accepts_blue_dye_value = false;

            defaults.shedding_info = "What animals shed feathers or scales? And approximately how often (in seconds; so 900 (15*60) means roughly every 15 minutes (4 times every 3 game-days). Format of single entry is entity=item=time; no quotes; you can have any number of these entries, separate them with commas and optionally spaces after commas.";
            defaults.shedding_value = "minecraft:chicken=minecraft:feather=900, minecraft:armadillo=minecraft:armadillo_scute=1800";

            defaults.feather_litter_enabled_info = "Is feather litter enabled? If yes, feathers on the ground became small piles (that don't tick all the time like dropped items. If not enabled, feathers wait on the ground as normal.";
            defaults.feather_litter_enabled_value = false;
            defaults.feather_litter_decay_target_info = "How long on average does feather litter last? Default 6 means 1/6 chance every minute for one leaf in litter to decay. That makes them last somewhat longer than normal but with no ticks.";
            defaults.feather_litter_decay_target_value = 6;

            return defaults;
        }
    }
    ////////////////////////////////

    public Options getModOptions()
    {
        if (this.instance != null)
        {
            return this.instance;
        }
        Path configPath = FabricLoader.getInstance().getConfigDir().resolve("i-dont-want-to-kill-cows.json");
        if (configPath.toFile().exists())
        {
            try
            {
                Gson gson = new Gson();
                this.instance = gson.fromJson(Files.readString(configPath), Options.class);
            }
            catch (IOException ignored)
            {
            }
        }
        boolean hasComments = false;
        if (this.instance == null)
        {
            this.instance = OptionsWithComments.create();
            hasComments = true;
        }
        if (! configPath.toFile().exists())
        {
            try
            {
                Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
                String text = gson.toJson(this.instance, hasComments ? OptionsWithComments.class : Options.class);
                Files.writeString(configPath, text, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }
            catch (IOException ignored)
            {
            }
        }
        return this.instance;
    }
    private Options instance = null;
}
