package cn.crabapples.controller;

import cn.crabapples.dto.ResponseDTO;
import cn.crabapples.commons.BeanValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.ServiceNotFoundException;

/**
 * TODO 基础Controller
 *
 * @author Mr.He
 * @date 2019/7/19 0:29
 * e-mail wishforyou.xia@gmail.com
 * pc-name 29404
 */
public class BaseController {
    @Autowired
    protected BeanValidatorUtils beanValidatorUtils;

    /**
     * Exception异常处理
     *
     * @param e 捕获到的异常
     * @return 返回一个通用DTO
     */
    @SuppressWarnings("rawtypes")
    @ExceptionHandler()
    @ResponseBody
    public ResponseDTO expHandle(Exception e) {
        e.printStackTrace();
        return ResponseDTO.returnErrorResponse(e.getMessage());
    }

    @SuppressWarnings("rawtypes")
    @ExceptionHandler()
    @ResponseBody
    public String expHandle(ServiceNotFoundException e) {
        e.printStackTrace();
        return e.getMessage();
    }

//    /**
//     * UnauthorizedException权限认证失败异常
//     *
//     * @param e
//     * @return
//     */
//    @SuppressWarnings("rawtypes")
//    @ExceptionHandler()
//    @ResponseBody
//    public ResponseDTO expHandle(UnauthorizedException e) {
//        e.printStackTrace();
//        return ResponseDTO.returnUnauthorizedExceptionResponse("该操作未授权，操作失败，请联系超级管理员！");
//    }

//    /**
//     * ExpiredSessionException session 过期
//     *
//     * @param e
//     * @return
//     */
//    @ExceptionHandler()
//    @ResponseBody
//    public ResponseDTO ExpiredSessionException(ExpiredSessionException e) {
//        e.printStackTrace();
//        return ResponseDTO.returnUnauthorizedExceptionResponse("该操作未授权，操作失败，请联系超级管理员！");
//    }

//    /**
//     * 自定义异常处理
//     *
//     * @param e
//     * @return
//     */
//    @ExceptionHandler()
//    @ResponseBody
//    public ResponseDTO<Object> appException(AppException e) {
//        e.printStackTrace();
//        return ResponseDTO.returnErrorResponse(e.getMessage());
//    }
}
