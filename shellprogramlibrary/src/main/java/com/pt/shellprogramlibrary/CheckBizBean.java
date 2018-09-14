package com.pt.shellprogramlibrary;

/**
 * Created by pt198 on 14/09/2018.
 */

public class CheckBizBean {
    private boolean success;
    private AppConfig AppConfig;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public com.pt.shellprogramlibrary.AppConfig getAppConfig() {
        return AppConfig;
    }

    public void setAppConfig(com.pt.shellprogramlibrary.AppConfig appConfig) {
        AppConfig = appConfig;
    }

}
