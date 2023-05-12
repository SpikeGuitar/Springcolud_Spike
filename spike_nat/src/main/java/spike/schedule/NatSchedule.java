package spike.schedule;

import com.alibaba.fastjson.JSON;
import jdk.nashorn.internal.runtime.logging.Logger;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetAddress;
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

    //每1分钟执行一次
    @Scheduled(cron = "0 0/1 * * * ?")
    private void SendUdp() throws IOException {
        if(!IS_SERVER) {
            String ipV4 = InetAddress.getLocalHost().getHostAddress();
            //存储 外网ip和端口到服务器
            doGet("http://" + TAR_URL + "/Nat/getNatMap?key="+KEY+"&ip="+ipV4);
            //查询 相同key的两个客户段nat信息
            String result = doGet("http://" + TAR_URL + "/Nat/getNatMap?key=");
            Map resultMap = JSON.parseObject(result, Map.class);
            // 使用
            if (resultMap != null && resultMap.get(KEY) != null) {
                List<String> list = (List<String>) resultMap.get(KEY);
                // 相同key 需要nat穿透的ip数组
                log.info(JSON.toJSONString(list));
                for(String str: list){
                    if(str==null||str.isEmpty()){
                        continue;
                    }
                    String[] strArr = str.split("/");
                    if(!strArr[0].equals(ipV4)){
                        //upd打洞
                        log.info("upd打洞 ip{} : 端口{}",strArr[1],Integer.valueOf(strArr[2]));
                        sendUdp(strArr[1], Integer.valueOf(strArr[2]));
                    }
                }
            }
        }
    }
    
    
}
