package com.zxs.cloud.util;

import java.util.Collection;

/**
 * 这个是判空工具类
 */
public class EmptyUtil {

    private EmptyUtil(){}
    public static boolean isEmpty(String string){
        if(null!=string){
            return string.trim().length()==0;
        }
        return true;
    }
    public static boolean isEmpty(Collection<?> collection){
        return collection.isEmpty();
    }

}
