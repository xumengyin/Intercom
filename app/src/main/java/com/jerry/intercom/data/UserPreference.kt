package com.jerry.intercom.data

import android.app.Application
import android.content.SharedPreferences
import com.jerry.simpleui.preference.BasePreference
import javax.inject.Inject

class UserPreference @Inject constructor( application: Application) : BasePreference(application) {

    var userName:String?=null
    var password:String?=null

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        loadPrefs(sharedPreferences)
    }

    override fun getPreferenceName(): String {
        return "user_pre"
    }

    override fun loadPrefs(sharedPreferences: SharedPreferences?) {
        sharedPreferences?.apply {
            userName= getString("userName","")
            password= getString("password","")
        }
    }

    fun saveAccount(account :String,pass:String)
    {
        apply("userName",account,false)
        apply("password",pass,true)
    }
}