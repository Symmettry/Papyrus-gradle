package me.symmettry.papyrus.script.members;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.graalvm.polyglot.HostAccess.Export;

@SuppressWarnings("unused")
public class ScriptWorld {

    @Export
    public Location location(final double x, final double y, final double z) {
        return this.location(x, y, z, this.world("world"));
    }

    @Export
    public Location location(final double x, final double y, final double z, final World world) {
        return new Location(world, x, y, z);
    }

    @Export
    public World world(final String name) {
        return Bukkit.getWorld(name);
    }

}
