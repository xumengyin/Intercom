package com.jerry.intercom.tuLinSdk;

import com.algebra.sdk.entity.Channel;
import com.algebra.sdk.entity.Contact;

import java.util.List;

public interface LifeCycleCallback {

    default void onInitFinish(){

    }

    default void onLoginStep(int step){

    }

    default void onSelfStateChange(int uid, int state, String nick){

    }

    default void onFriendListGet(List<Contact> list){

    }

    default void onUnDisturbed(boolean b){

    }

    default void onChannelListGet(List<Channel> list){

    }

    default void onChannelListUpdate(int count){

    }

    default void onSessionMonitored(int selfUid, int ctype, int cid){

    }

    default void onSessionUnmonitored(int selfUid, int ctype, int cid){

    }

   default void onSessionTalkIndicate(int speaker, String nick, boolean canCut){

   }
}
