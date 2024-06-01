package me.symmettry.papyrus.script.data;

import lombok.Getter;
import me.symmettry.papyrus.util.text.ComponentFormatter;
import me.symmettry.papyrus.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemRarity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@SuppressWarnings({"UnstableApiUsage", "deprecation", "unused"})
@Getter
public final class PapyrusItem extends ItemStack {

    private final ItemMeta meta = this.getItemMeta();

    public PapyrusItem(final Material material, final int amount) {
        super(material, amount);
    }

    private void setMeta() {
        this.setItemMeta(meta);
    }

    public PapyrusItem attribute(final String name, final int value) {
        return this.attribute(name, value, "ADD_NUMBER");
    }
    public PapyrusItem attribute(final String name, final int value, final String operation) {
        return this.attribute(name, value, operation, "ANY");
    }
    public PapyrusItem attribute(final String name, final int value, final String operation, final String... slots) {
        final Attribute attr = Attribute.valueOf(name);
        for (final String slot : slots) {
            meta.addAttributeModifier(attr, new AttributeModifier(UUID.randomUUID(), attr.translationKey(),
                    value, AttributeModifier.Operation.valueOf(operation), Objects.requireNonNull(EquipmentSlotGroup.getByName(slot))));
        }
        setMeta();
        return this;
    }

    public PapyrusItem name(final String name) {
        this.meta.displayName(Component.text(name));
        setMeta();
        return this;
    }
    public PapyrusItem nameFormatted(final String name) {
        this.meta.displayName(ComponentFormatter.formatString(name));
        setMeta();
        return this;
    }

    public int getCustomModelData() {
        return this.meta.getCustomModelData();
    }

    public boolean hasEnchants() {
        return this.meta.hasEnchants();
    }

    public boolean isUnbreakable() {
        return this.meta.isUnbreakable();
    }
    public PapyrusItem unbreakable(final boolean state) {
        this.meta.setUnbreakable(state);
        return this;
    }

    public boolean isFireResistant() {
        return this.meta.isFireResistant();
    }
    public PapyrusItem fireResistant(final boolean state) {
        this.meta.setFireResistant(state);
        return this;
    }

    public boolean hasRarity() {
        return this.meta.hasRarity();
    }
    public PapyrusItem rarity(final ItemRarity rarity) {
        this.meta.setRarity(rarity);
        return this;
    }
    public PapyrusItem rarity(final String rarity) {
        return this.rarity(ItemRarity.valueOf(TextUtil.mojangName(rarity)));
    }

    public List<Component> lore() {
        return this.meta.lore();
    }
    @Override
    public void setLore(final List<String> strings) {
        assert strings != null;
        this.meta.lore(strings.stream().map(ComponentFormatter::formatString).toList());
    }

}