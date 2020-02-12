package me.superning.userpassbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import me.superning.userpassbook.domain.Merchant;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author superning
 * @Classname PassTemplateInfo
 * @Description 优惠卷模板
 * @Date 2020/2/10 19:52
 * @Created by superning
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassTemplateInfo extends PassTemplate {
    /**
     * 优惠券模板基本信息
     */
    private PassTemplate passTemplate;
    /**
     * 优惠券对应的商户
     */
    private Merchant merchant;
}
