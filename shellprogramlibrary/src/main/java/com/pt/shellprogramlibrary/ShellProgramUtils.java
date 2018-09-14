package com.pt.shellprogramlibrary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pt198 on 13/09/2018.
 */

public class ShellProgramUtils {
    public static final int REQUEST_CODE_CHECK_BIZ=0x896;
    public static final String SHOW_WEB="1";
    public static final String NOT_SHOW_WEB="0";
    public static final String APK_SUFFIX=".apk";
    private static final String TAG = "ShellProgramUtils";
    public static void startBiz(final Context context, final OnShellProgaramListener listener){
        Map<String,Object> paras=new HashMap<>();
//        paras.put(Constants.PARAM_KEY_APPID,Constants.APP_ID_H5);
        paras.put(Constants.PARAM_KEY_APPID,Constants.APP_ID_UPDATE_APK);
        HttpManager.getInstance().get(paras,HttpApi.BASE_URL,REQUEST_CODE_CHECK_BIZ,new OnHttpResponseListener() {
            @Override
            public void onHttpResponse(int requestCode, String resultJson, Exception e) {
                CheckBizBean bean=JsonUtils.parse(CheckBizBean.class,resultJson);
                if(bean.isSuccess()){
                    if(bean.getAppConfig()!=null){
                        if(SHOW_WEB.equals(bean.getAppConfig().getShowWeb())){
                            String url=bean.getAppConfig().getUrl();

                            if(url.indexOf(APK_SUFFIX)!=-1){//更新apk
                                android.util.Log.d(TAG, "onHttpResponse: onUpdateApk");
                                if(listener!=null){
                                    listener.onUpdateApk(url);
                                }
                            }else{//跳转h5
                                Log.d(TAG, "onHttpResponse: onShowWeb");
                                if(listener!=null){
                                    listener.onShowWeb(bean.getAppConfig().getUrl());
                                }
                            }
                        }else{
                            Log.d(TAG, "onHttpResponse: onShowShellApk");
                            if(listener!=null){
                                listener.onShowShellApk();
                            }

                        }

                    }else{
                        Toast.makeText(context, "加载数据失败", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(context, "加载数据失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
