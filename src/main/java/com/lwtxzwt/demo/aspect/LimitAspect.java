package com.lwtxzwt.demo.aspect;

import com.lwtxzwt.demo.annotation.Limit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 流量控制AOP
 *
 * @author wentao.zhang
 * @since 2022-01-08
 */
@Aspect
@Component
public class LimitAspect {

    private static final Map<String, AtomicInteger> limitCount = new ConcurrentHashMap<>();
    private static final Map<String, Long> limitDatetime = new ConcurrentHashMap<>();

    @Pointcut("@annotation(com.lwtxzwt.demo.annotation.Limit)")
    public void pointCut() {}

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodInvocationProceedingJoinPoint mjp = (MethodInvocationProceedingJoinPoint) joinPoint;
        MethodSignature signature = (MethodSignature) mjp.getSignature();
        Method method = signature.getMethod();

        Limit limit = method.getAnnotation(Limit.class);
        if (limit == null) {
            return joinPoint.proceed();
        }
        int max = limit.max();
        String key = limit.key();
        long time = System.currentTimeMillis() / 1000;
        if (limitDatetime.get(key) == null || !limitDatetime.get(key).equals(time)) {
            limitDatetime.put(key, time);
            limitCount.put(key, new AtomicInteger(1));
        } else {
            int count = limitCount.get(key).incrementAndGet();
            if (count > max) {
                throw new RuntimeException("blocked!");
            }
            limitCount.put(key, new AtomicInteger(count));
        }
        return joinPoint.proceed();
    }
}
