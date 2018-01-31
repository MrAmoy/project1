package cn.edu.ncist.model;

import cn.edu.ncist.bean.Corp;
import cn.edu.ncist.bean.Parameter;
import cn.edu.ncist.bean.Trainee;
import cn.edu.ncist.dao.CorpDAO;
import cn.edu.ncist.dao.DBGet;

import java.sql.ResultSet;

import static cn.edu.ncist.bean.TraineesList.traineesList;
import static cn.edu.ncist.dao.DBGet.conn;
import static cn.edu.ncist.model.ProcAndIdent.needResultNum;

public class Run {
    public static void run(String reqSelTime) throws Exception {

        //0.初始化参数
        Parameter.setParameter();
        //读取出全部企业
        ResultSet rs = CorpDAO.queryAll(reqSelTime.substring(0,7));
        Corp corp;

        System.out.println(reqSelTime);
        System.out.println(rs.getRow());

        while (rs.next()) {

            //如果已满，则终止循环
            if (traineesList.size() >= needResultNum) break;

            /*1：
             * 每次循环读取企业记录一条
             * */
            corp = new Corp();
            corp.setCorp_id(rs.getString(1));
            corp.setCorp_name(rs.getString(2));
            //1，检查记录总数
            corp.setYearcheckcount(Integer.parseInt(rs.getString(3)));
            //2，一般限期未整改
            corp.setYearybwzg(Integer.parseInt(rs.getString(4)));
            //3，一般限期整改
            corp.setYearybxqzg(Integer.parseInt(rs.getString(5)));
            //4，一般现场整改
            //corp.setYearybxczg(Integer.parseInt(rs.getString(6)));
            //5，重大隐患:重大整改与重大未整改之和
            corp.setYearzdyh(Integer.parseInt(rs.getString(6)) + Integer.parseInt(rs.getString(7)));
            //6，安监检查隐患
            corp.setAjjcyh(rs.getString(8));

            /*2：
             * 如果有企业，放入模型求结果(1或0)
             * 如果此企业经过模型得出结果为0，则剔除此企业，继续选择下一个企业
             * */
            if (ProcAndIdent.ProcAndIdent(corp) != 1)
                continue;

            /*3：
             * 如果上一步的得出的type为1，进入下一步，如果安监查出非0，读取企业负责人姓名。
             * 如果为0，则判断隐患是否为重大或超期
             * */
            Trainee trainee;
            if (corp.getAjjcyh() > 0) {         /*安监查出有隐患*/
                //找到企业负责人
                trainee = CorpDAO.findCorpDutyPerson(corp);
                if (trainee != null)
                    traineesList.add(trainee);
            } else if (corp.getYearzdyh() > 0) {       /*重大隐患*/
                //找到整改负责人
                trainee = CorpDAO.findChangeDutyPerson(corp, reqSelTime);
                if (trainee != null) {
                    traineesList.add(trainee);
                }
            } else if (corp.getYearybwzg() > 0) {       /*超期未整改*/
                //找到整改负责人
                trainee = CorpDAO.findChangeDutyPerson(corp, reqSelTime);
                if (trainee != null) {
                    traineesList.add(trainee);
                }
            } else if (corp.getYearcheckcount() == 0) {     /*总检查次数为0*/
                //找到企业负责人
                trainee = CorpDAO.findCorpDutyPerson(corp);
                if (trainee != null)
                    traineesList.add(trainee);
            }
        }
        //关闭结果集
        rs.close();
        //把结果写入数据库
        CorpDAO.insertTrainees(traineesList, reqSelTime);
        //关闭连接
        DBGet.closeConnection(conn);
    }
}
