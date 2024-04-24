package com.thx.commonlibrary.base

import androidx.appcompat.app.AppCompatActivity

//import com.tbruyelle.rxpermissions2.RxPermissions

/**
 * @Description:
 * @Author: tanghongxiang（thx76222@gmail.com）
 * @Version: V1.00
 * @since 2021/10/11 1:36 下午
 */
open class PermissionsActivity:AppCompatActivity() {
//    private var mPermissions: RxPermissions?=null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        mPermissions=RxPermissions(this)
//    }
//
//    /**
//     * 注意：不要传Manifest.permission_group
//     */
//    @SuppressLint("CheckResult")
//    fun requestPermissions(listener: PermissionsCallback, vararg permissions:String){
//        mPermissions?.requestEachCombined(*permissions)?.subscribe{permission->
//            when {
//                permission.granted -> //所有权限同意
//                    listener.getPermissionsSuccess()
//                permission.shouldShowRequestPermissionRationale -> //至少一个拒绝的权限
//                    listener.getPermissionsFailed()
//                else -> //至少有一个被拒绝的权限，不再询问<这种可以进入设置：AppUtils.jumpToPermissionsSetting>
//                    listener.forbidPermissions()
//            }
//        }
//    }
}