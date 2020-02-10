package me.superning.userpassbook.vo;

import com.google.common.base.Enums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.superning.userpassbook.constant.FeedbackType;

/**
 * @author superning
 * @Classname FeedBack
 * @Description Hbase feedback表 对应对象
 * @Date 2020/2/8 16:50
 * @Created by superning
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedBack {
    /**
     * 用户id
     */
    private String userId;
    /**
     * passtemplate 的rowKey
     */
    private String tempalteId;
    /**
     * 评论类型
     */
    private String type;
    /**
     * 评论内容
     */
    private String comment;

    public boolean vaild() {
        FeedbackType feedbackType = Enums.getIfPresent(
                FeedbackType.class, this.type.toUpperCase()
        ).orNull();


        return !(null == feedbackType || null == comment || comment.length() > 140);

    }

}
