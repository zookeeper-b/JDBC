package com.crud;

import com.bean.Customers;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

//演示 PreparedStatement替换 Statement 解决SQL注入问题
public class PreparedStatementTest {
    @Test
    public void testLogin() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("请输入名称");
        String user = scanner.nextLine();
        System.out.print("请输入密码");
        String password = scanner.nextLine();

//        String  sql = "select user,password form usertable where user='"+user+"' and password='"+password+"'";
        String sql = "select user,password form usertable where user=? and password=? ";
        Customers customers = getInstance(Customers.class, sql, user, password);
        if (customers != null) {
            System.out.println("登陆成功");
        } else {
            System.out.println("登陆失败");
        }

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
