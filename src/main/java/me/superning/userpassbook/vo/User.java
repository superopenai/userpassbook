package me.superning.userpassbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Classname user
 * @Description HBase  user用户表对应对象
 * @Date 2020/2/8 15:46
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /**
     * 用户id
     */
    private String id;
    /**
     * 用户基本信息
     */
    private Personal Personal;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Personal {
        private String name;
        private Integer age;
        private String sex;
        private String phone;
        private String address;
    }

}
