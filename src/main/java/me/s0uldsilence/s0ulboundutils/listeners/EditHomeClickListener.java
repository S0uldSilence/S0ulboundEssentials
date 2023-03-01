package me.s0uldsilence.s0ulboundutils.listeners;

import me.s0uldsilence.s0ulboundutils.S0ulboundUtils;
import me.s0uldsilence.s0ulboundutils.guis.HomesInventory;
import me.s0uldsilence.s0ulboundutils.homes.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EditHomeClickListener implements Listener {

    private Player player;
    private String homeName;

    public EditHomeClickListener(Player player, String homeName) {
        this.player = player;
        this.homeName = homeName;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BOLD + "Edit Home")) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null) {
                if (event.getSlot() == 0) {
                    // Rename home
                    player.closeInventory();
                    player.sendMessage(ChatColor.YELLOW + "Please enter a new name for your home:");
                    ChatListener listener = new ChatListener(newHomeName -> {
                        // The new home name is passed back to us here
                        HomeManager.renameHome(player, homeName, newHomeName);
                    });
                    Bukkit.getPluginManager().registerEvents(listener, S0ulboundUtils.getInstance());
                } else if (event.getSlot() == 1) {
                    // Delete home
                    HomeManager.deleteHome(event.getWhoClicked().getUniqueId().toString(), homeName);
                    player.sendMessage(ChatColor.YELLOW + "Home deleted.");
                    player.closeInventory();
                } else if (event.getSlot() == 2) {
                    /*PlayerInventoryClickItem listener = new PlayerInventoryClickItem(player, clickedItem -> {
                        if (clickedItem != null) {
                            SetHomeCommand.updateHomeItemStack(player, homeName, clickedItem);
                            player.sendMessage(ChatColor.YELLOW + "Home icon updated.");
                        }
                    });
                    Bukkit.getPluginManager().registerEvents(listener, S0ulboundUtils.getInstance());*/
                    PlayerInventoryClickItem listener = new PlayerInventoryClickItem(player, clickedItem -> {
                        if (clickedItem != null) {
                            HomeManager.updateHomeItemStack(player, homeName, clickedItem);
                            player.sendMessage(ChatColor.YELLOW + "Home icon updated.");
                            player.closeInventory();
                        }
                    });
                    Bukkit.getPluginManager().registerEvents(listener, S0ulboundUtils.getInstance());
                } else if (event.getSlot() == 8) {
                    // Go back
                    player.closeInventory();
                    HomesInventory.showHomesInventory(player,1);
                }
            }
        }
    }
}