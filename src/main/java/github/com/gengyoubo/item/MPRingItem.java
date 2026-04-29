package github.com.gengyoubo.item;

import dev.emi.trinkets.api.TrinketItem;

public class MPRingItem extends TrinketItem {
    public MPRingItem() {
        super(new Properties().stacksTo(1).fireResistant());
    }
}
