package me.superning.userpassbook.service;

import me.superning.userpassbook.vo.FeedBack;
import me.superning.userpassbook.vo.Response;

/**
 * @author superning
 * @Classname FeedbackSercie
 * @Description HBase 评论表的相关服务接口
 * @Date 2020/2/10 16:17
 * @Created by superning
 */
public interface FeedbackService {
    /**
     * 创建评论
     *
     * @param feedBack {@link FeedBack}
     * @return {@link Response}
     */
    Response createFeedback(FeedBack feedBack);

    /**
     * 获取用户评论
     *
     * @param userId 用户id(RowKey)
     * @return {@link Response}
     */
    Response getFeedback(String userId);


}
