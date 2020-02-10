package me.superning.userpassbook.constant;

/**
 * @author superning
 * @Classname FeedbackType
 * @Description 评论类型枚举
 * @Date 2020/2/8 14:37
 * @Created by superning
 */
public enum FeedbackType {
    /**
     * 枚举常量
     */
    PASS(1,"针对优惠卷的评论"),
    APP(2,"针对app的评论");

    /** 编码 */
    private Integer code;
    /** 描述 */
    private String msg;

    FeedbackType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
