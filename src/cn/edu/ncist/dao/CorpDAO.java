package cn.edu.ncist.dao;

import java.sql.*;
import cn.edu.ncist.bean.Corp;
import cn.edu.ncist.bean.Trainee;
import java.util.List;
import static cn.edu.ncist.dao.DBGet.conn;

public class CorpDAO {

    //把符合条件的企业全部查找出来放到结果集中
    public static ResultSet queryAll(String reqSelTime) {

        PreparedStatement ps = null;
        ResultSet rs;
        try {

            /*String sql = "SELECT corp_statistics_temp.corp_id,corp_statistics_temp.corp_name,corp_statistics_temp.yearcheckcount,corp_statistics_temp.yearybwzg,corp_statistics_temp.yearybxqzg,corp_statistics_temp.yearybxczg,corp_statistics_temp.yearzdzg,corp_statistics_temp.yearzdwzg,SUM(zf_check_info.hidtrouble_check_result) " +
                    "FROM corp_statistics_temp " +
                    "LEFT JOIN zf_check_info ON corp_statistics_temp.corp_id = zf_check_info.corp_id " +
                    "LEFT JOIN corp ON corp_statistics_temp.corp_id = corp.id" +
                    " WHERE corp_statistics_temp.time LIKE '" + reqSelTime + "-%' AND zf_check_info.HIDTROUBLE_CHECK_TIME LIKE '" + reqSelTime + "-%' AND zf_check_info.HIDTROUBLE_HANDLE_RESULT = 1 " +
                    " AND corp.is_delete = 0 AND corp.reg_status = 1 GROUP BY corp_statistics_temp.corp_id";*/

            String sql = "SELECT cst.corp_id ,cst.corp_name ,cst.yearcheckcount ,cst.yearybwzg ,cst.yearybxqzg ,cst.yearzdzg ,cst.yearzdwzg ,zcis.hidtrouble_check_result " +
                    "FROM (" +
                        "(SELECT corp_id,corp_name,yearcheckcount,yearybwzg,yearybxqzg,yearybxczg,yearzdzg,yearzdwzg " +
                        "FROM corp_statistics_temp " +
                        "WHERE (yearcheckcount =0 OR yearcheckcount > safe_record) AND time like '" + reqSelTime + "-%') cst " +
                    "LEFT JOIN " +
                        "( SELECT corp_id, SUM(HIDTROUBLE_HANDLE_RESULT) AS hidtrouble_check_result " +
                        "FROM zf_check_info " +
                        "WHERE HIDTROUBLE_CHECK_TIME LIKE '" + reqSelTime + "-%'  AND HIDTROUBLE_HANDLE_RESULT = 1 " +
                        "GROUP BY corp_id ) zcis " +
                    "ON cst.corp_id = zcis.corp_id " +
                    "LEFT JOIN corp ON cst.corp_id = corp.id" +
                    ") WHERE corp.is_delete = 0 AND corp.reg_status = 1";

            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            /*Corp corp = new Corp();
            corp.setCorp_id(rs.getString(1));
            corp.setCorp_name(rs.getString(2));
            //1，检查记录总数
            corp.setYearcheckcount(Integer.parseInt(rs.getString(3)));
            //2，一般限期未整改
            corp.setYearybwzg(Integer.parseInt(rs.getString(4)));
            //3，一般限期整改
            corp.setYearybxqzg(Integer.parseInt(rs.getString(5)));
            //4，一般现场整改
            corp.setYearybxczg(Integer.parseInt(rs.getString(6)));
            //5，重大隐患:重大整改与重大未整改之和
            corp.setYearzdyh(Integer.parseInt(rs.getString(7)) + Integer.parseInt(rs.getString(8)));
            //6，安监检查隐患
            corp.setAjjcyh(Integer.parseInt(rs.getString(9)));*/

            return rs;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //DBGet.closeResultSet(rs);
            //DBGet.closePreparedStatement(ps);
            //DBGet.closeConnection(conn);
        }
        return null;
    }

