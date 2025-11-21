package top.peacefuly.serverStatus.Command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class commands implements CommandExecutor {

    private final JavaPlugin plugin;
    public commands(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (command.getName().equalsIgnoreCase("ssreload")) {
            if (!sender.hasPermission("serverstatus.reload")) {
                sender.sendMessage("§cYou do not have permission to execute this command.");
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "[ServerStatus] 配置文件已重新加载！");
        }
        return true;
    }
}
