package com.pt.shellprogramlibrary;

import com.alibaba.fastjson.*;

/**
 * Created by pt198 on 14/09/2018.
 */

public class JsonUtils {
    public static <T> T parse(Class<T> cls,String json){
        return (T)JSON.parseObject(json,cls);
    }
}
