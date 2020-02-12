package me.superning.userpassbook.controller;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.Log.LogConstants;
import me.superning.userpassbook.Log.LogGenerator;
import me.superning.userpassbook.service.UserService;
import me.superning.userpassbook.vo.Response;
import me.superning.userpassbook.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author superning
 * @Classname CreateUserController
 * @Description 创建用户服务
 * @Date 2020/2/12 16:03
 * @Created by superning
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class CreateUserController {
    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;


    /**
     * <h2>创建用户</h2>
     * @param user {@link User}
     * @return {@link Response}
     * */
    @ResponseBody
    @PostMapping("/createuser")
    Response createUser(@RequestBody User user) throws Exception {

        LogGenerator.genLog(
                httpServletRequest, "-1",
                LogConstants.ActionName.CREATE_USER,
                user
        );
        return userService.createUser(user);
    }
}
