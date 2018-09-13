package com.pt.shellprogramlibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by pt198 on 13/09/2018.
 */

public class ShellProgramUtils {
    public static final int REQUEST_CODE_CHECK_BIZ=0x896;
    public static void startBiz(){
        Map<String,Object> paras=new HashMap<>();
        paras.put()
        HttpManager.getInstance().get(paras,REQUEST_CODE_CHECK_BIZ);
    }
}
