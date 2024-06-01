package me.symmettry.papyrus.script.members;

import me.symmettry.papyrus.listeners.InventoryListener;
import me.symmettry.papyrus.script.data.PapyrusGui;
import me.symmettry.papyrus.util.misc.PredUtil;
import me.symmettry.papyrus.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.graalvm.polyglot.HostAccess.Export;

import java.util.function.BiPredicate;

@SuppressWarnings("unused")
public final class ScriptGui {

    @Export
    public PapyrusGui create(final String name) {
        return new PapyrusGui(name);
    }

    @Export
    public InventoryType type(final String type) {
        return InventoryType.valueOf(TextUtil.mojangName(type));
    }

    @Export
    public void setUnstealable(final Inventory inventory, final boolean state) {
        this.setUnstealable(inventory, PredUtil.biPred(state));
    }
    @Export
    public void setUnstealable(final Inventory inventory, final BiPredicate<ItemStack, Integer> state) {
        InventoryListener.modifyInvUnstealableStates(inventory, state);
    }

    @Export
    public void setItemUse(final Inventory inventory, final boolean state) {
        this.setItemUse(inventory, PredUtil.biPred(state));
    }
    @Export
    public void setItemUse(final Inventory inventory, final BiPredicate<ItemStack, Integer> state) {
        InventoryListener.modifyInvItemUseState(inventory, state);
    }

    @Export
    public void setDisableChat(final Inventory inventory, final boolean state) {
        this.setDisableChat(inventory, PredUtil.biPred(state));
    }
    @Export
    public void setDisableChat(final Inventory inventory, final BiPredicate<Component, Integer> state) {
        InventoryListener.modifyInvDisableChatState(inventory, state);
    }

}