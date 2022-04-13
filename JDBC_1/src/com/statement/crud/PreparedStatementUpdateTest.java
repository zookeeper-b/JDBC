package com.statement.crud;
//使用preparedstatement代替statem，实现对数据库的增删改查操作

import com.connection.ConnectionTest;
import com.util.JDBCUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class PreparedStatementUpdateTest {
    //通用的增删改操作
    public void Update(String sql, Object... args) {
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
            prepareStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closeResource(connection, prepareStatement);
        }

    }

    @Test
    public void testCommonUpdate() {
//        String sql="delete from customers where id=?";
//        Update(sql,3);
//        String sql="insert into customers(name,email,birth)values(?,?,?) ";
//        Update(sql,"lyw","lyw.com","2000-07-04");

        String sql1 = "update  customers set email=? where id=?";
        Update(sql1, "bhq.com", "1");

    }

    //向表中添加数据
    @Test
    public void testInsert() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            //1.读取配置文件中的4个基本信息
            InputStream inputStream = ConnectionTest.class.getClassLoader().getResourceAsStream("jdbc.properties");
            Properties properties = new Properties();
            properties.load(inputStream);

            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

            String driverClass = properties.getProperty("driverClass");
            //2.加载驱动
            Class.forName(driverClass);
            //3.获取连接
            connection = DriverManager.getConnection(url, user, password);

            System.out.println(connection);
            //4.预编译sql语句，返回preparedstatem的实例
            String sql = "insert into customers(name,email,birth)values(?,?,?)";
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            //5.填充占位符
            prepareStatement.setString(1, "哪吒");
            prepareStatement.setString(2, "bhq.com");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse("2000-09-09");
            prepareStatement.setDate(3, new java.sql.Date(date.getTime()));
            //6.执行操作
//            prepareStatement.execute();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            //7.资源的关闭
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }

    //修改表中数据
    public void testUpdate() {
        //1.获取数据库连接
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        try {
            connection = JDBCUtils.getConnection();

            //2.预编译SQL语句，返回preparestatement实例
            String sql = "update customers set name=? where  id=?";
            prepareStatement = connection.prepareStatement(sql);

            //3.填充占位符
            prepareStatement.setObject(1, "wbd");
            prepareStatement.setObject(2, 1);
            prepareStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //4。资源的关闭
            JDBCUtils.closeResource(connection, prepareStatement);
        }

    }


}
