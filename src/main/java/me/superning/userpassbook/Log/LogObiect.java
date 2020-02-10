package me.superning.userpassbook.Log;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author superning
 * @Classname LogObiect
 * @Description 日志对象
 * @Date 2020/2/8 15:30
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogObiect {
    /** 日志动作类型 */
    private String action;
    /** 用户id */
    private Long userId;
    /** 时间戳 */
    private Long timestamp;
    /** 客户端ip */
    private String clientIp;
    /** 日志信息 */
    private Object info =null;
}
