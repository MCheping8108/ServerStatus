package top.peacefuly.serverStatus;

import com.sun.management.OperatingSystemMXBean;
import org.bukkit.Bukkit;
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
//        startWebServer();
//        ListenServerStatus();
        saveDefaultConfig();
        this.getCommand("ssreload").setExecutor(new commands(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("ServerStatus has been disabled!");
        statusService.stopWebServer();
    }

//    public void startWebServer(){
//        serverThread = new Thread(()->{
//            try {
//                int port = getConfig().getInt("server-port");
//                serverSocket = new ServerSocket(port);
//                while (true) {
//                    Socket clientSocket = serverSocket.accept();
//                    new Thread(() -> {
//                        try {
//                            handleRequest(clientSocket);
//                            clientSocket.close();
//                        } catch (IOException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }).start();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });
//        serverThread.start();
//    }
//
//    public void stopWebServer(){
//        try {
//            serverSocket.close();
//            serverThread.interrupt();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void handleRequest(Socket clientSocket) throws IOException {
//        // 读取客户端请求
//        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//        String requestLine = reader.readLine();
//
//        // 监听服务器占用资源
//        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
//        double cpuUsage = osBean.getCpuLoad();
//        long memUsage = osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize();
//        int playerCount = getServer().getOnlinePlayers().size();
//        var playerMax = getServer().getMaxPlayers();
//        var icon = getServer().getServerIcon();
//
//        // 书写规范
////        var cpuMessage = String.format("%.2f", cpuUsage * 100) + "%";
////        var memMessage = String.format("%.2f", memUsage / 1024.0 / 1024.0) + "MB";
////        var playerMessage = playerCount;
//
//
//        // 处理GET请求
//        if (requestLine.startsWith("GET")) {
//            // 返回数据
//
//            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
//            writer.println("Access-Control-Allow-Origin: *");
//            writer.println("HTTP/1.1 200 OK");
////            打印出json数据
//            writer.println("Content-Type: application/json");
//            writer.println();
//            writer.println("{\"cpuUsage\": " + cpuUsage + ", \"memUsage\": " + memUsage + ", \"playerCount\": " + playerCount + ", \"playerMax\": " + playerMax + "}");
//            writer.close();
//        }
//    }
}
