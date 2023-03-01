package me.s0uldsilence.s0ulboundutils.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

public class PlayerInventoryClickItem implements Listener {

    private final Player player;
    private final Consumer<ItemStack> callback;

    public PlayerInventoryClickItem(Player player, Consumer<ItemStack> callback) {
        this.player = player;
        this.callback = callback;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked().equals(player)) {
            ItemStack clickedItem = event.getCurrentItem();
            if (clickedItem != null) {
                callback.accept(clickedItem);
            }
            player.closeInventory();
            HandlerList.unregisterAll(this); // unregister the listener
        }
    }
}
