package me.superning.userpassbook.service;

import me.superning.userpassbook.vo.GainPassTemplateRequest;
import me.superning.userpassbook.vo.Response;

import java.io.IOException;
import java.text.ParseException;

/**
 * @author superning
 * @Classname GainPassTemplateService
 * @Description 用户领取优惠卷服务接口
 * @Date 2020/2/10 20:09
 * @Created by superning
 */
public interface GainPassTemplateService {
    /**
     * 用户领取优惠卷的请求动作
     * @param request {@link GainPassTemplateRequest}
     * @return {@link Response}
     */
    Response gainPassTemplate(GainPassTemplateRequest request) throws ParseException, IOException;

}
