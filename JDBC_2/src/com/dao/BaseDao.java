package com.dao;

import com.bean.Customer;
import com.util.JDBCUtils;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDao {
    //通用的增删改操作
    public void Update(Connection connection, String sql, Object... args) {
        PreparedStatement prepareStatement = null;
        try {
            //1.获取数据库连接
            connection = JDBCUtils.getConnection();
            //2.预编译SQL语句，返回prepareStatement实例
            prepareStatement = connection.prepareStatement(sql);
            //3.填充占位符
            for (int i = 0; i < args.length; i++) {
                prepareStatement.setObject(i + 1, args[i]);
            }
            //4.执行
            prepareStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closeResource(connection, prepareStatement);
        }

    }

    //通用的查询操作，用于返回数据库表中的一条记录，(version 2.0 考虑上事务)
    public <T> T getInstance(Connection connection, Class<T> clazz, String sql, Object... args) {


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
            JDBCUtils.closeResource(null, prepareStatement, resultSet);
        }

        return null;


    }

    //通用的查询操作用于返回数据表中的多条记录构成的集合（version 2.0 考虑上事务）
    public <T> List<T> getForList(Connection connection, Class<T> clazz, String sql, Object... args) {


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
            JDBCUtils.closeResource(null, prepareStatement, resultSet);
        }
        return null;
    }

    public <E> E getValue(Connection connection, String sql, Object... args) {

        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        try {
            prepareStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                prepareStatement.setObject(i + 1, args[i]);

            }
            resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {
                return (E) resultSet.getObject(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            JDBCUtils.closeResource(null, prepareStatement, resultSet);
        }

        return null;

    }


    public abstract List<Customer> getAll(Connection connection);
}
