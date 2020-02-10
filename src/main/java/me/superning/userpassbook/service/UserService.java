package me.superning.userpassbook.service;

import me.superning.userpassbook.vo.Response;
import me.superning.userpassbook.vo.User;

/**
 * @author superning
 * @Classname UserServicew
 * @Description 用户服务
 * @Date 2020/2/10 15:23
 * @Created by superning
 */
public interface UserService {
    /**
     * 创建用户服务
     *
     * @param user {@link User}
     * @return {@link Response}
     */
    Response createUser(User user);
}
