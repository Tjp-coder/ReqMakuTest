package util;
//		1. 定义一个类 JDBCUtils
//      2. 提供静态代码块加载配置文件，初始化连接池对象
//      3. 提供方法
//                1. 获取连接方法：通过数据库连接池获取连接
//                2. 释放资源
//                3. 获取连接池的方法


import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Druid连接池的工具类
 */
public class JDBCUtils {
    private static DataSource ds;

    static{
        //加载配置文件
        Properties pro = new Properties();
        InputStream inputStream = JDBCUtils.class.getClassLoader().getResourceAsStream("druid.properties");
        try {
            pro.load(inputStream);
            //初始化资源池
            ds = DruidDataSourceFactory.createDataSource(pro);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取连接方法：通过数据库连接池获取连接
    /**
    * 获取连接
    */
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }

    /**
     * 获取连接池的方法
     */
    public  static DataSource getDataSource(){
        return ds;
    }

    //释放资源
    /**
     * 释放资源
     * */
    public static void close(Statement stmt,Connection conn){
        //既然是重复的代码，那么可以直接传空参调用另一个
        close(null,stmt,conn);
    }
    /**
     * 重载关闭方法
     * */
    public static void close(ResultSet rs ,Statement stmt, Connection conn){
        if (rs!=null){
            try {
                rs.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (stmt!=null){
            try {
                stmt.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        if (conn!=null){
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }


}
