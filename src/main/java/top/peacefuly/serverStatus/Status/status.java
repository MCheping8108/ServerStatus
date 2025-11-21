// 已经废弃
package top.peacefuly.serverStatus.Status;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.net.ServerSocket;
import java.net.Socket;
import org.bukkit.event.Listener;
//import java.lang.management.OperatingSystemMXBean;

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
        // 读取客户端请求
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String requestLine = reader.readLine();

        // 监听服务器占用资源
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        double cpuUsage = osBean.getCpuLoad();
        long memUsage = osBean.getTotalPhysicalMemorySize() - osBean.getFreePhysicalMemorySize();
        int playerCount = plugin.getServer().getOnlinePlayers().size();
        var playerMax = plugin.getServer().getMaxPlayers();
        var icon = plugin.getServer().getServerIcon();

        // 书写规范
//        var cpuMessage = String.format("%.2f", cpuUsage * 100) + "%";
//        var memMessage = String.format("%.2f", memUsage / 1024.0 / 1024.0) + "MB";
//        var playerMessage = playerCount;


        // 处理GET请求
        if (requestLine.startsWith("GET")) {
            // 返回数据

            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
//            这玩意直接文字形式显示出来了，干脆直接注释，偷个懒
//            writer.println("Access-Control-Allow-Origin: *");
            writer.println("HTTP/1.1 200 OK");
//            打印出json数据
            writer.println("Content-Type: application/json");
            writer.println();
            writer.println("{\"cpuUsage\": " + cpuUsage + ", \"memUsage\": " + memUsage + ", \"playerCount\": " + playerCount + ", \"playerMax\": " + playerMax + "}");
            writer.close();
        }
    }
}
