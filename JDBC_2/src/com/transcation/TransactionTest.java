package com.transcation;

import com.bean.User;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.*;

public class TransactionTest {
    /**
     * update usertable set balance=balance-100 where user='AA';
     * update usertable set balance=balance+100 where user='BB';
     * 1什么叫数据库事务ACID
     * 事务：一组逻辑操作单元，使数据从一种状态转化为另一种状态
     * 一组逻辑操作单元，一个或多个dml操作。
     * <p>
     * 2事务处理的原则:保证所有的事务都作为一个工作单元来执行，即出现了故障，都不能改变这种方式。
     * 当在一个事务中药执行多个操作时，要么所有的事务都被提交（commit），那么这些修改就永久的保存
     * 下来，要么数据库管理系统将放弃所修改的数据，整个事务回滚（rollback）到最初的状态
     * <p>
     * 3数据一旦提交就不能回滚
     * <p>
     * 4那些操作会导致数据库自动提交？
     * >DDL操作一旦执行就会自动提交
     * set autocommit= false对DDL操作失效
     * <p>
     * <p>
     * >DML操作默认情况下，一旦执行就会默认提交，但是可以通过set autocommit= false的方式取消DML操作的自动提交
     * <p>
     * 默认关闭数据库连接时也会自动提交
     */

    @Test
    public void testUpdate() {
        String sql1 = "update usertable set balance=balance-100 where user=?";
        Update(sql1, "AA");

        //模拟网络异常
        System.out.println(10 / 0);

        String sql2 = "update usertable set balance=balance+100 where user=?";
        Update(sql2, "BB");

        System.out.println("转账成功");
    }


    //通用的增删改操作 version 1.0;
    public int Update(String sql, Object... args) {
        Connection connection = null;
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
            return prepareStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closeResource(connection, prepareStatement);
        }
        return 0;
    }

    @Test
    public void testUpdateWithTX() {
        Connection connection = null;
        try {
            connection = JDBCUtils.getConnection();
            System.out.println(connection.getAutoCommit());
            //取消数据自动提交
            connection.setAutoCommit(false);

            String sql1 = "update usertable set balance=balance-100 where user=?";
            Update(connection, sql1, "AA");

            //模拟网络异常
//            System.out.println(10 / 0);

            String sql2 = "update usertable set balance=balance+100 where user=?";
            Update(connection, sql2, "BB");

            System.out.println("转账成功");
            connection.commit();


        } catch (Exception e) {
            e.printStackTrace();
            //回滚数据
            try {
                connection.rollback();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        } finally {
            //关闭资源connection
            JDBCUtils.closeResource(connection, null);
        }

    }

    //考虑数据库事务以后
    @Test
    public void testTransactionSelection() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        System.out.println(connection.getTransactionIsolation());
//        connection.setTransactionIsolation(4);
        connection.setAutoCommit(false);

        String sql = "select user,password,balance from usertable where user=?";
        User user = getInstance(connection, User.class, sql, "CC");
        System.out.println(user);
    }

    @Test
    public void testTransactionUpdate() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        connection.setAutoCommit(false);
        String sql = "update usertable set balance =? where user=? ";
        Update(connection, sql, 5000, "CC");
        Thread.sleep(1200);
        System.out.println("修改结束");

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


    //通用的增删改操作 version 2.0，考虑事务
    public int Update(Connection connection, String sql, Object... args) {

        PreparedStatement prepareStatement = null;
        try {
            //1.预编译SQL语句，返回prepareStatement实例
            prepareStatement = connection.prepareStatement(sql);
            //2.填充占位符
            for (int i = 0; i < args.length; i++) {
                prepareStatement.setObject(i + 1, args[i]);
            }
            //3.执行
            return prepareStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //修改回自动提交数据，针对于使用数据库连接池的使用
                connection.setAutoCommit(true);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            //4.关闭资源
            JDBCUtils.closeResource(null, prepareStatement);
        }
        return 0;
    }


}
