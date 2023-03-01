package me.s0uldsilence.s0ulboundutils.listeners;

import me.s0uldsilence.s0ulboundutils.S0ulboundUtils;
import me.s0uldsilence.s0ulboundutils.commands.HomeCommand;
import me.s0uldsilence.s0ulboundutils.commands.HomesCommand;
import me.s0uldsilence.s0ulboundutils.commands.SetHomeCommand;
import me.s0uldsilence.s0ulboundutils.guis.EditHomeInventory;
import me.s0uldsilence.s0ulboundutils.guis.HomesInventory;
import me.s0uldsilence.s0ulboundutils.homes.HomeManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HomesClickListener implements Listener {

    private HomeCommand homeCommand;

    public HomesClickListener(HomeCommand homeCommand) {
        this.homeCommand = homeCommand;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().startsWith(ChatColor.BOLD + "Homes Page")) {
            event.setCancelled(true);
            if (event.isRightClick() && event.getSlot() >= 0 && event.getSlot() <= 44) {
                Player player = (Player) event.getWhoClicked();
                String homeName = event.getCurrentItem().getItemMeta().getDisplayName().substring(2); // Remove color code
                player.closeInventory();
                //HomesCommand.showEditHomeInventory(player, homeName);
                EditHomeInventory.showEditHomeInventory(player, homeName);
            }
            if (event.getCurrentItem() != null && event.getCurrentItem().getItemMeta() != null && event.isLeftClick()) {
                Player player = (Player) event.getWhoClicked();
                String homeName = event.getCurrentItem().getItemMeta().getDisplayName().substring(2); // Remove color code
                String[] parts = event.getView().getTitle().split(" ");
                int currentPage = Integer.parseInt(parts[2].split("/")[0]);
                int numPages = Integer.parseInt(parts[2].split("/")[1]);
                if (event.getSlot() == 45) { // Create Home item
                    player.closeInventory();
                    player.sendMessage(ChatColor.YELLOW + "Please enter a name for your new home:");
                    ChatListener listener = new ChatListener(setHomeName -> {
                        // The home name is passed back to us here
                        HomeManager.createHome(player, setHomeName);
                        player.sendMessage(ChatColor.GREEN + "Home " + ChatColor.WHITE + setHomeName + ChatColor.GREEN +" created");
                    });
                    Bukkit.getPluginManager().registerEvents(listener, S0ulboundUtils.getInstance());

                }
                if (event.getSlot() == 52) {
                    player.closeInventory();
                }
                if (event.getSlot() == 51 && currentPage > 1) { // Previous page
                    //HomesCommand.showHomesInventory(player, currentPage - 1);
                    HomesInventory.showHomesInventory(player, currentPage - 1);
                } else if (event.getSlot() == 53 && currentPage < numPages) { // Next page
                    //HomesCommand.showHomesInventory(player, currentPage + 1);
                    HomesInventory.showHomesInventory(player, currentPage +1);
                } else { // Teleport to home
                    HomeManager.teleportToHome(player, player.getUniqueId().toString(), homeName);
                    //HomesCommand.showHomesInventory(player, currentPage);
                    //HomesInventory.showHomesInventory(player, currentPage + 1);
                }
            }
        }
    }
}