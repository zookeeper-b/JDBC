package com.practice;

import com.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Exert1Test {
    @Test
    public void testInsert(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入用户名");
        String name = scanner.next();
        System.out.println("输入邮箱");
        String email = scanner.next();
        System.out.println("输入生日");
        String birth = scanner.next();

        String sql="insert into customers (name,email,birth)values(?,?,?)";
        int update = Update(sql, name, email, birth);
        if (update>0) {
            System.out.println("添加成功");
        }else {
            System.out.println("添加失败");
        }

    }

    //通用的增删改操作
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
            //如果是查询操作，有返回结果，则此方法true，如果执行的是增删改操作，没有返回结果，则此方法返回false
//          return   prepareStatement.execute();
            return prepareStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //5.关闭资源
            JDBCUtils.closeResource(connection, prepareStatement);
        }
    return 0;
    }



}
