package com.gx.emergency.bean;

import org.litepal.crud.DataSupport;

/**
 * 工具实体类
 */
public class Tool  {
    private String name;//工具名称
    private boolean must;//是否必须

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isMust() {
        return must;
    }

    public void setMust(boolean must) {
        this.must = must;
    }


    public Tool(String name, boolean must) {
        this.name = name;
        this.must = must;
    }
}
