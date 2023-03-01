package me.s0uldsilence.s0ulboundutils.commands;

import me.s0uldsilence.s0ulboundutils.homes.HomeManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class SetHomeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 1) {
                String homeName = args[0];
                if (HomeManager.createHome(player, homeName)) {
                    player.sendMessage(ChatColor.GREEN + "Home " + homeName + " set.");
                } else {
                    player.sendMessage(ChatColor.RED + "An error occurred while setting the home.");
                }
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "Usage: /sethome <name>");
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
        }
        return false;
    }
}