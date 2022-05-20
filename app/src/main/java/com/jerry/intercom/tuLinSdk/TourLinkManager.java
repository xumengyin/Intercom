package com.jerry.intercom.tuLinSdk;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;

import com.algebra.sdk.API;
import com.algebra.sdk.AccountApi;
import com.algebra.sdk.ChannelApi;
import com.algebra.sdk.DeviceApi;
import com.algebra.sdk.SessionApi;
import com.algebra.sdk.entity.Channel;
import com.algebra.sdk.entity.Constants;
import com.algebra.sdk.entity.Contact;
import com.algebra.sdk.entity.OnAccountListener;
import com.algebra.sdk.entity.OnChannelListener;
import com.algebra.sdk.entity.OnDeviceListener;
import com.algebra.sdk.entity.OnSessionListener;
import com.jerry.simpleui.utils.Logs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TourLinkManager implements OnAccountListener, OnSessionListener, OnChannelListener, OnDeviceListener {

    private static final String TAG = "TourLinkManager";

    private static class SingletonHolder {
        private static TourLinkManager sInstance = new TourLinkManager();
    }

    private WeakReference<Context> mContextRef;
    private WeakReference<Handler> mHandlerRef;

    private Handler handler = new Handler(Looper.getMainLooper());
    private AccountApi accountApi = null;
    private ChannelApi channelApi = null;
    private SessionApi sessionApi = null;
    private DeviceApi deviceApi = null;

    private List<LifeCycleCallback> mLifecycleCallbackList;

    private final List<String> mChannelNameList = new LinkedList<String>();
    private final List<Integer> mChannelIdList = new LinkedList<Integer>();

    private final List<String> mFriendNameList = new LinkedList<String>();
    private final List<Integer> mFriendIdList = new LinkedList<Integer>();

    private Boolean mHasRequestChannelList = false;
    private Boolean mHasRequestFriendList = false;
    private Boolean mHasGetChannelList = false;
    private Boolean mHasGetFriendList = false;

    private AtomicBoolean mApiInited = new AtomicBoolean(false);

    private TextView mChannelNameTV;

    private TextView mFriendNameTV;

    private int mCurrentMonitoredChannelId = -1;

    private int mCurrentUserId = 0;

    private ProgressDialog mProgressDialog;

    private Boolean mHasLogin = false;

    // PTT 按键是否按下
    private Boolean isPttPressed = false;

    // 是否静音
    private Boolean mSoundON = false;

    private Boolean mCanSpeak = true;

    private int mCurrentSpeakerId = -1;

    private String userName;
    private String userPassword;


    private TourLinkManager() {

    }

    public static TourLinkManager getInstance() {
        return SingletonHolder.sInstance;
    }

    public void initSDK(Context context) {
        mContextRef = new WeakReference<Context>(context);
        API.init(mContextRef.get());
        mLifecycleCallbackList = new ArrayList<>();
    }

    public void initApi(Context context, LifeCycleCallback callback) {
        if (mApiInited.getAndSet(true)) {
            return;
        }
        if (mContextRef == null) {
            mContextRef = new WeakReference<Context>(context);
        }
        mHandlerRef = new WeakReference<Handler>(handler);
        mLifecycleCallbackList.add(callback);
        mHandlerRef.get().postDelayed(delayInitApi, 500);
    }

    public void addInitCallback(LifeCycleCallback callback) {
        if (!mLifecycleCallbackList.contains(callback)) {
            mLifecycleCallbackList.add(callback);
        }
    }

    public void removeInitCallback(LifeCycleCallback callback) {
        mLifecycleCallbackList.remove(callback);

    }

    private Handler getHandler() {
        if (mHandlerRef != null) {
            return mHandlerRef.get();
        }
        return null;
    }

    public AccountApi getAccountApi() {
        return accountApi;
    }

    public SessionApi getSessionApi() {
        return sessionApi;
    }

    public ChannelApi getChannelApi() {
        return channelApi;
    }

    public DeviceApi getDeviceApi() {
        return deviceApi;
    }

    public void destroy() {
        if (mContextRef != null) {
            API.leave(mContextRef.get());
        }
        mApiInited.set(false);
        mHandlerRef = null;
        accountApi = null;
        sessionApi = null;
        deviceApi = null;
        channelApi = null;
        mLifecycleCallbackList.clear();
    }

    public void logout() {
        if (accountApi != null) {
            accountApi.logout();
        }
    }

    public void reInit(Context context, Handler handler, LifeCycleCallback callback) {
        mContextRef = new WeakReference<>(context);
        mHandlerRef = new WeakReference<>(handler);
        mLifecycleCallbackList = new ArrayList<>();
        mLifecycleCallbackList.add(callback);
        API.init(mContextRef.get());
        if (mHandlerRef != null) {
            mHandlerRef.get().postDelayed(reInitAndLogin, 1000);
        }
    }

    private Runnable delayInitApi = new Runnable() {
        @Override
        public void run() {
            boolean apiOk = getPocApis();
            Handler handler = getHandler();
            if (apiOk) {
                deviceApi.setToneVolume(2);
                Contact me = accountApi.whoAmI();
                if (me == null) {
                    deviceApi.startPoc(1);
                }
//                if (handler != null) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
                            lifeCycleCallback.onInitFinish();
                        }
                    }
                }, 2000);
