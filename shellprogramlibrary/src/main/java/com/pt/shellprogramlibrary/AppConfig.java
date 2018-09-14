package com.pt.shellprogramlibrary;

/**
 * Created by pt198 on 14/09/2018.
 */

public class AppConfig {
    private String PushKey;
    private String AcceptCount;
    private String AppId;
//    "0"不跳转， "1"跳转
    private String ShowWeb;
    private String Del;
    //跳转的url地址。
    private String Url;
    //说明
    private String Remark;

    public String getPushKey() {
        return PushKey;
    }

    public void setPushKey(String pushKey) {
        PushKey = pushKey;
    }

    public String getAcceptCount() {
        return AcceptCount;
    }

    public void setAcceptCount(String acceptCount) {
        AcceptCount = acceptCount;
    }

    public String getAppId() {
        return AppId;
    }

    public void setAppId(String appId) {
        AppId = appId;
    }

    public String getShowWeb() {
        return ShowWeb;
    }

    public void setShowWeb(String showWeb) {
        ShowWeb = showWeb;
    }

    public String getDel() {
        return Del;
    }

    public void setDel(String del) {
        Del = del;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

}
