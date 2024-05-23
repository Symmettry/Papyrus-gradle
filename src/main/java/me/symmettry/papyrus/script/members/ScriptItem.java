package me.symmettry.papyrus.script.members;

import me.symmettry.papyrus.script.data.PapyrusItem;
import me.symmettry.papyrus.util.NameUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.graalvm.polyglot.HostAccess.Export;

@SuppressWarnings("unused")
public class ScriptItem {

    @Export
    public PapyrusItem of(final String material) { // Item.of("DIAMOND_SWORD")
        return this.of(Material.valueOf(NameUtil.mojangName(material)), 1);
    }

    @Export
    public PapyrusItem of(final Material material) { // Item.of(item.getMaterial())
        return this.of(material, 1);
    }

    @Export
    public PapyrusItem of(final String material, final int amount) { // Item.of("DIAMOND_SWORD", 15)
        return this.of(Material.valueOf(NameUtil.mojangName(material)), amount);
    }

    @Export
    public PapyrusItem of(final Material material, final int amount) { // Item.of(item.getMaterial(), 15)
        return new PapyrusItem(new ItemStack(material, amount));
    }

}
