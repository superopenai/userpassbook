package me.superning.userpassbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author superning
 * @Classname GainPassTemplateRequest
 * @Description 用户领取优惠卷的请求
 * @Date 2020/2/10 19:59
 * @Created by superning
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class GainPassTemplateRequest {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 请求的优惠卷的对象
     */
    private PassTemplate passTemplate;

}
