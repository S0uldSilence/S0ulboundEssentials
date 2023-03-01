package me.s0uldsilence.s0ulboundutils.homes;

import me.s0uldsilence.s0ulboundutils.utils.SQLiteDatabase;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HomeManager {
    public static boolean createHome(Player player, String homeName) {
        String playerUUID = player.getUniqueId().toString();
        Location location = player.getLocation();
        ItemStack itemStack = player.getInventory().getItemInMainHand();

        // Insert or update the home in the database
        try {
            PreparedStatement stmt = SQLiteDatabase.getConnection().prepareStatement("INSERT INTO homes (player_uuid, home_name, x, y, z, pitch, yaw, world_name, item_stack) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON CONFLICT(player_uuid, home_name) DO UPDATE SET x=?, y=?, z=?, pitch=?, yaw=?, world_name=?, item_stack=?");
            stmt.setString(1, playerUUID);
            stmt.setString(2, homeName);
            stmt.setDouble(3, location.getX());
            stmt.setDouble(4, location.getY());
            stmt.setDouble(5, location.getZ());
            stmt.setFloat(6, location.getPitch());
            stmt.setFloat(7, location.getYaw());
            stmt.setString(8, location.getWorld().getName());
            stmt.setString(9, serializeItemStack(itemStack));
            stmt.setDouble(10, location.getX());
            stmt.setDouble(11, location.getY());
            stmt.setDouble(12, location.getZ());
            stmt.setFloat(13, location.getPitch());
            stmt.setFloat(14, location.getYaw());
            stmt.setString(15, location.getWorld().getName());
            stmt.setString(16, serializeItemStack(itemStack));
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean renameHome(Player player, String oldHomeName, String newHomeName) {
        String playerUUID = player.getUniqueId().toString();
        // Update the home name in the database
        try {
            PreparedStatement stmt = SQLiteDatabase.getConnection().prepareStatement("UPDATE homes SET home_name=? WHERE player_uuid=? AND home_name=?");
            stmt.setString(1, newHomeName);
            stmt.setString(2, playerUUID);
            stmt.setString(3, oldHomeName);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private static String serializeItemStack(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return null;
        }
        YamlConfiguration config = new YamlConfiguration();
        config.set("item", itemStack);
        return config.saveToString();
    }
    public static boolean updateHomeItemStack(Player player, String homeName, ItemStack newItemStack) {
        String playerUUID = player.getUniqueId().toString();
        Location location = player.getLocation();

        // Update the home item stack in the database
        try {
            PreparedStatement stmt = SQLiteDatabase.getConnection().prepareStatement("UPDATE homes SET item_stack=? WHERE player_uuid=? AND home_name=?");
            stmt.setString(1, serializeItemStack(newItemStack));
            stmt.setString(2, playerUUID);
            stmt.setString(3, homeName);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean doesHomeExist(String playerUUID, String homeName) {
        String query = "SELECT COUNT(*) FROM homes WHERE player_uuid=? AND home_name=?";
        try (PreparedStatement stmt = SQLiteDatabase.getConnection().prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, homeName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return (count > 0);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void deleteHome(String playerUUID, String homeName) {
        String query = "DELETE FROM homes WHERE player_uuid=? AND home_name=?";
        try (PreparedStatement stmt = SQLiteDatabase.getConnection().prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, homeName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void teleportToHome(Player player, String playerUUID, String homeName) {
        String query = "SELECT * FROM homes WHERE player_uuid=? AND home_name=?";
        try (PreparedStatement stmt = SQLiteDatabase.getConnection().prepareStatement(query)) {
            stmt.setString(1, playerUUID);
            stmt.setString(2, homeName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    double x = rs.getDouble("x");
                    double y = rs.getDouble("y");
                    double z = rs.getDouble("z");
                    float pitch = rs.getFloat("pitch");
                    float yaw = rs.getFloat("yaw");
                    String worldName = rs.getString("world_name");

                    World world = Bukkit.getWorld(worldName);
                    if (world != null) {
                        Location location = new Location(world, x, y, z, yaw, pitch);
                        player.teleport(location);
                        player.sendMessage(ChatColor.GREEN + "Teleported to home " + homeName + ".");
                    } else {
                        player.sendMessage(ChatColor.RED + "The world " + worldName + " doesn't exist.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
