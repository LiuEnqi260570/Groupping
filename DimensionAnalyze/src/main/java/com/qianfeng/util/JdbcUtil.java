package com.qianfeng.util;

import com.qianfeng.common.GlobalConstants;

import java.sql.*;

/**
 * @Auther: lyd
 * @Date: 2018/7/27 17:09
 * @Description:获取mysql的连接和关闭
 */
public class JdbcUtil {

    static {
        try {
            Class.forName(GlobalConstants.DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取mysql的连接
     * @return
     */
    public static Connection getConn(){
        Connection conn = null;
        try {
            conn =  DriverManager.getConnection(GlobalConstants.URL,
                    GlobalConstants.USERNAME,GlobalConstants.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 关闭相关对象
     * @param conn
     * @param ps
     * @param rs
     */
    public static void close(Connection conn, PreparedStatement ps, ResultSet rs){
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException e) {
                //do nothing
            }
        }

        if(ps != null){
            try {
                ps.close();
            } catch (SQLException e) {
                //do nothing
            }
        }

        if(rs != null){
            try {
                rs.close();
            } catch (SQLException e) {
                //do nothing
            }
        }
    }

    public static void main(String[] args) {
        System.out.println(getConn());
    }
}
