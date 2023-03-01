package me.s0uldsilence.s0ulboundutils.commands;

import me.s0uldsilence.s0ulboundutils.guis.HomesInventory;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class HomesCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            String playerUUID = player.getUniqueId().toString();

            //try {
            int page = 1;
            if (args.length > 0) {
                try {
                    page = Integer.parseInt(args[0]);
                    if (page < 1) {
                        page = 1;
                    }
                } catch (NumberFormatException e) {
                    player.sendMessage(ChatColor.RED + "Invalid page number.");
                    return true;
                }
            }
            // Show the homes inventory for the current page
            HomesInventory.showHomesInventory(player, page);
            return true;
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return false;
        }
    }
}