package com.foresee.gt3kf.sjtb.bean;

import java.util.ResourceBundle;

/**
 * Created by Administrator on 2014/10/30.
 */
public class Const {

    private static ResourceBundle systemConfig = ResourceBundle.getBundle("jdbc");

    public static String DB_XML = systemConfig.getString("file.dbxml");
    public static String DB_DATA_XML = systemConfig.getString("file.dbxml.data");
    public static String DB_DATA_COUNT_XML = systemConfig.getString("file.dbxml.data.count");
}
