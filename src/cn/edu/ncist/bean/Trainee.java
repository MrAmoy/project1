package cn.edu.ncist.bean;

public class Trainee {

    private String person_id;
    private String person_name;
    private String user_id;
    private String username;
    private String corp_id;
    private String corp_name;
    private String organ_id;
    private String organ_name;
    private String culture;
    private String phone;
    //生成时间
    private String generateTime;
    //隐患等级：     4-安监查出，3-重大隐患，2-超期未整改隐患，1-未检查
    private String hiddenLevel;

    //setter和getter方法

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        if (person_id == null)
            this.person_id = "";
        else
            this.person_id = person_id;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        if (person_name == null)
            this.person_name = "";
        else
            this.person_name = person_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        if (user_id == null)
            this.user_id = "";
        else
            this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (username == null)
            this.username = "";
        else
            this.username = username;
    }

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

    public String getOrgan_id() {
        return organ_id;
    }

    public void setOrgan_id(String organ_id) {
        if (organ_id == null)
            this.organ_id = "";
        else
            this.organ_id = organ_id;
    }

    public String getOrgan_name() {
        return organ_name;
    }

    public void setOrgan_name(String organ_name) {
        if (organ_name == null)
            this.organ_name = "";
        else
            this.organ_name = organ_name;
    }

    public String getCulture() {
        return culture;
    }

    public void setCulture(String culture) {
        if (culture == null)
            this.culture = "";
        else
            this.culture = culture;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        if (phone == null)
            this.phone = "";
        else
            this.phone = phone;
    }

    public String getGenerateTime() {
        return generateTime;
    }

    public void setGenerateTime(String generateTime) {
        this.generateTime = generateTime;
    }

    public String getHiddenLevel() {
        return hiddenLevel;
    }

    public void setHiddenLevel(String hiddenLevel) {
        this.hiddenLevel = hiddenLevel;
    }
}
