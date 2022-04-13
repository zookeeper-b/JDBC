package com.crud;


import com.bean.Customers;
import com.bean.Order;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

//使用 PreparedStatement 实现针对于不同表的通用查询操作
public class PreparedStatementQueryTest {
    @Test
    public void testGetForList(){
        String sql = "select id,name,email,birth from customers where id<=?";
        List<Customers> list = getForList(Customers.class, sql, 2);
        list.forEach(System.out::println);
        System.out.println("----------------------------------------------------");
        String sql1 = "select  order_id orderId, order_name orderName, order_date orderDate from `order`  where  order_id< ?";
        List<Order> list1=getForList(Order.class,sql1,5);
        list1.forEach(System.out::println);
        System.out.println("----------------------------------------------------");
    }

    public <T> List<T> getForList(Class<T> clazz, String sql, Object... args) {

        Connection connection = null;
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();

            prepareStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                prepareStatement.setObject(i + 1, args[i]);
            }
            resultSet = prepareStatement.executeQuery();

            //获取结果集的元数据

            ResultSetMetaData metaData = resultSet.getMetaData();

            //通过resultmetadata获取集合中的列数
            int columnCount = metaData.getColumnCount();
            ArrayList<T> list = new ArrayList<>();
            while (resultSet.next()) {
                T t = clazz.newInstance();
                //处理结果集中的每一行数据中的每一个列：给t对象指定的属性赋值。
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取每个列的列名
//                    String columnName = metaData.getColumnName(i + 1);
                    //获取列的别名（无别名时默认列名）
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //给customers对象的指定某个属性，赋值为columnValue
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(t, columnValue);
                }
                list.add(t);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, prepareStatement, resultSet);
        }
        return null;
    }



    @Test
    public void testGetInstance() {
        String sql = "select id,name,email,birth from customers where id=?";
        Customers customers = getInstance(Customers.class, sql, 2);
        System.out.println(customers);

        System.out.println("----------------------------------------------------");
        String sql1 = "select  order_id orderId, order_name orderName, order_date orderDate from `order`  where  order_id=?";
        Order order = getInstance(Order.class, sql1, 1);
        System.out.println(order);
    }


    public <T> T getInstance(Class<T> clazz, String sql, Object... args) {

        Connection connection = null;
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();

            prepareStatement = connection.prepareStatement(sql);

            for (int i = 0; i < args.length; i++) {
                prepareStatement.setObject(i + 1, args[i]);
            }
            resultSet = prepareStatement.executeQuery();

            //获取结果集的元数据

            ResultSetMetaData metaData = resultSet.getMetaData();

            //通过resultmetadata获取集合中的列数
            int columnCount = metaData.getColumnCount();

            if (resultSet.next()) {
                T t = clazz.newInstance();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取每个列的列名
//                    String columnName = metaData.getColumnName(i + 1);
                    //获取列的别名（无别名时默认列名）
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //给customers对象的指定某个属性，赋值为columnValue
                    Field declaredField = clazz.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(t, columnValue);
                }
                return t;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, prepareStatement, resultSet);
        }

        return null;


    }


}
