package com.thx.commonlibrary.network

import com.thx.anynetworkmodule.AnyRequestId
import retrofit2.Call

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
class MyRequestId(private var call: Call<*>?) : AnyRequestId() {

    public fun getCall():Call<*>?{
        return call
    }

    public fun setCall(call: Call<*>?){
        this.call=call
    }
}