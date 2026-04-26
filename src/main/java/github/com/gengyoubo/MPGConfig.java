package github.com.gengyoubo;

public final class MPGConfig {
    public static boolean creative_range_destroy_value;
    public static boolean easy_mode_value;
    public static int item_drops_doubling_value;
    public static int experience_drops_doubling_value;
    public static int crafting_doubling_value;
    public static int furnace_doubling_value;
    public static int brewing_doubling_value;
    public static int destroy_doubling_value;
    public static int source_doubling_value;

    private MPGConfig() {
    }

    public static void initDefaults() {
        creative_range_destroy_value = false;
        easy_mode_value = false;
        item_drops_doubling_value = 4;
        experience_drops_doubling_value = 4;
        crafting_doubling_value = 64;
        furnace_doubling_value = 64;
        brewing_doubling_value = 64;
        destroy_doubling_value = 4;
        source_doubling_value = 64;
    }
}
