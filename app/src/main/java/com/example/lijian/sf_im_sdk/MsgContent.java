package com.example.lijian.sf_im_sdk;

public class MsgContent {

    private String name;
    private String msgtime;
    private String msgData;
    private int resCode;

    public MsgContent(String name, String msgtime, String msgData, int resCode, String userID) {
        this.name = name;
        this.msgtime = msgtime;
        this.msgData = msgData;
        this.resCode = resCode;
        this.userID = userID;
    }

    private String userID;
    public MsgContent(){
    }

    public MsgContent(String name, String msgtime , String msgData) {
        this.name = name;
        this.msgtime = msgtime;
        this.msgData = msgData;

    }
    public MsgContent(int resCode,String userID) {
        this.resCode = resCode;
        this.userID = userID;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(String msgtime) {
        this.msgtime = msgtime;
    }

    public String getMsgData() {
        return msgData;
    }

    public void setMsgData(String msgData) {
        this.msgData = msgData;
    }
    public int getResCode() {
        return resCode;
    }

    public void setResCode(int resCode) {
        this.resCode = resCode;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
