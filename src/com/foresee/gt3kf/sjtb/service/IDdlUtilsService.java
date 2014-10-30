package com.foresee.gt3kf.sjtb.service;


import java.util.List;
import java.util.Map;

public interface IDdlUtilsService {

    public Map initServerDB();
    public Map initEtpDB();

    /**
     * 获取表集合
     * @param owner
     * @param tables
     * @return
     */
    public List getTableList(String owner,List tables);

    public List stringToList(String arr[]);



}