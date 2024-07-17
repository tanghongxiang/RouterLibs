package com.thx.logicroutermodule.logic;

import androidx.annotation.Nullable;

import com.thx.logicroutermodule.LogicRouter;

import java.util.HashMap;

/**
 * @Description:
 * @Author: tanghx
 * @Version: V1.00
 * @Create Date: 2024/7/17 5:52 PM
 */
public class LogicUse {

    private static String PROCESS_SHOULD_RUN_CHECK_UN_PASS_CONTENT =
            "commonLibrary-网络请求-处理Logic shouldRun校验不通过 的提示内容---同步";


    @Nullable
    public static String processUnPassShouldRunErrMsg(Boolean async){
        HashMap map = new HashMap<String, Boolean>();
        map.put("async", async);
        return (String) LogicRouter.syncExecute(PROCESS_SHOULD_RUN_CHECK_UN_PASS_CONTENT, map).data;
    }

}
