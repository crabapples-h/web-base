package cn.crabapples.commons;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.lang.reflect.Method;

/**
 * TODO 请求切面拦截
 *
 * @author Mr.He
 * 2019/11/14 21:34
 * e-mail wishforyou.xia@gmail.com
 * qq 294046317
 * pc-name 29404
 */
@Aspect
@Configuration
@Order(50)
public class RequestAOP {
    private static final Logger logger = LoggerFactory.getLogger(RequestAOP.class);
    private static final String BASE_CONTROLLER_AOP = "execution(* cn.crabapples.*.controller.*.*(..))";
    private static final String ALL_CONTROLLER_AOP = "execution(* *.*.*.controller.*.*(..))";

    @Pointcut(BASE_CONTROLLER_AOP)
    public void baseControllerAop() {
    }

    @Pointcut(ALL_CONTROLLER_AOP)
    public void allControllerAop() {
    }

    @Around("baseControllerAop()")
    Object baseControllerAop(ProceedingJoinPoint join) throws Throwable {
        logger.debug("接口拦截成功");
        Object[] args = join.getArgs();
        for (Object arg : args) {
            logger.debug("请求参数:【{}】", arg);
        }
        Object obj = join.proceed();
        logger.debug("返回值:【{}】", obj);
        MethodSignature methodSignature = (MethodSignature) join.getSignature();
        Method method = methodSignature.getMethod();
        logger.debug("被调用的方法:【{}】", method);
        return obj;
    }

    @Around("allControllerAop()")
    Object allControllerAop(ProceedingJoinPoint join) throws Throwable {
        logger.debug("通用拦截成功");
        return join.proceed();
    }
}

