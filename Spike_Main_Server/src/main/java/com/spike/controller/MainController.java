package com.spike.controller;

import com.spike.UtilClass.ResponseResult;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/MainController")
public class MainController {

    public static final String SUCCESS = "操作成功!";

    public static final String SPIKE_SERVER = "http://SPIKE-SERVICE";

    @Autowired
    private RestOperations restTemplate;

    @ApiOperation("useSpikeServer")
    @PostMapping("/useSpikeServer")
    public ResponseResult generateAttachmentPackage(@RequestBody Map<String,Object> map) {
        Map resultStr = restTemplate.postForObject(SPIKE_SERVER+"/spike/UtilController/getQRCode",map,Map.class);
        return this.getResponseResult(resultStr.toString());
    }

    protected ResponseResult<Object> getResponseResult(String errMsg) {
        return ResponseResult.builder().errcode(0l).errmsg(errMsg).build();
    }

}
