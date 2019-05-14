package com.example.lijian.sf_im_sdk;

import android.util.Log;

public class IM_SDK {

    static {
        System.loadLibrary("Android_IM_SDK");
    }

    public IM_SDK(){
    }
    OnGetInterface m_callback = null;
    public void setCalReCallBackListenner(OnGetInterface callback){
        this.m_callback = callback;
        return;
    }
    //登录状态回调LOGIN_RESCODE rescode
    public void OnGetLoginState(int rescode){
        this.m_callback.GetLoginStaete(rescode);
    }

    //接收消息
    public void onGetMsgData(int resCode,int fromUserRole,String userID,String fromUsernam,String fromUserHead,String fromUerGroupID,String fromGroup,String msgTime,String msgcontent)
    {
        Log.e("Login","接收消息内容： " + msgcontent + "名字： " + userID + "时间 " + msgTime );
        MsgContent item = new MsgContent(fromUsernam,msgTime,msgcontent);
        m_callback.AddItem(item);
    }

    //发消息回调
    public void onSendMsgData(int resCode,String userID)
    {
        Log.e("Login","发消息内容:" + resCode + " 名字:" + userID);
        this.m_callback.GetSendMsgData(resCode,userID);
        //return;
    }

    //接收禁言消息
    public void onGetDisableSend(int sendType, int resCode ,int forbidType, String userId, String username, String time){
        this.m_callback.GetDisableSend(sendType,resCode,forbidType,userId,username,time);
        if (sendType ==1) {
            Log.e("Login", "禁言的用户名:" + username + "  时间:" + time);
        }else {
            Log.e("Login", "解除禁言的用户名:" + username);
        }
    }

    public native int InitSDK(int type, String userID ,String userName, String groupID,
                                String userGroup,String serverIp,String userHeadPortrait,int port);

    public native void ConnectMsgServer();

    public native void SendMsg(String msgdata ,int size);

    public native void ForbidSendMsg(int send_Type , int forbidType ,String userID, String userNname, String time);

    public native void DisConnServer();

    public native int GetServerTime();

}
