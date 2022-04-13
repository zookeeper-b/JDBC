package com.blob;

//使用PreparedStatement实现批量数据的操作

//update,delete本身就具有批量操作的效果
//insert批量操作，批处理,使用PreparedStatement更高效的批量数据的操作

//方式一statement


import com.util.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class InsertTest {
    @Test
    public void test2() {
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        try {
            long start = System.currentTimeMillis();

            //方式二PreparedStatement
            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            prepareStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= 20000; i++) {
                prepareStatement.setObject(1, "name_" + i);

                prepareStatement.execute();
            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为");
            System.out.println(end - start);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, prepareStatement);
        }


    }

    //批量插入的方式三
    //addBatch(),executeBatch(),clearBatch()
    @Test
    public void test3() {
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        try {
            long start = System.currentTimeMillis();

            //方式三PreparedStatement
            //mysql默认是关闭批处理的，我们需要一个参数，让mysql支持批处理的操作
            //?rewriteBatchedStatements=true 卸载配置文件的后面

            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            prepareStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= 2000000; i++) {
                prepareStatement.setObject(1, "name_" + i);

                //1.攒sql
                prepareStatement.addBatch();
                if (i % 500 == 0) {
                    //2.执行
                    prepareStatement.executeBatch();
                    //3.清空batch
                    prepareStatement.clearBatch();
                }

            }
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为");
            System.out.println(end - start);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, prepareStatement);
        }


    }

    @Test
    public void test4() {
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        try {
            long start = System.currentTimeMillis();

            //方式三PreparedStatement
            //mysql默认是关闭批处理的，我们需要一个参数，让mysql支持批处理的操作
            //?rewriteBatchedStatements=true 卸载配置文件的后面

            connection = JDBCUtils.getConnection();
            //设置不允许自动提交数据
            connection.setAutoCommit(false);



            String sql = "insert into goods(name) values(?)";
            prepareStatement = connection.prepareStatement(sql);
            for (int i = 1; i <= 2000000; i++) {
                prepareStatement.setObject(1, "name_" + i);

                //1.攒sql
                prepareStatement.addBatch();
                if (i % 500 == 0) {
                    //2.执行
                    prepareStatement.executeBatch();
                    //3.清空batch
                    prepareStatement.clearBatch();
                }

            }

            //统一的提交数据
            connection.commit();
            long end = System.currentTimeMillis();
            System.out.println("花费的时间为");
            System.out.println(end - start);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            JDBCUtils.closeResource(connection, prepareStatement);
        }


    }


}
