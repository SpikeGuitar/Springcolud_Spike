package spike.server.Impl;

import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import spike.server.TransactionServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @PACKAGE_NAME: spike.server.Impl
 * @NAME: CheckSyncStateServerImpl
 * @USER: spike
 * @DATE: 2023/5/22 14:43
 * @PROJECT_NAME: Springcolud_Spike
 */
@Service
public class TransactionServerImpl implements TransactionServer {

    /**
     * 调用Get请求方法
     *
     * @param httpUrl
     * @return
     */
    public String doGet(String httpUrl, String cookie) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;// 返回结果字符串
        try {
            // 创建远程url连接对象
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开一个连接，强转成httpURLConnection类
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接方式：get
            connection.setRequestMethod("GET");
            // 设置连接主机服务器的超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取远程返回的数据时间：60000毫秒
            connection.setReadTimeout(60000);
            if (cookie != null) {
                connection.setRequestProperty("Cookie", cookie);
            }
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭资源
                if (null != br) {
                    br.close();
                }
                if (null != is) {
                    is.close();
                }
                connection.disconnect();// 关闭远程连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public SaResult getInfoFromUrl(String httpUrl, String cookie, Map<String, String> paraMap) {
        httpUrl += "?";
        for (Map.Entry<String, String> entity : paraMap.entrySet()) {
            String key = entity.getKey();
            String value = entity.getValue();
            String temp = key + "=" + value + "&";
            httpUrl += temp;
        }
        httpUrl = httpUrl.substring(0, httpUrl.length() - 1);
        String result = doGet(httpUrl, cookie);
        SaResult saResult = JSON.parseObject(result, SaResult.class);
        return saResult;
    }

    @Override
    public SaResult get50UpGoods(String httpUrl, String cookie, Map<String, String> paraMap) throws InterruptedException {
        List<Map<String, Object>> items = getAllItems(httpUrl, cookie, paraMap);
        List<Map<String, Object>> qualified = new ArrayList<>();
        for (Map<String, Object> map : items) {
            //buff价格
            Double sell_min_price = Double.valueOf(map.get("sell_min_price").toString());
            Map<String, Object> goods_info = (Map<String, Object>) map.get("goods_info");
            //steam价格
            Double steam_price_cny = Double.valueOf(goods_info.get("steam_price_cny").toString());
            double multiple = steam_price_cny / sell_min_price;
            if (multiple > 1.5 && sell_min_price > 2.0) {
                map.put("steam_price_cny", steam_price_cny);
                map.remove("goods_info");
                qualified.add(map);
            }
        }
        return SaResult.data(qualified);
    }

    private List<Map<String, Object>> getAllItems(String httpUrl, String cookie, Map<String, String> paraMap) throws InterruptedException {
        List<Map<String, Object>> allItems = new ArrayList<>();
        SaResult saResult = getInfoFromUrl(httpUrl, cookie, paraMap);
        Map<String, Object> data = (Map<String, Object>) saResult.getData();
        List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
        Integer total_page = (Integer) data.get("total_page");
        allItems.addAll(items);
        String cutUrl = httpUrl.substring(0, httpUrl.length() - 1);
        for (int i = 2; i < total_page; i++) {
            cutUrl += i;
            allItems.addAll(getItems(cutUrl, cookie, paraMap));
            System.out.println("success "+i);
        }
        return allItems;
    }

    private List<Map<String, Object>> getItems(String cutUrl, String cookie, Map<String, String> paraMap) throws InterruptedException {
        SaResult saResultTemp = getInfoFromUrl(cutUrl, cookie, paraMap);
        if(saResultTemp==null){
            saResultTemp = tryConn(cutUrl, cookie, paraMap);
        }
        Map<String, Object> dataTemp = (Map<String, Object>) saResultTemp.getData();
        List<Map<String, Object>> itemsTemp = (List<Map<String, Object>>) dataTemp.get("items");
        return itemsTemp;
    }

    private boolean ok=Boolean.TRUE;
    
    private SaResult tryConn(String cutUrl, String cookie, Map<String, String> paraMap) throws InterruptedException {
        Thread.sleep(2000);
        System.out.println("再次尝试 ");
        SaResult saResultTemp = null;
        while (ok) {
             saResultTemp = getInfoFromUrl(cutUrl, cookie, paraMap);
             if(saResultTemp!=null){
                 ok = Boolean.FALSE;
             }
        }
        return saResultTemp;
    }
}
