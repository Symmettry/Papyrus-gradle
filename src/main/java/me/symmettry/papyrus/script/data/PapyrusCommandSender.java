package me.symmettry.papyrus.script.data;

import com.ibm.icu.impl.locale.XCldrStub;
import me.symmettry.papyrus.Papyrus;
import me.symmettry.papyrus.util.text.ComponentFormatter;
import me.symmettry.papyrus.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("unused")
public record PapyrusCommandSender(CommandSender sender) {

    public void sendMessage(final String... messages) {
        this.sendMessage(ComponentFormatter.formatString(XCldrStub.join(messages, " ")));
    }

    public void sendMessage(final Component message) {
        this.sender.sendMessage(message);
    }

    public String getName() {
        return this.sender.getName();
    }

    public Server getServer() {
        return this.sender.getServer();
    }

    public void openInventory(final Object potentialInventory) {
        if(!(potentialInventory instanceof final Inventory inventory)) {
            Papyrus.LOGGER.warning("A non-inventory class was passed into openInventory -- make sure to .build() on Gui.create()");
            return;
        }
        if(this.sender instanceof final Player player) {
            player.openInventory(inventory);
        }
    }

    public void setGameMode(final GameMode gameMode) {
        if(!(this.sender instanceof final Player player)) return;
        player.setGameMode(gameMode);
    }
    public void setGameMode(final String gameMode) {
        this.setGameMode(GameMode.valueOf(TextUtil.mojangName(gameMode)));
    }

    public void setGMA() {
        this.setGameMode(GameMode.ADVENTURE);
    }
    public void setGMC() {
        this.setGameMode(GameMode.CREATIVE);
    }
    public void setGMS() {
        this.setGameMode(GameMode.SURVIVAL);
    }
    public void setGMSP() {
        this.setGameMode(GameMode.SPECTATOR);
    }

}