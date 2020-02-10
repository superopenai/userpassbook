package me.superning.userpassbook.service;

import me.superning.userpassbook.vo.PassTemplate;

/**
 * @author superning
 * @Classname HbasePassService
 * @Description HBase  Pass 服务
 * @Date 2020/2/10 10:19
 * @Created by superning
 */
public interface passTemplateService {
    /**
     * 将PassTemplate 写入 HBase
     *
     * @param passTemplate {@link PassTemplate}
     * @return true/false
     */
    boolean dropPassTemplateToHBase(PassTemplate passTemplate);


}
