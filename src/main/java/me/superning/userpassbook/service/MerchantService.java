package me.superning.userpassbook.service;

import me.superning.userpassbook.domain.Merchant;

import java.util.List;

/**
 * @author superning
 * @Description TODO
 * @Date 2020/2/8 14:55
 * @Created by superning
     * @Classname MerchantService*
 */
public interface MerchantService{
    /**
     * 通过id获取商户对象
     * @param id
     * @return {@link Merchant}
     */
    Merchant findById(Integer id);

    /**
     * 通过商户名字获取对象
     * @param name
     * @return {@link Merchant}
     */
    Merchant findByName(String name);

    /**
     * 根据ids 获取一些对象
     * @param ids
     * @return {@link Merchant}
     */
    List<Merchant> findByIdPutIn(List<Integer> ids);

}
