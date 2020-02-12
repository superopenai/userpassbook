package me.superning.userpassbook.service.Impl;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.Constants;
import me.superning.userpassbook.constant.PassSatatus;
import me.superning.userpassbook.domain.Merchant;
import me.superning.userpassbook.service.MerchantService;
import me.superning.userpassbook.service.UserPassService;
import me.superning.userpassbook.utils.Hbaseutil;
import me.superning.userpassbook.vo.Pass;
import me.superning.userpassbook.vo.PassInfo;
import me.superning.userpassbook.vo.PassTemplate;
import me.superning.userpassbook.vo.Response;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.filter.CompareFilter;

import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author superning
 * @Classname UserPassServiceImpl
 * @Description 用户优惠券服务接口实现
 * @Date 2020/2/10 20:39
 * @Created by superning
 */
@Service
@Slf4j
public class UserPassServiceImpl implements UserPassService {
    String[] format = new String[]{"yyyy-MM-dd HH:mm:ss"};
    @Autowired
    MerchantService merchantService;

    @Override
    public Response getUserPassInfo(String userId) throws Exception {

        return getPassInfoByStatus(userId,PassSatatus.UNUSED);
    }

    @Override
    public Response getUserUsedPassInfo(String userId) throws Exception {
        return getPassInfoByStatus(userId,PassSatatus.USED);

    }

    @Override
    public Response getUserAllPassInfo(String userId) throws Exception {

        return getPassInfoByStatus(userId,PassSatatus.ALL);
    }

    @Override
    public void userUsedPass(Pass pass) throws IOException {
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(
                String.valueOf(pass.getUserId())).reverse().toString());
        FilterList filterList = new FilterList();
        filterList.addFilter(new PrefixFilter(rowPrefix));
        filterList.addFilter(new SingleColumnValueFilter(
                Constants.PassTable.FAMILY.getBytes(),
                Constants.PassTable.TEMPLATE_ID.getBytes(),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes(pass.getTemplateId())
        ));
        filterList.addFilter(new SingleColumnValueFilter(
                Constants.PassTable.FAMILY.getBytes(),
                Constants.PassTable.CON_DATE.getBytes(),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("-1")
        ));
        ResultScanner results = Hbaseutil.getscanner(Constants.PassTable.TABLE_NAME, filterList);

