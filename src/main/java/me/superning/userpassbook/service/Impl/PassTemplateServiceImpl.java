package me.superning.userpassbook.service.Impl;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.Constants;
import me.superning.userpassbook.service.passTemplateService;
import me.superning.userpassbook.utils.Hbaseutil;
import me.superning.userpassbook.utils.RowGen;
import me.superning.userpassbook.vo.PassTemplate;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.stereotype.Service;

/**
 * @author superning
 * @Classname HbasePassServiceImpl
 * @Description Passtemplate 表的 服务接口实现
 * @Date 2020/2/10 10:30
 * @Created by superning
 */
@Service
@Slf4j
public class PassTemplateServiceImpl implements passTemplateService {

    @Override
    public boolean dropPassTemplateToHBase(PassTemplate passTemplate) {
        if (passTemplate == null) {
            return false;
        }

        String rowKey = RowGen.genPassTemplate(passTemplate);
        Result rowData = Hbaseutil.getRowData(Constants.PassTemplateTable.TABLE_NAME, rowKey);
        // 检查是否已经存在这个rowkey
        if (!rowData.isEmpty()) {
            log.warn("RowKey-->[{}] is already exist", rowKey);
            return false;
        }

        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_B, rowKey, Constants.PassTemplateTable.ID, Bytes.toBytes(passTemplate.getMerchantId()));
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_B, rowKey, Constants.PassTemplateTable.BACKGROUND, Bytes.toBytes(passTemplate.getBackground()));
        Hbaseutil.putRowStringData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_B, rowKey, Constants.PassTemplateTable.DESC, passTemplate.getDesc());
        Hbaseutil.putRowStringData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_B, rowKey, Constants.PassTemplateTable.SUMMARY, passTemplate.getSummary());
        Hbaseutil.putRowStringData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_B, rowKey, Constants.PassTemplateTable.TITLE, passTemplate.getTitle());
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_B, rowKey, Constants.PassTemplateTable.HAS_TOKEN, Bytes.toBytes(passTemplate.getHasToken()));
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_C, rowKey, Constants.PassTemplateTable.LIMIT, Bytes.toBytes(passTemplate.getLimit()));
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_C, rowKey, Constants.PassTemplateTable.START, Bytes.toBytes(DateFormatUtils.format(passTemplate.getStart(), "yyyy-MM-dd HH:mm:ss")));
        Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME, Constants.PassTemplateTable.FAMILY_C, rowKey, Constants.PassTemplateTable.END, Bytes.toBytes(DateFormatUtils.format(passTemplate.getEnd(), "yyyy-MM-dd HH:mm:ss")));


        return true;
    }
}
