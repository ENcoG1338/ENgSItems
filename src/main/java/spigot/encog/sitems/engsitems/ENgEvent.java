package spigot.encog.sitems.engsitems;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ENgEvent implements Listener {

    private final FileConfiguration config;
    private final Map<UUID, List<ItemStack>> itemsToReturnMap = new HashMap<>();

    public ENgEvent(FileConfiguration config) {
        this.config = config;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        List<String> itemsToKeep = config.getStringList("items-to-keep");
        boolean playSound = config.getBoolean("play-sound");
        boolean showTitle = config.getBoolean("show-title");
        boolean sendMessage = config.getBoolean("show-message");
        String chatMessage = ChatColor.translateAlternateColorCodes('&', config.getString("chat-message"));
        String title = ChatColor.translateAlternateColorCodes('&', config.getString("title"));
        String subtitle = ChatColor.translateAlternateColorCodes('&', config.getString("subtitle"));
        String soundName = config.getString("sound-name", "ENTITY_PLAYER_LEVELUP");

        Random random = new Random();
        List<ItemStack> itemsToReturn = new ArrayList<>();
        for (ItemStack item : player.getInventory().getContents()) {
            if (item != null && itemsToKeep.contains(item.getType().toString())) {
                double keepChance = config.getDouble("keep-chance." + item.getType().toString(), 0.0);
                if (random.nextDouble() < keepChance) {
                    event.getDrops().remove(item);
                    itemsToReturn.add(item);
                }
            }
        }

        itemsToReturnMap.put(player.getUniqueId(), itemsToReturn);

        if (playSound) {
            Sound sound = Sound.valueOf(soundName);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        }

        if (sendMessage) {
            player.sendMessage(chatMessage);
        }

        if (showTitle) {
            player.sendTitle(title, subtitle, 10, 70, 20);
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        List<ItemStack> itemsToReturn = itemsToReturnMap.remove(player.getUniqueId());

        if (itemsToReturn != null) {
            for (ItemStack item : itemsToReturn) {
                player.getInventory().addItem(item);
            }
        }
    }

}