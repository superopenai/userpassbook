package me.superning.userpassbook.service.Impl;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.service.GainPassTemplateService;
import me.superning.userpassbook.vo.GainPassTemplateRequest;
import me.superning.userpassbook.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Classname GainPassTemplateServiceImpl
 * @Description 用户领取优惠卷功能实现
 * @Date 2020/2/12 10:52
 * @Created by superning
 */
@Slf4j
@Service
public class GainPassTemplateServiceImpl implements GainPassTemplateService {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public Response gainPassTemplate(GainPassTemplateRequest request) {
        return null;
    }

    /**
     * 将已经使用的token记录到文件中
     * @param merchantId 商户id
     * @param passtemplateId passtemplated表的rowkey
     * @param token token
     */
    private void recordTokenTofile(Long merchantId, String passtemplateId, String token) {

    }


}
