package me.s0uldsilence.s0ulboundutils;

import me.s0uldsilence.s0ulboundutils.commands.DelHomeCommand;
import me.s0uldsilence.s0ulboundutils.commands.HomeCommand;
import me.s0uldsilence.s0ulboundutils.commands.HomesCommand;
import me.s0uldsilence.s0ulboundutils.commands.SetHomeCommand;
import me.s0uldsilence.s0ulboundutils.listeners.ChatListener;
import me.s0uldsilence.s0ulboundutils.listeners.EditHomeClickListener;
import me.s0uldsilence.s0ulboundutils.listeners.HomesClickListener;
import me.s0uldsilence.s0ulboundutils.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class S0ulboundUtils extends JavaPlugin {
    private static S0ulboundUtils instance;
    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        getLogger().info("S0ulboundUtils enabled.");
        ConfigManager.init();
        LanguageManager.init();

        // Connect to database
        DatabaseManager.connectToDatabase();

        DatabaseManager.createTables();
        DatabaseManager.createDefaultTables();
        getCommand("sethome").setExecutor(new SetHomeCommand());
        getCommand("delhome").setExecutor(new DelHomeCommand());
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("homes").setExecutor(new HomesCommand());
        Bukkit.getServer().getPluginManager().registerEvents(new HomesClickListener(new HomeCommand()), instance);
        //Bukkit.getPluginManager().registerEvents(new EditHomeClickListener(), this);
        //Bukkit.getPluginManager().registerEvents(new ItemSelectionListener(), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("S0ulboundUtils disabled.");

        // Disconnect from database
        DatabaseManager.disconnectFromDatabase();
    }

    public static S0ulboundUtils getInstance() {
        return instance;
    }

}
