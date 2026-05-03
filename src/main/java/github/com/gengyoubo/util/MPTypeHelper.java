package github.com.gengyoubo.util;

public final class MPTypeHelper {
    private MPTypeHelper() {
    }

    public static String getTypes(int i) {
        if (i == 1) return "wooden.";
        if (i == 2) return "stone.";
        if (i == 3) return "iron.";
        if (i == 4) return "gold.";
        if (i == 5) return "diamond.";
        if (i == 6) return "emerald.";
        if (i == 7) return "redstone.";
        if (i == 8) return "netherite.";
        return "";
    }
}

