package me.superning.userpassbook.controller;

import lombok.extern.slf4j.Slf4j;
import me.superning.userpassbook.Log.LogConstants;
import me.superning.userpassbook.Log.LogGenerator;
import me.superning.userpassbook.service.FeedbackService;
import me.superning.userpassbook.service.GainPassTemplateService;
import me.superning.userpassbook.service.InventoryService;
import me.superning.userpassbook.service.UserPassService;
import me.superning.userpassbook.vo.FeedBack;
import me.superning.userpassbook.vo.GainPassTemplateRequest;
import me.superning.userpassbook.vo.Pass;
import me.superning.userpassbook.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author superning
 * @Classname PassBookController
 * @Description PassBook Rest Controller
 * @Date 2020/2/12 15:50
 * @Created by superning
 */
@Slf4j
@RestController
@RequestMapping(value = "/passbook")
public class PassBookController {
    @Autowired
    private  UserPassService userPassService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    GainPassTemplateService gainPassTemplateService;
    @Autowired
    private FeedbackService feedbackService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 获取用户个人的优惠卷信息
     * @param userId
     * @return {@link Response}
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "/userpassinfo")
    Response userPassInfo(String userId) throws Exception {
        LogGenerator.genLog(httpServletRequest,userId, LogConstants.ActionName.USER_PASS_INFO,
                null);
        return userPassService.getUserPassInfo(userId);

    }
    /**
     * <h2>获取用户使用了的优惠券信息</h2>
     * @param userId 用户 id
     * @return {@link Response}
     * */
    @ResponseBody
    @GetMapping("userusedpassinfo")
    Response userUsedPassInfo(String userId) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                userId, LogConstants.ActionName.USER_USED_PASS_INFO,
                null
        );
        return userPassService.getUserUsedPassInfo(userId);
    }


    /**
     * <h2>获取库存信息</h2>
     * @param userId 用户 id
     * @return {@link Response}
     * */
    @ResponseBody
    @GetMapping("/inventoryinfo")
    Response inventoryInfo(String userId) throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.INVENTORY_INFO,
                null
        );
        return inventoryService.getInventoryInfo(userId);
    }

    /**
     * <h2>用户领取优惠券</h2>
     * @param request
     * @return {@link Response}
     * */
    @ResponseBody
    @PostMapping("/gainpasstemplate")
    Response gainPassTemplate(@RequestBody GainPassTemplateRequest request)
            throws Exception {

        LogGenerator.genLog(
                httpServletRequest,
                request.getUserId(),
                LogConstants.ActionName.GAIN_PASS_TEMPLATE,
                request
        );
        return gainPassTemplateService.gainPassTemplate(request);
    }

    /**
     * <h2>用户创建评论</h2>
     * @param feedback
     * @return {@link Response}
     * */
    @ResponseBody
    @PostMapping("/createfeedback")
    Response createFeedback(@RequestBody FeedBack feedback) {

        LogGenerator.genLog(
                httpServletRequest,
                feedback.getUserId(),
                LogConstants.ActionName.CREATE_FEEDBACK,
                feedback
        );
        return feedbackService.createFeedback(feedback);
    }

    /**
     * <h2>用户获取评论信息</h2>
     * @param userId 用户 id
     * @return {@link Response}
     * */
    @ResponseBody
    @GetMapping("/getfeedback")
    Response getFeedback(String userId) {

        LogGenerator.genLog(
                httpServletRequest,
                userId,
                LogConstants.ActionName.GET_FEEDBACK,
                null
        );
        return feedbackService.getFeedback(userId);
    }

    /**
     * <h2>异常演示接口</h2>
     * @return {@link Response}
     * */
    @ResponseBody
    @GetMapping("/exception")
    Response exception() throws Exception {
        throw new Exception("HAAHAHHAHAHAHAHA");
    }




}
