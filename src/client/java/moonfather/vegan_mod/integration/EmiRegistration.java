package moonfather.vegan_mod.integration;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.recipe.EmiWorldInteractionRecipe;
import dev.emi.emi.api.render.EmiTexture;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import moonfather.vegan_mod.Config;
import moonfather.vegan_mod.VeganMod;
import moonfather.vegan_mod.blocks.DryingRecipeManager;
import moonfather.vegan_mod.blocks.KilnBlockEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;

@EmiEntrypoint
public class EmiRegistration implements dev.emi.emi.api.EmiPlugin
{

    @Override
    public void register(EmiRegistry emiRegistry)
    {
        // drying rack
        if (Config.leather_make_on_drying_rack())
        {
            emiRegistry.addCategory(RACK_RECIPE_CATEGORY);
            emiRegistry.addWorkstation(RACK_RECIPE_CATEGORY, RACK_RECIPE_WORKSTATION);
            for (var entry  : DryingRecipeManager.getAll().entrySet())
            {
                emiRegistry.addRecipe(new EmiRackRecipe(entry.getKey(), entry.getValue().getBaseItem(), entry.getValue().getResult(), entry.getValue().getTimeInMinutes()));
            }
        }
        // feathers
        int counter = 1;
        emiRegistry.addRecipe(EmiWorldInteractionRecipe.builder()
                .leftInput(EmiIngredient.of(Ingredient.of(Items.GRASS_BLOCK)))
                .rightInput(EmiIngredient.of(Ingredient.of(Items.CLOCK)), true)
                .output(EmiStack.of(Items.FEATHER))
                .id(ResourceLocation.fromNamespaceAndPath(VeganMod.MOD_ID, "/shedding_recipe_" + (counter++)))
                .build());
        // kiln
        if (Config.kiln_enabled())
        {
            ItemStack bottle = VeganMod.Items.THICK_OIL.getDefaultInstance();
            bottle.set(DataComponents.ITEM_NAME, Component.translatable("item.vegan_mod.thick_oil2"));
            ItemStack tar = new ItemStack(Items.BLACK_DYE);
            tar.set(DataComponents.ITEM_NAME, Component.translatable("item.vegan_mod.black_paint"));
            emiRegistry.addCategory(KILN_RECIPE_CATEGORY);
            emiRegistry.addWorkstation(KILN_RECIPE_CATEGORY, KILN_RECIPE_WORKSTATION);
            ResourceLocation id = ResourceLocation.fromNamespaceAndPath(VeganMod.MOD_ID, "/kiln_rec_1");
            ArrayList<ItemStack> list = new ArrayList<>(3);  list.add(bottle);    if (Config.kiln_gives_tar_paint()) { list.add(tar); }
            int time = (int) (KilnBlockEntity.BASE_TIME_IN_SECONDS * Config.kiln_time_multiplier() / 60);
            emiRegistry.addRecipe(new EmiKilnRecipe(id, ItemTags.LOGS_THAT_BURN, Items.CHARCOAL, list, time));
        }
    }



    private static final ResourceLocation DUMMY_SPRITE_LOCATION = ResourceLocation.fromNamespaceAndPath("emi", "textures/gui/widgets.png");  // for the tree-screen. i won't bother using separate icons now.
    private static final EmiTexture DUMMY_SPRITE = new EmiTexture(DUMMY_SPRITE_LOCATION, 64, 148, 16, 16);  // for the tree-screen. i won't bother using separate icons now.

    private static final EmiStack RACK_RECIPE_WORKSTATION = EmiStack.of(VeganMod.Blocks.DRYING_RACK_BLOCK);
    public static final EmiRecipeCategory RACK_RECIPE_CATEGORY = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(VeganMod.MOD_ID, "emi_category1"), RACK_RECIPE_WORKSTATION, DUMMY_SPRITE);

    private static final EmiStack KILN_RECIPE_WORKSTATION = EmiStack.of(VeganMod.Blocks.KILN_ITEM);
    public static final EmiRecipeCategory KILN_RECIPE_CATEGORY = new EmiRecipeCategory(ResourceLocation.fromNamespaceAndPath(VeganMod.MOD_ID, "emi_category2"), KILN_RECIPE_WORKSTATION, DUMMY_SPRITE);
}
