package me.superning.userpassbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author superning
 * @Classname ErrorInfo
 * @Description 统一的错误信息
 * @Date 2020/2/10 9:55
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorInfo<T> {

    /** 错误码 */
    public static final Integer ERROR = -1;
    /** 特定错误码*/
    public Integer code;
    /** 错误信息*/
    private String message;
    /** 请求url*/
    private String url;
    /** 返回数据*/
    private T data;

}
