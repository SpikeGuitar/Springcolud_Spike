package com.spike;

import com.spike.utils.GdalUtils;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.gdalconst.gdalconstJNI;

public class GisApplication {

    public static void main(String[] args) {
        System.out.println("***************** 《开始执行测试程序》********************");
        GdalUtils gdalUtils = new GdalUtils();
        gdalUtils.cutPicture("D:\\DemoFile\\testTIFF.tiff","D:\\DemoFile\\testTIFF4.tiff","GTiff",0,0,150,150);
//        gdalUtils.printDriver();
//        gdalUtils.driverByShortName("GTiff");
//        GisPictureBase gisPictureBase = gdalUtils.cutPicture("D:\\DemoFile\\testTif.tiff");
//        System.out.println(JSON.toJSON(gisPictureBase));
        System.out.println("***************** 《测试程序关闭》***********************");

    }
}
