package spigot.encog.sitems.engsitems;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ENgSItems extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("§aвключён. §fby ENcoG");
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new ENgEvent(getConfig()), this);
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        getLogger().info("§cотключён. §fby ENcoG");
        // Plugin shutdown logic
    }

}
