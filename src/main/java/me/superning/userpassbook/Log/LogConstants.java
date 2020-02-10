package me.superning.userpassbook.Log;

/**
 * @author superning
 * @Classname LogConstants
 * @Description 日志记录
 * @Date 2020/2/8 15:03
 * @Created by superning
 */
public class LogConstants {
    /**
     * 内部类 用户动作名称
     */
    public class ActionName {
        /* 用户查看优惠卷信息 */
        public static final String USER_PASS_INFO = "UserPassInfo";
        /* 用户查看已经使用后的优惠卷信息*/
        public static final String USER_USED_PASS_INFO = "UserUsedPassInfo";
        /* 用户使用优惠卷 */
        public static final String USER_USE_PASS = "UserUsePass";
        /* 用户获取库信息*/
        public static final String INVENTORY_INFO = "InventoryInfo";
        /* 用户领取优惠卷*/
        public static final String GAIN_PASS_TEMPLATE = "GainPassTemplate";
        /* 用户创建评论*/
        public static final String CREATE_FEEDBACK = "CreateFeedback";
        /* 用户获取评论*/
        public static final String GET_FEEDBACK = "GetFeedBack";




    }
}
