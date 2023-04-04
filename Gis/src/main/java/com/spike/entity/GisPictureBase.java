package com.spike.entity;

import lombok.Data;
import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;

import java.io.Serializable;
import java.util.List;

@Data
public class GisPictureBase implements Serializable {
//    picture width
    int width;

//    picture height
    int height;

//    picture bandCount
    int bandCount;

//    picture geoTransform
    double[] geoTransform;

//    picture projection
    String projection;

//    picture describe
    String describe;

//    gdal dataSet
    Dataset dataSet;

//    band list
    List<Band> bandList;
}
