package me.symmettry.papyrus.script.members;

import me.symmettry.papyrus.script.data.PapyrusItem;
import me.symmettry.papyrus.util.text.TextUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemRarity;
import org.graalvm.polyglot.HostAccess.Export;

@SuppressWarnings("unused")
public final class ScriptItem {

    @Export
    public PapyrusItem of(final String material) { // Item.of("DIAMOND_SWORD")
        return this.of(Material.valueOf(TextUtil.mojangName(material)), 1);
    }

    @Export
    public PapyrusItem of(final Material material) { // Item.of(item.getMaterial())
        return this.of(material, 1);
    }

    @Export
    public PapyrusItem of(final String material, final int amount) { // Item.of("DIAMOND_SWORD", 15)
        return this.of(Material.valueOf(TextUtil.mojangName(material)), amount);
    }

    @Export
    public PapyrusItem of(final Material material, final int amount) { // Item.of(item.getMaterial(), 15)
        return new PapyrusItem(material, amount);
    }

    @Export
    public ItemRarity rarity(final String string) {
        return ItemRarity.valueOf(TextUtil.mojangName(string));
    }

}