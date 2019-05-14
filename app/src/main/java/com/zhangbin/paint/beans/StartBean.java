package com.zhangbin.paint.beans;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class StartBean implements Serializable {

    /**
     * msg : ok
     * ppt_info : [{"sourceId":"8663841961388352","deployPath":"http://192.168.1.207:8081/","clientId":"1001","convertTime":"1551766867","totalPage":"19","createType":"0","liveId":"100120190330gEdVJz8U","downloadTime":"1551766729","valid":"1","pptId":"83461B08A0401FC68D9C2A7E036C4710","pptPath":"E:/PPT_DOWNLOAD/【基础作业】连加、连减解决问题.pptx","currentSlide":"1","convertPath":"E:/PPT_CONVERT/","deployTime":"1551766867","currentPage":"1","pptFlag":"6"},{"sourceId":"8663843515148608","deployPath":"http://192.168.1.207:8081/","clientId":"1001","convertTime":"1553234949","totalPage":"14","createType":"0","liveId":"100120190330gEdVJz8U","downloadTime":"1553234870","valid":"1","pptId":"ED23A6E027208FC5694437401106668B","pptPath":"E:/PPT_DOWNLOAD/【提升作业】两、三位数乘一位数解决问题第1课时(2).pptx","currentSlide":"1","convertPath":"E:/PPT_CONVERT/","deployTime":"1553234949","currentPage":"1","pptFlag":"6"},{"sourceId":"8663849891473728","deployPath":"http://192.168.1.207:8081/","clientId":"1001","convertTime":"1553234949","totalPage":"14","createType":"0","liveId":"100120190330gEdVJz8U","downloadTime":"1553234870","valid":"1","pptId":"ED23A6E027208FC5694437401106668B","pptPath":"E:/PPT_DOWNLOAD/【提升作业】两、三位数乘一位数解决问题第1课时(2).pptx","currentSlide":"1","convertPath":"E:/PPT_CONVERT/","deployTime":"1553234949","currentPage":"1","pptFlag":"6"},{"sourceId":"8663855634000192","deployPath":"http://192.168.1.207:8081/","clientId":"1001","convertTime":"1553234949","totalPage":"14","createType":"0","liveId":"100120190330gEdVJz8U","downloadTime":"1553234870","valid":"1","pptId":"ED23A6E027208FC5694437401106668B","pptPath":"E:/PPT_DOWNLOAD/【提升作业】两、三位数乘一位数解决问题第1课时(2).pptx","currentSlide":"1","convertPath":"E:/PPT_CONVERT/","deployTime":"1553234949","currentPage":"1","pptFlag":"6"}]
     * code : 0
     * live_info : {"liveFlag":"18","hostName":"李剑","liveNum":"0","boardWidth":"884","planStartTime":"1553940000","liveType":"0","liveTitle":"test","boardHeight":"497","hostId":"hostId","realStartTime":"0","liveId":"100120190330gEdVJz8U","pullUrl":"rtmp://192.168.1.207/live/","planDuration":"60"}
     * timestamp : 1557736675
     */

    @SerializedName("msg")
    private String msg;
    @SerializedName("code")
    private String code;
    @SerializedName("live_info")
    private LiveInfoBean liveInfo;
    @SerializedName("timestamp")
    private String timestamp;
    @SerializedName("ppt_info")
    private List<PptInfoBean> pptInfo;

    public static StartBean objectFromData(String str) {

        return new Gson().fromJson(str, StartBean.class);
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LiveInfoBean getLiveInfo() {
        return liveInfo;
    }

    public void setLiveInfo(LiveInfoBean liveInfo) {
        this.liveInfo = liveInfo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<PptInfoBean> getPptInfo() {
        return pptInfo;
    }

    public void setPptInfo(List<PptInfoBean> pptInfo) {
        this.pptInfo = pptInfo;
    }
    public static class LiveInfoBean {
        /**
         * liveFlag : 18
         * hostName : 李剑
         * liveNum : 0
         * boardWidth : 884
         * planStartTime : 1553940000
         * liveType : 0
         * liveTitle : test
         * boardHeight : 497
         * hostId : hostId
         * realStartTime : 0
         * liveId : 100120190330gEdVJz8U
         * pullUrl : rtmp://192.168.1.207/live/
         * planDuration : 60
         */

        @SerializedName("liveFlag")
        private String liveFlag;
        @SerializedName("hostName")
        private String hostName;
        @SerializedName("liveNum")
        private String liveNum;
        @SerializedName("boardWidth")
        private String boardWidth;
        @SerializedName("planStartTime")
        private String planStartTime;
        @SerializedName("liveType")
        private String liveType;
        @SerializedName("liveTitle")
        private String liveTitle;
        @SerializedName("boardHeight")
        private String boardHeight;
        @SerializedName("hostId")
        private String hostId;
        @SerializedName("realStartTime")
        private String realStartTime;
        @SerializedName("liveId")
        private String liveId;
        @SerializedName("pullUrl")
        private String pullUrl;
        @SerializedName("planDuration")
        private String planDuration;

        public static LiveInfoBean objectFromData(String str) {

            return new Gson().fromJson(str, LiveInfoBean.class);
        }

        public String getLiveFlag() {
            return liveFlag;
        }

        public void setLiveFlag(String liveFlag) {
            this.liveFlag = liveFlag;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public String getLiveNum() {
            return liveNum;
        }

        public void setLiveNum(String liveNum) {
            this.liveNum = liveNum;
        }

        public String getBoardWidth() {
            return boardWidth;
        }

        public void setBoardWidth(String boardWidth) {
            this.boardWidth = boardWidth;
        }

        public String getPlanStartTime() {
            return planStartTime;
        }

        public void setPlanStartTime(String planStartTime) {
            this.planStartTime = planStartTime;
        }

        public String getLiveType() {
            return liveType;
        }

        public void setLiveType(String liveType) {
            this.liveType = liveType;
        }

        public String getLiveTitle() {
            return liveTitle;
        }

        public void setLiveTitle(String liveTitle) {
            this.liveTitle = liveTitle;
        }

        public String getBoardHeight() {
            return boardHeight;
        }

        public void setBoardHeight(String boardHeight) {
            this.boardHeight = boardHeight;
        }

        public String getHostId() {
            return hostId;
        }

        public void setHostId(String hostId) {
            this.hostId = hostId;
        }

        public String getRealStartTime() {
            return realStartTime;
        }

        public void setRealStartTime(String realStartTime) {
            this.realStartTime = realStartTime;
        }

        public String getLiveId() {
            return liveId;
        }

        public void setLiveId(String liveId) {
            this.liveId = liveId;
        }

        public String getPullUrl() {
            return pullUrl;
        }

        public void setPullUrl(String pullUrl) {
            this.pullUrl = pullUrl;
        }

        public String getPlanDuration() {
            return planDuration;
        }

        public void setPlanDuration(String planDuration) {
            this.planDuration = planDuration;
        }
    }

    public static class PptInfoBean {
        /**
         * sourceId : 8663841961388352
         * deployPath : http://192.168.1.207:8081/
         * clientId : 1001
         * convertTime : 1551766867
         * totalPage : 19
         * createType : 0
         * liveId : 100120190330gEdVJz8U
         * downloadTime : 1551766729
         * valid : 1
         * pptId : 83461B08A0401FC68D9C2A7E036C4710
         * pptPath : E:/PPT_DOWNLOAD/【基础作业】连加、连减解决问题.pptx
         * currentSlide : 1
         * convertPath : E:/PPT_CONVERT/
         * deployTime : 1551766867
         * currentPage : 1
         * pptFlag : 6
         */

        @SerializedName("sourceId")
        private String sourceId;
        @SerializedName("deployPath")
        private String deployPath;
        @SerializedName("clientId")
        private String clientId;
        @SerializedName("convertTime")
        private String convertTime;
        @SerializedName("totalPage")
        private String totalPage;
        @SerializedName("createType")
        private String createType;
        @SerializedName("liveId")
        private String liveId;
        @SerializedName("downloadTime")
        private String downloadTime;
        @SerializedName("valid")
        private String valid;
        @SerializedName("pptId")
        private String pptId;
        @SerializedName("pptPath")
        private String pptPath;
        @SerializedName("currentSlide")
        private String currentSlide;
        @SerializedName("convertPath")
        private String convertPath;
        @SerializedName("deployTime")
        private String deployTime;
        @SerializedName("currentPage")
        private String currentPage;
        @SerializedName("pptFlag")
        private String pptFlag;

        public static PptInfoBean objectFromData(String str) {

            return new Gson().fromJson(str, PptInfoBean.class);
        }

        public String getSourceId() {
            return sourceId;
        }

        public void setSourceId(String sourceId) {
            this.sourceId = sourceId;
        }

        public String getDeployPath() {
            return deployPath;
        }

        public void setDeployPath(String deployPath) {
            this.deployPath = deployPath;
        }

        public String getClientId() {
            return clientId;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public String getConvertTime() {
            return convertTime;
        }

        public void setConvertTime(String convertTime) {
            this.convertTime = convertTime;
        }

        public String getTotalPage() {
            return totalPage;
        }

        public void setTotalPage(String totalPage) {
            this.totalPage = totalPage;
        }

        public String getCreateType() {
            return createType;
        }

        public void setCreateType(String createType) {
            this.createType = createType;
        }

        public String getLiveId() {
            return liveId;
        }

        public void setLiveId(String liveId) {
            this.liveId = liveId;
        }

        public String getDownloadTime() {
            return downloadTime;
        }

        public void setDownloadTime(String downloadTime) {
            this.downloadTime = downloadTime;
        }

        public String getValid() {
            return valid;
        }

        public void setValid(String valid) {
            this.valid = valid;
        }

        public String getPptId() {
            return pptId;
        }

        public void setPptId(String pptId) {
            this.pptId = pptId;
        }

        public String getPptPath() {
            return pptPath;
        }

        public void setPptPath(String pptPath) {
            this.pptPath = pptPath;
        }

        public String getCurrentSlide() {
            return currentSlide;
        }

        public void setCurrentSlide(String currentSlide) {
            this.currentSlide = currentSlide;
        }

        public String getConvertPath() {
            return convertPath;
        }

        public void setConvertPath(String convertPath) {
            this.convertPath = convertPath;
        }

        public String getDeployTime() {
            return deployTime;
        }

        public void setDeployTime(String deployTime) {
            this.deployTime = deployTime;
        }

        public String getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(String currentPage) {
            this.currentPage = currentPage;
        }

        public String getPptFlag() {
            return pptFlag;
        }

        public void setPptFlag(String pptFlag) {
            this.pptFlag = pptFlag;
        }
    }
}
