package com.dao;

import com.bean.Customer;
import com.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.Date;
import java.util.List;

import static org.junit.Assert.*;

public class CustomerDaoImplTest {
    private CustomerDaoImpl dao = new CustomerDaoImpl();

    @Test
    public void insert() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customer = new Customer(4, "xbh", "xaiofei@126.com", new Date(21343452342l));
            dao.insert(connection, customer);
            System.out.println("添加成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void deleteById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            dao.deleteById(connection, 3);

            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void updateById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customer = new Customer(1, "xbh", "xaiofei@126.com", new Date(21343452342l));
            dao.updateById(connection, customer);
            System.out.println("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void getCustomerById() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            Customer customer = dao.getCustomerById(connection, 1);
            System.out.println(customer);
            System.out.println("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void getAll() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            List<Customer> list = dao.getAll(connection);
            list.forEach(System.out::println);
            System.out.println("查询成功");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void getCount() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();

            Long count = dao.getCount(connection);
            System.out.println("表中的记录数为" + count);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void getMaxBirth() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            java.util.Date maxBirth = dao.getMaxBirth(connection);
            System.out.println("最大生日是"+maxBirth);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, null);
        }

    }
}