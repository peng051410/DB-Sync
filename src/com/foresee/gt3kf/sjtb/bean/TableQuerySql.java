package com.foresee.gt3kf.sjtb.bean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2014/10/29.
 */
public enum TableQuerySql {

    /**表定义
     * 第一个是表名
     * 第二个是查询语句（一般是单表查询）
     * 第三个为false时，代表不导出局端表结构，表数据到本地
    **/
    DM_DJ_SWDJLX("DM_DJ_SWDJLX","select * from DM_DJ_SWDJLX where rownum between 0 and 1000",true),
    DM_ZS_SKSX("DM_ZS_SKSX","select * from DM_ZS_SKSX where rownum between 0 and 1000",true),
    DM_FP_FPZT("DM_FP_FPZT","select * from DM_FP_FPZT where rownum between 0 and 1000",true),
    DM_DJ_DJZCLX("DM_DJ_DJZCLX","select * from DM_DJ_DJZCLX where rownum between 0 and 1000",true),
    DM_DJ_GHLB("DM_DJ_GHLB","select * from DM_DJ_GHLB where rownum between 0 and 1000",true),
    DM_GY_GJHDQ("DM_GY_GJHDQ","select * from DM_GY_GJHDQ where rownum between 0 and 1000",true),
    DM_FP_FPGPFS("DM_FP_FPGPFS","select * from DM_FP_FPGPFS where rownum between 0 and 1000",true),
    DM_GY_HSFS("DM_GY_HSFS","select * from DM_GY_HSFS where rownum between 0 and 1000",true),
    DM_GY_HY("DM_GY_HY","select * from DM_GY_HY where rownum between 0 and 1000",true),
    DM_GY_DWLSGX("DM_GY_DWLSGX","select * from DM_GY_DWLSGX where rownum between 0 and 1000",true);


    /** 表名 **/
    private String tableName;
    /** 查询语句 **/
    private String querySql;
    /** 是否加入表集合 **/
    private boolean scan;

    TableQuerySql(String tableName,String querySql,boolean scan) {
        this.tableName = tableName;
        this.querySql = querySql;
        this.scan = scan;
    }

    public String getTableName() {
        return tableName;
    }

    public String getQuerySql() {
        return querySql;
    }

    public boolean isScan() {
        return scan;
    }

    /**
     * 获取所有表
     * @return
     */
    public static List getTables() {
        List<String> tableNameList = new ArrayList<String>();
        try {
            Object[] objs = TableQuerySql.class.getEnumConstants();
            for (Object obj : objs) {
                Method m = obj.getClass().getDeclaredMethod("values", null);
                Object[] result = (Object[]) m.invoke(obj, null);
                List list = Arrays.asList(result);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Object objOne = it.next();
                    Field nameField = objOne.getClass().getDeclaredField("tableName");
                    Field scanField = objOne.getClass().getDeclaredField("scan");
                    nameField.setAccessible(true);
                    scanField.setAccessible(true);
                    if((Boolean) scanField.get(objOne)){
                        String name = (String) nameField.get(objOne);
                        tableNameList.add(name);
                    }
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableNameList;
    }

    /**
     * 获取所有表
     * @return
     */
    public static String getTableQuerySql(String tableName) {
        try {
            Object[] objs = TableQuerySql.class.getEnumConstants();
            for (Object obj : objs) {
                Method m = obj.getClass().getDeclaredMethod("values", null);
                Object[] result = (Object[]) m.invoke(obj, null);
                List list = Arrays.asList(result);
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Object objOne = it.next();
                    Field nameField = objOne.getClass().getDeclaredField("tableName");
                    Field sqlField = objOne.getClass().getDeclaredField("querySql");
                    nameField.setAccessible(true);
                    String name = (String) nameField.get(objOne);
                    if(tableName.equalsIgnoreCase(name)){
                        String sql = (String)sqlField.get(objOne);
                        return sql;
                    }
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
