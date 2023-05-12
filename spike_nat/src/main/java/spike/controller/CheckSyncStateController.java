package spike.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.example.controller
 * @NAME: CheckSyncState
 * @USER: spike
 * @DATE: 2023/5/8 8:57
 * @PROJECT_NAME: SendAlertMsg
 */
@RestController
@RequestMapping("Nat")
public class CheckSyncStateController {

    private static Map<String, Object> NAT_MAP = new HashMap<>();

    /**
     * 查询程序运行状态
     */
    @ApiOperation(value = "查询程序运行状态", tags = "CheckSyncStateController 检查同步状态")
    @RequestMapping(value = "/getNatMap", method = RequestMethod.GET)
    public Object getSyncState(HttpServletRequest request, @RequestParam(required = false, value = "key") String key,@RequestParam(required = false, value = "ip") String ip) {
        if (key.isEmpty()) {
            return NAT_MAP;
        }
        String client_ip = request.getRemoteAddr();
        Integer client_port = request.getRemotePort();
        String client_url = ip+"/"+client_ip + "/" + client_port.toString();
        if (NAT_MAP.get(key) != null) {
            String[] list = (String[]) NAT_MAP.get(key);
            boolean flag = Boolean.TRUE;
            for(String str:list){
                if(str==null||str.isEmpty()){
                    continue;
                }
                String[] urlArr = str.split("/");
                String uuid = urlArr[0]+"/"+urlArr[1];
                if(uuid.equals(ip+"/"+client_ip)){
                    flag = Boolean.FALSE;
                    break;
                }
            }
            if(flag){
                list[1] = list[0];
                list[0] = client_url;
            }
        } else {
            String[] list = new String[2];
            list[0] = client_url;
            NAT_MAP.put(key, list);
        }
        return NAT_MAP.get(key);
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
    public String sendToUdp(@RequestParam(value = "client_ip") String client_ip, @RequestParam(value = "client_port") Integer client_port) throws IOException {
        sendUdp(client_ip, client_port);
        return "打洞完成";
    }

    public static void sendUdp(String client_ip, Integer client_port) throws IOException {
        // 1，建立udp的socket服务。
        DatagramSocket ds = new DatagramSocket();//指定发送端口，这个可以不指定，系统会随机分配。
        // 2，明确要发送的具体数据。
        String text = "udp传输 nat打洞";
        byte[] buf = text.getBytes();
        // 3，将数据封装成了数据包。
        DatagramPacket dp = new DatagramPacket(buf, buf.length, InetAddress.getByName(client_ip), client_port);
        // 4，用socket服务的send方法将数据包发送出去。
        ds.send(dp);
        // 5，关闭资源。
        ds.close();
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