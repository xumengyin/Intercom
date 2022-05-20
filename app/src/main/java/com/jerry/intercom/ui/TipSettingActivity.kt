package com.jerry.intercom.ui

import android.os.Bundle
import android.view.View
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.view.OptionsPickerView
import com.jerry.intercom.R
import com.jerry.intercom.data.SettingPreference
import com.jerry.intercom.tuLinSdk.LifeCycleCallback
import com.jerry.intercom.tuLinSdk.TourLinkManager
import com.jerry.intercom.utils.formatTime
import com.jerry.simpleui.base.BaseActivity
import com.jerry.simpleui.utils.Logs
import com.qmuiteam.qmui.kotlin.throttleClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_tip_setting.*
import javax.inject.Inject

@AndroidEntryPoint
class TipSettingActivity : BaseActivity() {


    val hours = (0..10).toMutableList()
    val minutes = (0..59).toMutableList()

    @Inject
    lateinit var settingPreference: SettingPreference

    @Inject
    lateinit var sdk: TourLinkManager

    var mTimeGap: Int = 0
    var mWorkTimeGap: Int = 0
    var mWorkTime: Int = 0
    override fun getLayoutId(): Int {
        return R.layout.activity_tip_setting
    }

    override fun loadData(savedInstanceState: Bundle?) {
        initData()
    }


    override fun initViews() {
        vTimeGapLayout.setOnClickListener(throttleClick {
            createPicker(it).show()
        })
        vWorkLayout.setOnClickListener(
            throttleClick {
                createPicker(it).show()
            }
        )
        vWorkTipLayout.setOnClickListener(
            throttleClick {
                createPicker(it).show()
            }
        )
        vSave.setOnClickListener(throttleClick {
            settingPreference.saveTime(mTimeGap, mWorkTime, mWorkTimeGap)
            finish()
        })
    }

    fun convertTime(hour: Int, minute: Int): Int {
        return hour * 60 + minute
    }

    fun createPicker(view: View): OptionsPickerView<Int> {
        val options = OptionsPickerBuilder(this) { options1, options2, options3, v ->

            Logs.dMsg("options1:${options1} options2:${options2}")
            val value = convertTime(hours[options1], minutes[options2])
            when (view) {
                vTimeGapLayout -> {
                    mTimeGap = value
                    vTimeGap.text = value.formatTime()
                }
                vWorkLayout -> {
                    mWorkTime = value
                    vWorkTime.text = value.formatTime()
                }
                vWorkTipLayout -> {
                    mWorkTimeGap = value
                    vWorkTipTime.text = value.formatTime()
                }
            }


        }.setLabels("小时", "分", "")
            .isCenterLabel(true)
            .setCyclic(true, true, true)
            .build<Int>()
        options.setNPicker(hours, minutes, null)
        return options
    }

    private fun initData() {
        Logs.dMsg("settingPreference ${settingPreference.toString()}")
        settingPreference.apply {
            Logs.dMsg("initData:${timeGap} obj:${this}")
            mTimeGap = timeGap
            mWorkTimeGap = workTimeGap
            mWorkTime = workTime
            vTimeGap.setText(timeGap.formatTime())
            vWorkTipTime.setText(workTimeGap.formatTime())
            vWorkTime.setText(workTime.formatTime())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        sdk.removeInitCallback(sdkCallBack)
    }

    val sdkCallBack = object : LifeCycleCallback {

    }
}

