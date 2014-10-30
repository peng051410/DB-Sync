package com.foresee.gt3kf.sjtb;

import com.foresee.gt3kf.sjtb.bean.Const;
import com.foresee.gt3kf.sjtb.bean.TableQuerySql;
import com.foresee.gt3kf.sjtb.service.IDdlUtilsService;
import com.foresee.gt3kf.sjtb.util.FileUtil;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.custom.io.CustomDataWriter;
import org.apache.ddlutils.custom.platform.oracle.CustomOracle8Platform;
import org.apache.ddlutils.io.*;
import org.apache.ddlutils.model.Database;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.xml.sax.InputSource;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/26.
 * 将指定服务端数据同步存放data/sjtb文件夹下。读取客户端enterTaxPlat\config\ jdbc.properties文件夹获取客户端数据库连接。
 * 先删除数据再将数据导入到客户端。
 * 导出时，在判断表的数据量，超过两千条，打印出提示，数据量太大。
 * 检查表中是否含有DJXH字段，如果有，需要根据DJXH导出对应的数据。
 * DJXH根据jdbc.properties中的链接截取
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/foresee/gt3kf/sjtb/config/application*.xml")
public class Gt3KfSjtbSjtbByTableName {

    private static String[] _defaultTableTypes = {"TABLE"};

    String tableDataFileName = Const.DB_DATA_XML;
    String tableDataCountFileName = Const.DB_DATA_COUNT_XML;
    String tableSchemaFileName = Const.DB_XML;

    @Resource
    IDdlUtilsService ddlUtilsService;

    @Test
    //全表导出
    public void exoprtData() throws Exception {
        FileUtil.createFileByPath(tableDataFileName);
        Map obj = ddlUtilsService.initServerDB();
        DataSource ds = (DataSource) obj.get("ds");
        String sid = (String) obj.get("sid");
        String userName = (String) obj.get("userName");
        Platform pf = PlatformFactory.createNewPlatformInstance(ds);
        List tableList = ddlUtilsService.getTableList("DB_DZBS", TableQuerySql.getTables());
        Database db = pf.readModelFromDatabase(sid, null, userName, _defaultTableTypes, tableList);


        File file = new File(tableDataFileName);
        FileOutputStream fos = new FileOutputStream(file, false);

        CustomDataWriter dw = new CustomDataWriter(fos);
        dw.writeDocumentStart();
        List<Map<String, Object>> tableCount = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < tableList.size(); i++) {
            String tempSql = "select * from " + tableList.get(i) + "&" + tableList.get(i);
            Iterator it = pf.query(db, tempSql);
//            dw.write(it);
            Map map = dw.writeAndRetrunList(it, (String) tableList.get(i));
            tableCount.add(map);
        }
        dw.writeDocumentEnd();

        //生成表记录数
        File countFile = new File(tableDataCountFileName);
        FileOutputStream fos_count = new FileOutputStream(countFile, false);

        CustomDataWriter dw_count = new CustomDataWriter(fos_count);
        dw_count.custom_writeDocumentStart("tableDataCount");
        dw_count.writeTableCountXml(tableCount);
        dw_count.custom_writeDocumentEnd();
    }

    @Test
    //按条件导出
    public void exoprtSelectData() throws Exception {
        FileUtil.createFileByPath(tableDataFileName);
        Map obj = ddlUtilsService.initServerDB();
        DataSource ds = (DataSource) obj.get("ds");
        String sid = (String) obj.get("sid");
        String userName = (String) obj.get("userName");
        Platform pf = PlatformFactory.createNewPlatformInstance(ds);
        List tableList = ddlUtilsService.getTableList("DB_DZBS", TableQuerySql.getTables());
        Database db = pf.readModelFromDatabase(sid, null, userName, _defaultTableTypes, tableList);


        File file = new File(tableDataFileName);
        FileOutputStream fos = new FileOutputStream(file, false);

        CustomDataWriter dw = new CustomDataWriter(fos);
        dw.writeDocumentStart();
        List<Map<String,Object>> tableCount = new ArrayList<Map<String,Object>>();
        for (int i = 0; i < tableList.size(); i++) {
            String tableName = (String) tableList.get(i);
            String tempSql = TableQuerySql.getTableQuerySql(tableName)+"&"+tableName;
            Iterator it = pf.query(db, tempSql);
//            dw.write(it);
            Map map = dw.writeAndRetrunList(it,tableName);
            tableCount.add(map);
        }
        dw.writeDocumentEnd();

        //生成表记录数
        File countFile = new File(tableDataCountFileName);
        FileOutputStream fos_count = new FileOutputStream(countFile, false);

        CustomDataWriter dw_count = new CustomDataWriter(fos_count);
        dw_count.custom_writeDocumentStart("tableDataCount");
        dw_count.writeTableCountXml(tableCount);
        dw_count.custom_writeDocumentEnd();
    }

    @Test
    //导入数据到本地库
    public void importData() throws Exception {
        Map obj = ddlUtilsService.initEtpDB();
        DataSource ds = (DataSource) obj.get("ds");
        Platform pf = PlatformFactory.createNewPlatformInstance(ds);

        //加载结构表
        InputStream is = new FileInputStream(new File(tableSchemaFileName));
        Database db = new DatabaseIO().read(new InputSource(is));
        Connection conn = pf.borrowConnection();

        //首先删除存在于表结构中的表的数据
        CustomOracle8Platform cpf = new CustomOracle8Platform();
        cpf.deleteData(db,pf);

        //获取本地数据库提交模式
        boolean autoCommit = conn.getAutoCommit();
        conn.setAutoCommit(false);
        DataToDatabaseSink dtds = new DataToDatabaseSink(pf, db);
        dtds.setConnection(conn);
        dtds.setUseBatchMode(true);// 是否使用批量
        dtds.setBatchSize(100);// 批量的长度
        dtds.start();
        DataReader dr = new DataReader();
        dr.setModel(db);
        dr.setSink(dtds);
        dr.parse(tableDataFileName);
        dtds.end();
        conn.commit();
        conn.setAutoCommit(autoCommit);
        cpf.returnConnection(conn);
        System.out.println("数据导入完毕");
    }

    @Test
    public void writeData() throws Exception {
        Map obj = ddlUtilsService.initEtpDB();
        DataSource ds = (DataSource) obj.get("ds");
        Platform pf = PlatformFactory.createNewPlatformInstance(ds);
        //加载结构表
        InputStream is = new FileInputStream(new File(tableSchemaFileName));
        Database db = new DatabaseIO().read(new InputSource(is));

        //首先删除存在于表结构中的表的数据
        CustomOracle8Platform cpf = new CustomOracle8Platform();
        cpf.deleteData(db,pf);

        //获取外部数据库数据文件
        File dump = new File(tableDataFileName);
        //构造一个InputStream输入流数组，根据实际需要可以添加多个
        FileInputStream fio = new FileInputStream(dump);
        InputStream infi[] = new InputStream[1];
        infi[0] = fio;
        DatabaseDataIO databasedataIO = new DatabaseDataIO();
//        //设定导入不启批量导入
//        databasedataIO.setUseBatchMode(false);
//        //设定遇到重复数据时直接忽略错误
//        databasedataIO.setFailOnError(false);
        //数据导入
        databasedataIO.writeDataToDatabase(pf, db, infi);
        System.out.println("数据导入完毕");
    }

}
