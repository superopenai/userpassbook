package me.superning.userpassbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author superning
 * @Classname Response
 * @Description 通用的响应
 * @Date 2020/2/10 15:14
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    /**
     * 错误码，正确返回 0
     */
    private Integer errorCode = 0;
    /**
     * 错误信息，正确返回空
     */
    private String errorMsg = "";
    /**
     * 返回值对象
     */
    private Object data;

    /**
     * 正确的响应的构造函数
     */
    public Response(Object data) {
        this.data = data;
    }

    /**
     * 空响应
     */
    public static Response success() {
        return new Response();
    }

    /**
     * 失败响应
     */
    public static Response failure(String errorMsg) {
        return new Response(-1, errorMsg, null);
    }


}
