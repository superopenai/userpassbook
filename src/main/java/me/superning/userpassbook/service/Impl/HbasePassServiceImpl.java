package me.superning.userpassbook.service.Impl;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.Constants;
import me.superning.userpassbook.service.HbasePassService;
import me.superning.userpassbook.utils.HBaseConn;
import me.superning.userpassbook.utils.Hbaseutil;
import me.superning.userpassbook.utils.RowGen;
import me.superning.userpassbook.vo.PassTemplate;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.yarn.webapp.hamlet.Hamlet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author superning
 * @Classname HbasePassServiceImpl
 * @Description TODO
 * @Date 2020/2/10 10:30
 * @Created by superning
 */
@Service
@Slf4j
public class HbasePassServiceImpl implements HbasePassService {

    @Override
    public boolean dropPassTemplateToHBase(PassTemplate passTemplate) {
        if (passTemplate == null) {
            return false;
        }

        String rowKey = RowGen.genPassTemplate(passTemplate);
        Result rowData = Hbaseutil.getRowData(Constants.PassTemplateTable.TABLE_NAME, rowKey);
        if (!rowData.isEmpty()) {
            log.warn("RowKey-->[{}] is already exist", rowKey);
            return false;
        }

        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_B, rowKey, Constants.PassTemplateTable.ID, Bytes.toBytes(passTemplate.getMerchantId()));
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME,Constants.PassTemplateTable.FAMILY_B,rowKey,Constants.PassTemplateTable.BACKGROUND,Bytes.toBytes(passTemplate.getBackground()));
        Hbaseutil.putRowStringData(Constants.PassTemplateTable.TABLE_NAME,Constants.PassTemplateTable.FAMILY_B,rowKey,Constants.PassTemplateTable.DESC,passTemplate.getDesc());
        Hbaseutil.putRowStringData(Constants.PassTemplateTable.TABLE_NAME,Constants.PassTemplateTable.FAMILY_B,rowKey,Constants.PassTemplateTable.SUMMARY,passTemplate.getSummary());
        Hbaseutil.putRowStringData(Constants.PassTemplateTable.TABLE_NAME,Constants.PassTemplateTable.FAMILY_B,rowKey,Constants.PassTemplateTable.TITLE,passTemplate.getTitle());
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME,Constants.PassTemplateTable.FAMILY_B,rowKey,Constants.PassTemplateTable.HAS_TOKEN,Bytes.toBytes(passTemplate.getHasToken()));
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME,Constants.PassTemplateTable.FAMILY_C,rowKey,Constants.PassTemplateTable.LIMIT,Bytes.toBytes(passTemplate.getLimit()));
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME,Constants.PassTemplateTable.FAMILY_C,rowKey,Constants.PassTemplateTable.START,Bytes.toBytes(DateFormatUtils.format(passTemplate.getStart(), "yyyy-MM-dd HH:mm:ss")));
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME,Constants.PassTemplateTable.FAMILY_C,rowKey,Constants.PassTemplateTable.END,Bytes.toBytes(DateFormatUtils.format(passTemplate.getEnd(), "yyyy-MM-dd HH:mm:ss")));




        return false;
    }
}
