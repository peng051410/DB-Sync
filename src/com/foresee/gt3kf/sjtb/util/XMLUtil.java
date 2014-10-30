package com.foresee.gt3kf.sjtb.util;

import org.apache.ddlutils.custom.bean.Table;
import org.apache.ddlutils.custom.bean.TableDataCount;
import org.apache.ddlutils.custom.util.CustomXmlParse;

import java.util.List;

/**
 * Created by Administrator on 2014/10/28.
 */
public class XMLUtil {

    private String filePath;

    public XMLUtil(String filePath) {
        this.filePath = filePath;
    }

    /**
     * 获取表记录数
     * @param tableName
     * @return
     */
    public int getTableCount(String tableName){
        try{
            CustomXmlParse xmlParse = new CustomXmlParse();
            TableDataCount tableDataCount = xmlParse.getDigester(filePath);
            List<Table> list = tableDataCount.getTableList();
            for(Table table : list ){
                if(tableName.equalsIgnoreCase(table.getTableName())){
                    return table.getCount();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
        return 0;
    }
}
