package com.dbutils;

/*
 * commons-dbutils 是 Apache 组织提供的一个开源 JDBC工具类库,封装了针对于数据库的增删改查操作
 *
 */


import com.bean.Customer;
import com.connection.JDBCUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class QueryRunnerTest {
    //测试插入用dbutils
    @Test
    public void testInsert() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();
            String sql = "insert into customers(name,email,birth)values (?,?,?)";
            int insercount = runner.update(connection, sql, "蔡徐坤", "cxk.163.com", "2000-09-09");
            System.out.println("添加了" + insercount + "记录");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, null);
        }

    }


    //测试查询
    //BeanHandler:是ResultSetHandler接口的实现类，用于封装一条记录。
    //BeanListHandler:是ResultSetHandler接口的实现类，用于封装多条记录构成的集合。
    @Test
    public void testQuery() {
        try {
            QueryRunner runner = new QueryRunner();
            Connection connection = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id=?";
            String sql1 = "select id,name,email,birth from customers where id<=?";
            BeanHandler<Customer> handler = new BeanHandler<>(Customer.class);

            BeanListHandler<Customer> handler1 = new BeanListHandler<>(Customer.class);

            Customer customer = runner.query(connection, sql, handler, 3);
            List<Customer> list = runner.query(connection, sql1, handler1, 3);
            System.out.println(customer);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //MapHandler:是ResultSetHandler接口的实现类，用于封装一条记录。将字段及相应字段的值作为map中的key和value


    @Test
    public void testQuery3() {
        try {
            QueryRunner runner = new QueryRunner();
            Connection connection = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id=?";
            String sql1 = "select id,name,email,birth from customers where id<=?";
            MapHandler handler = new MapHandler();
            Map<String, Object> map = runner.query(connection, sql, handler, 5);
            System.out.println(map);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //MapListHandler:是ResultSetHandler接口的实现类，用于封装多条记录构成的集合。将字段及相应字段的值作为map中的key和value
    @Test
    public void testQuery4() {
        try {
            QueryRunner runner = new QueryRunner();
            Connection connection = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id<?";
            String sql1 = "select id,name,email,birth from customers where id<=?";
            MapListHandler handler = new MapListHandler();
            List<Map<String, Object>> list = runner.query(connection, sql, handler, 5);
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //    ScalarHandler用于查询特殊值
    @Test
    public void testQuery5() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();
            String sql = "select count(*) from customers ";
            ScalarHandler handler = new ScalarHandler();
            Long query = (Long) runner.query(connection, sql, handler);
            System.out.println(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    @Test
    public void testQuery6() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();
            String sql = "select max(birth) from customers ";
            ScalarHandler handler = new ScalarHandler();
            Date query = (Date) runner.query(connection, sql, handler);
            System.out.println(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }

    //自定义ResultSetHandler的实现类
    @Test
    public void testQuery7() {
        Connection connection = null;
        try {
            QueryRunner runner = new QueryRunner();
            connection = JDBCUtils.getConnection3();
            String sql = "select id,name,email,birth from customers where id=?";
            ResultSetHandler<Customer> handler = new ResultSetHandler<Customer>() {
                @Override
                public Customer handle(ResultSet rs) throws SQLException {
//                    System.out.println("QueryRunnerTest.handle");
//                    return new Customer(12,"bhq","asd",new java.sql.Date(3242342324345l));
                    if (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        java.sql.Date birth = rs.getDate("birth");
                        Customer customer = new Customer(id, name, email, birth);
                        return customer;
                    }

                    return null;
                }
            };
            Customer customer = runner.query(connection, sql, handler, 5);
            System.out.println(customer);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, null);
        }

    }


}
