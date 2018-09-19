package com.pt.shellprogramlibrary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.*;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import static com.pt.shellprogramlibrary.Constants.ACTION_RESTART_APP;

/**
 * Created by pt198 on 13/09/2018.
 */

public class UpdateApkActivity extends Activity implements SimpleHttpDownloadUtils.OnDownloadListener,ShellBaseApplication.OnChangeBackgroudForegroundListener{
    private String mUrl;
    private ProgressBar mPb;
    private TextView mPbTv;
    private MyReceiver mReceiver;
    private Handler mHandler;
    private static final String TAG = "UpdateApkActivity";
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_apk);
        getIntentData();
        initView();
        mHandler=new Handler();
        registerRecevier();
        SimpleHttpDownloadUtils.downLoad(mUrl,this,"apk",this);
        ((ShellBaseApplication)ShellBaseApplication.getInstance()).setmListener(this);
    }

    private void registerRecevier(){
        mReceiver=new MyReceiver();
        IntentFilter filter=new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_INSTALL);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        registerReceiver(mReceiver,filter);
    }

    @Override
    public void onBackToForeground() {
        Log.d(TAG, "onBackToForeground: ");
        boolean exist=isInstallApp(Constants.PKG_NAME_ORIGIN);
        if(exist){
            new AlertDialog.Builder(this).setMessage("是否卸载马甲？").setNegativeButton("不卸载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    },1500);

                    //start origin app
//                    sendBroadcast(new Intent(ACTION_RESTART_APP));
                    final Intent intent = getPackageManager().getLaunchIntentForPackage(Constants.PKG_NAME_ORIGIN);
                    startActivity(intent);
                }
            }).setPositiveButton("卸载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    dialogInterface.dismiss();
                    unstallApp();
                }
            }).show();
        }else{
            //todo:do nothing
        }
    }

    //卸载应用程序
    public void unstallApp(){
        Intent uninstall_intent = new Intent();
        uninstall_intent.setAction(Intent.ACTION_DELETE);
        uninstall_intent.setData(Uri.parse("package:"+getPackageName()));
        startActivity(uninstall_intent);
    }

    boolean isInstallApp(String packageName)
    {
        try {
            PackageManager pm=getPackageManager();
            ApplicationInfo info=pm.getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return info!=null;

        } catch (PackageManager.NameNotFoundException e) {
            // TODO: handle exception
            return false;
        }
    }


    @Override
    public void onBackToBackground() {

    }

    private class MyReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            android.util.Log.d(TAG, "onReceive: "+intent.getAction());
            String action = intent.getAction();
            String data = intent.getDataString();//获取被安装，删除...的应用包名
        }
    }

    private void initView(){
        mPb=(ProgressBar)findViewById(R.id.pb);
        mPbTv=(TextView)findViewById(R.id.tv_progress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    private void getIntentData(){
        mUrl=getIntent().getStringExtra(IntentUtils.KEY_INTENT_EXTRA);
    }
    @Override
    public void onBackPressed() {

    }

    @Override
    public void onDownloadProgress(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPb.setProgress(progress);
                mPbTv.setText(progress+"%");
            }
        });
    }

    @Override
    public void onDownloadFinish(final String savedPath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UpdateApkActivity.this, "下载apk成功", Toast.LENGTH_SHORT).show();
                installApk(new File(savedPath));
            }
        });
    }

    private void installApk(File file){
        if(file==null||!file.exists()){
            return;
        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    public void onDownloadFail() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(UpdateApkActivity.this, "更新apk失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDownloadStart() {

    }
}
