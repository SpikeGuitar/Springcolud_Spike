package com.spike.Controller;

import com.alibaba.fastjson.JSON;
import com.spike.utils.GdalUtils;
import com.spike.utils.GeoToolUtils;
import com.vividsolutions.jts.geom.Geometry;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.geotools.map.MapContent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @PACKAGE_NAME: com.spike.Controller
 * @NAME: GIsController
 * @USER: spike
 * @DATE: 2023/4/6 15:57
 * @PROJECT_NAME: Springcolud_Spike
 */

@Slf4j
@RestController
@RequestMapping("/GisController")
public class GIsController {

    @ApiOperation("openShp")
    @PostMapping("/openShp")
    public Object openShp(@RequestBody Map<String, Object> map){
        GdalUtils gdalUtils = new GdalUtils();
        String strVectorFile = map.get("strVectorFile").toString();
        String shpDriverName = map.get("shpDriverName").toString();
        String jsonString =gdalUtils.openShp(strVectorFile,shpDriverName);
        Object jsonObject = JSON.parseObject(jsonString);
        return jsonObject;
    }

    @ApiOperation("readShpInfo")
    @PostMapping("/readShpInfo")
    public Object readShpInfo(@RequestBody Map<String, Object> map) throws IOException {
        GeoToolUtils geoToolUtils = new GeoToolUtils();
        String filePath = map.get("filePath").toString();
        List list = geoToolUtils.readShpInfo(filePath);
        Object jsonObject = JSON.toJSON(list);
        return jsonObject;
    }

    @ApiOperation("writeShpByGeom")
    @PostMapping("/writeShpByGeom")
    public void writeShpByGeom(@RequestBody Map<String, Object> map) throws Exception {
        GeoToolUtils geoToolUtils = new GeoToolUtils();
        String geomString = map.get("geometry").toString();
        Geometry geometry =geoToolUtils.wktStrToGeometry(geomString);
        geoToolUtils.writeShpByGeom(map,geometry);
    }

    @ApiOperation("getMapContentByPath")
    @PostMapping("/getMapContentByPath")
    public void getMapContentByPath(@RequestBody Map<String, Object> map, HttpServletResponse response) throws Exception {
        String filePath = map.get("filePath").toString();
        String destImagePath = map.get("destImagePath").toString();
        String color = map.get("color").toString();
        GeoToolUtils geoToolUtils = new GeoToolUtils();
        geoToolUtils.shp2Image(filePath,destImagePath,color,response);
    }
}
