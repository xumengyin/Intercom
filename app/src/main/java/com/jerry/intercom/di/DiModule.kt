package com.jerry.intercom.di

import android.app.Application
import com.jerry.intercom.data.SettingPreference
import com.jerry.intercom.data.UserPreference
import com.jerry.intercom.tuLinSdk.TourLinkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DiModule {

    @Singleton
    @Provides
    fun provideTuLinSdk():TourLinkManager{

        return  TourLinkManager.getInstance()
    }
    @Singleton
    @Provides
    fun provideUserPre(app:Application):UserPreference{

        return  UserPreference(app)
    }
    @Singleton
    @Provides
    fun provideSettingPre(app:Application):SettingPreference{

        return  SettingPreference(app)
    }

}