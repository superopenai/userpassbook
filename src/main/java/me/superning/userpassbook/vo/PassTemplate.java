package me.superning.userpassbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author superning
 * @Classname passTemplate
 * @Description Hbase passtemplate 优惠卷表对应对象
 * @Date 2020/2/8 16:29
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassTemplate {
    /** 商户id */
    private Long merchantId;
    /** 优惠卷标题 不能重复  */
    private String title;
    /** 优惠卷摘要*/
    private String summary;
    /** 优惠卷详细信息*/
    private String desc;
    /** 最大个数限制*/
    private Long limit;
    /** 开始时间*/
    private Date start;
    /** 结束时间*/
    private Date end;
    /** 背景颜色代码*/
    private Integer background;
    /** 优惠券是否有 Token*/
    private Boolean hasToken;

}
