package me.superning.userpassbook.constant;

/**
 * @author superning
 * @Classname Constants
 * @Description 通用常量定义
 * @Date 2020/2/8 14:40
 * @Created by superning
 */
public class Constants {
    /**
     * 商户优惠卷的 kafka的topic
     */
    public static final String TEMPLATE_TOPIC = "demo";
    /**
     * token 文件
     */
    public static final String TOKEN_DIR = "tmp/token";

    /**
     * 已经使用的文件后缀名
     */
    public static final String USED_TOKEN_SUFFIX = "___";
    /**
     *
     */
    public static final String USE_COUNT_REDIS_KEY="user-count";

    /**
     *  <h2>User HBase Table</h2>
     */
    public class UserTable {

        /** User HBase 表名 */
        public static final String TABLE_NAME = "user";

        /** 基本信息列族 */
        public static final String FAMILY = "personal";

        /** 用户名 */
        public static final String NAME = "name";

        /** 用户年龄 */
        public static final String AGE = "age";

        /** 用户性别 */
        public static final String SEX = "sex";
        /** 电话号码 */
        public static final String PHONE = "phone";

        /** 住址 */
        public static final String ADDRESS = "address";
    }

    /**
     * <h2>PassTemplate HBase Table</h2>
     * */
    public static class PassTemplateTable {

        /** PassTemplate HBase 表名 */
        public static final String TABLE_NAME = "passtemplate";

        /** 基本信息列族 */
        public static final String FAMILY_B = "base";

        /** 商户 id */
        public static final String ID = "id";

        /** 优惠券标题 */
        public static final String TITLE = "title";

        /** 优惠券摘要信息 */
        public static final String SUMMARY = "summary";

        /** 优惠券详细信息 */
        public static final String DESC = "desc";

        /** 优惠券是否有 token */
        public static final String HAS_TOKEN = "has_token";

        /** 优惠券背景色 */
        public static final String BACKGROUND = "background";

        /** 约束信息列族 */
        public static final String FAMILY_C = "other";

        /** 最大个数限制 */
        public static final String LIMIT = "limit";

        /** 优惠券开始时间 */
        public static final String START = "start";

        /** 优惠券结束时间 */
        public static final String END = "end";
    }

    /**
     * <h2>Pass HBase Table</h2>
     * */
    public static class PassTable {

        /** Pass HBase 表名 */
        public static final String TABLE_NAME = "pass";

        /** 信息列族 */
        public static final String FAMILY = "card";

        /** 用户 id */
        public static final String USER_ID = "user_id";

        /** 优惠券 id */
        public static final String TEMPLATE_ID = "template_id";

        /** 优惠券识别码 */
        public static final String TOKEN = "token";

        /** 领取日期 */
        public static final String ASSIGNED_DATE = "assigned_date";

        /** 消费日期 */
        public static final String CON_DATE = "con_date";
    }

    /**
     * <h2>Feedback Hbase Table</h2>
     * */
    public static class Feedback {

        /** Feedback HBase 表名 */
        public static final String TABLE_NAME = "feedback";

        /** 信息列族 */
        public static final String FAMILY_I = "fd";

        /** 用户 id */
        public static final String USER_ID = "user_id";

        /** 评论类型 */
        public static final String TYPE = "type";

        /** PassTemplate RowKey, 如果是 app 评论, 则是 -1 */
        public static final String TEMPLATE_ID = "template_id";

        /** 评论内容 */
        public static final String COMMENT = "comment";
    }


}
