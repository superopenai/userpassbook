package me.superning.userpassbook.service.Impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.Constants;
import me.superning.userpassbook.service.passTemplateService;
import me.superning.userpassbook.vo.PassTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author superning
 * @Classname ConsumerPassTemplate
 * @Description 消费Kafka中dPassTemplate
 * @Date 2020/2/10 10:26
 * @Created by superning
 */
@Slf4j
@Component
public class ConsumerPassTemplate {
    @Autowired
    me.superning.userpassbook.service.passTemplateService passTemplateService;

    @KafkaListener(topics = {Constants.TEMPLATE_TOPIC})
    public void receive(@Payload String passTemplate,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        log.info("Consumer Receive Passtemplate : [{}]", passTemplate);
        PassTemplate pt;
        try {
            pt = JSON.parseObject(passTemplate, PassTemplate.class);

        } catch (Exception e) {
            log.error("Parse Passtemplate Error :[{}]", e.getMessage());
            return;
        }
        log.info("DropPassTemplateToHBase :[{}]", passTemplateService.dropPassTemplateToHBase(pt));
    }


}
