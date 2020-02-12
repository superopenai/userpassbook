package me.superning.userpassbook.utils;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.vo.FeedBack;
import me.superning.userpassbook.vo.GainPassTemplateRequest;
import me.superning.userpassbook.vo.PassTemplate;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * @author superning
 * @Classname RowGen
 * @Description RowKey生成器工具类
 * @Date 2020/2/10 9:35
 * @Created by superning
 */
@Slf4j
public class RowGen {
    /**
     * 根据提供的Passtemplate 对象生成rowkey
     * @param passTemplate {@link PassTemplate}
     * @return String RowKey
     */
    public static String genPassTemplate(PassTemplate passTemplate) {
        String passInfo = String.valueOf(passTemplate.getMerchantId()) + "&&" + passTemplate.getTitle();
        String rowKey = DigestUtils.md5Hex(passInfo);
        log.info("GenPassTemplate  RowKey :[{}],[{}]", passInfo, rowKey);
        return rowKey;

    }

    /**
     * 生成FeedBack RowKey
     * @param feedBack {@link FeedBack}
     * @return RowKey
     */
    public static String genFeedBack(FeedBack feedBack) {
        String rowKey= new StringBuilder(String.valueOf(feedBack.getUserId())).reverse().toString()
                + (Long.MAX_VALUE - System.currentTimeMillis());
        log.info("GenFeedBack  RowKey :[{}],[{}]", feedBack, rowKey);
        return rowKey;
    }

    /**
     * 根据提供的领取优惠卷请求生成RowKey, 只可以在领取优惠卷时使用.
     * Pass RowKey = reversed(UserId)+passTemplate RowKey
     * @param request {@link GainPassTemplateRequest}
     * @return RowKey
     */
    public static String genPassRowKey(GainPassTemplateRequest request) {
        String rowKey=new StringBuilder(String.valueOf(request.getUserId())).reverse().toString()
                +(Long.MAX_VALUE - System.currentTimeMillis())
                +genPassTemplate(request.getPassTemplate());
        log.info("GenPassRowKey  RowKey :[{}],[{}]", request, rowKey);

        return rowKey;
    }

}
