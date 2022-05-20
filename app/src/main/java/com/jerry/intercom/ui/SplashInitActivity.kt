package com.jerry.intercom.ui

import android.Manifest
import android.os.Bundle
import com.algebra.sdk.entity.Constants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.jerry.intercom.MainActivity
import com.jerry.intercom.R
import com.jerry.intercom.data.UserPreference
import com.jerry.intercom.tuLinSdk.LifeCycleCallback
import com.jerry.intercom.tuLinSdk.TourLinkManager
import com.jerry.simpleui.base.BaseActivity
import com.jerry.simpleui.utils.Logs
import com.permissionx.guolindev.PermissionX
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashInitActivity : BaseActivity() {
    //途琳需要一对权限
    var permissions = listOf(
        Manifest.permission.INTERNET,
        Manifest.permission.VIBRATE,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WAKE_LOCK,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.MODIFY_AUDIO_SETTINGS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
//        Manifest.permission.REQUEST_INSTALL_PACKAGES,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.REORDER_TASKS
    )

    @Inject
    lateinit var userPreference: UserPreference

    @Inject
    lateinit var sdk: TourLinkManager
    override fun getLayoutId(): Int {
        return R.layout.activity_splash_init
    }
    override fun loadData(savedInstanceState: Bundle?) {
        PermissionX.init(this).permissions(permissions)
            .onExplainRequestReason{
                    scope, deniedList ->
                scope.showRequestReasonDialog(deniedList, "为更好地使用,请开启相应权限", "确定", "取消")
            }
            .request{ allGranted, grantedList, deniedList ->
                    Logs.dMsg("PermissionX deniedList: ${deniedList} ,  ${grantedList}")
                    if(allGranted)
                    {
                        //初始化sdk
                        initSdk()
                    }else
                    {
                        loadData(null)
                    }
            }
//        lifecycleScope.launch {
//            delay(3000)
//            ActivityUtils.startActivity(LoginActivity::class.java)
//            finish()
//        }
    }


    fun initSdk(){
        sdk.initSDK(applicationContext)
        sdk.initApi(applicationContext,sdkCallBack)
    }

    override fun onDestroy() {
        super.onDestroy()
        sdk.removeInitCallback(sdkCallBack)
    }
    val sdkCallBack=object :LifeCycleCallback{
        override fun onInitFinish() {
           Logs.dMsg("onInitFinish")
            if (!userPreference.userName.isNullOrEmpty()&&!userPreference.password.isNullOrEmpty()) {

                userPreference.apply {
                    sdk.accountApi.login("TL1000", userName, password)
                }

            }else
            {
                ActivityUtils.startActivity(LoginActivity::class.java)
                finish()
            }
        }
        override fun onSelfStateChange(uid: Int, state: Int, nick: String?) {
            when (state) {
                Constants.UST_ONLINE -> {
                   // userPreference.saveAccount("", "")
                    ActivityUtils.startActivity(MainActivity::class.java)
                }
                Constants.UST_OFFLINE ->
                    ToastUtils.showShort("账号或者密码错误")


            }


        }
        override fun onLoginStep(step: Int) {
         //   Logs.dMsg("SplashInitActivity onLoginStep:${step}")
            if(step==5)
            {
                //去主页
            }
        }
    }
}