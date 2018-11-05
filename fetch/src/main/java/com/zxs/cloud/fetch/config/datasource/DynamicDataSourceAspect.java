package com.zxs.cloud.fetch.config.datasource;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 动态数据源Aspect
 * 根据TargetDataSource注解选择不同数据源
 */
@Aspect
public class DynamicDataSourceAspect {

    @Pointcut("@annotation(com.zxs.cloud.fetch.config.datasource.TargetDataSource)")
    public void methodPointCut() {
        throw new UnsupportedOperationException();
    }

    @Pointcut("@within(com.zxs.cloud.fetch.config.datasource.TargetDataSource)")
    public void typePointCut() {
        throw new UnsupportedOperationException();
    }

    @Before("methodPointCut() || typePointCut()")
    public void setRouteKey(JoinPoint pjp) {
        String routeKey = null;
        TargetDataSource targetDataSource = getTypeTargetDataSource(pjp);
        if (Objects.nonNull(targetDataSource)) {
            routeKey = targetDataSource.value().getName();
        }
        targetDataSource = getMethodTargetDataSource(pjp);
        if (Objects.nonNull(targetDataSource)) {
            routeKey = targetDataSource.value().getName();
        }
        if (StringUtils.isNotEmpty(routeKey)) {
            DynamicDataSourceManager.setRouteKey(routeKey);
        }

    }

    /**
     * 方法声明数据源
     *
     * @param pjp
     * @return
     */
    private TargetDataSource getMethodTargetDataSource(JoinPoint pjp) {
        TargetDataSource targetDataSource;
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        targetDataSource = method.getAnnotation(TargetDataSource.class);
        return targetDataSource;
    }

    /**
     * 类声明数据源
     *
     * @param pjp
     * @return
     */
    private TargetDataSource getTypeTargetDataSource(JoinPoint pjp) {
        Object o = pjp.getTarget();
        return o.getClass().getAnnotation(TargetDataSource.class);
    }

    @After("methodPointCut() || typePointCut() ")
    public void removeRouteKey() {
        DynamicDataSourceManager.removeRouteKey();
    }
}
