package com.blob;

import com.bean.Customers;
import com.util.JDBCUtils;
import org.junit.Test;

import java.io.*;
import java.sql.*;

//使用PreparedStatement操作Blob类型的数据
public class BlobTest {
    //向数据表中插入blob类型字段
    @Test
    public void testInsert() throws Exception {
        Connection connection = JDBCUtils.getConnection();
        String sql = "insert into customers(name,email,birth,photo)values(?,?,?,?)";
        PreparedStatement prepareStatement = connection.prepareStatement(sql);
        prepareStatement.setObject(1, "xwy");
        prepareStatement.setObject(2, "bhq.com");
        prepareStatement.setObject(3, "1999-01-01");
        FileInputStream is = new FileInputStream(new File("src/com/blob/play.jpg"));
        prepareStatement.setBlob(4, is);
        prepareStatement.execute();
        JDBCUtils.closeResource(connection, prepareStatement);

    }

    @Test
    public void testQuery() {
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        ResultSet resultSet = null;
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            connection = JDBCUtils.getConnection();
            String sql = "select id,name,email,birth,photo from customers where id=?";
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setObject(1, 0);
            resultSet = prepareStatement.executeQuery();
            if (resultSet.next()) {

                //方式一
                //            int id = resultSet.getInt(1);
                //            String name = resultSet.getString(2);
                //            String email = resultSet.getString(3);
                //            Date birth = resultSet.getDate(4);
                //方式一
                int id1 = resultSet.getInt("id");
                String name1 = resultSet.getString("name");
                String email1 = resultSet.getString("email");
                Date birth1 = resultSet.getDate("birth");
                Customers customers = new Customers(id1, name1, email1, birth1);
                System.out.println(customers);

                //将blob类型的字段下载下来，以文件的形式保存在本地
                Blob photo = resultSet.getBlob("photo");
                is = photo.getBinaryStream();
                fos = new FileOutputStream("bhq.jpg");
                byte[] buffer = new byte[1024];
                int len;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            JDBCUtils.closeResource(connection, prepareStatement, resultSet);

        }


    }
}