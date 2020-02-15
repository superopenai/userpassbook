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
    public Merchant findById(Long id) {

        return merchantMapper.selectByPrimaryKey(id);

    }

    @Override
    public Merchant findByName(String name) {

        Example example = new Example(Merchant.class);
        example.createCriteria().andEqualTo("name",name);
        return merchantMapper.selectOneByExample(example);

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

    @Override
    public void updateNameInMerchantById(Long id, String newName) {
        Merchant merchant = merchantMapper.selectByPrimaryKey(id);
        merchant.setName(newName);

        merchantMapper.updateByPrimaryKeySelective(merchant);
    }


}
