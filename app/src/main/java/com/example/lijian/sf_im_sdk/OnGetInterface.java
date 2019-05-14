package com.example.lijian.sf_im_sdk;

public interface OnGetInterface {

    //public TextView getText();

    //public List<MsgContent>  getDataList();

    //public  MsgItemAdapter getAdapter();

    public void AddItem(MsgContent item);

    public void GetLoginStaete(int state);

    public void GetSendMsgData(int resCode,String userID);

    public void GetDisableSend(int sendType, int resCode ,int forbidType, String userId, String username, String time);
    //public ListView getListView();
}
