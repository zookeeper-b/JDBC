package com.dao1;

import com.bean.Customer;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

public class CustomerDaoImpl extends BaseDao <Customer>implements CustomerDao {

    @Override
    public void insert(Connection connection, Customer customer) {

        String sql = "insert into customers(name,email,birth) values(?,?,?)";

        Update(connection, sql,customer.getName(), customer.getEmail(), customer.getBirth());
    }

    @Override
    public void deleteById(Connection connection, int id) {
        String sql = "delete from customers where id= ?";
        Update(connection, sql, id);
    }

    @Override
    public void updateById(Connection connection, Customer customer) {
        String sql = "update customers set name=?,email=?,birth=? where id=?";
        Update(connection, sql, customer.getName(), customer.getEmail(), customer.getBirth(), customer.getId());

    }

    @Override
    public Customer getCustomerById(Connection connection, int id) {
        String sql = "select id,name,email,birth from customers where id=?";
        Customer customer = getInstance(connection, sql, id);
        return customer;
    }

    @Override
    public List<Customer> getAll(Connection connection, int id) {
        return null;
    }

    @Override
    public List<Customer> getAll(Connection connection) {
        String sql = "select name,email,birth from customers ";
        List<Customer> list = getForList(connection, sql);
        return list;
    }

    @Override
    public Long getCount(Connection connection) {
        String sql = "select count(*) from customers ";
        return getValue(connection, sql);
    }

    @Override
    public Date getMaxBirth(Connection connection) {
        String sql = "select max(birth) from customers ";
        return getValue(connection, sql);
    }
}
