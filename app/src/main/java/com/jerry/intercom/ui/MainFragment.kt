package com.jerry.intercom.ui

import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.jerry.intercom.R
import com.jerry.simpleui.base.BaseFragment
import com.qmuiteam.qmui.kotlin.throttleClick
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.main_fragment
    }

    override fun initViews() {
        vSettingBtn.setOnClickListener(
            throttleClick {
                ActivityUtils.startActivity(TipSettingActivity::class.java)
            }
        )
    }

    override fun loadData(savedInstanceState: Bundle?) {

    }
}