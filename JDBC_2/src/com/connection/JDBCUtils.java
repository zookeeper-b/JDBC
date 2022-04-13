package com.connection;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.apache.commons.dbutils.DbUtils;
import org.junit.Test;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtils {
    //方式一：
    @Test
    public void testGetConnection() throws PropertyVetoException, SQLException {
        //获取c3p0数据库连接池
        ComboPooledDataSource cpds = new ComboPooledDataSource();
        cpds.setDriverClass("com.mysql.jdbc.Driver");
        cpds.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        cpds.setUser("root");
        cpds.setPassword("root");
        //通过设置相关的参数，对数据库连接池进行管理：
        //设置初始时数据库连接池中的连接数
        cpds.setInitialPoolSize(10);

        Connection conn = cpds.getConnection();
        System.out.println(conn);

        //销毁c3p0数据库连接池
//		DataSources.destroy( cpds );
    }

    private static ComboPooledDataSource cpds = new ComboPooledDataSource("c3p0");

    public static Connection getConnection1() throws SQLException {

        Connection connection = cpds.getConnection();

        return connection;
    }


    private static DataSource source;

    static {


        try {
            //方式2：
            Properties pros = new Properties();
            FileInputStream is = new FileInputStream(new File("src/dbcp.properties"));
            pros.load(is);
            //创建dbcp数据库连接池
            source = BasicDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection2() throws Exception {

        Connection conn = source.getConnection();

        return conn;
    }


    private static DataSource source1;

    static {


        try {
            //方式2：
            Properties pros = new Properties();

            InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("druid.properties");

            pros.load(is);

            source1 = DruidDataSourceFactory.createDataSource(pros);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Connection getConnection3() throws Exception {

        Connection conn = source1.getConnection();

        return conn;
    }


    public static void closeResource(Connection connection, PreparedStatement preparedStatement) {
        //资源的关闭
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

    public static void closeResource(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        //资源的关闭
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
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    //使用dbutils.jar中提供的Dbutils工具类，实现资源的关闭
    public static void closeResource1(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
//        try {
//            DbUtils.close(connection);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(preparedStatement);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
//        try {
//            DbUtils.close(resultSet);
//        } catch (SQLException throwables) {
//            throwables.printStackTrace();
//        }
        DbUtils.closeQuietly(connection);
        DbUtils.closeQuietly(preparedStatement);
        DbUtils.closeQuietly(resultSet);
    }


}
