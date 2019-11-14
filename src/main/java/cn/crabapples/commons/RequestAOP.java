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
 * @author hequan@gogpay.cn
 * @date 2019/7/19 15:14
 */
@Aspect
@Configuration
@Order(50)
public class RequestAOP {
    private static final Logger LOGGER = LoggerFactory.getLogger(RequestAOP.class);
    private static final String BASE_AOP = "execution(* cn.crabapples.*.controller.*.*(..))";
    private static final String ALL_AOP = "execution(* *.*.*.controller.*.*(..))";
    @Pointcut(BASE_AOP)
    public void baseAop(){}
    @Pointcut(ALL_AOP)
    public void allAop(){}

    @Around("baseAop()")
    Object baseAop(ProceedingJoinPoint join) throws Throwable {
        LOGGER.debug("baseAop拦截成功");
        Object[] args = join.getArgs();
        for (Object arg : args) {
            LOGGER.debug("请求参数:【{}】",arg);
        }
        Object obj = join.proceed();
        LOGGER.debug("返回值:【{}】",obj);
        MethodSignature methodSignature = (MethodSignature) join.getSignature();
        Method method = methodSignature.getMethod();
        LOGGER.debug("被调用的方法:【{}】",method);
        return obj;
    }

    @Around("allAop()")
    Object allAop(ProceedingJoinPoint join) throws Throwable {
        LOGGER.debug("allAop拦截成功");
        return join.proceed();
    }
}

