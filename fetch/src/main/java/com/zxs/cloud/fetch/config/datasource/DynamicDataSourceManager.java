package com.zxs.cloud.fetch.config.datasource;

/**
 * 动态数据源管理者
 * 使用ThreadLocal实现
 */
public class DynamicDataSourceManager {

    private DynamicDataSourceManager() {
    }

    private static ThreadLocal<String> routeKey = new ThreadLocal<>();

    /**
     * 获取当前线程的数据源路由的key
     */
    public static String getRouteKey() {
        return routeKey.get();
    }

    /**
     * 绑定当前线程数据源路由的key
     * 使用完成后必须调用removeRouteKey()方法删除
     */
    public static void setRouteKey(String key) {
        routeKey.set(key);
    }

    /**
     * 删除与当前线程绑定的数据源路由的key
     */
    public static void removeRouteKey() {
        routeKey.remove();
    }
}
