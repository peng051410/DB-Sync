package com.foresee.gt3kf.sjtb.service.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;

import com.foresee.gt3kf.sjtb.service.IDdlUtilsService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


@Service("sjtbService")
public class DdlUtilsService implements IDdlUtilsService {

    private static Logger logger = Logger.getLogger(DdlUtilsService.class);


    @Resource(name = "gt3kf_dataSource")
    private DataSource dataSource;

    @Resource(name = "etp_dataSource")
    private DataSource dataSource_etp;

    String userName;
    String sid;
    String url;

    public Map initServerDB() {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            DatabaseMetaData dmd = conn.getMetaData();
            url = dmd.getURL();
            userName = dmd.getUserName().toUpperCase();
            String tempArray[] = url.split(":");
            sid = tempArray[5].toUpperCase();
            Map dsMap = new HashMap();
            dsMap.put("ds", dataSource);
            dsMap.put("sid", sid);
            dsMap.put("userName", userName);
            return dsMap;
        } catch (SQLException e) {
            logger.info("数据同步加载数据库实例 获取数据库信息异常" + e.getMessage());
            return null;
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn = null;
            }
        }
    }

    @Override
    public Map initEtpDB() {
        Connection conn = null;
        try {
            conn = dataSource_etp.getConnection();
            DatabaseMetaData dmd = conn.getMetaData();
            url = dmd.getURL();
            userName = dmd.getUserName().toUpperCase();
            String tempArray[] = url.split("_");
            sid = tempArray[1].toUpperCase();
            Map dsMap = new HashMap();
            dsMap.put("ds", dataSource_etp);
            dsMap.put("sid", "ETP_"+sid);
            dsMap.put("userName", userName);
            return dsMap;
        } catch (SQLException e) {
            logger.info("数据同步加载数据库实例 获取数据库信息异常" + e.getMessage());
            return null;
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn = null;
            }
        }
    }

    @Override
    public List getTableList(String owner ,List list) {
        StringBuffer sb = new StringBuffer();
        sb.append("( ");
        for (int i = 0; i <list.size() ; i++) {
            sb.append("'"+list.get(i)+"'");
            if(i!=list.size()-1){
                sb.append(",");
            }
        }
        sb.append(" )");
        List<String> tableNameList = null;
        String sql = "select table_name as TABLENAME from all_tables where owner = '"+owner+"' and table_name in "+sb.toString();
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (null != rs) {
                tableNameList = new ArrayList<String>();
                while (rs.next()) {
                    tableNameList.add(rs.getString("TABLENAME"));
                }
                rs.close();
            }
        } catch (SQLException e) {
            logger.info("数据同步加载数据库实例 获取数据库信息异常" + e.getMessage());
        } finally {
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                conn = null;
            }
        }
        return tableNameList;
    }

    @Override
    public List stringToList(String[] arr) {
        List<String> tableNameList = new ArrayList<String>();
        for (String str : arr) {
            tableNameList.add(str);
        }
        return tableNameList;
    }



}
