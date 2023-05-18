package spike.schedule;

import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spike.controller.CheckSyncStateController.doGet;

/**
 * @PACKAGE_NAME: spike.schedule
 * @NAME: NatSchedule
 * @USER: spike
 * @DATE: 2023/5/12 10:27
 * @PROJECT_NAME: Springcolud_Spike
 */
@Slf4j
@Component
public class NatSchedule {

    @Value("${tarurl}")
    private String TAR_URL;

    @Value("${isserver}")
    private boolean IS_SERVER;

    @Value("${key}")
    private String KEY;

    @Value("${sendport}")
    private Integer SEND_PORT;

    @Value("${receiveport}")
    private Integer RECEIVE_PORT;

    private static DatagramSocket RECEIVE_SOCKET = null;
    public static DatagramSocket SEND_SOCKET = null;

    public static Map<String, Object> NAT_MAP = new HashMap<>();

//    public boolean SUCCESS_CONNECTED = Boolean.FALSE;

    //每1分钟执行一次
    @Scheduled(cron = "0/15 * * * * ?")
    private void SendUdp() throws IOException {
        sendUpdMsg();
    }

    //每1秒钟执行一次
    @Scheduled(cron = "*/1 * * * * ?")
    private void receiveUpd() throws IOException {
        receiveUpdMsg();
    }

    public void sendUpdMsg() throws IOException {
        if (!IS_SERVER) {
            if (SEND_SOCKET == null) {
                //清除key值
                doGet("http://" + TAR_URL + "/Nat/clearNatMap?key=" + KEY);
                SEND_SOCKET = new DatagramSocket();
                Thread thread = new Thread() {
                    @SneakyThrows
                    @Override
                    public void run() {
                        log.info("开始接收upd请求");
                        responseSend();
                    }
                };
                thread.start();
            }
            sendUpdToServer();
        }
    }

    private void responseSend() throws IOException {
        byte[] bytes = new byte[1024];
        DatagramPacket packet1 = new DatagramPacket(bytes, bytes.length);
        while (true) {
            SEND_SOCKET.receive(packet1);
            String receive = new String(bytes, 0, packet1.getLength());
            if (receive.contains("client")) {
                String msg = "****************************打洞成功****************************";
                log.info(msg);
            }
            log.info("upd传输信息为: {}", receive);
        }
    }

    private void sendUpdToServer() throws IOException {
        String ipV4 = InetAddress.getLocalHost().getHostAddress();
        String[] ipArr = TAR_URL.split(":");
        // 客户段发送upd请求到服务端(服务端记录 客户端内网ip/外网ip/外网端口)
        String text = KEY + ":" + ipV4 + ":" + SEND_SOCKET.getLocalPort();
        byte[] buf = text.getBytes();
        sendUdp(buf, ipArr[0], RECEIVE_PORT, SEND_SOCKET);
        //查询 相同key的两个客户段nat信息
        String result = doGet("http://" + TAR_URL + "/Nat/getNatMap?key=" + KEY);
        if (result.isEmpty()) {
            return;
        }
        List<String> list = JSON.parseObject(result, List.class);
        // 相同key 需要nat穿透的ip数组
        log.info("键值：{} 下穿透ip数组 {}", KEY, JSON.toJSONString(list));
        String client = "";
        for (String str : list) {
            if (str == null || str.isEmpty()) {
                continue;
            }
            String[] strArr = str.split(":");
            if (strArr[0].equals(ipV4)) {
                client = str;
            }
            if (!strArr[0].equals(ipV4)) {
                //upd打洞
                String[] udpArr = str.split("/")[1].split(":");
                log.info("upd打洞 ip{} : 端口{}", udpArr[0], Integer.valueOf(udpArr[1]));
                // 2，明确要发送的具体数据。
                String clientMsg = "来自 client " + client + " upd 信息";
                byte[] data = clientMsg.getBytes();
                sendUdp(data, udpArr[0], Integer.valueOf(udpArr[1]), SEND_SOCKET);
            }
        }
    }

    public static void sendUdp(byte[] data, String ip, int port, DatagramSocket datagramSocket) throws IOException {
        // 3，将数据封装成了数据包。
        DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName(ip), port);
        log.info("向服务端" + ip + ":" + port + "发送upd 信息");
        datagramSocket.send(packet);
    }

    /**
     * 服务端接收upd请求
     *
     * @throws IOException
     */
    public void receiveUpdMsg() throws IOException {
        if (IS_SERVER) {
            //创建一个数据包，用于接收数据
            byte[] bytes = new byte[1024];
            DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
            //DatagramSocket(int port)构造数据报套接字并将其绑定到本地主机上的指定端口
            if (RECEIVE_SOCKET == null) {
                RECEIVE_SOCKET = new DatagramSocket(RECEIVE_PORT);
            }
            DatagramSocket ds = RECEIVE_SOCKET;
            //调用DatagramSocket对象的方法接收数据
            ds.receive(dp);
            InetAddress address = dp.getAddress();
            int port = dp.getPort();
            String url = address.getHostAddress() + ":" + port;
            //解析数据包，并把数据在控制台显示
            byte[] data = dp.getData();
            //int getLength()返回要发送的数据的长度或接收到的数据的长度
            int length = dp.getLength();
            String dataString = new String(data, 0, length);
            log.info("\n客户端Nat地址：  **** {}**** \n客户端内网地址： **** {} **** ", url, dataString);
            String[] clientArr = dataString.split(":");
            if (IS_SERVER) {
                String key = clientArr[0];
                putNatMap(key, clientArr[1] + ":" + clientArr[2] + "/" + url);
                log.info("key :{} 映射数组更新完成", KEY);
            }
        }
    }

    private void putNatMap(String key, String value) {
        if (NAT_MAP.get(KEY) == null) {
            List<String> tempArr = new ArrayList<>();
            tempArr.add(value);
            NAT_MAP.put(key, tempArr);
        } else {
            List<String> tempArr = (List<String>) NAT_MAP.get(key);
            String refresh = null;
            for (String str : tempArr) {
                String uuid = StringUtils.substringBeforeLast(str, ":");
                String uuidValue = StringUtils.substringBeforeLast(value, ":");
                if (uuid.equals(uuidValue)) {
                    refresh = str;
                    break;
                }
            }
            if (refresh != null) {
                tempArr.remove(refresh);
            }
            tempArr.add(value);
            NAT_MAP.put(key, tempArr);
        }
    }
}
