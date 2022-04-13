package com.practice;

import com.bean.Student;
import com.util.JDBCUtils;
import org.junit.Test;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Scanner;

public class ExertTest2 {
    /**
     * Type:
     * IDCard:
     * ExamCard
     * StudentName
     * Location
     * Grade
     */

    @Test
    public void testInsert() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("四级/六级");
        int type = scanner.nextInt();
        System.out.println("身份证号码");
        final String IDCard = scanner.nextLine();
        System.out.println("准考证号");
        String examCard = scanner.nextLine();
        System.out.println("学生姓名");
        String studentName = scanner.nextLine();
        System.out.println("所在城市");
        String location = scanner.nextLine();
        System.out.println("考试成绩");
        int grade = scanner.nextInt();

        String sql = "insert into examstudent(Type,IDCard,examCard,StudentName,location,grade)values(?,?,?,?,?,?)";
        int i = Update(sql, type, IDCard, examCard, studentName, location, grade);
        if (i > 0) {
            System.out.println("添加成功");
        } else {
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

    //根据身份证号查询学生相关信息
    @Test
    public void queryWithIDCardOrExamCard() {
        System.out.println("请选择你要输入的类型");
        System.out.println("a.准考证号");
        System.out.println("b.身份证号");
        Scanner scanner = new Scanner(System.in);
        String selection = scanner.next();
        if ("a".equalsIgnoreCase(selection)) {
            System.out.println("请输入准考证号");
            String examCard = scanner.next();

            String sql = "select FlowID flowID,Type tpe,IDCard ,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard= ?";
            Student student = getInstance(Student.class, sql, examCard);
            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("你输入的准考证号有误");
            }

        } else if ("b".equalsIgnoreCase(selection)) {
            System.out.println("请输入身份证号");
            String IDCard = scanner.next();

            String sql = "select FlowID flowID,Type tpe,IDCard ,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where IDCard= ?";
            Student student = getInstance(Student.class, sql, IDCard);
            if (student != null) {
                System.out.println(student);
            } else {
                System.out.println("你输入的身份证号有误");
            }

        } else {
            System.out.println("您输入的有误，请从新输入");
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


    //完成学生信息的删除功能
    @Test
    public void testDeleteByExamCard() {
        System.out.println("请输入学生的准考证号");
        Scanner scanner = new Scanner(System.in);
        String e = scanner.next();
        //查询指定指定学生的信息
        String sql = "select FlowID flowID,Type tpe,IDCard ,ExamCard examCard,StudentName name,Location location,Grade grade from examstudent where examCard= ?";
        Student student = getInstance(Student.class, sql, e);
        if (student == null) {
            System.out.println("查无此人，请重新输入");
        } else {
            String sql1 = "delete from examstudent where examCard =?";
            int update = Update(sql1, e);
            if (update > 0) {
                System.out.println("删除成功");
            }

        }

    }


    @Test
    public void testDeleteByExamCard1() {
        System.out.println("请输入学生的考号");
        Scanner scanner = new Scanner(System.in);
        String e = scanner.next();
        String sql = "delete from examstudent where examCard =?";
        int update = Update(sql, e);
        if (update > 0) {
            System.out.println("删除成功");
        } else {
            System.out.println("查无此人，请重新输入");
        }

    }
}
