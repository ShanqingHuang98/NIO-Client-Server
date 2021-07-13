package nio;

import java.math.BigInteger;

/**
 * 主要是information表格和account表格里面的内容
 *
 * @author concise
 */
public class User {
    private int id;
    private String uname;
    private String upass;
    private int type;
    private int klass;
    private String sex;
    private BigInteger number;
    private long year;
    private String academy;
    private String major;

    public User(String uname) {
        this.uname = uname;
    }

    public User(int id, String uname, int type) {
        this.id = id;
        this.uname = uname;
        this.type = type;
    }


    // 提供公有的getter 和setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpass() {
        return upass;
    }

    public void setUpass(String upass) {
        this.upass = upass;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public BigInteger getNumber() {
        return number;
    }

    public void setNumber(BigInteger number) {
        this.number = number;
    }

    public long getYear() {
        return year;
    }

    public void setYear(long year) {
        this.year = year;
    }

    public String getAcademy() {
        return academy;
    }

    public void setAcademy(String academy) {
        academy = academy;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public int getKlass() {
        return klass;
    }

    public void setKlass(int klass) {
        this.klass = klass;
    }
    // 提供四个公有的构造方法

    public User(int id) {
        this.id = id;
    }

    public User(String uname, String upass) {
        this.uname = uname;
        this.upass = upass;
    }

    public User(String uname, String upass, int type) {
        this.uname = uname;
        this.upass = upass;
        this.type = type;
    }

    public User(int id, String uname, int type, BigInteger number) {
        this.id = id;
        this.uname = uname;
        this.type = type;
        this.number = number;
    }

    public User(int id, String uname, String sex, int type, String major, String academy, int klass, int year) {
        this.id = id;
        this.uname = uname;
        this.type = type;
        this.klass = klass;
        this.sex = sex;
        this.year = year;
        this.academy = academy;
        this.major = major;
    }

    public User(String uname, String upass, int type, int klass, String sex, BigInteger number, long year, String academy, String major) {
        this.uname = uname;
        this.upass = upass;
        this.type = type;
        this.klass = klass;
        this.sex = sex;
        this.number = number;
        this.year = year;
        this.academy = academy;
        this.major = major;
    }

    public User(String uname, String upass, int type, String sex, long year, String academy, String major) {
        this.uname = uname;
        this.upass = upass;
        this.type = type;
        this.year = year;
        this.academy = academy;
        this.major = major;
    }

    public User(int id, String uname, String sex, int type, BigInteger number, String major, String academy, int klass, int year) {
        this.id = id;
        this.uname = uname;
        this.type = type;
        this.klass = klass;
        this.sex = sex;
        this.number = number;
        this.year = year;
        this.academy = academy;
        this.major = major;
    }

    public User(String uname, String upass, int type, String sex, BigInteger number, Long year, String academy, String major) {
        this.uname = uname;
        this.upass = upass;
        this.type = type;
        this.sex = sex;
        this.number = number;
        this.year = year;
        this.academy = academy;
        this.major = major;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", upass='" + upass + '\'' +
                ", type=" + type +
                ", sex='" + sex + '\'' +
                ", number=" + number +
                ", year=" + year +
                ", Academy='" + academy + '\'' +
                ", major='" + major + '\'' +
                '}';
    }


}

