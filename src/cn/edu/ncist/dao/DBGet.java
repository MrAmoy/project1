package cn.edu.ncist.dao;

import cn.edu.ncist.bean.Parameter;
import java.sql.*;

public class DBGet {

    public static Connection conn;
    //类初始化块
    static {
        conn = DBGet.getConnection();
    }

    //连接数据库project1
    static Connection getConnection() {

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/"+Parameter.getOrigDB();
        String user = Parameter.getDbUserName();
        String password = Parameter.getDbPassWord();
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    //连接数据库project2
    /*public static Connection getConnection2() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/"+Parameter.getResuDB();
        String user = Parameter.getDbUserName();
        String password = Parameter.getDbPassWord();
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }*/


    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void closePreparedStatement(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

