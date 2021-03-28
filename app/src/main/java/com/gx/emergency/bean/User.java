package com.gx.emergency.bean;

import org.litepal.crud.DataSupport;

//用户实体类
public class User extends DataSupport {
    private String account;//账号
    private String password;//密码

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }
}
