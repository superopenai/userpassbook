package me.superning.userpassbook.constant;

/**
 * @author superning
 * @Classname DBType
 * @Description TODO
 * @Date 2020/2/15 11:37
 * @Created by superning
 */
public enum  DBType {
    /** 枚举*/
    MASTER(1,"Master"),
    SLAVE1(2,"Slave1"),
    SLAVE2(3,"Slave2");

    private Integer code;
    private String msg;

    DBType(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
