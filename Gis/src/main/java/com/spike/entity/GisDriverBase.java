package com.spike.entity;

import lombok.Data;
import org.gdal.gdal.Dataset;

import java.io.Serializable;
import java.util.List;

@Data
public class GisDriverBase implements Serializable {
//    driver shortName
    String shortName;

//    driver longName
    String longName;

//    driver info
    String driverInfo;

//    driver method list
    List<String> methodList;
}
