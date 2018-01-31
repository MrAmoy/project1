package cn.edu.ncist.bean;

public class Corp {

    //企业id
    private String corp_id;
    //企业名称
    private String corp_name;
    //1，总检查记录数
    private int yearcheckcount;
    //2，一般限期未整改
    private int yearybwzg;
    //3，一般限期整改
    private int yearybxqzg;
    //4，一般现场整改
    private int yearybxczg;
    //5，重大隐患:重大整改与重大未整改之和
    private int yearzdyh;
    //6，安监检查隐患
    private int ajjcyh = 0;

    //Getter和Setter方法
    public String getCorp_id() {
        return corp_id;
    }

    public void setCorp_id(String corp_id) {
        if (corp_id == null)
            this.corp_id = "";
        else
            this.corp_id = corp_id;
    }

    public String getCorp_name() {
        return corp_name;
    }

    public void setCorp_name(String corp_name) {
        if (corp_name == null)
            this.corp_name = "";
        else
            this.corp_name = corp_name;
    }

    public int getYearcheckcount() {
        return yearcheckcount;
    }

    public void setYearcheckcount(int yearcheckcount) {
        this.yearcheckcount = yearcheckcount;
    }

    public int getYearybwzg() {
        return yearybwzg;
    }

    public void setYearybwzg(int yearybwzg) {
        this.yearybwzg = yearybwzg;
    }

    public int getYearybxqzg() {
        return yearybxqzg;
    }

    public void setYearybxqzg(int yearybxqzg) {
        this.yearybxqzg = yearybxqzg;
    }

    public int getYearybxczg() {
        return yearybxczg;
    }

    public void setYearybxczg(int yearybxczg) {
        this.yearybxczg = yearybxczg;
    }

    public int getYearzdyh() {
        return yearzdyh;
    }

    public void setYearzdyh(int yearzdyh) {
        this.yearzdyh = yearzdyh;
    }

    public int getAjjcyh() {
        return ajjcyh;
    }

    public void setAjjcyh(String ajjcyh) {
        if (ajjcyh == null || ajjcyh.equals(""))
            this.ajjcyh = 0;
        else
            this.ajjcyh = Integer.parseInt(ajjcyh);
    }
}
