package me.s0uldsilence.s0ulboundutils.commands;

import me.s0uldsilence.s0ulboundutils.homes.HomeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String homeName = args[0];
                String playerUUID = player.getUniqueId().toString();

                // Check if the home exists in the database
                if (HomeManager.doesHomeExist(playerUUID, homeName)) {
                    // Teleport the player to the home
                    HomeManager.teleportToHome(player, playerUUID, homeName);
                } else {
                    player.sendMessage(ChatColor.RED + "That home doesn't exist.");
                }
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /home <name>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
        }
        return false;
    }
}