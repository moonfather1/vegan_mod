package moonfather.vegan_mod;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class OptionsCommon
{
    public static double leather_multiplier()
    {
        return 0.51d;
    }
    public static boolean leather_make_on_crafting_table()
    {
        return true;
    }
    public static boolean leather_make_on_drying_rack()
    {
        return ! leather_make_on_crafting_table();
    }
    public static boolean ink_accepts_blue_dye()
    {
        return false;
    }
    public static boolean ink_simple_recipe()
    {
        return false;
    }

    ///////////////////

    public static boolean doesEntityShed(Entity entity)
    {
        return entity instanceof Chicken || entity instanceof Armadillo;
    }
    public static Item getEntityShedItem(Entity entity)
    {
        return entity instanceof Chicken ? Items.FEATHER :
                entity instanceof Armadillo ? Items.ARMADILLO_SCUTE :
                Items.AIR;
    }
    public static int getEntityShedIntervalInSeconds(Entity entity)
    {
        return entity instanceof Chicken ? 15*60 :  // 15 min = 4 in 3 days
                entity instanceof Armadillo ? 25*60 :  // 25 min = 4 in 5 days
                        308*3600;
    }
}
