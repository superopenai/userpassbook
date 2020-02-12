package me.superning.userpassbook.service.Impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.Constants;
import me.superning.userpassbook.service.GainPassTemplateService;
import me.superning.userpassbook.utils.Hbaseutil;
import me.superning.userpassbook.utils.RowGen;
import me.superning.userpassbook.vo.GainPassTemplateRequest;
import me.superning.userpassbook.vo.PassTemplate;
import me.superning.userpassbook.vo.Response;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.util.Date;

/**
 * @Classname GainPassTemplateServiceImpl
 * @Description 用户领取优惠卷功能实现
 * @Date 2020/2/12 10:52
 * @Created by superning
 */
@Slf4j
@Service
public class GainPassTemplateServiceImpl implements GainPassTemplateService {
    String[] format = new String[]{"yyyy-MM-dd HH:mm:ss"};

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Response gainPassTemplate(GainPassTemplateRequest request) throws ParseException, IOException {
        String passTemplateId = RowGen.genPassTemplate(request.getPassTemplate());
        Result rowData = Hbaseutil.getRowData(Constants.PassTemplateTable.TABLE_NAME, passTemplateId);
        PassTemplate passTemplate = new PassTemplate();
        if (rowData != null) {
            passTemplate.setMerchantId(Bytes.toLong(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.ID))));
            passTemplate.setSummary(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.SUMMARY))));
            passTemplate.setHasToken(Bytes.toBoolean(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN))));
            passTemplate.setDesc(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.DESC))));
            passTemplate.setTitle(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.TITLE))));
            passTemplate.setBackground(Bytes.toInt(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND))));

            passTemplate.setLimit(Bytes.toLong(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C), Bytes.toBytes(Constants.PassTemplateTable.LIMIT))));
            passTemplate.setEnd(DateUtils.parseDate(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C), Bytes.toBytes(Constants.PassTemplateTable.END))), format));
            passTemplate.setStart(DateUtils.parseDate(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C), Bytes.toBytes(Constants.PassTemplateTable.START))), format));
        } else {
            log.error("Generate Passtemplate failure");
            return Response.failure("Generate Passtemplate failure");
        }
        if (passTemplate.getLimit() <= 1 && passTemplate.getLimit() != -1) {
            log.error("PassTemplate Limit Max: {}",
                    JSON.toJSONString(request.getPassTemplate()));
            return Response.failure("PassTemplate Limit Max!");
        }
        Date curtime = new Date();
        if (!(curtime.getTime()>= passTemplate.getStart().getTime()&&curtime.getTime()
        <passTemplate.getEnd().getTime())) {
            log.error("PassTemplate ValidTime Error: [{}]",
                    JSON.toJSONString(request.getPassTemplate()));
            return Response.failure("PassTemplate ValidTime Error!");
        }

        // 减去优惠券的 limit
        if (passTemplate.getLimit() != -1) {
            Hbaseutil.putRowCustomData(Constants.PassTemplateTable.TABLE_NAME,Constants.PassTemplateTable.FAMILY_C,passTemplateId,Constants.PassTemplateTable.LIMIT,Bytes.toBytes(passTemplate.getLimit()-1));

        }

        // 将优惠券保存到用户优惠券表
        if (!addPassForUser(request, passTemplate.getMerchantId(), passTemplateId)) {
            return Response.failure("GainPassTemplate Failure!");
        }

        return Response.success();

    }

    /**
     * 将已经使用的token记录到文件中
     *
     * @param merchantId     商户id
     * @param passtemplateId passtemplated表的rowkey
     * @param token          token
     */
    private void recordTokenTofile(Long merchantId, String passtemplateId, String token) throws IOException {
        Files.write(
                Paths.get(Constants.TOKEN_DIR, String.valueOf(merchantId),
                        passtemplateId + Constants.USED_TOKEN_SUFFIX),
                (token + "\n").getBytes(),
                StandardOpenOption.CREATE, StandardOpenOption.APPEND
        );
    }

    /**
     * 给用户添加优惠券
     *
     * @param request        {@link GainPassTemplateRequest}
     * @param merchantId
     * @param passtemplateId
     * @return
     */
    private boolean addPassForUser(GainPassTemplateRequest request, Long merchantId, String passtemplateId) throws IOException {
        if (request.getPassTemplate().getHasToken()) {
            String token = stringRedisTemplate.opsForSet().pop(passtemplateId);
            if (token == null) {
                log.error("Token not exist: ----->[{}]", passtemplateId);
                return false;
            }
            recordTokenTofile(merchantId, passtemplateId, token);
            Hbaseutil.putRowCustomData(Constants.PassTable.TABLE_NAME, Constants.PassTable.FAMILY, RowGen.genPassRowKey(request), Constants.PassTable.TOKEN, Bytes.toBytes(token));
        } else {
            Hbaseutil.putRowCustomData(Constants.PassTable.TABLE_NAME, Constants.PassTable.FAMILY, RowGen.genPassRowKey(request), Constants.PassTable.TOKEN, Bytes.toBytes(-1));

        }

        Hbaseutil.putRowCustomData(Constants.PassTable.TABLE_NAME, Constants.PassTable.FAMILY, RowGen.genPassRowKey(request), Constants.PassTable.USER_ID, Bytes.toBytes(request.getUserId()));
        Hbaseutil.putRowCustomData(Constants.PassTable.TABLE_NAME, Constants.PassTable.FAMILY, RowGen.genPassRowKey(request), Constants.PassTable.TEMPLATE_ID, Bytes.toBytes(passtemplateId));
        Hbaseutil.putRowCustomData(Constants.PassTable.TABLE_NAME, Constants.PassTable.FAMILY, RowGen.genPassRowKey(request), Constants.PassTable.ASSIGNED_DATE, Bytes.toBytes(DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss")));
        Hbaseutil.putRowCustomData(Constants.PassTable.TABLE_NAME, Constants.PassTable.FAMILY, RowGen.genPassRowKey(request), Constants.PassTable.CON_DATE, Bytes.toBytes(-1));

        return true;

    }


}
