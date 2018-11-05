package com.zxs.cloud.fetch.constant;

import lombok.Getter;

@Getter
public enum DataSource {
    DC("dc", "数据中心"),
    FETCH("fetch", "爬虫")
    ;

    private final String name;

    private final String desc;

    DataSource(final String name, final String desc) {
        this.name = name;
        this.desc = desc;
    }
}
