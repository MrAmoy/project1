package cn.edu.ncist.bean;

import cn.edu.ncist.dao.CorpDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.ResultSet;

public class Parameter {
/*
    该类用于存储程序相关参数
*/

    private static String dbUserName = null;//数据库连接用户
    private static String dbPassWord = null;//数据库连接密码
    private static String origDB = null;//原始数据库名
    private static int theEndNum = 0;//需要获取记录的数量

    public static void setParameter() throws Exception {

        //从配置文件读取数据库配置参数
        BufferedReader br = new BufferedReader(new FileReader(new File("src/cn/edu/ncist/conf/dbaParam.txt")));
        String line;

        while ((line = br.readLine()) != null) {
            String splits[] = line.split("=");

            if (splits.length > 0) {
                try {
                    if ("dbUserName".equals(splits[0])) {
                        dbUserName = splits[1];
                    } else if ("dbPassWord".equals(splits[0])) {
                        dbPassWord = splits[1];
                    } else if ("origDB".equals(splits[0])) {
                        origDB = splits[1];
                    }
                } catch (NumberFormatException e) {
                    // 非数字，报错
                    System.out.println("dbaParam.txt格式错误");
                }
            }
        }

        //从数据库获取相关参数
        String sql = "select * from parameter";
        ResultSet rs = CorpDAO.readFromDB(sql);
        while (rs.next()) {
            if ("theEndNum".equals(rs.getString("attribute"))) {
                theEndNum = Integer.valueOf(rs.getString("value"));
            }
        }
        //rs.close();
    }


    public static String getDbUserName() {
        return dbUserName;
    }

    public static String getDbPassWord() {
        return dbPassWord;
    }

    public static String getOrigDB() {
        return origDB;
    }

    public static int getTheEndNum() {
        return theEndNum;
    }

}
