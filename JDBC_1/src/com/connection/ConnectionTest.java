package com.connection;

import org.junit.Test;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionTest {
    @Test
    //方式一连接数据库
    public void testConnection1() throws SQLException {

        //1.获取Driver实现类对象
        Driver driver = new com.mysql.jdbc.Driver();
        //jdbc:mysql是协议
        //localhost:3306是IP地址
        //world是数据库名称
        //2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/world";
        //3.将用户和密码封装在Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式二连接数据库：对齐方式一的迭代，在如下的程序中不出现第三方api，使得程序具有更好的可移植型
    @Test
    public void testConnect2() throws Exception {

        //1.获取Driver实现类对象，使用反射

        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //2.提供要连接的数据库
        String url = "jdbc:mysql://localhost:3306/world";
        //3.将用户和密码封装在Properties中
        Properties info = new Properties();
        info.setProperty("user", "root");
        info.setProperty("password", "root");
        Connection connect = driver.connect(url, info);
        System.out.println(connect);
    }

    //方式三，使用DriverManager替换Driver
    @Test
    public void testConnectionTest3() throws Exception {
        //1.获取Driver实现类对象，使用反射
        Class clazz = Class.forName("com.mysql.jdbc.Driver");
        Driver driver = (Driver) clazz.newInstance();
        //2.获取另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/world";
        String user = "root";
        String password = "root";
        //3.注册驱动
        DriverManager.registerDriver(driver);
        //4.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }

    //方式四，使用DriverManager替换Driver，可以只是加载驱动，不用显示的注册驱动过了
    @Test
    public void testConnectionTest4() throws Exception {
        //1.获取另外三个连接的基本信息
        String url = "jdbc:mysql://localhost:3306/world";
        String user = "root";
        String password = "root";
        //2.加载驱动(在driver实现类中，的static静态方法声明如下操作)
        Class.forName("com.mysql.jdbc.Driver");
//        Driver driver = (Driver) clazz.newInstance();
//        //注册驱动
//        DriverManager.registerDriver(driver);

        //3.获取连接
        Connection connection = DriverManager.getConnection(url, user, password);
        System.out.println(connection);

    }

    //方式五：也是最常用的连接数据库的方式，将数据库连接需要的4个基本信息声明在配置文件中，通过读取配置文件的方式，获取连接
    //实现了数据和代码的分离，解除耦合。
    //如果需要修改配置文件的信息，可以避免程序重新打包
    @Test
    public void testConnectionTest5() throws Exception {
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
        Connection connection = DriverManager.getConnection(url, user, password);

        System.out.println(connection);
    }


}
