package com.app.geoTracker.config.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Aspect
@Component
@Slf4j
public class ControllerLoggingAspect {

    // Thread pool for async logging
    private final ExecutorService executor = Executors.newCachedThreadPool();

    // Pointcut: Match all public methods in classes under controller package
    @Pointcut("execution(public * com.app.geoTracker.controller..*(..))")
    public void controllerMethods() {}

    // Log before method execution
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        String controllerMethod = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();
        executor.submit(() -> log.info("➡️ Starting: {} | Args: {}", controllerMethod, args));
    }

    // Log after successful execution
    @AfterReturning(pointcut = "controllerMethods()", returning = "result")
    public void logAfterSuccess(JoinPoint joinPoint, Object result) {
        String controllerMethod = joinPoint.getSignature().toShortString();
        executor.submit(() -> log.info("✅ Completed: {} | Result: {}", controllerMethod, result));
    }

    // Log when an exception is thrown
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logAfterFailure(JoinPoint joinPoint, Throwable ex) {
        String controllerMethod = joinPoint.getSignature().toShortString();
        executor.submit(() -> log.error("❌ Error in {} | Message: {}", controllerMethod, ex.getMessage(), ex));
    }

    @Around("controllerMethods()")
    public Object logExecution(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        String method = pjp.getSignature().toShortString();

        executor.submit(() -> log.info("➡️ Started: {} | Args: {}", method, pjp.getArgs()));

        try {
            Object result = pjp.proceed();
            long duration = System.currentTimeMillis() - start;
            executor.submit(() -> log.info("✅ Finished: {} | Time: {} ms | Result: {}", method, duration, result));
            return result;
        } catch (Exception ex) {
            long duration = System.currentTimeMillis() - start;
            executor.submit(() -> log.error("❌ Failed: {} | Time: {} ms | Error: {}", method, duration, ex.getMessage()));
            throw ex;
        }
    }
}
