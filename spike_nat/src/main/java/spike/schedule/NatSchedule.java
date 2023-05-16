package spike.schedule;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spike.controller.CheckSyncStateController.doGet;
import static spike.controller.CheckSyncStateController.sendUdp;

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

    public static Map<String, Object> NAT_MAP = new HashMap<>();

    //每1分钟执行一次
    @Scheduled(cron = "0/1 * * * * ?")
    private void SendUdp() throws IOException {
        if (!IS_SERVER) {
            String ipV4 = InetAddress.getLocalHost().getHostAddress();
            // 客户段发送upd请求到服务端(服务端记录 客户端内网ip/外网ip/外网端口)
            String[] ipArr = TAR_URL.split(":");
            log.info("向服务端"+ipArr[0]+":10000"+"发送upd");
            sendUdp(KEY, ipArr[0], 10000);
            //查询 相同key的两个客户段nat信息
            String result = doGet("http://" + TAR_URL + "/Nat/getNatMap?key=" + KEY);
            List<String> list = JSON.parseObject(result, List.class);
            // 使用
            // 相同key 需要nat穿透的ip数组
            log.info(JSON.toJSONString(list));
            for (String str : list) {
                if (str == null || str.isEmpty()) {
                    continue;
                }
                String[] strArr = str.split(":");
                if (!strArr[0].equals(ipV4)) {
                    //upd打洞
                    log.info("upd打洞 ip{} : 端口{}", strArr[1], Integer.valueOf(strArr[2]));
                    log.info("向客户端"+strArr[1]+":"+strArr[2]+"发送upd");
                    sendUdp(KEY, strArr[1], Integer.valueOf(strArr[2]));
                }
            }
        }
    }

    //每1秒钟执行一次
    @Scheduled(cron = "*/1 * * * * ?")
    private void receiveUpd() throws IOException {
        receiveUpdMsg();
    }

    /**
     * 服务端接收upd请求
     *
     * @throws IOException
     */
    public void receiveUpdMsg() throws IOException {
        //创建接收端的Socket对象(DatagramSocket)
        //DatagramSocket(int port)构造数据报套接字并将其绑定到本地主机上的指定端口
        DatagramSocket ds;
        if(IS_SERVER) {
            ds = new DatagramSocket(10000);
        }else {
            ds = new DatagramSocket(9999);
        }
        //创建一个数据包，用于接收数据
        //DatagramPacket(byte[] buf,int length)构造一个DatagramPacket用于接收长度为length的数据包
        byte[] bytes = new byte[1024];
        DatagramPacket dp = new DatagramPacket(bytes, bytes.length);
        //调用DatagramSocket对象的方法接收数据
        ds.receive(dp);
        InetAddress address = dp.getAddress();
        int port = dp.getPort();
        String url = address.getHostAddress() + ":" + port;
        log.info("收到->" + url + "的消息：");
        //解析数据包，并把数据在控制台显示
        //byte[] getData() 返回数据缓冲区
        byte[] datas = dp.getData();
        //int getLength()返回要发送的数据的长度或接收到的数据的长度
        int length = dp.getLength();
        String dataString = new String(datas, 0, length);
        String client_url = dataString + ":" + url;
        log.info("收到upd请求地址{}", client_url);
        String[] clientArr = client_url.split(":");
        if(IS_SERVER) {
            String key = clientArr[0];
            putNatMap(key, clientArr[1] + ":" + url);
        }
        //关闭接收端
        ds.close();
    }

    private void putNatMap(String key, String value) {
        if (NAT_MAP.get(KEY) == null) {
            String[] tempArr = new String[]{value, ""};
            NAT_MAP.put(key, tempArr);
        } else {
            String[] tempArr = (String[]) NAT_MAP.get(key);
            boolean flag = Boolean.TRUE;
            for (String str : tempArr) {
                if (str.equals(value)) {
                    flag = Boolean.FALSE;
                    break;
                }
            }
            if (flag) {
                tempArr[1] = tempArr[0];
                tempArr[0] = value;
                NAT_MAP.put(key, tempArr);
            }
        }
    }
}
