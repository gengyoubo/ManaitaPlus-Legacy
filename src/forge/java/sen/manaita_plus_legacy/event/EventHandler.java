package sen.manaita_plus_legacy.event;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;
import sen.manaita_plus_legacy.ManaitaPlusLegacy;
import sen.manaita_plus_legacy.ManaitaPlusLegacyConfig;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyBlockCore;
import sen.manaita_plus_legacy.core.ManaitaPlusLegacyItemCore;
import sen.manaita_plus_legacy.item.data.IManaitaPlusLegacyDoubling;
import sen.manaita_plus_legacy.item.data.IManaitaPlusLegacyKey;
import sen.manaita_plus_legacy.trades.ManaitaPlusLegacyBowVillagerTrade;
import sen.manaita_plus_legacy.trades.ManaitaPlusLegacySwordGodVillagerTrade;
import sen.manaita_plus_legacy.util.ManaitaPlusLegacyEntityData;
import sen.manaita_plus_legacy.util.ManaitaPlusUtils;

import java.util.Iterator;
import java.util.List;

@Mod.EventBusSubscriber(modid = ManaitaPlusLegacy.MODID)
public class EventHandler {
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        if (item instanceof IManaitaPlusLegacyKey) {
            List<Component> toolTip = event.getToolTip();
            Iterator<Component> iterator = toolTip.iterator();
            while (iterator.hasNext()) {
                Component component = iterator.next();
                if (component instanceof MutableComponent mutableComponent) {
                    ComponentContents contents = mutableComponent.getContents();
                    if (contents instanceof TranslatableContents translatableContents) {
                        if (translatableContents.getKey().startsWith("item.modifiers.")) {
                            while (iterator.hasNext()) {
                                iterator.next();
                                iterator.remove();
                            }
                            break;
                        }
                    }
                }
            }
            if (!toolTip.isEmpty()) {
                toolTip.remove(toolTip.size() - 1);
            }
            if (!toolTip.isEmpty()) {
                toolTip.remove(toolTip.size() - 1);
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getAction() != PlayerInteractEvent.LeftClickBlock.Action.START) return;
        Player player = event.getEntity();
        ManaitaPlusUtils.destroyBlocks(player.getMainHandItem(), event.getLevel(), event.getPos(), player);
    }

    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        ManaitaPlusLegacyEntityData.death.remove(event.getEntity());
        ManaitaPlusLegacyEntityData.remove.remove(event.getEntity());
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        Player player;
        if (event.getSource().getEntity() instanceof Player) {
            player = (Player) event.getSource().getEntity();
        } else {
            LivingEntity killCredit = event.getEntity().getKillCredit();
            if (killCredit instanceof Player) {
                player = (Player) killCredit;
            } else {
                return;
            }
        }
        ItemStack mainHandItem = player.getMainHandItem();
        if (mainHandItem.getItem() instanceof IManaitaPlusLegacyDoubling doublingItem) {
            if (doublingItem.isDoubling(mainHandItem)) {
                int magnification = ManaitaPlusLegacyConfig.item_drops_doubling_value;
                for (ItemEntity drop : event.getDrops()) {
                    ItemStack dropStack = drop.getItem().copy();
                    dropStack.setCount(dropStack.getCount() * magnification);
                    ItemHandlerHelper.giveItemToPlayer(player, dropStack);
                }
            } else {
                for (ItemEntity drop : event.getDrops()) {
                    ItemHandlerHelper.giveItemToPlayer(player, drop.getItem().copy());
                }
            }
            event.getDrops().clear();
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingExperienceDrop(LivingExperienceDropEvent event) {
        Player attackingPlayer = event.getAttackingPlayer();
        if (attackingPlayer == null) return;

        ItemStack mainHandItem = attackingPlayer.getMainHandItem();
        if (mainHandItem.getItem() instanceof IManaitaPlusLegacyDoubling doublingItem) {
            if (doublingItem.isDoubling(mainHandItem)) {
                attackingPlayer.giveExperiencePoints(event.getDroppedExperience() * ManaitaPlusLegacyConfig.experience_drops_doubling_value);
            } else {
                attackingPlayer.giveExperiencePoints(event.getDroppedExperience());
            }
            event.setDroppedExperience(0);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingFall(LivingFallEvent event) {
        if (!(event.getEntity() instanceof Player player) || !hasManaitaProtectionForFall(player)) return;
        event.setCanceled(true);
        resetPlayerDamageState(player);
    }

    @SubscribeEvent
    public static void onLivingAttack(LivingAttackEvent event) {
        if (!(event.getEntity() instanceof Player player) || hasManaitaProtection(player)) return;
        event.setCanceled(true);
        resetPlayerDamageState(player);
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player) || hasManaitaProtection(player)) return;
        event.setCanceled(true);
        resetPlayerDamageState(player);
    }

    private static boolean hasManaitaProtection(Player player) {
        return !ManaitaPlusUtils.isManaitaArmor(player) && !ManaitaPlusUtils.isManaita(player);
    }

    private static boolean hasManaitaProtectionForFall(Player player) {
        return ManaitaPlusUtils.isManaitaArmorPart(player) || ManaitaPlusUtils.isManaita(player);
    }

    private static void resetPlayerDamageState(Player player) {
        player.setHealth(player.getMaxHealth());
        player.fallDistance = 0;
        player.hurtTime = 0;
        player.deathTime = 0;
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getEntity() instanceof Player player) || hasManaitaProtection(player)) return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.WEAPONSMITH) {
            List<VillagerTrades.ItemListing> tradesTier = event.getTrades().get(5);

            tradesTier.add(new ManaitaPlusLegacyBowVillagerTrade(
                    new ItemStack(ManaitaPlusLegacyBlockCore.CraftingBlockItem.get(), 64),
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBow.get(), 1),
                    1, 0, 1));
            tradesTier.add(new ManaitaPlusLegacyBowVillagerTrade(
                    new ItemStack(ManaitaPlusLegacyBlockCore.FurnaceBlockItem.get(), 64),
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBow.get(), 1),
                    1, 0, 1));
            tradesTier.add(new ManaitaPlusLegacyBowVillagerTrade(
                    new ItemStack(ManaitaPlusLegacyBlockCore.BrewingBlock.get(), 64),
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBow.get(), 1),
                    1, 0, 1));

            tradesTier.add(new ManaitaPlusLegacySwordGodVillagerTrade(
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaBow.get(), 1),
                    new ItemStack(ManaitaPlusLegacyItemCore.ManaitaSwordGod.get(), 1),
                    1, 0, 1));
        }
    }
}
