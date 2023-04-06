package com.spike.utils;

import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j;
import org.geotools.data.FileDataStore;
import org.geotools.data.FileDataStoreFinder;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.styling.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;


import java.io.File;
import java.io.IOException;
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
}
