package me.superning.userpassbook.constant;

/**
 * @author superning
 * @Classname PassSatatus
 * @Description 优惠卷的状态
 * @Date 2020/2/8 14:27
 * @Created by superning
 */
public enum PassSatatus {
    /**
     * 枚举常量
     */
    UNUSED("未使用的", 1),
    USED("已经使用的", 2),
    ALL("全部领取的", 3);

    /**
     * 描述信息
     */
    private String Msg;
    /**
     * 信息编码
     */
    private Integer code;

    PassSatatus(String msg, Integer code) {
        this.Msg = msg;
        this.code = code;
    }


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
