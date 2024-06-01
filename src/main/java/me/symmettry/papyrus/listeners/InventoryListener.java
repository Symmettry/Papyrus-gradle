package me.symmettry.papyrus.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.symmettry.papyrus.script.data.PapyrusGui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

public final class InventoryListener implements Listener {

    private static final Map<Inventory, BiPredicate<ItemStack, Integer>> unstealableInventories = new HashMap<>(),
                                                                             itemUseInventories = new HashMap<>();
    private static final Map<Inventory, BiPredicate<Component, Integer>> disableChatInventories = new HashMap<>();
    private static final Map<Inventory, Collection<PapyrusGui.PapyrusGuiSlot>>   inventorySlots = new HashMap<>();

    public static void modifyInvUnstealableStates(final Inventory inventory, final BiPredicate<ItemStack, Integer> unstealable) {
        unstealableInventories.put(inventory, unstealable);
    }
    public static void modifyInvItemUseState(final Inventory inventory, final BiPredicate<ItemStack, Integer> itemUse) {
        itemUseInventories.put(inventory, itemUse);
    }
    public static void modifyInvDisableChatState(final Inventory inventory, final BiPredicate<Component, Integer> disableChat) {
        disableChatInventories.put(inventory, disableChat);
    }
    public static void modifyInvSlots(final Inventory inventory, final Collection<PapyrusGui.PapyrusGuiSlot> slots) {
        inventorySlots.put(inventory, slots);
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        if(!unstealableInventories.containsKey(event.getInventory())) return;

        final PapyrusGui.PapyrusGuiSlot slot = inventorySlots.get(event.getInventory()).stream()
                .filter(papSlot -> papSlot.stack().equals(event.getCurrentItem()))
                .findFirst().orElse(null);
        boolean unstealableLocal;
        try {
            unstealableLocal = slot != null && !slot.stealable().test(event.getClick(), event.getRawSlot());
        } catch(final NullPointerException e) { // no response
            unstealableLocal = false;
        } catch(final Exception e) { // everything else
            e.printStackTrace();
            unstealableLocal = false;
        }
        final boolean unstealableGlobal = unstealableInventories.get(event.getInventory()).test(event.getCurrentItem(), event.getRawSlot());

        if(unstealableLocal || unstealableGlobal) event.setCancelled(true);
    }

    @EventHandler
    public void onDrop(final PlayerDropItemEvent event) {
        final Player player = event.getPlayer();
        final Inventory inv = player.getOpenInventory().getTopInventory();
        if(!unstealableInventories.containsKey(inv)) return;

        final boolean cancelled = unstealableInventories.get(inv).test(event.getItemDrop().getItemStack(), -1);
        if(cancelled) event.setCancelled(true);
    }

    @EventHandler
    public void onItemUse(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final Inventory inv = player.getOpenInventory().getTopInventory();
        if(!itemUseInventories.containsKey(inv)) return;

        final boolean cancelled = itemUseInventories.get(inv).test(event.getItem(), player.getInventory().getHeldItemSlot());
        if(cancelled) event.setCancelled(true);
    }

    @EventHandler
    public void onItemConsume(final PlayerItemConsumeEvent event) {
        final Player player = event.getPlayer();
        final Inventory inv = player.getOpenInventory().getTopInventory();
        if(!itemUseInventories.containsKey(inv)) return;

        final boolean cancelled = itemUseInventories.get(inv).test(player.getActiveItem(), player.getInventory().getHeldItemSlot());
        if(cancelled) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityInteract(final PlayerInteractEntityEvent event) {
        final Player player = event.getPlayer();
        final Inventory inv = player.getOpenInventory().getTopInventory();
        if(!itemUseInventories.containsKey(inv)) return;

        final boolean cancelled = itemUseInventories.get(inv).test(player.getActiveItem(), player.getInventory().getHeldItemSlot());
        if(cancelled) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityAttack(final EntityDamageByEntityEvent event) {
        if(!(event.getDamager() instanceof final Player player)) {
            return;
        }

        final Inventory inv = player.getOpenInventory().getTopInventory();
        if(!itemUseInventories.containsKey(inv)) return;

        final boolean cancelled = itemUseInventories.get(inv).test(player.getActiveItem(), player.getInventory().getHeldItemSlot());
        if(cancelled) event.setCancelled(true);
    }

    @EventHandler
    public void onInteractBlock(final HangingBreakByEntityEvent event) {
        if(!(event.getRemover() instanceof final Player player)) {
            return;
        }

        final Inventory inv = player.getOpenInventory().getTopInventory();
        if(!itemUseInventories.containsKey(inv)) return;

        final boolean cancelled = itemUseInventories.get(inv).test(player.getActiveItem(), player.getInventory().getHeldItemSlot());
        if(cancelled) event.setCancelled(true);
    }

    @EventHandler
    public void onChat(final AsyncChatEvent event) {
        final Player player = event.getPlayer();
        final Inventory inv = player.getOpenInventory().getTopInventory();
        if(!disableChatInventories.containsKey(inv)) return;

        final boolean cancelled = disableChatInventories.get(inv).test(event.message(), 0); // 0 for message
        if(cancelled) event.setCancelled(true);
    }

    @EventHandler
    public void onCommand(final PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final Inventory inv = player.getOpenInventory().getTopInventory();
        if(!disableChatInventories.containsKey(inv)) return;

        final boolean cancelled = disableChatInventories.get(inv).test(Component.text(event.getMessage()), 1); // 1 for command
        if(cancelled) event.setCancelled(true);
    }

}