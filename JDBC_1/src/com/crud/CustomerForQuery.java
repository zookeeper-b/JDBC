package com.crud;

import com.bean.Customers;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

public class CustomerForQuery {
    @Test
    public void testQueryForCustomers() throws Exception {
        String sql = "select id,name,email,birth from customers where id=?";
        String sql1 = "select id,name  from customers where name=?";

        Customers customers = queryForCustomers(sql, 1);
        Customers customers1 = queryForCustomers(sql1, "lxy");

        System.out.println(customers);
        System.out.println(customers1);
    }


    public Customers queryForCustomers(String sql, Object... args) {
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
                Customers customers = new Customers();
                for (int i = 0; i < columnCount; i++) {
                    Object columnValue = resultSet.getObject(i + 1);

                    //获取每个列的列名
                    String columnName = metaData.getColumnName(i + 1);

                    //给customers对象的指定某个属性，赋值为columnValue
                    Field declaredField = Customers.class.getDeclaredField(columnName);
                    declaredField.setAccessible(true);
                    declaredField.set(customers, columnValue);
                }
                return customers;
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
        String sql = "select  id ,name,email,birth from customers where id=?";
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setObject(1, 1);
        //执行并返回结果集
        ResultSet resultSet = prepareStatement.executeQuery();
        //处理结果集
        if (resultSet.next()) {//判断结果集是否有数据，如果有数据返回true，并指针下移，如果false，指针不会下移，直接结束

            //获取当前这条数据的各个字段值
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            String email = resultSet.getString(3);
            Date birth = resultSet.getDate(4);

            //方式一
//            System.out.println("id="+id+",name="+name+",email="+email+",date="+birth);
            //防守二
//            Object[]data=new Object[]{id,name,email,birth};
//            System.out.println(data);
            //防守三
            Customers customers = new Customers(id, name, email, birth);
            System.out.println(customers);

            //关闭资源
            JDBCUtils.closeResource(connection, prepareStatement, resultSet);
        }

    }


}
