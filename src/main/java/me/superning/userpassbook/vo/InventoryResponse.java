package me.superning.userpassbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author superning
 * @Classname InventoryResponse
 * @Description 优惠卷库存响应
 * @Date 2020/2/10 19:55
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryResponse {
    /**
     * 用户Id
     */
    private String userId;
    /**
     * 每个优惠卷的信息
     */
    private List<PassTemplateInfo> passTemplates;

}
