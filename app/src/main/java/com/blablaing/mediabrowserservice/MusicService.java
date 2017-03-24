package com.blablaing.mediabrowserservice;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ProviderInfo;
import android.graphics.Bitmap;
import android.media.MediaMetadata;
import android.media.browse.MediaBrowser;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.service.media.MediaBrowserService;
import android.text.TextUtils;

import com.blablaing.mediabrowserservice.model.MusicProvider;
import com.blablaing.mediabrowserservice.utils.LogHelper;
import com.blablaing.mediabrowserservice.utils.MediaIDHelper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Linh on 3/24/2017.
 */

public class MusicService extends MediaBrowserService implements Playback.Callback {
    public static final String ACTION_CMD = "com.blablaing.mediabrowserservice.ACTION_CMD";
    public static final String CMD_NAME = "CMD_NAME";
    public static final String CMD_PAUSE = "CMD_PAUSE";
    private static final String TAG = LogHelper.makeLogTag(MusicService.class);
    private static final String CUSTOM_ACTION_THUMBS_UP =
            "com.blablaing.mediabrowserservice.THUMBS_UP";
    private static final int STOP_DELAY = 30000;
    private MusicProvider mMusicProvider;
    private MediaSession mSession;
    private List<MediaSession.QueueItem> mPlayingQueue;
    private int mCurrentIndexOnQueue;
    private MediaNotificationManager mMediaNotificationManager;
    private boolean mServiceStarted;
    private DelayedStopHandler mDelayedStopHandler = new DelayedStopHandler(this);
    private Playback mPlayback;
    private PackageValidator mPackageValidator;


    private static class DelayedStopHandler extends Handler {
        private final WeakReference<MusicService> mWeakReference;

        private DelayedStopHandler(MusicService service) {
            mWeakReference = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            MusicService service = mWeakReference.get();
            if (service != null && service.mPlayback != null) {
                if (service.mPlayback.isPlaying()) {
                    LogHelper.d(TAG, "Ignoring delayed stop since the media player is in use.");
                    return;
                }
                LogHelper.d(TAG, "Stopping service with delay handler.");
                service.stopSelf();
                service.mServiceStarted = false;
            }
        }
    }
}
