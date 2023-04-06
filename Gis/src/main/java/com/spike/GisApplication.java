package com.spike;

import com.alibaba.fastjson.JSON;
import com.spike.utils.GdalUtils;
import com.spike.utils.GeoToolUtils;
import org.gdal.gdalconst.gdalconstConstants;
import org.gdal.gdalconst.gdalconstJNI;

import java.io.IOException;
import java.util.List;

public class GisApplication {

    public static void main(String[] args) throws IOException {
        System.out.println("***************** 《开始执行测试程序》********************");
//        GdalUtils gdalUtils = new GdalUtils();
//        gdalUtils.cutPicture("D:\\DemoFile\\testTIFF.tiff","D:\\DemoFile\\testTIFF4.tiff","GTiff",0,0,150,150);
//        gdalUtils.printDriver();
//        gdalUtils.driverByShortName("GTiff");
//        GisPictureBase gisPictureBase = gdalUtils.cutPicture("D:\\DemoFile\\testTif.tiff");
//        System.out.println(JSON.toJSON(gisPictureBase));
        GeoToolUtils geoToolUtils = new GeoToolUtils();
        List list = geoToolUtils.readShpInfo("D:\\DemoFile\\武汉市\\武汉市.shp");
        System.out.println(JSON.toJSON(list));
//        geoToolUtils.showMap("shp");
        System.out.println("***************** 《测试程序执行完成》***********************");

    }
}
