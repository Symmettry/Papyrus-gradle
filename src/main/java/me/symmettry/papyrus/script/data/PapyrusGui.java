package me.symmettry.papyrus.script.data;

import me.symmettry.papyrus.listeners.InventoryListener;
import me.symmettry.papyrus.util.misc.PredUtil;
import me.symmettry.papyrus.util.text.ComponentFormatter;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;

@SuppressWarnings("unused")
public final class PapyrusGui {

    /**
     * The number of rows in the inventory.
     * Default value is 6 (double chest).
     */
    private int rows = 6;

    /**
     * Whether to determine if items in the gui can be stolen, and if the player should be able to use items while in the gui.
     */
    private BiPredicate<ItemStack, Integer> unstealable = PredUtil::falseBiPred, blockItemUse = PredUtil::trueBiPred; // item and slot
    private BiPredicate<Component, Integer> disableChat = PredUtil::trueBiPred; // text and type (0 = chat, 1 = command)

    /**
     * The owner of the inventory.
     * Default value is null, and typically should be null unless you know otherwise.
     */
    private InventoryHolder owner = null;

    /**
     * The type of the inventory.
     * Default value is {@link InventoryType#CHEST}.
     */
    private InventoryType type = InventoryType.CHEST;

    /**
     * The title of the inventory.
     */
    private final Component title;

    private final Map<Integer, PapyrusGuiSlot> slots = new HashMap<>();

    public PapyrusGui(final String title) {
        this.title = ComponentFormatter.formatString(title);
    }

    public PapyrusGui rows(final int rows) {
        this.rows = rows;
        return this;
    }

    public PapyrusGui unstealable(final BiPredicate<ItemStack, Integer> unstealable) {
        this.unstealable = unstealable;
        return this;
    }
    public PapyrusGui unstealable(final boolean unstealable) {
        return this.unstealable(PredUtil.biPred(unstealable));
    }
    public PapyrusGui unstealable() {
        return this.unstealable(PredUtil::trueBiPred);
    }

    public PapyrusGui blockItemUse(final BiPredicate<ItemStack, Integer> blockItemUse) {
        this.blockItemUse = blockItemUse;
        return this;
    }
    public PapyrusGui blockItemUse(final boolean blockItemUse) {
        return this.blockItemUse(PredUtil.biPred(blockItemUse));
    }
    public PapyrusGui blockItemUse() {
        return this.blockItemUse(PredUtil::trueBiPred);
    }

    public PapyrusGui disableChat(final BiPredicate<Component, Integer> disableChat) {
        this.disableChat = disableChat;
        return this;
    }
    public PapyrusGui disableChat(final boolean disableChat) {
        return this.disableChat(PredUtil.biPred(disableChat));
    }
    public PapyrusGui disableChat() {
        return this.disableChat(PredUtil::trueBiPred);
    }

    public PapyrusGui owner(final InventoryHolder owner) {
        this.owner = owner;
        return this;
    }

    public PapyrusGui type(final InventoryType type) {
        this.type = type;
        return this;
    }

    public PapyrusGui setSlot(final int slot, final ItemStack stack, final BiPredicate<ClickType, Integer> stealable) {
        this.slots.put(slot, new PapyrusGuiSlot(stack, stealable));
        return this;
    }
    public PapyrusGui setSlot(final int slot, final ItemStack stack, final boolean stealable) {
        return this.setSlot(slot, stack, PredUtil.biPred(stealable));
    }
    public PapyrusGui setSlot(final int slot, final ItemStack stack) {
        return this.setSlot(slot, stack, true);
    }

    public Inventory build() {
        final Inventory inventory = this.type == InventoryType.CHEST
                ? Bukkit.createInventory(this.owner, this.rows * 9, this.title)
                : Bukkit.createInventory(this.owner, this.type, this.title);
        InventoryListener.modifyInvUnstealableStates(inventory, this.unstealable);
        InventoryListener.modifyInvItemUseState(inventory, this.blockItemUse);
        InventoryListener.modifyInvDisableChatState(inventory, this.disableChat);

        this.slots.forEach((slotPos, slot) -> inventory.setItem(slotPos, slot.stack()));
        InventoryListener.modifyInvSlots(inventory, this.slots.values());

        return inventory;
    }

    public record PapyrusGuiSlot(ItemStack stack, BiPredicate<ClickType, Integer> stealable) {}

}