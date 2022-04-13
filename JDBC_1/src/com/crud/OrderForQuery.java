package com.crud;

import com.bean.Order;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

public class OrderForQuery {

    /**
     * 针对于表的字段名和类的属性名不一致的情况
     * 1.必须声明sql时，使用类的属性名作为字段的别名
     * 2.使用resultSetMeteData是，需要使用getColumnLabel()来替换getColumnName();
     * 获取类的别名
     * 说明：如果SQL中没有给字段名的别名，getColumnLabel()默认获取的就是列名。
     */


    @Test
    public void testOrderForQuery() {
        String sql = "select  order_id orderId, order_name orderName, order_date orderDate from `order`  where  order_id=?";


        Order order = orderForQuery(sql, 2);
        System.out.println(order);

    }

    //通用的order表的通用操作
    public Order orderForQuery(String sql, Object... args) {
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
                Order order = new Order();
                for (int i = 0; i < columnCount; i++) {
                    //获取每个列的列值
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取每个列的列名
//                    String columnName = metaData.getColumnName(i + 1);
                    //获取列的别名（无别名时默认列名）
                    String columnLabel = metaData.getColumnLabel(i + 1);
                    //给customers对象的指定某个属性，赋值为columnValue
                    Field declaredField = Order.class.getDeclaredField(columnLabel);
                    declaredField.setAccessible(true);
                    declaredField.set(order, columnValue);
                }
                return order;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.closeResource(connection, prepareStatement, resultSet);
        }

        return null;
    }

    @Test
    public void testQuery1() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "select  order_id , order_name, order_date  from `order`  where  order_id=?";
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setObject(1, 1);
        ResultSet resultSet = prepareStatement.executeQuery();
        if (resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            Date date = resultSet.getDate(3);


            Order order = new Order(id, name, date);
            System.out.println(order);

            JDBCUtils.closeResource(connection, prepareStatement, resultSet);
        }

    }


}
