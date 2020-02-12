package me.superning.userpassbook.service;

import me.superning.userpassbook.vo.Pass;
import me.superning.userpassbook.vo.Response;

import java.io.IOException;

/**
 * @author superning
 * @Classname UserPassService
 * @Description 获取用户个人优惠卷信息
 * @Date 2020/2/10 20:12
 * @Created by superning
 */
public interface UserPassService {

    /**
     * 获取个人可以使用的优惠卷信息,即我的优惠卷功能
     * @param userId
     * @return {@link Response}
     */
    Response getUserPassInfo (String userId) throws Exception;

    /**
     * 获取用户已经使用的优惠卷 即已使用优惠卷
     * @param userId
     * @return {@link Response}
     */
    Response getUserUsedPassInfo(String userId) throws Exception;

    /**
     * 获取用户所拥有的所有优惠卷
     * @param userId
     * @return {@link Response}
     */
    Response getUserAllPassInfo(String userId) throws Exception;

    /**
     * 用户使用优惠卷
     * @param pass
     */
    void userUsedPass(Pass pass) throws IOException;

}
