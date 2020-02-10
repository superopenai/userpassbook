package me.superning.userpassbook.domain;

import java.io.Serializable;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname Merchant
 * @Description 商户实体
 * @Date 2020/2/8 14:55
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Merchant")
public class Merchant implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(generator = "JDBC")
    private Long id;

    /**
     * 商户名字
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 商标logo的url
     */
    @Column(name = "logo_url")
    private String logoUrl;

    /**
     * 执照url
     */
    @Column(name = "license_url")
    private String licenseUrl;

    /**
     * 个人电话
     */
    @Column(name = "personal_phone")
    private String personalPhone;

    /**
     * 地址
     */
    @Column(name = "address")
    private String address;

    /**
     * 人工审核默认为0
     */
    @Column(name = "is_audit")
    private Boolean isAudit;

    private static final long serialVersionUID = 1L;
}