package com.jerry.intercom.data

import android.app.Application
import android.content.SharedPreferences
import com.jerry.simpleui.preference.BasePreference
import com.jerry.simpleui.utils.Logs
import javax.inject.Inject

class SettingPreference @Inject constructor(application: Application) : BasePreference(application) {

    var timeGap :Int=3 //默认3分钟
    var workTime=120 //俩小时
    var workTimeGap=30 //半小时

    init {
        Logs.dMsg("SettingPreference:init${this.toString()} -----test :" )
        test()
    }
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        loadPrefs(sharedPreferences)
    }

    override fun getPreferenceName(): String {
        return "setting_pre"
    }

    override fun loadPrefs(sharedPreferences: SharedPreferences?) {
//        sharedPreferences?.apply {
//            this@SettingPreference.timeGap= getInt("timeGap",3)
//            this@SettingPreference.workTime= getInt("workTime",120)
//            this@SettingPreference.workTimeGap= getInt("workTimeGap",30)
//          //  Logs.dMsg("SettingPreference:${timeGap}")
//        }
        if (sharedPreferences!=null) {
            this@SettingPreference.timeGap= sharedPreferences.getInt("timeGap",3)
           this@SettingPreference.workTime= sharedPreferences.getInt("workTime",120)
            this@SettingPreference.workTimeGap= sharedPreferences.getInt("workTimeGap",30)
        }

        Logs.dMsg("loadPrefs:${this.toString()}")
        test()
    }

    fun test()
    {
        Logs.dMsg("timeGap:${timeGap.toString()} workTime${workTime.toString()} workTimeGap${workTimeGap.toString()} hash:${this.hashCode()}")
    }
//    override fun toString(): String {
//        return "timeGap:${timeGap.toString()} workTime${workTime.toString()} workTimeGap${workTimeGap.toString()} hash:${this.hashCode()}"
//    }
    fun saveTime(timeGap :Int,workTime:Int,workTimeGap:Int)
    {
        apply("timeGap",timeGap,false)
        apply("workTime",workTime,false)
        apply("workTimeGap",workTimeGap,true)
    }
}