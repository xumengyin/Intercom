package com.jerry.intercom

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.blankj.utilcode.util.NotificationUtils
import com.jerry.intercom.tuLinSdk.LifeCycleCallback
import com.jerry.intercom.tuLinSdk.TourLinkManager
import com.jerry.simpleui.utils.Logs
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class IntercomService : Service() {
    companion object {
        const val KEY_PTT_DOWN = "android.intent.action.ACTION_PTTKEY_DOWN"
        const val KEY_PTT_UP = "android.intent.action.ACTION_PTTKEY_UP"
        const val KEY_PTT_DOWN2 = "android.intent.action.PTT.down"
        const val KEY_PTT_UP2 = "android.intent.action.PTT.up"
    }

    @Inject
    lateinit var tourLinkManager:TourLinkManager
    override fun onBind(intent: Intent?): IBinder? {
        Logs.dMsg("IntercomService onBind")
        return null
    }


    val lifeCycleCallback = object:LifeCycleCallback
    {
        override fun onSessionTalkIndicate(speaker: Int, nick: String?, canCut: Boolean) {
            super.onSessionTalkIndicate(speaker, nick, canCut)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }
    override fun onCreate() {
        super.onCreate()
        Logs.dMsg("IntercomService onCreate")
        startForeground(110, NotificationUtils.getNotification(NotificationUtils.ChannelConfig.DEFAULT_CHANNEL_CONFIG,null))
        val filter1 = IntentFilter(KEY_PTT_DOWN)
        filter1.addAction(KEY_PTT_UP)
        filter1.addAction(KEY_PTT_DOWN2)
        filter1.addAction(KEY_PTT_UP2)
        registerReceiver(KeyBroadCast(), filter1)
//        tourLinkManager.addInitCallback(lifeCycleCallback)
    }

    override fun onDestroy() {
      //  tourLinkManager.removeInitCallback(lifeCycleCallback)
        super.onDestroy()

    }
    class KeyBroadCast : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Logs.dMsg("IntercomService ${intent.toString()}")
            when (intent?.action) {
                KEY_PTT_DOWN ->
                    Logs.dMsg("KEY_PTT_DOWN")
                KEY_PTT_UP ->
                    Logs.dMsg("KEY_PTT_UP")
                KEY_PTT_DOWN2 ->
                    Logs.dMsg("KEY_PTT_DOWN2")
                KEY_PTT_UP2 ->
                    Logs.dMsg("KEY_PTT_UP2")

            }
        }

    }
}