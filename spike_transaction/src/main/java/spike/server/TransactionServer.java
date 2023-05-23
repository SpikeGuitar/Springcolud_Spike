package spike.server;

import cn.dev33.satoken.util.SaResult;

import java.util.Map;

/**
 * @PACKAGE_NAME: spike.server
 * @NAME: CheckSyncStateServer
 * @USER: spike
 * @DATE: 2023/5/22 14:43
 * @PROJECT_NAME: Springcolud_Spike
 */
public interface TransactionServer {
    SaResult getInfoFromUrl(String httpUrl, String cookie, Map<String, String> paraMap);
    
    SaResult get50UpGoods(String httpUrl, String cookie, Map<String, String> paraMap) throws InterruptedException;
}
