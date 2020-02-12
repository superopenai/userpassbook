package me.superning.userpassbook.service;

import me.superning.userpassbook.vo.Response;

/**
 * @author superning
 * @Classname InventoryService
 * @Description 获取库存信息,只返回用户没有领取的,优惠卷库存服务接口
 * @Date 2020/2/10 20:06
 * @Created by superning
 */
public interface InventoryService {

    /**
     * 获取库存信息
     * @param userId 用户id
     * @return {@link Response}
     */
    Response getInventoryInfo(String userId) throws Exception;

}
