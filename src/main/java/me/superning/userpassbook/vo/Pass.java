package me.superning.userpassbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author superning
 * @Classname Pass
 * @Description HBase表 pass表 已经使用优惠卷
 * @Date 2020/2/8 16:40
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Pass {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * pass表的rowKEY
     */
    private String passRowKey;

    /**
     * Template表的 id
     */
    private String templateId;
    /**
     * 优惠卷 token 有可能是null
     */
    private String token;
    /**
     * 优惠卷领取日期
     */
    private Date assignedDate;
    /**
     * 消费日期
     */
    private Date conDate;
}
