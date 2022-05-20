package com.jerry.intercom

import android.app.Application
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Apps : Application() {
    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger() {
        val formatStrategy: FormatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false) // (Optional) Whether to show thread info or not. Default true
            .methodCount(0) // (Optional) How many method line to show. Default 2
            .methodOffset(7) // (Optional) Hides internal method calls up to offset. Default 5
            // .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
            .tag("xmy") // (Optional) Global tag for every log. Default PRETTY_LOGGER
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))
    }
}