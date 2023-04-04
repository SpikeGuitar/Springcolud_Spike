package com.spike.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.spike.entity.GisDriverBase;
import com.spike.entity.GisPictureBase;
import org.apache.poi.ss.formula.functions.T;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.ogr.ogr;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

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
    public GisDriverBase printDriverByShortName(String shortName) {
        GisDriverBase gisDriverBase = new GisDriverBase();
        gdal.AllRegister();
        Driver driver = gdal.GetDriverByName(shortName);
        gisDriverBase.setDriverInfo(JSON.toJSON(driver).toString());
        driver.Register();
        gisDriverBase.setShortName(driver.getShortName());
        gisDriverBase.setLongName(driver.getLongName());
        Class<?> methods = Driver.class;
        Method[] methodList = methods.getDeclaredMethods();
        List<String> list = new ArrayList<>();
        for (Method obj :methodList) {
            list.add(obj.getName());
        }
        gisDriverBase.setMethodList(list);
        return gisDriverBase;
    }

    public GisPictureBase printPictureInfoBySystemPath(String systemPath) {
        String srcFile = systemPath;
        gdal.AllRegister();
        GisPictureBase gisPictureBase = new GisPictureBase();
        Dataset dataSet = gdal.Open(srcFile);
        gisPictureBase.setDataSet(dataSet);
        int width = dataSet.getRasterXSize();
        gisPictureBase.setWidth(width);
        int height = dataSet.getRasterYSize();
        gisPictureBase.setHeight(height);
        int bandCount = dataSet.getRasterCount();
        gisPictureBase.setBandCount(bandCount);
        double[] geoTransform = dataSet.GetGeoTransform();
        gisPictureBase.setGeoTransform(geoTransform);
        String proj = dataSet.GetProjection();
        gisPictureBase.setProjection(proj);
        String describe = dataSet.GetDescription();
        gisPictureBase.setDescribe(describe);
        List<Band> bandList = new ArrayList<>();
        for (int i=1;i<=bandCount;i++) {
            Band band = dataSet.GetRasterBand(i);
            bandList.add(band);
        }
        gisPictureBase.setBandList(bandList);
        return gisPictureBase;
    }

}