    //查找企业负责人的各项信息
    public static Trainee findCorpDutyPerson(Corp corp) {

        Trainee trainee = new Trainee();
        String corpDutyPerson;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        try {
            //第一步：从corp这个表中选出 负责人,分管领导
            String sql = "SELECT corp.person_name,corp.leader_name FROM corp " +
                    "WHERE corp_code = '" + corp.getCorp_id() + "'";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            if (!rs.next()) return null;
            corpDutyPerson = rs.getString(1);
            if (corpDutyPerson == null || corpDutyPerson.equals("")) {    /*如果找不到责任人，则找到分管领导*/
                corpDutyPerson = rs.getString(2);
            }

            String sql1 = "SELECT person.id,person.name,users.username,person.organ_id,person.organ_name,person.culture,person.phone " +
                    "FROM person LEFT JOIN users ON person.user_id=users.id " +
                    "WHERE person.corp_id='" + corp.getCorp_id() + "' AND person.name='" + corpDutyPerson + "' AND users.username IS NOT  NULL AND users.username <> ''";
            ps = conn.prepareStatement(sql1);
            rs1 = ps.executeQuery();

            //如果没查找出记录，返回null
            if (!rs1.next()) return null;
            //如果查找到记录，但username为空，也返回null
            if (rs1.getString(3) == null) return null;

            trainee.setPerson_id(rs1.getString(1));
            trainee.setPerson_name(rs1.getString(2));
            trainee.setUsername(rs1.getString(3));
            trainee.setCorp_id(corp.getCorp_id());
            trainee.setCorp_name(corp.getCorp_name());
            trainee.setOrgan_id(rs1.getString(4));
            trainee.setOrgan_name(rs1.getString(5));
            trainee.setCulture(rs1.getString(6));
            trainee.setPhone(rs1.getString(7));
            if (corp.getAjjcyh() > 0)       /*如果是安监部门查出的隐患，则隐患等级设置为1*/
                trainee.setHiddenLevel("4");
            else                            /*如果是未检查，则隐患等级设置为0*/
                trainee.setHiddenLevel("1");

            return trainee;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBGet.closeResultSet(rs);
            DBGet.closeResultSet(rs1);
            DBGet.closePreparedStatement(ps);
            //DBGet.closeConnection(conn);
        }
        return null;
    }

