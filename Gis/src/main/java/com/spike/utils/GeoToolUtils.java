package com.spike.utils;

import com.alibaba.fastjson.JSON;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.WKTReader;
import lombok.extern.log4j.Log4j;

import org.geotools.data.FeatureWriter;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.Transaction;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.spike.utils
 * @NAME: GeoToolUtils
 * @USER: spike
 * @DATE: 2023/4/6 11:57
 * @PROJECT_NAME: Springcolud_Spike
 */
@Log4j
public class GeoToolUtils {

    /**
     * 输出shp文件的信息
     *
     * @param filePath 文件系统地址
     * @throws IOException
     */
    public List<Map<String, Object>> readShpInfo(String filePath) throws IOException {
        //加载文件
        File file = new File(filePath);
        if (!file.exists()) {
            log.info("文件不存在");
            return null;
        }
        //map记录shapefile key-value数据
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        //通过store获取featurecollection
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        SimpleFeatureCollection simpleFeatureCollection = featureSource.getFeatures();
        SimpleFeatureIterator itertor = simpleFeatureCollection.features();
        //遍历featurecollection
        while (itertor.hasNext()) {
            Map<String, Object> data = new HashMap<String, Object>();
            SimpleFeature feature = itertor.next();
            Collection<Property> p = feature.getProperties();
            Iterator<Property> it = p.iterator();
            //遍历feature的properties
            while (it.hasNext()) {
                Property pro = it.next();
                if (null != pro && null != pro.getValue()) {
                    String field = pro.getName().toString();
                    String value = pro.getValue().toString();
                    field = field.equals("the_geom") ? "wkt" : field;
                    byte[] bytes = value.getBytes("iso8859-1");
                    value = new String(bytes, "utf-8");
                    data.put(field, value);
                }
            }
            list.add(data);
        }
        return list;
    }

    /**
     * 打开 后缀名地图
     *
     * @param extension 后缀名
     * @throws IOException
     */
    public void showMap(String extension) throws IOException {
        File file = JFileDataStoreChooser.showOpenFile(extension, null);
        if (file == null) {
            return;
        }
        FileDataStore store = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        // Create a map content and add our shapefile to it
        MapContent map = new MapContent();
        map.setTitle("Quickstart");
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        Layer layer = new FeatureLayer(featureSource, style);
        map.addLayer(layer);
        // Now display the map
        JMapFrame.showMap(map);
    }

    /**
     * 将一个几何对象写进shapefile
     * @param map
     * @param geometry
     */
    public void writeShpByGeom(Map<String,Object> map, Geometry geometry) throws Exception{
        String filePath = map.get("filePath").toString();
        String name = map.get("name").toString();
        Integer adcode = (Integer) map.get("adcode");
        String des = map.get("des").toString();
        if(filePath.isEmpty()||adcode == 0||name.isEmpty()||des.isEmpty()){
            log.error("禁止写入,Map缺少数值请仔细检查！");
        }
        ShapefileDataStore ds = getshpDS(filePath, geometry);
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(ds.getTypeNames()[0],
                Transaction.AUTO_COMMIT);
        // Interface SimpleFeature：一个由固定列表值以已知顺序组成的SimpleFeatureType实例。
        SimpleFeature feature = writer.next();
        feature.setAttribute("name", name);
        feature.setAttribute("path", filePath);
        feature.setAttribute("the_geom", geometry);
        feature.setAttribute("id",adcode);
        feature.setAttribute("des", des);
        System.out.println("========= 写入【"+geometry.getGeometryType()+"】成功 ！=========");
        // 写入
        writer.write();
        // 关闭
        writer.close();
        // 释放资源
        ds.dispose();
    }

    /**
     * 拿到配置好的DataStore
     * @param filePath
     * @param geometry
     * @return
     * @throws IOException
     */
    private static ShapefileDataStore getshpDS(String filePath, Geometry geometry) throws IOException {
        // 1.创建shape文件对象
        File file = new File(filePath);
        Map<String, Serializable> params = new HashMap<>();
        // 2、用于捕获参数需求的数据类 URLP:url to the .shp file.
        params.put(ShapefileDataStoreFactory.URLP.key, file.toURI().toURL());
        // 3、创建一个新的数据存储【如果存在，则不创建】
        ShapefileDataStore ds = (ShapefileDataStore) new ShapefileDataStoreFactory().createNewDataStore(params);
        // 4、定义图形信息和属性信息 -- SimpleFeatureTypeBuilder 构造简单特性类型的构造器
        SimpleFeatureTypeBuilder tBuilder = new SimpleFeatureTypeBuilder();
        // 5、设置 -- WGS84:一个二维地理坐标参考系统，使用WGS84数据
        tBuilder.setCRS(DefaultGeographicCRS.WGS84);
        tBuilder.setName("shapefile");
        // 添加名称
        tBuilder.add("name", String.class);
        // 添加shp所在目录名称
        tBuilder.add("path", String.class);
        // 添加 一个几何对象
        tBuilder.add("the_geom", geometry.getClass());
        // 添加一个id
        tBuilder.add("id", Long.class);
        // 添加描述
        tBuilder.add("des", String.class);
        // 设置此数据存储的特征类型
        ds.createSchema(tBuilder.buildFeatureType());
        // 设置编码
        ds.setCharset(Charset.forName("UTF-8"));
        return ds;
    }

    /**
     * wkt字符串转Geometry
     *
     * @param wkt 字符串
     * @return Geometry
     */
    public Geometry wktStrToGeometry(String wkt) {
        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        WKTReader reader = new WKTReader(geometryFactory);
        try {
            return reader.read(wkt);
        } catch (com.vividsolutions.jts.io.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

}
