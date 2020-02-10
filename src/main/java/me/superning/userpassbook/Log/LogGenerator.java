package me.superning.userpassbook.Log;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * @author superning
 * @Classname LogGenerator
 * @Description 日志生成器
 * @Date 2020/2/8 15:36
 * @Created by superning
 */
@Slf4j
public class LogGenerator {
    /**
     * 生成Log
     * @param request {@link HttpServletRequest}
     * @param userId 用户id
     * @param action 用户行为
     * @param info 日志信息
     */
    public static void genLog(HttpServletRequest request,Long userId,String action,Object info){
        log.info(
                JSON.toJSONString(
                        new LogObiect(action,userId,System.currentTimeMillis(),request.getRemoteAddr(),info)
                )


        );
    }
}
