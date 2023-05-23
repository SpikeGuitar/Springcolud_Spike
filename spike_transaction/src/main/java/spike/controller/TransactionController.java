package spike.controller;


import cn.dev33.satoken.util.SaResult;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import spike.server.TransactionServer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.example.controller
 * @NAME: CheckSyncState
 * @USER: spike
 * @DATE: 2023/5/8 8:57
 * @PROJECT_NAME: SendAlertMsg
 */
@Slf4j
@RestController
@RequestMapping("Transaction")
public class TransactionController {

    @Autowired
    TransactionServer checkSyncStateServer;

    /**
     * 请求
     */
    @ApiOperation(value = "查看buff物品信息", tags = "TransactionController 交易信息")
    @RequestMapping(value = "/getInfoFromUrl", method = RequestMethod.POST)
    public SaResult getInfoFromUrl(@RequestParam("url") String url, @RequestBody Map<String, String> map, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String cookie = "";
        if (cookies != null) {
            for (Cookie cookieStr : cookies) {
                String key = cookieStr.getName();
                String value = cookieStr.getValue();
                cookie += key + "=" + value + ";";
            }
            cookie = cookie.substring(0, cookie.length() - 1);
        }
        return checkSyncStateServer.getInfoFromUrl(url, cookie, map);
    }

    /**
     * 请求
     */
    @ApiOperation(value = "查看buff物品信息和steam差价大于50%的物品", tags = "TransactionController 交易信息")
    @RequestMapping(value = "/get50UpGoods", method = RequestMethod.POST)
    public SaResult get50UpGoods(@RequestParam("url") String url, @RequestBody Map<String, String> map, HttpServletRequest request) throws InterruptedException {
        Cookie[] cookies = request.getCookies();
        String cookie = "";
        if (cookies != null) {
            for (Cookie cookieStr : cookies) {
                String key = cookieStr.getName();
                String value = cookieStr.getValue();
                cookie += key + "=" + value + ";";
            }
            cookie = cookie.substring(0, cookie.length() - 1);
        }
        return checkSyncStateServer.get50UpGoods(url, cookie, map);
    }

}