    //查找整改负责人的各项信息
    public static Trainee findChangeDutyPerson(Corp corp, String reqSelTime) {
        Trainee trainee = new Trainee();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String sql;
            if (corp.getYearzdyh() > 0) {    /*若为重大隐患*/
                sql = "SELECT person.id,person.name,users.username,person.organ_id,person.organ_name,person.culture,person.phone " +
                        "FROM person LEFT JOIN users ON person.user_id=users.id " +
                        "WHERE person.id IN ( " +
                            "(SELECT duty_person_id FROM danger_6401 WHERE corp_id = '" + corp.getCorp_id() + "' " +
                            "AND check_time LIKE '" + reqSelTime.substring(0,7) + "-%' AND leve = '2' AND is_report=1)" +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6402 WHERE corp_id = '" + corp.getCorp_id() + "' " +
                            "AND check_time LIKE '" + reqSelTime.substring(0,7) + "-%' AND leve = '2' AND is_report=1)" +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6403 WHERE corp_id = '" + corp.getCorp_id() + "' " +
                            "AND check_time LIKE '" + reqSelTime.substring(0,7) + "-%' AND leve = '2' AND is_report=1)" +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6404 WHERE corp_id = '" + corp.getCorp_id() + "' " +
                            "AND check_time LIKE '" + reqSelTime.substring(0,7) + "-%' AND leve = '2' AND is_report=1)" +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6405 WHERE corp_id = '" + corp.getCorp_id() + "' " +
                            "AND check_time LIKE '" + reqSelTime.substring(0,7) + "-%' AND leve = '2' AND is_report=1)" +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6406 WHERE corp_id = '" + corp.getCorp_id() + "' " +
                            "AND check_time LIKE '" + reqSelTime.substring(0,7) + "-%' AND leve = '2' AND is_report=1)" +
                        ") AND users.username IS NOT NULL";
                trainee.setHiddenLevel("3");
            } else {     /*若为超期*/
                sql = "SELECT person.id,person.name,users.username,person.organ_id,person.organ_name,person.culture,person.phone " +
                        "FROM person LEFT JOIN users ON person.user_id=users.id " +
                        "WHERE person.id IN (" +
                            "(SELECT duty_person_id FROM danger_6401 " +
                            "WHERE corp_id = '" + corp.getCorp_id() + "' AND " +
                            "leve = '1' AND state != '已归档' AND is_report=1 AND limit_time<'" + reqSelTime + "') " +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6402 " +
                            "WHERE corp_id = '" + corp.getCorp_id() + "' AND " +
                            "leve = '1' AND state != '已归档' AND is_report=1 AND limit_time<'" + reqSelTime + "') " +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6403 " +
                            "WHERE corp_id = '" + corp.getCorp_id() + "' AND " +
                            "leve = '1' AND state != '已归档' AND is_report=1 AND limit_time<'" + reqSelTime + "') " +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6404 " +
                            "WHERE corp_id = '" + corp.getCorp_id() + "' AND " +
                            "leve = '1' AND state != '已归档' AND is_report=1 AND limit_time<'" + reqSelTime + "') " +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6405 " +
                            "WHERE corp_id = '" + corp.getCorp_id() + "' AND " +
                            "leve = '1' AND state != '已归档' AND is_report=1 AND limit_time<'" + reqSelTime + "') " +
                            "UNION " +
                            "(SELECT duty_person_id FROM danger_6406 " +
                            "WHERE corp_id = '" + corp.getCorp_id() + "' AND " +
                            "leve = '1' AND state != '已归档' AND is_report=1 AND limit_time<'" + reqSelTime + "') " +
                        ") AND users.username IS NOT NULL AND users.username <> ''";
                trainee.setHiddenLevel("2");
            }
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                trainee.setPerson_id(rs.getString(1));
                trainee.setPerson_name(rs.getString(2));
                trainee.setUsername(rs.getString(3));
                trainee.setCorp_id(corp.getCorp_id());
                trainee.setCorp_name(corp.getCorp_name());
                trainee.setOrgan_id(rs.getString(4));
                trainee.setOrgan_name(rs.getString(5));
                trainee.setCulture(rs.getString(6));
                trainee.setPhone(rs.getString(7));

                if (!trainee.getUsername().equals("")) return trainee;
            }
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBGet.closeResultSet(rs);
            DBGet.closePreparedStatement(ps);
            //DBGet.closeConnection(conn);
        }
        return null;
    }

    //把选出来的培训人员插入到trainees表里去
    public static void insertTrainees(List<Trainee> traineesList, String reqSelTime) {
        PreparedStatement ps = null;
        try {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO trainees VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            for (Trainee trainees : traineesList) {
                ps.setString(1, trainees.getPerson_id());
                ps.setString(2, trainees.getPerson_name());
                ps.setString(3, trainees.getUsername());
                ps.setString(4, trainees.getCorp_id());
                ps.setString(5, trainees.getCorp_name());
                ps.setString(6, trainees.getOrgan_id());
                ps.setString(7, trainees.getOrgan_name());
                ps.setString(8, trainees.getCulture());
                ps.setString(9, trainees.getPhone());
                ps.setString(10, reqSelTime);
                ps.setString(11, trainees.getHiddenLevel());
                ps.addBatch();
            }
            ps.executeBatch();
            conn.commit();
            ps.clearBatch();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBGet.closePreparedStatement(ps);
            //DBGet.closeConnection(conn);
        }
    }

    //从数据库的parameter表中读取参数
    public static ResultSet readFromDB(String sql) {
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            return rs;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}