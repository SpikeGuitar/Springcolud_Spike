package com.spike;

import com.alibaba.fastjson.JSON;
import com.spike.entity.GisPictureBase;
import com.spike.utils.GdalUtils;

public class GisApplication {

    public static void main(String[] args) {
        System.out.println("***************** 《开始执行测试程序》********************");
        GdalUtils gdalUtils = new GdalUtils();
        GisPictureBase gisPictureBase = gdalUtils.printPictureInfoBySystemPath("D:\\DemoFile\\testTif.tiff");
        System.out.println(JSON.toJSON(gisPictureBase));
        System.out.println("***************** 《测试程序关闭》***********************");
    }
}
