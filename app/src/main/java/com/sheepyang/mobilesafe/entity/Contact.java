package com.sheepyang.mobilesafe.entity;

import java.io.Serializable;

/**
 * 联系人
 * Created by SheepYang on 2016/6/10 12:35.
 */
public class Contact implements Serializable{
    String name;
    String phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
