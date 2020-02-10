package me.superning.userpassbook.advice;

import me.superning.userpassbook.vo.ErrorInfo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author superning
 * @Classname Global
 * @Description 全局异常处理
 * @Date 2020/2/10 10:01
 * @Created by superning
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ErrorInfo<String> errorHandler(HttpServletRequest request,Exception exception) {
        ErrorInfo<String> stringErrorInfo = new ErrorInfo<>();
        stringErrorInfo.setCode(ErrorInfo.ERROR);
        stringErrorInfo.setMessage(exception.getMessage());
        stringErrorInfo.setUrl(request.getRequestURI().toString());
        stringErrorInfo.setData("Do not Have Return Data");
        return stringErrorInfo;
    }
}
