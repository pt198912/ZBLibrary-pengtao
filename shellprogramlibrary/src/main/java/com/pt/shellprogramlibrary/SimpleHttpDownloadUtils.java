package com.pt.shellprogramlibrary;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;
import android.util.*;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by pt198 on 14/08/2018.
 */

public class SimpleHttpDownloadUtils {
    private static final String TAG = "SimpleHttpDownloadUtils";
    public static String getDigest(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[] byteArray = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0")
                        .append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString().toUpperCase();
    }
    public static HttpURLConnection getConnection(String httpUrl) throws Exception {
        URL url = new URL(httpUrl);
        HttpURLConnection connection =  (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Content-Type", "application/octet-stream");
//        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.connect();
        return connection;

    }



    public interface OnDownloadListener{
        void onDownloadProgress(int progress);
        void onDownloadFinish(String savedPath);
        void onDownloadFail();
        void onDownloadStart();
    }

    public static void downLoad(final String downloadUrl,final Context context,final String fileExtension,final OnDownloadListener downloadListener){
        if(TextUtils.isEmpty(downloadUrl)||context==null){
            return ;
        }
        new AsyncTask<Void,Integer,String>(){
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }

            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onCancelled(String s) {
                super.onCancelled(s);
            }

            @Override
            protected void onCancelled() {
                super.onCancelled();
            }

            @Override
            protected String doInBackground(Void... params) {
                if(downloadListener!=null){
                    downloadListener.onDownloadStart();
                }
                BufferedInputStream bis =null;
                BufferedOutputStream bos=null;
                String savedPath="";
                try {
                    Log.d(TAG, "doInBackground: downloadUrl "+downloadUrl);
                    HttpURLConnection conn=getConnection(downloadUrl);
                    int contentLength = conn.getContentLength();
                    Log.d(TAG, "doInBackground: contentLength "+contentLength);
                    if (contentLength>0) {
                        InputStream is= conn.getInputStream();
                        if(is==null){
                            if(downloadListener!=null){
                                downloadListener.onDownloadFail();
                            }
                        }
                        bis = new BufferedInputStream(is);
                        savedPath=Environment.getExternalStorageDirectory()+File.separator+context.getApplicationInfo().name+File.separator+getDigest(downloadUrl)+"."+fileExtension;
                        File dest=new File(savedPath);
                        if(!dest.getParentFile().exists()){
                            dest.getParentFile().mkdirs();
                        }
                        if(!dest.exists()) {
                            dest.createNewFile();
                        }
                        FileOutputStream fos = new FileOutputStream(dest);
                        bos= new BufferedOutputStream(fos);
                        int b = 0;
                        byte[] byArr = new byte[1024*512];
                        long donwloaded=0;
                        int lastProgress=0;
                        while((b= bis.read(byArr))!=-1){
                            bos.write(byArr, 0, b);
                            donwloaded+=b;
                            Log.d(TAG, "doInBackground: write length "+b);
                            int proress=(int )(donwloaded*100/contentLength);
                            if(proress-lastProgress>=1){
                                if(downloadListener!=null){
                                    downloadListener.onDownloadProgress(proress);
                                }
                            }
                            lastProgress=proress;
                        }
                        if(downloadListener!=null){
                            downloadListener.onDownloadFinish(savedPath);
                        }
                    }else{
                        if(downloadListener!=null){
                            downloadListener.onDownloadFail();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if(downloadListener!=null){
                        downloadListener.onDownloadFail();
                    }
                }finally{
                    try {
                        if(bis !=null){
                            bis.close();
                        }
                        if(bos !=null){
                            bos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return savedPath;
            }
        }.execute();

    }
}
