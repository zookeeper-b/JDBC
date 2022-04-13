package com.bean;
//orm编程思想（Object relational mapping）对象关系映射
//一个数据表对应一个Java类
//表中的一条数据对应java类中的一个对象
//表中的一个字段对应java类中的一个属性

import java.sql.Date;

public class Customers {
    private int id;
    private String name;
    private String email;
    private Date birth;

    public Customers() {
        super();
    }

    public Customers(int id, String name, String email, Date birth) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.birth = birth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate() {
        return birth;
    }

    public void setDate(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Customers{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                '}';
    }

}
