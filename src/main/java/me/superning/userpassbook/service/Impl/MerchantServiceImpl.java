package me.superning.userpassbook.service.Impl;

import me.superning.userpassbook.domain.Merchant;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import me.superning.userpassbook.mapper.MerchantMapper;
import me.superning.userpassbook.service.MerchantService;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;

/**
 * @Classname MerchantServiceImpl
 * @Description TODO
 * @Date 2020/2/8 14:55
 * @Created by superning
 */
@Service
public class MerchantServiceImpl implements MerchantService{

    @Resource
    private MerchantMapper merchantMapper;

    @Override
    public Merchant findById(Integer id) {
        return null;
    }

    @Override
    public Merchant findByName(String name) {
        return null;
    }

    @Override
    public List<Merchant> findByIdPutIn(List<Long> ids) {
        List<Merchant> merchants = new ArrayList<>();
        ids.forEach(item->{
            Merchant merchant = merchantMapper.selectByPrimaryKey(item);
            merchants.add(merchant);
        });
       return merchants;
    }
}
