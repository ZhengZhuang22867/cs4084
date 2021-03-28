package com.gx.emergency.bean;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * 物品实体类
 */
public class Goods extends DataSupport {
    private String name;//名称
    private String type;//类型
    private String producedDate;//生成日期
    private int expirationDate;//保质期
    private int count;//数量
    private String location;//地点

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProducedDate() {
        return producedDate;
    }

    public void setProducedDate(String producedDate) {
        this.producedDate = producedDate;
    }

    public int getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(int expirationDate) {
        this.expirationDate = expirationDate;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Goods(String name, String type,  String producedDate, int expirationDate, int count, String location) {
        this.name = name;
        this.type = type;
        this.producedDate = producedDate;
        this.expirationDate = expirationDate;
        this.count=count;
        this.location = location;
    }
}
