# JDBC
JDBC、连接数据库（MySQL关系型数据库）
1.项目一是通过自己创建connection，获取jdbc驱动，然后编译sql语句，执行，然后资源的关闭的方式来进行的java连接数据库的五种不同的方式。
2.项目二是通过数据库连接池技术，来连接数据库，有dbcp,c3p0,druid等三种主流的数据库连接池技术，最后一种是通过Apache基金会下面的dbutils工具类来实现对jdbc的连接。
