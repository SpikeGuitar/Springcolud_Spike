package com.spike.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.poi.ss.formula.functions.T;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.ogr.ogr;

import java.lang.reflect.Method;

public class GdalUtils {

    /**
     * 获取所有驱动的名称并打印
     */
    public void printDriver() {
        gdal.AllRegister();
        int count = ogr.GetDriverCount();
        for (int i = 0; i < count; i++) {
            org.gdal.ogr.Driver driver = ogr.GetDriver(i);
            System.out.print( JSON.toJSON(driver).toString()+ "\n");
        }
        gdal.GDALDestroyDriverManager();
    }

    /**
     * 按照输入的驱动简称 输出驱动的信息和全部方法
     * @param shortName
     */
    public void printDriverByShortName(String shortName) {
        gdal.AllRegister();
        Driver driver = gdal.GetDriverByName(shortName);
        System.out.println("驱动信息: "+JSON.toJSON(driver));
        driver.Register();
        System.out.println("驱动简称："+driver.getShortName());
        System.out.println("驱动全称："+driver.getLongName());
        Class<?> methods = Driver.class;
        Method[] methodList = methods.getDeclaredMethods();
        System.out.println("驱动全部方法：");
        for (Method obj : methodList) {
            System.out.println(obj+"\n");
        }
    }

    public void printPictureInfoBySystemPath(String systemPath) {
        String srcFile = systemPath;
        gdal.AllRegister();
        Dataset dataSet = gdal.Open(srcFile);
        System.out.println("图像数据集："+JSON.toJSONString(dataSet));
        int width = dataSet.getRasterXSize();
        System.out.println("宽："+width);
        int height = dataSet.getRasterYSize();
        System.out.println("高："+height);
        int bandCount = dataSet.getRasterCount();
        System.out.println("波段数量："+bandCount);
        double[] geoTransform = dataSet.GetGeoTransform();
        System.out.println("空间信息："+JSON.toJSON(geoTransform));
        String proj = dataSet.GetProjection();
        System.out.println("投影信息："+proj);
        String describe = dataSet.GetDescription();
        System.out.println("描述信息："+describe);
    }

}
