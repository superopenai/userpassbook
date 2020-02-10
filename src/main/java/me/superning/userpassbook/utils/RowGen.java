package me.superning.userpassbook.utils;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.vo.FeedBack;
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
     *
     * @param passTemplate {@link PassTemplate}
     * @return String RowKey
     */
    public static String genPassTemplate(PassTemplate passTemplate) {
        String passInfo = String.valueOf(passTemplate.getMerchantId()) + "&&" + passTemplate.getTitle();
        String rowKey = DigestUtils.md5Hex(passInfo);
        log.info("GenPassTemplate  RowKey :[{}],[{}]", passInfo, rowKey);
        return rowKey;

    }

    public static String genFeedBack(FeedBack feedBack) {
        return new StringBuilder(String.valueOf(feedBack.getUserId())).reverse().toString()
                + (Long.MAX_VALUE - System.currentTimeMillis());
    }


}
