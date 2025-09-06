package moonfather.vegan_mod;

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
        return true;
    }
}
