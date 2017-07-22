package yz.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * author: liuyazong
 * datetime: 2017/7/21 下午4:19
 */
@Component
@Aspect
@Slf4j
public class LogApj {

    @Around(value = "execution(* yz.server.HttpRequestHandler.*(..))")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        Object[] args = point.getArgs();
        Object result = point.proceed(args);
        log.debug("args:{},result:{}", args, result);
        return result;
    }
}
