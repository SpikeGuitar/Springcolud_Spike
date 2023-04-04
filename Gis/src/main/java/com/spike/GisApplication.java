package com.spike;

import com.spike.utils.GdalUtils;

public class GisApplication {

    public static void main(String[] args) {
        System.out.println("***************** 《开始执行测试程序》********************");
        GdalUtils gdalUtils = new GdalUtils();
        gdalUtils.printPictureInfoBySystemPath("D:\\DemoFile\\testTif.tiff");
        System.out.println("***************** 《测试程序关闭》***********************");
    }
}
