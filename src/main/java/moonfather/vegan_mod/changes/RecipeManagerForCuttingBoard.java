
package moonfather.vegan_mod.changes;

import moonfather.vegan_mod.items.CharcoalReplacementRecipe;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeManagerForCuttingBoard extends RecipeReplacerForTheWholeThing
{
//    public static void joined(ServerPlayer serverPlayer, boolean joined)
//    {
//        if (serverPlayer == null || serverPlayer.getServer() == null) { return; }
//        if (! FabricLoader.getInstance().isModLoaded("farmersdelight")) { return; }
//        Collection<RecipeHolder<?>> all = serverPlayer.getServer().getRecipeManager().getRecipes();
//        List<RecipeHolder<?>> newList = new ArrayList<>(all.size());
////        for (RecipeHolder<?> recipe : all)
////        {
////            if (recipe.id().getNamespace().equals(("farmersdelight")))
////            {
////                if (recipe.id().getPath().equals("cutting/leather_boots")
////                    || recipe.id().getPath().equals("cutting/leather_chestplate")
////                    || recipe.id().getPath().equals("cutting/leather_helmet")
////                    || recipe.id().getPath().equals("cutting/leather_horse_armor")
////                    || recipe.id().getPath().equals("cutting/leather_leggings") )
////                {
////                    continue;
////                }
////            }
////            newList.add(recipe);
////        }
////        serverPlayer.getServer().getRecipeManager().replaceRecipes(newList);
//        int remaining = 5;
//        for (RecipeHolder<?> recipe : all)
//        {
//            if (recipe.id().getNamespace().equals(("farmersdelight")))
//            {
//                if (recipe.id().getPath().equals("cutting/leather_boots")
//                    || recipe.id().getPath().equals("cutting/leather_chestplate")
//                    || recipe.id().getPath().equals("cutting/leather_helmet")
//                    || recipe.id().getPath().equals("cutting/leather_horse_armor")
//                    || recipe.id().getPath().equals("cutting/leather_leggings") )
//                {
//                    var accessor = (RecipeHolderAccessor<Recipe<?>>) (Object) recipe;
//                    accessor.setValue(new CharcoalReplacementRecipe());  // could make a new type but whatever
//                    remaining -= 1;
//                    if (remaining == 0)
//                    {
//                        break;
//                    }
//                }
//            }
//        }
//    }

    @Override
    protected Recipe<?> replacement()
    {
        return new CharcoalReplacementRecipe();  // could make a new type but whatever
    }

    @Override
    protected boolean shouldReplace(Identifier identifier, Recipe<?> value)
    {
        if (this.remaining == 0) { return false; }
        if (identifier.getNamespace().equals(("farmersdelight")))
        {
            if (identifier.getPath().equals("cutting/leather_boots")
                    || identifier.getPath().equals("cutting/leather_chestplate")
                    || identifier.getPath().equals("cutting/leather_helmet")
                    || identifier.getPath().equals("cutting/leather_horse_armor")
                    || identifier.getPath().equals("cutting/leather_leggings"))
            {
                this.remaining -= 1;
                return true;
            }
        }
        return false;
    }

    @Override
    protected void initialize()
    {
        this.remaining = 5;
    }

    private int remaining = 0;
}
