package com.cecilylove.blueocean.redis.aspect;

import com.cecilylove.blueocean.redis.annotation.DistributedLock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 分布式锁切面处理
 * 负责解析 SpEL 表达式，并调用 Redisson 客户端进行加锁/解锁操作。
 *
 * @author cecilylove
 * @since 1.0.0
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DistributedLockAspect {

    private final RedissonClient redissonClient;

    private final ExpressionParser parser = new SpelExpressionParser();
    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws Throwable {
        String lockKey = parseSpel(joinPoint, distributedLock.key());
        RLock lock = distributedLock.fair() ? redissonClient.getFairLock(lockKey) : redissonClient.getLock(lockKey);

        boolean locked = false;
        try {
            // 尝试加锁
            locked = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.unit());
            if (locked) {
                log.debug("成功获取分布式锁: {}", lockKey);
                return joinPoint.proceed();
            } else {
                log.warn("获取分布式锁超时: {}", lockKey);
                // 暂时直接抛出运行时异常，业务层可捕获
                throw new RuntimeException("系统繁忙，请稍后再试");
            }
        } finally {
            if (locked && lock.isHeldByCurrentThread()) {
                lock.unlock();
                log.debug("成功释放分布式锁: {}", lockKey);
            }
        }
    }

    /**
     * 解析 SpEL 表达式
     */
    private String parseSpel(ProceedingJoinPoint joinPoint, String spel) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = nameDiscoverer.getParameterNames(method);

        if (parameterNames == null || parameterNames.length == 0 || !spel.contains("#")) {
            return spel;
        }

        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }

        Expression expression = parser.parseExpression(spel);
        Object value = expression.getValue(context);
        return value != null ? value.toString() : spel;
    }

}
