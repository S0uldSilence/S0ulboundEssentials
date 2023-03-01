package me.s0uldsilence.s0ulboundutils.guis;

import me.s0uldsilence.s0ulboundutils.S0ulboundUtils;
import me.s0uldsilence.s0ulboundutils.listeners.EditHomeClickListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.List;

public class EditHomeInventory {
    public static void showEditHomeInventory(Player player, String homeName) {
        Bukkit.getServer().getPluginManager().registerEvents(new EditHomeClickListener(player, homeName), S0ulboundUtils.getInstance());
        // Create the inventory with 9 slots
        Inventory inventory = Bukkit.createInventory(null, 9, ChatColor.BOLD + "Edit Home");

        // Add buttons to rename, delete, and change the item representation of the home
        ItemStack renameItem = new ItemStack(Material.NAME_TAG);
        ItemMeta renameMeta = renameItem.getItemMeta();
        renameMeta.setDisplayName(ChatColor.YELLOW + "Rename Home");
        renameItem.setItemMeta(renameMeta);

        ItemStack deleteItem = new ItemStack(Material.REDSTONE_BLOCK);
        ItemMeta deleteMeta = deleteItem.getItemMeta();
        deleteMeta.setDisplayName(ChatColor.RED + "Delete Home");
        deleteItem.setItemMeta(deleteMeta);

        ItemStack changeItem = new ItemStack(Material.CHEST);
        ItemMeta changeMeta = changeItem.getItemMeta();
        changeMeta.setDisplayName(ChatColor.YELLOW + "Change Item");
        changeItem.setItemMeta(changeMeta);
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Click on an item in your Inventory");
        changeMeta.setLore(lore);
        changeItem.setItemMeta(changeMeta);

        ItemStack exitItem = new ItemStack(Material.BARRIER);
        ItemMeta exitMeta = exitItem.getItemMeta();
        exitMeta.setDisplayName(ChatColor.YELLOW + "Exit Item");
        exitItem.setItemMeta(exitMeta);



        inventory.setItem(0, renameItem);
        inventory.setItem(1, deleteItem);
        inventory.setItem(2, changeItem);
        inventory.setItem(8, exitItem);

        // Save the home name and open the inventory for the player
        player.setMetadata("edit-home", new FixedMetadataValue(S0ulboundUtils.getInstance(), homeName));
        player.openInventory(inventory);
    }
}
