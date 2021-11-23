package net.ttcxy.chat.entity;

/**
 * 注册参数
 * @author huanglei
 */
public class RegisterParam {

    private String mail;

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
