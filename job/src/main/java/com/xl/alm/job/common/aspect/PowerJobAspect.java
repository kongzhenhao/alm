package com.xl.alm.job.common.aspect;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class PowerJobAspect {

    @Pointcut("@annotation(tech.powerjob.worker.annotation.PowerJobHandler)")
    public void aopPoint() {
    }

    @Around("aopPoint()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        return null;
    }
}
