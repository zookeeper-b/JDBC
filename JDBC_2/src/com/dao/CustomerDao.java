package com.dao;


import com.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

//此接口用于规范针对于customer表的常规操作
public interface CustomerDao {
    //把customer对象添加到数据库中
    void insert(Connection connection, Customer customer);

    //针对指定的id，删除表中的一条记录
    void deleteById(Connection connection, int id);

    //针对对内存中的customer对象，去修改数据库表中指定的记录
    void updateById(Connection connection, Customer customer);

    //针对指定的id查询得到对应得customer对象
    Customer getCustomerById(Connection connection, int id);

    //查询表中所有的记录构成的集合
    List<Customer> getAll(Connection connection, int id);


    //返回数据库表中的条目数
    Long getCount(Connection connection);

    //放回数据表中的最大生日
    Date getMaxBirth(Connection connection);
}
