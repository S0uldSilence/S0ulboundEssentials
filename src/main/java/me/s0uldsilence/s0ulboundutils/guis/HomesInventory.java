package me.s0uldsilence.s0ulboundutils.guis;

import me.s0uldsilence.s0ulboundutils.utils.SQLiteDatabase;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomesInventory {

    public static void showHomesInventory(Player player, int page) {
        String playerUUID = player.getUniqueId().toString();

        try {
            int numHomes = 0;
            int numPages = 1;

            // Get the total number of homes for the player
            PreparedStatement countStmt = SQLiteDatabase.getConnection().prepareStatement("SELECT COUNT(*) FROM homes WHERE player_uuid=?");
            countStmt.setString(1, playerUUID);
            ResultSet countRs = countStmt.executeQuery();
            if (countRs.next()) {
                numHomes = countRs.getInt(1);
            }
            countRs.close();
            countStmt.close();

            // Calculate the number of pages
            numPages = (int) Math.ceil((double) numHomes / 45);
            // Make sure page is within range
            if (page < 1) {
                page = 1;
            } else if (page > numPages) {
                page = numPages;
            }

            // Calculate the offset and limit for the homes query
            int offset = (page - 1) * 45;
            int limit = 45;

            // Get homes for the current page from the database
            PreparedStatement homesStmt = SQLiteDatabase.getConnection().prepareStatement("SELECT home_name, world_name, item_stack FROM homes WHERE player_uuid=? ORDER BY home_name ASC LIMIT ? OFFSET ?");
            homesStmt.setString(1, playerUUID);
            homesStmt.setInt(2, limit);
            homesStmt.setInt(3, offset);
            ResultSet homesRs = homesStmt.executeQuery();

            // Create inventory with homes as items
            Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.BOLD + "Homes Page " + page + "/" + numPages);

            // Add homes to the inventory
            while (homesRs.next()) {
                String homeName = homesRs.getString("home_name");
                String worldName = homesRs.getString("world_name");
                String itemStackString = homesRs.getString("item_stack");

                ItemStack item = deserializeItemStack(itemStackString);
                if (item == null) {
                    item = new ItemStack(Material.ENDER_PEARL);
                }

                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(ChatColor.YELLOW + homeName);
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.GRAY + "Dimension: " + ChatColor.WHITE + worldName);
                lore.add(ChatColor.GREEN + "Right Click to edit");
                meta.setLore(lore);
                item.setItemMeta(meta);

                inventory.addItem(item);
            }
            ItemStack newHome = new ItemStack(Material.NAME_TAG);
            ItemMeta meta = newHome.getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + "Create Home");
            newHome.setItemMeta(meta);

            ItemStack exitItem = new ItemStack(Material.BARRIER);
            ItemMeta exitMeta = exitItem.getItemMeta();
            exitMeta.setDisplayName(ChatColor.RED + "Exit");
            exitItem.setItemMeta(exitMeta);

            // Add page navigation buttons to the inventory footer
            ItemStack prevPageItem = new ItemStack(Material.ARROW, 1);
            ItemMeta prevPageMeta = prevPageItem.getItemMeta();
            prevPageMeta.setDisplayName(ChatColor.YELLOW + "Previous Page");
            prevPageItem.setItemMeta(prevPageMeta);

            ItemStack nextPageItem = new ItemStack(Material.ARROW, 1);
            ItemMeta nextPageMeta = nextPageItem.getItemMeta();
            nextPageMeta.setDisplayName(ChatColor.YELLOW + "Next Page");
            nextPageItem.setItemMeta(nextPageMeta);

            // Set the footer items based on the current page number
            if (numPages == 1) {
                // Only one page, don't need navigation buttons
                inventory.setItem(53, new ItemStack(Material.AIR));
            } else if (page == 1) {
                // First page, only show next page button
                inventory.setItem(53, nextPageItem);
            } else if (page == numPages) {
                // Last page, only show previous page button
                inventory.setItem(51, prevPageItem);
            } else {
                // Middle page, show both navigation buttons
                inventory.setItem(53, nextPageItem);
                inventory.setItem(51, prevPageItem);
            }
            inventory.setItem(45, newHome);
            inventory.setItem(52, exitItem);
            // Open the inventory for the player
            player.openInventory(inventory);

            homesRs.close();
            homesStmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
            player.sendMessage(ChatColor.RED + "An error occurred while loading the homes.");
        }
    }
    private static ItemStack deserializeItemStack(String itemStackString) {
        if (itemStackString == null) {
            return null;
        }
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(itemStackString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        ItemStack itemStack = config.getItemStack("item");
        return itemStack;
    }
}
