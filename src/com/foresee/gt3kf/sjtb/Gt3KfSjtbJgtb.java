package com.foresee.gt3kf.sjtb;

import com.foresee.gt3kf.sjtb.bean.Const;
import com.foresee.gt3kf.sjtb.bean.TableQuerySql;
import com.foresee.gt3kf.sjtb.service.IDdlUtilsService;
import com.foresee.gt3kf.sjtb.util.FileUtil;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.io.DatabaseIO;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/26.
 * Main方面内定义数组，根据数组，将指定服务端单张或多张表的结构同步存放data/sjtb文件夹下。
 * 读取客户端enterTaxPlat\config\ jdbc.properties文件夹获取客户端数据连接。
 * 先判断表是否存在，存在先删除再导入。
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/foresee/gt3kf/sjtb/config/application*.xml")
public class Gt3KfSjtbJgtb {

    Platform pf;
    private static String[] _defaultTableTypes = {"TABLE"};

    String tableSchemaFileName = Const.DB_XML;

    @Resource
    IDdlUtilsService ddlUtilsService;

    @Test
    //导出服务器指定表结构
    public void exportServerTable() throws Exception {
        FileUtil.createFileByPath(tableSchemaFileName);
        Map obj = ddlUtilsService.initServerDB();
        DataSource ds = (DataSource) obj.get("ds");
        String sid = (String) obj.get("sid");
        String userName = (String) obj.get("userName");
        pf = PlatformFactory.createNewPlatformInstance(ds);
        List tableList = ddlUtilsService.getTableList("DB_DZBS",TableQuerySql.getTables());

        Database db = pf.readModelFromDatabase(sid, null, userName, _defaultTableTypes, tableList);
        new DatabaseIO().write(db,tableSchemaFileName);
    }

    @Test
    public void importTableToClient() throws Exception{
        //获取缓存到本地的数据结构文件
        DatabaseIO di = new DatabaseIO();
        Database database = di.read(tableSchemaFileName);

        //初始化本地数据库
        Map obj = ddlUtilsService.initEtpDB();
        DataSource ds = (DataSource) obj.get("ds");
        pf = PlatformFactory.createNewPlatformInstance(ds);
        //先删除再导入
        pf.createTables(database,true,true);
    }

    public List<String> toStringList(Table[] tables){
        List<String> tablesNames = new ArrayList<String>();
        for (Table t : tables) {
            tablesNames.add(t.getName());
        }
        return  tablesNames;
    }


    public String fetchDjxh() {
        String path = "e:\\Database";
        File fileml = new File(path);
        File[] filemls = fileml.listFiles();
        String djxh = "";
        // 先查询该目录下有几个ETP_DJXH的数据文件
        for (int i = 0; i < filemls.length; i++) {
            // 去除ETP_lib文件夹即可
            if (filemls[i].isDirectory() && filemls[i].getName().contains("ETP_")) {
                djxh = filemls[i].getName().split("_")[1];
                break;
            }
        }
        return djxh;
    }

    @Test
    public void getTables(){
        List list = TableQuerySql.getTables();
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
    }
}
