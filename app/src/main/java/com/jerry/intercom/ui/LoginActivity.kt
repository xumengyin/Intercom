package com.jerry.intercom.ui

import android.os.Bundle
import android.text.TextUtils
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.algebra.sdk.entity.Constants
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.jerry.intercom.MainActivity
import com.jerry.intercom.R
import com.jerry.intercom.data.UserPreference
import com.jerry.intercom.tuLinSdk.LifeCycleCallback
import com.jerry.intercom.tuLinSdk.TourLinkManager
import com.jerry.intercom.utils.TipDialogUtils
import com.jerry.simpleui.base.BaseActivity
import com.jerry.simpleui.utils.Logs
import com.qmuiteam.qmui.kotlin.throttleClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    lateinit var account:String
    lateinit var pass:String

    @Inject
    lateinit var userPreference: UserPreference

    @Inject
    lateinit var tourLinkManager: TourLinkManager

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    val sdkCallBack = object : LifeCycleCallback {
        override fun onLoginStep(step: Int) {
            // Logs.dMsg("LoginActivity onLoginStep:${step}")
            //5 代表他成功
            if (step == 5) {
                TipDialogUtils.dismissProgressDialog()
                // userPreference.saveAccount()
                //去往主页
            }
        }

        override fun onSelfStateChange(uid: Int, state: Int, nick: String?) {
            when (state) {
                Constants.UST_ONLINE -> {
                    userPreference.saveAccount("", "")
                    ActivityUtils.startActivity(MainActivity::class.java)
                }
                Constants.UST_OFFLINE ->
                    ToastUtils.showShort("账号或者密码错误")


            }


        }
    }

    override fun loadData(savedInstanceState: Bundle?) {
//        vBtnLogin
        tourLinkManager.addInitCallback(sdkCallBack)

    }

    override fun initViews() {
        vAccount.doOnTextChanged { text, _, _, _ ->
            setBtnStatus()
        }
        vPassword.doOnTextChanged { text, start, count, after ->
            setBtnStatus()
        }


        vBtnLogin.setOnClickListener(throttleClick {
            val account = vAccount.text.toString()
            val pass = vPassword.text.toString()
            if (!TextUtils.isEmpty(pass) && !TextUtils.isEmpty(account)) {
                this.pass=pass
                this.account=account
                TipDialogUtils.showProgressDialog(this)
                tourLinkManager.accountApi.login("TL1000", account, pass)
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        tourLinkManager.removeInitCallback(sdkCallBack)
    }

    private fun setBtnStatus() {
        if (vAccount.text.toString().isNullOrEmpty() || vPassword.text.toString().isNullOrEmpty()) {
            vBtnLogin.isEnabled = false
            vBtnLogin.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.ui_dark_config_color_grey_button
                )
            )
        } else {
            vBtnLogin.isEnabled = true
            vBtnLogin.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.qmui_config_color_white
                )
            )
        }
    }
}