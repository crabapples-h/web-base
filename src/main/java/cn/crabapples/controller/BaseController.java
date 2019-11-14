package cn.crabapples.controller;

import cn.crabapples.dto.ResponseDTO;
import cn.crabapples.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.management.ServiceNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

/**
 * TODO 基础Controller，其他controller请继承此类
 *
 * @author Mr.He
 * @date 2019/9/21 18:28
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
public abstract class BaseController {
    @Autowired
    private Validator validator;

    /**
     * 属性校验的方法
     * @param object
     */
    protected <T> void validator(Object object,Class<T> ... groups){
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object,groups);
        for (ConstraintViolation constraintViolation : constraintViolations ) {
            throw new ApplicationException(constraintViolation.getMessageTemplate());
        }
    }

    /**
     * Exception异常处理
     *
     * @param e 捕获到的异常
     * @return 返回一个通用DTO
     */
//    @SuppressWarnings("rawtypes")
    @ExceptionHandler()
    @ResponseBody
    public ResponseDTO expHandle(Exception e) {
        e.printStackTrace();
        return ResponseDTO.returnErrorResponse(e.getMessage());
    }

//    @SuppressWarnings("rawtypes")
    @ExceptionHandler()
    @ResponseBody
    public String expHandle(ServiceNotFoundException e) {
        e.printStackTrace();
        return e.getMessage();
    }
}
