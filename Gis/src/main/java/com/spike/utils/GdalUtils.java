package com.spike.utils;

import com.alibaba.fastjson.JSON;
import com.spike.entity.GisDriverBase;
import com.spike.entity.GisPictureBase;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.Driver;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstJNI;
import org.gdal.ogr.DataSource;
import org.gdal.ogr.Feature;
import org.gdal.ogr.Geometry;
import org.gdal.ogr.Layer;
import org.gdal.ogr.ogr;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @PACKAGE_NAME: com.spike.utils
 * @NAME: GdalUtils
 * @USER: spike
 * @DATE: 2023/4/4 11:57
 * @PROJECT_NAME: Springcolud_Spike
 */
public class GdalUtils {

    /**
     * 获取所有驱动的名称并打印
     */
    public void printDriver() {
        gdal.AllRegister();
        int count = ogr.GetDriverCount();
        for (int i = 0; i < count; i++) {
            Driver driver = gdal.GetDriver(i);
            System.out.print(JSON.toJSON(driver).toString() + "\n");
        }
        gdal.GDALDestroyDriverManager();
    }

    /**
     * 按照输入的驱动简称 输出驱动的信息和全部方法
     *
     * @param shortName 驱动简称
     * @return 驱动数据信息
     */
    public GisDriverBase driverByShortName(String shortName) {
        Driver driver = getDrive(shortName);
        GisDriverBase gisDriverBase = new GisDriverBase();
        gisDriverBase.setDriverInfo(JSON.toJSON(driver).toString());
        driver.Register();
        gisDriverBase.setShortName(driver.getShortName());
        gisDriverBase.setLongName(driver.getLongName());
        Class<?> methods = Driver.class;
        Method[] methodList = methods.getDeclaredMethods();
        List<String> list = new ArrayList<>();
        for (Method obj : methodList) {
            list.add(obj.getName());
        }
        gisDriverBase.setMethodList(list);
        return gisDriverBase;
    }

    /**
     * 获取目标图片数据信息
     *
     * @param systemPath 目标图片 系统路径
     * @return 目标图片数据信息
     */
    public GisPictureBase pictureInfoBySystemPath(String systemPath) {
        gdal.AllRegister();
        GisPictureBase gisPictureBase = new GisPictureBase();
        Dataset dataSet = gdal.Open(systemPath);
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
        for (int i = 1; i <= bandCount; i++) {
            Band band = dataSet.GetRasterBand(i);
            bandList.add(band);
        }
        gisPictureBase.setBandList(bandList);
        return gisPictureBase;
    }

    /**
     * 获取图像对图像进行裁剪
     *
     * @param oldSystemPath 目标图像系统路径
     * @param currentSystemPath 生成图像系统路径
     * @param shortName 驱动简称
     * @param startX 水平位移像素
     * @param startY 上下平移像素
     * @param width 生成图像宽
     * @param height 生成图像长
     */
    public void cutPicture(String oldSystemPath, String currentSystemPath, String shortName,int startX,int startY,int width,int height) {
        Driver driver = getDrive(shortName);
//        获取数据集
        Dataset oldDataSet = gdal.Open(oldSystemPath);
//        获取波段
        int bandCount = oldDataSet.getRasterCount();
        Dataset currentDataset = driver.Create(currentSystemPath, width, height, bandCount, oldDataSet.GetRasterBand(1).GetRasterDataType());
        currentDataset.SetProjection(oldDataSet.GetProjection());
        currentDataset.SetGeoTransform(oldDataSet.GetGeoTransform());
        //按band读取
        for (int i = 0; i < height; i++) {
//            按行读取
            for (int j = 1; j <= bandCount; j++) {
                Band orgBand = oldDataSet.GetRasterBand(j);
                int[] cache = new int[width];
//              从位置x开始，只读一行  注：gdalconstConstants类 在新版本gdal中已经无法使用 需要使用gdalconstJNI类代替
                orgBand.ReadRaster(startX, startY + i, width,1,gdalconstJNI.GDT_Int32_get(), cache);
                Band desBand = currentDataset.GetRasterBand(j);
//              从左上角开始，只写一行
                desBand.WriteRaster(0, i, width, 1,gdalconstJNI.GDT_Int32_get(), cache);
                desBand.FlushCache();
            }
        }
//释放资源
        oldDataSet.delete();
        currentDataset.delete();
    }

    /**
     * 获取整个图层的json数据
     *
     * @param strVectorFile 文件系统路径
     * @return
     */
    public String openShp(String strVectorFile,String shpDriverName) {
        Driver shpDriver =getDrive(shpDriverName);
        if (shpDriver == null) {
            throw new RuntimeException(shpDriverName+ " 驱动不可用！\n");
        }
        //获取数据源
        DataSource shpDataSource = ogr.Open(strVectorFile,0);
        if (shpDataSource == null)
        {
            throw new RuntimeException("打开文件【"+ strVectorFile + "】失败！" );
        }
        //获取图层0
        Layer shpLayer = shpDataSource.GetLayerByIndex(0);
        if (shpLayer == null)
        {
            throw new RuntimeException("获取shp图层失败！\n");
        }
        //转化为json
        Feature feature = shpLayer.GetNextFeature();
        Geometry geometry = feature.GetGeometryRef();
        String json = geometry.ExportToJson();
        shpDataSource.delete();
        gdal.GDALDestroyDriverManager();
        return json;
    }

    public Driver getDrive(String shortName){
        //准备，注册驱动
        gdal.AllRegister();;
        //获取驱动
        Driver driver = gdal.GetDriverByName(shortName);
        return driver;
    }
}
