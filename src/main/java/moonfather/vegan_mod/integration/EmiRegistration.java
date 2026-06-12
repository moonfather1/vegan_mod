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
import moonfather.vegan_mod.blocks.DataMapManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.registries.BaseMappedRegistry;

import java.util.Map;

//@EmiEntrypoint
public class EmiRegistration //implements dev.emi.emi.api.EmiPlugin
{

//    @Override
//    public void register(EmiRegistry emiRegistry)
//    {
//        // drying rack
//        if (Config.leather_make_on_drying_rack())
//        {
//            if (BuiltInRegistries.ITEM instanceof BaseMappedRegistry bmr)
//            {
//                emiRegistry.addCategory(RACK_RECIPE_CATEGORY);
//                emiRegistry.addWorkstation(RACK_RECIPE_CATEGORY, RACK_RECIPE_WORKSTATION);
//                Map<ResourceKey<Item>, DataMapManager.DryingRecipe> map = bmr.getDataMap(DataMapManager.DRYING_RECIPE);
//                for (Map.Entry<ResourceKey<Item>, DataMapManager.DryingRecipe> entry : map.entrySet())
//                {
//                    Identifier id = Identifier.fromNamespaceAndPath(VeganMod.MODID, "/" + entry.getKey().location().getNamespace() + "_" + entry.getKey().location().getPath());
//                    emiRegistry.addRecipe(new EmiRackRecipe(id, (Item) bmr.get(entry.getKey()), entry.getValue().output().value(), entry.getValue().timeInMinutes()));
//                }
//            }
//        }
//        // feathers
//        int counter = 1;
//        emiRegistry.addRecipe(EmiWorldInteractionRecipe.builder()
//                .leftInput(EmiIngredient.of(Ingredient.of(Items.GRASS_BLOCK)))
//                .rightInput(EmiIngredient.of(Ingredient.of(Items.CLOCK)), true)
//                .output(EmiStack.of(Items.FEATHER))
//                .id(Identifier.fromNamespaceAndPath(VeganMod.MODID, "/shedding_recipe_" + (counter++)))
//                .build());
//    }



    //private static final Identifier DUMMY_SPRITE_LOCATION = Identifier.fromNamespaceAndPath("emi", "textures/gui/widgets.png");  // for the tree-screen. i won't bother using separate icons now.
    //private static final EmiTexture DUMMY_SPRITE = new EmiTexture(DUMMY_SPRITE_LOCATION, 64, 148, 16, 16);  // for the tree-screen. i won't bother using separate icons now.

    //private static final EmiStack RACK_RECIPE_WORKSTATION = EmiStack.of(VeganMod.Blocks.DRYING_RACK.get());
    //public static final EmiRecipeCategory RACK_RECIPE_CATEGORY = new EmiRecipeCategory(Identifier.fromNamespaceAndPath(VeganMod.MODID, "emi_category"), RACK_RECIPE_WORKSTATION, DUMMY_SPRITE);
}
