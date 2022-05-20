package com.jerry.intercom

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ServiceUtils
import com.jerry.intercom.adapters.MainAdapter
import com.jerry.simpleui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initViews() {
        vViewPager.adapter=MainAdapter(this)
    }
    override fun loadData(savedInstanceState: Bundle?) {
        ServiceUtils.startService(IntercomService::class.java)
    }
}