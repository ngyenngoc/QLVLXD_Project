package model;

public class Users {
    private int userID;
    private String userName;
    private String passWord;
    public Users(int userID, String userName, String passWord) {
        this.userID = userID;
        this.userName = userName;
        this.passWord = passWord;
    }

    public int getUserId() {
        return userID;
    }
    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setUserId(int userId) {
        this.userID = userID;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public void setPassWord(String passWord){
        this.passWord = passWord;
    }
}
