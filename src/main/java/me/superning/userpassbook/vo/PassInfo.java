package me.superning.userpassbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.superning.userpassbook.domain.Merchant;

/**
 * @author superning
 * @Classname PassInfo
 * @Description 用户已经领取的优惠表
 * @Date 2020/2/10 20:02
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassInfo {
    /**
     * 领取了的优惠卷的对象
     */
    private Pass pass;
    /**
     * 模板对象
     */
    private PassTemplate passTemplate;
    /**
     * 商户对象
     */
    private Merchant merchant;


}
