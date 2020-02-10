package me.superning.userpassbook.service.Impl;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.constant.Constants;
import me.superning.userpassbook.service.UserService;
import me.superning.userpassbook.utils.Hbaseutil;
import me.superning.userpassbook.vo.Response;
import me.superning.userpassbook.vo.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Classname UserServiceImpl
 * @Description 用户服务实现
 * @Date 2020/2/10 15:26
 * @Created by superning
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * @param user {@link User}
     * @return {@link Response}
     */
    @Override
    public Response createUser(User user) {
        //todo 没理解
        Long increment = redisTemplate.opsForValue().increment(Constants.USE_COUNT_REDIS_KEY, 1);
        String userRowKey = genUserId(increment);

        /* 向表插入用户*/
        Hbaseutil.putRowStringData(Constants.UserTable.TABLE_NAME, Constants.UserTable.FAMILY, userRowKey, Constants.UserTable.NAME, user.getPersonal().getName());
        Hbaseutil.putRowStringData(Constants.UserTable.TABLE_NAME, Constants.UserTable.FAMILY, userRowKey, Constants.UserTable.ADDRESS, user.getPersonal().getAddress());
        Hbaseutil.putRowStringData(Constants.UserTable.TABLE_NAME, Constants.UserTable.FAMILY, userRowKey, Constants.UserTable.PHONE, user.getPersonal().getPhone());
        Hbaseutil.putRowStringData(Constants.UserTable.TABLE_NAME, Constants.UserTable.FAMILY, userRowKey, Constants.UserTable.SEX, user.getPersonal().getSex());
        Hbaseutil.putRowCustomData(Constants.UserTable.TABLE_NAME, Constants.UserTable.FAMILY, userRowKey, Constants.UserTable.AGE, Bytes.toBytes(user.getPersonal().getAge()));
        user.setId(userRowKey);
        return new Response(user);
    }

    /**
     * 生成userId
     *
     * @param prefix 当前用户数
     * @return 用户id 其实就是HBase user 表的rowkey
     */
    private String genUserId(Long prefix) {
        String suffix = RandomStringUtils.randomNumeric(5);
        return prefix + suffix;
    }
}
