package me.s0uldsilence.s0ulboundutils.listeners;

import me.s0uldsilence.s0ulboundutils.commands.SetHomeCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;
import java.util.function.Consumer;

public class ChatListener implements Listener {

    private final Consumer<String> callback;

    public ChatListener(Consumer<String> callback) {
        this.callback = callback;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = player.getUniqueId();
        event.setCancelled(true);
        String homeName = event.getMessage();
        if (homeName.length() > 20) {
            player.sendMessage(ChatColor.RED + "Home name cannot be longer than 20 characters.");
            player.sendMessage(ChatColor.YELLOW + "Please enter a new name:");
        } else if (homeName.matches("[a-zA-Z0-9_]+")) {
            callback.accept(homeName);
            HandlerList.unregisterAll(this);
        } else {
            player.sendMessage(ChatColor.RED + "Home name can only contain letters, numbers, and underscores.");
            player.sendMessage(ChatColor.YELLOW + "Please enter a new name:");
        }
    }
}