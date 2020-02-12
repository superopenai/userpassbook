package me.superning.userpassbook.service.Impl;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.Constants;
import me.superning.userpassbook.domain.Merchant;
import me.superning.userpassbook.service.InventoryService;
import me.superning.userpassbook.service.MerchantService;
import me.superning.userpassbook.service.UserPassService;
import me.superning.userpassbook.utils.Hbaseutil;
import me.superning.userpassbook.utils.RowGen;
import me.superning.userpassbook.vo.*;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.LongComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.protobuf.generated.FilterProtos;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author superning
 * @Classname InventoryServiceImpl
 * @Description 获取库存信息, 只返回用户没有领取的
 * @Date 2020/2/11 14:42
 * @Created by superning
 */
@Slf4j
@Service
public class InventoryServiceImpl implements InventoryService {
    String[] format = new String[]{"yyyy-MM-dd HH:mm:ss"};

    @Autowired
    MerchantService service;
    @Autowired
    UserPassService userPassService;

    @Override
    public Response getInventoryInfo(String userId) throws Exception {

        Response userAllPassInfo = userPassService.getUserAllPassInfo(userId);
        List<PassInfo> passInfoList = (List<PassInfo>) userAllPassInfo.getData();
        List<PassTemplate> excludeObject = passInfoList.stream().map(PassInfo::getPassTemplate).collect(Collectors.toList());
        List<String> excludeIds = new ArrayList<>();
        excludeObject.forEach(passTemplate -> {
            excludeIds.add(RowGen.genPassTemplate(passTemplate));
        });

        return new Response(new InventoryResponse(userId, buildPassTemplateInfo(getAvailablePassTemplate(excludeIds))));
    }

    /**
     * 获取系统中可以使用的优惠卷
     *
     * @param excludeIds 排除的优惠卷的id们
     * @return {@link PassTemplate}
     */
    private List<PassTemplate> getAvailablePassTemplate(List<String> excludeIds) throws IOException, ParseException {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.GREATER,
                        new LongComparator(0L)
                )
        );
        filterList.addFilter(
                new SingleColumnValueFilter(
                        Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C),
                        Bytes.toBytes(Constants.PassTemplateTable.LIMIT),
                        CompareFilter.CompareOp.GREATER,
                        Bytes.toBytes("-1")
                )

        );
        ResultScanner resultScanner = Hbaseutil.getscanner(Constants.PassTemplateTable.TABLE_NAME, filterList);
        List<PassTemplate> validPassTemplateList = new ArrayList<>();
        while (resultScanner.next() != null) {
            PassTemplate passTemplate = new PassTemplate();
            passTemplate.setMerchantId(Bytes.toLong(resultScanner.next().getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.ID))));
            passTemplate.setSummary(Bytes.toString(resultScanner.next().getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.SUMMARY))));
            passTemplate.setHasToken(Bytes.toBoolean(resultScanner.next().getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN))));
            passTemplate.setDesc(Bytes.toString(resultScanner.next().getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.DESC))));
            passTemplate.setTitle(Bytes.toString(resultScanner.next().getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.TITLE))));
            passTemplate.setBackground(Bytes.toInt(resultScanner.next().getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND))));

            passTemplate.setLimit(Bytes.toLong(resultScanner.next().getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C), Bytes.toBytes(Constants.PassTemplateTable.LIMIT))));
            passTemplate.setEnd(DateUtils.parseDate(Bytes.toString(resultScanner.next().getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C), Bytes.toBytes(Constants.PassTemplateTable.END))), format));
            passTemplate.setStart(DateUtils.parseDate(Bytes.toString(resultScanner.next().getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C), Bytes.toBytes(Constants.PassTemplateTable.START))), format));
            validPassTemplateList.add(passTemplate);
        }
        List<PassTemplate> availablePassTemplates = new ArrayList<>();
        Date curDate = new Date();
        for (PassTemplate passTemplate : validPassTemplateList) {
            //过滤已经领取的
            if (excludeIds.contains(RowGen.genPassTemplate(passTemplate))) {
                continue;
            }
            //过滤已经过期的
            if (curDate.getTime() >= passTemplate.getStart().getTime() && curDate.getTime() <= passTemplate.getEnd().getTime()) {
                availablePassTemplates.add(passTemplate);
            }
        }
        return availablePassTemplates;
    }

    /**
     * 构造优惠卷的信息
     *
     * @param passTemplateList {@link PassTemplate}
     * @return {@link me.superning.userpassbook.vo.PassTemplateInfo}
     */
    private List<PassTemplateInfo> buildPassTemplateInfo(List<PassTemplate> passTemplateList) {

        Map<Long, Merchant> merchantHashMap = new HashMap<>();
        List<Long> merchantIds = passTemplateList.stream()
                .map(PassTemplate::getMerchantId).collect(Collectors.toList());
        List<Merchant> merchantList = service.findByIdPutIn(merchantIds);
        merchantList.forEach(merchant -> {
            merchantHashMap.put(merchant.getId(), merchant);
        });

        List<PassTemplateInfo> result = new ArrayList<>(passTemplateList.size());
        for (PassTemplate passTemplate : passTemplateList) {
            Merchant mc = merchantHashMap.getOrDefault(passTemplate.getMerchantId(), null);
            if (mc == null) {
                log.error("Merchants Error---->[{}]", passTemplate.getMerchantId());
                continue;
            }
            result.add(new PassTemplateInfo(passTemplate, mc));
        }

        return result;
    }
}