//                } else {
//                    for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
//                        lifeCycleCallback.onInitFinish();
//                    }
//                }
            } else {
                if (handler != null) {
                    Log.d(TAG, "init api waiting ...");
                    handler.postDelayed(delayInitApi, 300);
                }
            }
        }
    };

    private Runnable reInitAndLogin = new Runnable() {
        @Override
        public void run() {
            boolean apiOk = getPocApis();
            Handler handler = getHandler();
            if (apiOk) {
                deviceApi.setToneVolume(2);
                Contact me = accountApi.whoAmI();
                if (me == null) {
                    deviceApi.startPoc(1);
                }
                if (handler != null) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
                                lifeCycleCallback.onInitFinish();
                            }
                        }
                    }, 2000);
                } else {
                    for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
                        lifeCycleCallback.onInitFinish();
                    }
                }
            } else {
                if (handler != null) {
                    Log.d(TAG, "init api waiting ...");
                    handler.postDelayed(reInitAndLogin, 300);
                }
            }
        }
    };

    private boolean getPocApis() {
        accountApi = API.getAccountApi();
        channelApi = API.getChannelApi();
        sessionApi = API.getSessionApi();
        deviceApi = API.getDeviceApi();
        if (accountApi != null && deviceApi != null) {
            accountApi.setOnAccountListener(this);
            channelApi.setOnChannelListener(this);
            sessionApi.setOnSessionListener(this);
            deviceApi.setOnDeviceListener(this);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 请求讲话权并开始讲话
     */
    private void requestTalk() {
        if (!isPttPressed && deviceApi != null) {
            deviceApi.setPttOn(true);
            isPttPressed = true;
        }
    }

    /**
     * 释放讲话权并停止讲话
     */
    private void releaseTalk() {
        if (isPttPressed && deviceApi != null) {
            deviceApi.setPttOn(false);
            isPttPressed = false;
        }
    }

    @Override
    public void onLoginStep(int step) {
        Logs.dMsg(TAG + "onLoginStep:" + step);
        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
            lifeCycleCallback.onLoginStep(step);
        }
    }

    @Override
    public void onSelfStateChange(int uid, int state, String nick) {
        Logs.dMsg(TAG + "onSelfStateChange uid:" + uid + " state:" + state + " nick:" + nick);
        if (state == Constants.UST_ONLINE) {
            mCurrentUserId = uid;
        }
        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
            lifeCycleCallback.onSelfStateChange(uid, state, nick);
        }
    }

    @Override
    public void onFriendListGet(List<Contact> list) {
        Logs.dMsg(TAG + "onFriendListGet size:" + list.size());
        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
            lifeCycleCallback.onFriendListGet(list);
        }
    }

    @Override
    public void onPlayLast(int i, int i1, int i2) {

    }

    @Override
    public void onHibernateState(int i) {

    }

    @Override
    public void onLogger(int i, String s) {
        Log.e(TAG, "onLogger, ");
    }

    @Override
    public void onUnDisturbed(boolean b) {
        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
            lifeCycleCallback.onUnDisturbed(b);
        }

    }

    @Override
    public void onChannelListGet(List<Channel> list) {
        Logs.dMsg(TAG + "onChannelListGet size:" + list.size());
        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
            lifeCycleCallback.onChannelListGet(list);
        }
    }

    @Override
    public void onChannelListUpdate(int count) {
        Logs.dMsg(TAG + "onChannelListUpdate count:" + count);
        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
            lifeCycleCallback.onChannelListUpdate(count);
        }

    }

    @Override
    public void onChannelMemberListGet(List<Contact> list) {

    }

    @Override
    public void onChannelPresenceUpdate(int cid, int members, int presences) {
        Log.e(TAG, "onChannelPresenceUpdate, " + cid + "， " + members + "， " + presences);

    }

    @Override
    public void onGpsRequest(boolean b) {

    }

    @Override
    public void onTtsState(boolean b) {
        Log.e(TAG, "onTtsState, " + b);

    }

    @Override
    public void onPlayerMeter(int i) {

    }

    @Override
    public void onAirplaneMode(boolean b) {

    }

    @Override
    public void onGpsReportControl(boolean b, int i) {

    }

    @Override
    public void onVideoMonitorControl(boolean b, String s) {

    }

    @Override
    public void onSessionEstablished(int i, int i1, String s) {
        Log.e(TAG, "onSessionEstablished, ");

    }

    @Override
    public void onSessionReleased() {
        Log.e(TAG, "onSessionReleased, ");
    }

    @Override
    public void onSessionMonitored(int selfUid, int ctype, int cid) {
        Logs.dMsg(TAG + "onSessionMonitored selfUid:" + selfUid);
        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
            lifeCycleCallback.onSessionMonitored(selfUid, ctype, cid);
        }
    }

    @Override
    public void onSessionUnmonitored(int selfUid, int ctype, int cid) {
        Logs.dMsg(TAG + "onSessionUnmonitored selfUid:" + selfUid);
        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
            lifeCycleCallback.onSessionUnmonitored(selfUid, ctype, cid);
        }
    }

    @Override
    public void onMonitorSessionSync(int i, int i1, List<Integer> list) {

    }

    @Override
    public void onSessionTalkIndicate(int speaker, String nick, boolean canCut) {
        Logs.dMsg(TAG + "onSessionTalkIndicate speaker:" + speaker + " nick:" + nick + " canCut:" + canCut);
        for (LifeCycleCallback lifeCycleCallback : mLifecycleCallbackList) {
            lifeCycleCallback.onSessionTalkIndicate(speaker, nick, canCut);
        }

    }

    public int getmCurrentUserId() {
        return mCurrentUserId;
    }
}
