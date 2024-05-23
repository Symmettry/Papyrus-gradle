package me.symmettry.papyrus.script.data;

import lombok.Getter;
import me.symmettry.papyrus.util.ColorUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

@SuppressWarnings("ALL")
@Getter
public class PapyrusItem {

    private final ItemStack item;
    private ItemMeta meta;

    public PapyrusItem(final ItemStack item) {
        this.item = item;
        this.meta = item.getItemMeta();
    }

    private void setMeta() {
        item.setItemMeta(meta);
    }

    public void setItemMeta(final ItemMeta meta) {
        this.meta = meta;
        setMeta();
    }

    public void addAttribute(final String name, final int value) {
        this.addAttribute(name, value, "ADD_NUMBER");
    }
    public void addAttribute(final String name, final int value, final String operation) {
        this.addAttribute(name, value, operation, "ANY");
    }
    public void addAttribute(final String name, final int value, final String operation, final String... slots) {
        final Attribute attr = Attribute.valueOf(name);
        for (final String slot : slots) {
            meta.addAttributeModifier(attr, new AttributeModifier(UUID.randomUUID(), attr.translationKey(),
                    value, AttributeModifier.Operation.valueOf(operation), EquipmentSlotGroup.getByName(slot)));
        }
        setMeta();
    }

    public void setName(final String name) {
        this.meta.displayName(ColorUtil.colorText(name));
        setMeta();
    }

}