        List<Pass> passList = new ArrayList<>();
        results.forEach(result -> {
            Pass newPass = new Pass();

            try {
                newPass.setUserId(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.USER_ID))));
                newPass.setConDate(DateUtils.parseDate(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.CON_DATE))),format));
                newPass.setAssignedDate(DateUtils.parseDate(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.ASSIGNED_DATE))),format));
                newPass.setToken(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.TOKEN))));
                newPass.setTemplateId(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.TEMPLATE_ID))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            passList.add(newPass);
        });


    }

    /**
     * 根据优惠卷状态获取优惠卷信息
     * @param userId
     * @param satatus
     * @return
     */
    private Response getPassInfoByStatus(String userId, PassSatatus satatus) throws Exception{
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(userId)).reverse().toString());

        CompareFilter.CompareOp compareOp =
                satatus == PassSatatus.UNUSED ?
                        CompareFilter.CompareOp.EQUAL : CompareFilter.CompareOp.NOT_EQUAL;

        FilterList filterList = new FilterList();
        // 1. 行键前缀过滤器, 找到特定用户的优惠券
        filterList.addFilter(new PrefixFilter(rowPrefix));
        // 2. 基于列单元值的过滤器, 找到未使用的优惠券
        if (satatus != PassSatatus.ALL) {
            filterList.addFilter( new SingleColumnValueFilter(
                    Constants.PassTable.FAMILY.getBytes(),
                    Constants.PassTable.CON_DATE.getBytes(), compareOp,
                    Bytes.toBytes("-1")));
        }
        ResultScanner results = Hbaseutil.getscanner(Constants.PassTable.TABLE_NAME, filterList);

        List<Pass> passList = new ArrayList<>();
        results.forEach(result -> {
            Pass pass = new Pass();

            try {
                pass.setUserId(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.USER_ID))));
                pass.setConDate(DateUtils.parseDate(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.CON_DATE))),format));
                pass.setAssignedDate(DateUtils.parseDate(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.ASSIGNED_DATE))),format));
                pass.setToken(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.TOKEN))));
                pass.setTemplateId(Bytes.toString(result.getValue(Bytes.toBytes(Constants.PassTable.FAMILY),Bytes.toBytes(Constants.PassTable.TEMPLATE_ID))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            passList.add(pass);
        });

        Map<String, PassTemplate> passTemplateMap = buildPassTemplateMap(passList);
        Map<Long, Merchant> merchantsMap = buildMerchantMap(
                new ArrayList<>(passTemplateMap.values()));

        List<PassInfo> result = new ArrayList<>();

        for (Pass pass : passList) {
            PassTemplate passTemplate = passTemplateMap.getOrDefault(
                    pass.getTemplateId(), null);
            if (null == passTemplate) {
                log.error("PassTemplate Null : {}", pass.getTemplateId());
                continue;
            }

            Merchant merchants = merchantsMap.getOrDefault(passTemplate.getMerchantId(), null);
            if (null == merchants) {
                log.error("Merchants Null : {}", passTemplate.getMerchantId());
                continue;
            }

            result.add(new PassInfo(pass, passTemplate, merchants));
        }

        return new Response(result);

    }


    /**
     * 通过获取的pass对象来构造这个map
     *
     * @param passList {@link Pass}
     * @return Map {@link PassTemplate}
     */
    private Map<String, PassTemplate> buildPassTemplateMap(List<Pass> passList) throws ParseException {
        List<String> templateIds = passList.stream().map(Pass::getTemplateId).collect(Collectors.toList());
        List<Get> getList = new ArrayList<Get>(templateIds.size());
        templateIds.forEach(tRK -> {
            getList.add(new Get(Bytes.toBytes(tRK)));
        });
        Result[] someRowData = Hbaseutil.getSomeRowData(Constants.PassTemplateTable.TABLE_NAME, getList);

        /* 构造PassTemplateId passtemplate object 的map 用于构造passInfo*/
        Map<String, PassTemplate> stringPassTemplateHashMap = new HashMap<>();

        for (Result rowData : someRowData) {
            PassTemplate passTemplate = new PassTemplate();
            passTemplate.setMerchantId(Bytes.toLong(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.ID))));
            passTemplate.setSummary(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.SUMMARY))));
            passTemplate.setHasToken(Bytes.toBoolean(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.HAS_TOKEN))));
            passTemplate.setDesc(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.DESC))));
            passTemplate.setTitle(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.TITLE))));
            passTemplate.setBackground(Bytes.toInt(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_B), Bytes.toBytes(Constants.PassTemplateTable.BACKGROUND))));

            passTemplate.setLimit(Bytes.toLong(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C), Bytes.toBytes(Constants.PassTemplateTable.LIMIT))));
            passTemplate.setEnd(DateUtils.parseDate(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C), Bytes.toBytes(Constants.PassTemplateTable.END))), format));
            passTemplate.setStart(DateUtils.parseDate(Bytes.toString(rowData.getValue(Bytes.toBytes(Constants.PassTemplateTable.FAMILY_C), Bytes.toBytes(Constants.PassTemplateTable.START))), format));

            stringPassTemplateHashMap.put(Bytes.toString(rowData.getRow()), passTemplate);
        }
        return stringPassTemplateHashMap;

    }

    /**
     * 通过获取的PassTemplate 对象构造Merchants Map
     * @param passTemplateList {@link PassTemplate}
     * @return {@link Merchant}
     */
    private Map<Long, Merchant> buildMerchantMap(List<PassTemplate> passTemplateList) {
        HashMap<Long, Merchant> merchantHashMap = new HashMap<>();
        List<Long> merchantsIds = passTemplateList.stream().map(
                PassTemplate::getMerchantId
        ).collect(Collectors.toList());
        List<Merchant> merchants = merchantService.findByIdPutIn(merchantsIds);
        merchants.forEach(merchant -> {
            merchantHashMap.put(merchant.getId(),merchant);
        });
            return merchantHashMap;
    }
}
