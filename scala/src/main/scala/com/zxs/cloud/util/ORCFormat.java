package com.zxs.cloud.util;

public class ORCFormat {

    private ORCFormat(){}
    /**
     * hive仓储user_install_status表的数据格式
     */
    public static final String INS_STATUS="struct<aid:string,pkgname:string,uptime:bigint,type:int,country:string,gpcategory:string>";

}
