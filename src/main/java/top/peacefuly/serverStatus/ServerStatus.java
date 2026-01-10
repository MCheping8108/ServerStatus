package top.peacefuly.serverStatus;

import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import top.peacefuly.serverStatus.Command.commands;
import top.peacefuly.serverStatus.Status.status;



public final class ServerStatus extends JavaPlugin implements Listener {
    private status statusService;

    @Override
    public void onEnable() {
        getLogger().info(ChatColor.GREEN + "ServerStatus has been enabled! the port is " + getConfig().getInt("server-port"));
        statusService = new status(this);
        getServer().getPluginManager().registerEvents(statusService, this);
        statusService.startWebServer();
        saveDefaultConfig();
        this.getCommand("ssreload").setExecutor(new commands(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info(ChatColor.RED + "ServerStatus has been disabled!");
        statusService.stopWebServer();
    }
}
