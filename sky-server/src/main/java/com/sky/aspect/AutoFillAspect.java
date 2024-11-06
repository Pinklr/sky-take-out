package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 自动拦截直接切面
 */

@Aspect
@Component
@Slf4j
public class AutoFillAspect {


    //定义切入点
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillCutPoint(){}


    @Before("autoFillCutPoint()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("自动填充");
        //使用反射为字段赋值
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        AutoFill annotation = methodSignature.getMethod().getAnnotation(AutoFill.class);
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object arg = args[0];


        if(annotation.value() == OperationType.INSERT){
            log.info("插入");
            //使用反射获取对象的方法
            //我们规定更新的对象只会出现在第一个参数上

            //获取对象字段的setter方法
            Method setCreateTime = arg.getClass().getMethod("setCreateTime", LocalDateTime.class);
            Method setUpdateTime = arg.getClass().getMethod("setUpdateTime", LocalDateTime.class);
            Method setCreateUser = arg.getClass().getMethod("setCreateUser", Long.class);
            Method setUpdateUser = arg.getClass().getMethod("setUpdateUser", Long.class);
            setCreateTime.invoke(arg, LocalDateTime.now());
            setUpdateTime.invoke(arg, LocalDateTime.now());
            setUpdateUser.invoke(arg, BaseContext.getCurrentId());
            setCreateUser.invoke(arg, BaseContext.getCurrentId());
        }else if(annotation.value() == OperationType.UPDATE){
            log.info("更新");
//            Method setCreateTime = arg.getClass().getMethod("setCreateTime", LocalDateTime.class);
            Method setUpdateTime = arg.getClass().getMethod("setUpdateTime", LocalDateTime.class);
//            Method setCreateUser = arg.getClass().getMethod("setCreateUser", Long.class);
            Method setUpdateUser = arg.getClass().getMethod("setUpdateUser", Long.class);
//            setCreateTime.invoke(arg, LocalDateTime.now());
            setUpdateTime.invoke(arg, LocalDateTime.now());
            setUpdateUser.invoke(arg, BaseContext.getCurrentId());
//            setCreateUser.invoke(arg, BaseContext.getCurrentId());
            log.info("更新完成");
        }

    }
}
