package com.pt.shellprogramlibrary;

import android.content.Context;
import android.content.Intent;

/**
 * Created by pt198 on 14/09/2018.
 */

public class IntentUtils {
    public static final String KEY_INTENT_EXTRA="extra";
    public static <T> void jumpActivity(Class<T> cls,Context context){
        Intent i=new Intent(context,cls);
        context.startActivity(i);
    }
    public static <T> void jumpActivityExtraStr(Class<T> cls,Context context,String extra){
        Intent i=new Intent(context,cls);
        i.putExtra(KEY_INTENT_EXTRA,extra);
        context.startActivity(i);
    }
}
