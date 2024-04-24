package com.thx.commonlibrary.network

import com.thx.anynetworkmodule.AnyConfig

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
class NetworkConfig : AnyConfig() {

    companion object {
        val RESPONSE_KEY_ERROR_NUM = "errcode"
        val RESPONSE_KEY_ERROR_SUBNUM = "subErrcode"
        val RESPONSE_KEY_DATA = "result"
        val RESPONSE_CODE_SUCCESS = 0
        val RESPONSE_CODE_JSON_FORMAT_ERROR = -999

        val DEFAULT_PARAMS_ENCODING = "utf-8"

    }

}