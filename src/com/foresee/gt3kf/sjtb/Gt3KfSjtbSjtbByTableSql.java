package com.foresee.gt3kf.sjtb;

import com.foresee.gt3kf.sjtb.bean.Const;
import com.foresee.gt3kf.sjtb.bean.TableQuerySql;
import com.foresee.gt3kf.sjtb.service.IDdlUtilsService;
import com.foresee.gt3kf.sjtb.util.FileUtil;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.PlatformFactory;
import org.apache.ddlutils.custom.io.CustomDataWriter;
import org.apache.ddlutils.model.Database;
import org.apache.ddlutils.model.Table;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2014/10/26.
 * Main方面内定义两个SQL数组，deleteSql 与 querySql,。只能单条执行 将指定服务端数据同步存放data/sjtb文件夹下。
 * 读取客户端enterTaxPlat\config\ jdbc.properties文件夹获取客户端数据库连接。
 * 先删除数据再将数据导入到客户端。
 * 导出时，在判断表的数据量，超过两千条，打印出提示，数据量太大。
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/com/foresee/gt3kf/sjtb/config/application*.xml")
public class Gt3KfSjtbSjtbByTableSql {

    private static String[] _defaultTableTypes = {"TABLE"};

    String tableDataFileName = Const.DB_DATA_XML;
    String tableDataCountFileName = Const.DB_DATA_COUNT_XML;

    @Resource
    IDdlUtilsService ddlUtilsService;



}
