package com.pt.shellprogramlibrary;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import zuo.biao.library.base.BaseApplication;

/**
 * Created by pt198 on 13/09/2018.
 */

public class ShellBaseApplication extends BaseApplication {
    private boolean isBackGround;
    private OnChangeBackgroudForegroundListener mListener;
    private RestartAppReceiver mReceiver;
    private static final String TAG = "ShellBaseApplication";
    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_UI_HIDDEN) {
            isBackGround = true;
            Log.i(TAG, "APP遁入后台");
            if(mListener!=null){
                mListener.onBackToBackground();
            }
        }
    }

    public void setmListener(OnChangeBackgroudForegroundListener mListener) {
        this.mListener = mListener;
    }

    public interface OnChangeBackgroudForegroundListener{
        void onBackToForeground();
        void onBackToBackground();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mReceiver=new RestartAppReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Constants.ACTION_RESTART_APP);
        registerReceiver(mReceiver,filter);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            }
            @Override
            public void onActivityStarted(Activity activity) {
            }
            @Override
            public void onActivityResumed(Activity activity) {
                if (isBackGround) {
                    isBackGround = false;
                    Log.i(TAG, "APP回到了前台");
                    if(mListener!=null){
                        mListener.onBackToForeground();
                    }
                }
            }
            @Override
            public void onActivityPaused(Activity activity) {
            }
            @Override
            public void onActivityStopped(Activity activity) {
            }
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            }
            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });
    }
    private class RestartAppReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Constants.ACTION_RESTART_APP)){
                //restart app
            }
        }
    }
}
