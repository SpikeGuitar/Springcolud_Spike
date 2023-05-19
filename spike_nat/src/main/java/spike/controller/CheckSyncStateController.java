package spike.controller;


import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spike.schedule.NatSchedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static spike.schedule.NatSchedule.RECEIVE_SOCKET;
import static spike.schedule.NatSchedule.SEND_SOCKET;
import static spike.schedule.NatSchedule.sendUdp;

/**
 * @PACKAGE_NAME: com.example.controller
 * @NAME: CheckSyncState
 * @USER: spike
 * @DATE: 2023/5/8 8:57
 * @PROJECT_NAME: SendAlertMsg
 */
@Slf4j
@RestController
@RequestMapping("Nat")
public class CheckSyncStateController {

    @Value("${isserver}")
    private boolean IS_SERVER;

    /**
     * 查询NAT_MAP
     */
    @ApiOperation(value = "查询NAT_MAP", tags = "Nat Upd打洞")
    @RequestMapping(value = "/getNatMap", method = RequestMethod.GET)
    public Object getSyncState(@RequestParam(required = false, value = "key") String key) {
        if (!key.isEmpty()) {
            return NatSchedule.NAT_MAP.get(key);
        }
        return NatSchedule.NAT_MAP;
    }

    /**
     * 清除指定NAT_MAP key
     */
    @ApiOperation(value = "清除指定NAT_MAP key", tags = "Nat Upd打洞")
    @RequestMapping(value = "/clearNatMap", method = RequestMethod.GET)
    public Object clearNatMap(@RequestParam(required = false, value = "key") String key) {
        if (NatSchedule.NAT_MAP.get(key) != null) {
            NatSchedule.NAT_MAP.put(key, null);
            return NatSchedule.NAT_MAP.get(key);
        }
        return NatSchedule.NAT_MAP.get(key);
    }

    /**
     * 请求
     */
    @ApiOperation(value = "请求", tags = "CheckSyncStateController 检查同步状态")
    @RequestMapping(value = "/send", method = RequestMethod.GET)
    public String send() {
        return doGet("http://43.137.43.142:5001/Nat/getSyncState");
    }

    /**
     * 请求
     */
    @ApiOperation(value = "udp打洞", tags = "CheckSyncStateController 检查同步状态")
    @RequestMapping(value = "/sendToUdp", method = RequestMethod.GET)
    public String sendToUdp(@RequestParam(value = "key") String key, @RequestParam(value = "client_ip") String client_ip, @RequestParam(value = "client_port") Integer client_port) throws IOException {
        DatagramSocket datagramSocket;
        if (IS_SERVER) {
            datagramSocket = RECEIVE_SOCKET;
        } else {
            datagramSocket = SEND_SOCKET;
        }
        sendUdp(key.getBytes(), client_ip, client_port, datagramSocket);
        log.info("GET接口向服务端 {}:{} 发送upd 信息：{}", client_ip, client_port, key);
        return "打洞完成";
    }

    public static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpurl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            // 发送请求
            connection.connect();
            // 通过connection连接，获取输入流
            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                // 封装输入流is，并指定字符集
                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                // 存放数据
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            connection.disconnect();// 关闭远程连接
        }

        return result;
    }

}
