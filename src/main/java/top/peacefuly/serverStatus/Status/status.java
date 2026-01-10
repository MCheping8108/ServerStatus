package top.peacefuly.serverStatus.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONObject;
import org.bukkit.event.Listener;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.plugin.java.JavaPlugin;
public class status implements Listener {
    private final JavaPlugin plugin;
    private ServerSocket serverSocket;
    private Thread serverThread;

    public status(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startWebServer(){
        serverThread = new Thread(()->{
            try {
                int port = plugin.getConfig().getInt("server-port");
                serverSocket = new ServerSocket(port);
                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    new Thread(() -> {
                        try {
                            handleRequest(clientSocket);
                            clientSocket.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();
    }

    public void stopWebServer(){
        try {
            serverSocket.close();
            serverThread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(Socket clientSocket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String requestLine = reader.readLine();
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuUsage = osBean.getCpuLoad();
        long memUsage = osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize();
        int playerCount = plugin.getServer().getOnlinePlayers().size();
        var playerMax = plugin.getServer().getMaxPlayers();
        var icon = plugin.getServer().getServerIcon();
        var version = plugin.getServer().getVersion();
        var onlineMode = plugin.getServer().getOnlineMode();
        var motd = plugin.getServer().getMotd();
        var serverName = plugin.getServer().getName();

        if (requestLine.startsWith("GET")) {
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
            JSONObject json = new JSONObject();
            writer.println("HTTP/1.1 200 OK");
            writer.println("Content-Type: application/json");
            writer.println();
            json.put("cpuUsage", cpuUsage);
            json.put("memUsage", memUsage);
            json.put("playerCount", playerCount);
            json.put("playerMax", playerMax);
            json.put("version", version);
            json.put("onlineMode", onlineMode);
            json.put("motd", motd);
            json.put("serverName", serverName);
            if (icon != null) {
                json.put("icon", icon.toString());
            } else {
                json.put("icon", JSONObject.NULL);
            }
            writer.println(json.toString(4));
            writer.close();
        }
    }
}
