package com.blablaing.mediabrowserservice.utils;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.text.TextUtils;

import java.util.Arrays;

/**
 * Created by Linh on 3/24/2017.
 */

public class MediaIDHelper {
    private static final String TAG = LogHelper.makeLogTag(MediaIDHelper.class);

    public static final String MEDIA_ID_ROOT = "__ROOT__";
    public static final String MEDIA_ID_MUSICS_BY_GENRE = "__BY_GENRE__";
    public static final String MEDIA_ID_MUSICS_BY_SEARCH = "__BY_SEARCH__";

    private static final char CATEGORY_SEPARATOR = '/';
    private static final char LEAF_SEPARATOR = '|';

    public static String createMediaID(String musicID, String... categories) {
        StringBuilder sb = new StringBuilder();
        if (categories != null && categories.length > 0) {
            sb.append(categories[0]);
            for (int i = 1; i < categories.length; i++) {
                sb.append(CATEGORY_SEPARATOR).append(categories[i]);
            }
        }
        if (musicID != null) {
            sb.append(LEAF_SEPARATOR).append(musicID);
        }
        return sb.toString();
    }

    public static String createBrowseCategoryMediaID(String categoryType, String categoryValue) {
        return categoryType + CATEGORY_SEPARATOR + categoryValue;
    }

    public static String extractMusicIDFromMediaID(String mediaID) {
        int pos = mediaID.indexOf(LEAF_SEPARATOR);
        if (pos >= 0) {
            return mediaID.substring(pos + 1);
        }
        return null;
    }

    public static String[] getHierarchy(String mediaID) {
        int pos = mediaID.indexOf(LEAF_SEPARATOR);
        if (pos >= 0) {
            mediaID = mediaID.substring(0, pos);
        }
        return mediaID.split(String.valueOf(CATEGORY_SEPARATOR));
    }

    public static String extractBrowseCategoryValueFromMediaID(String mediaID) {
        String[] hierarchy = getHierarchy(mediaID);
        if (hierarchy != null && hierarchy.length == 2) {
            return hierarchy[1];
        }
        return null;
    }

    private static boolean isBrowesable(String mediaID) {
        return mediaID.indexOf(LEAF_SEPARATOR) < 0;
    }

    public static String getParentMediaID(String mediaID) {
        String[] hierarchy = getHierarchy(mediaID);
        if (!isBrowesable(mediaID)) {
            return createMediaID(null, hierarchy);
        }
        if (hierarchy == null || hierarchy.length <= 1) {
            return MEDIA_ID_ROOT;
        }
        String[] parentHierarchy = Arrays.copyOf(hierarchy, hierarchy.length - 1);
        return createMediaID(null, parentHierarchy);
    }

    public static boolean isMediaItemPlaying(Context context,
                                             MediaBrowserCompat.MediaItem mediaItem) {
        MediaControllerCompat controller = ((FragmentActivity) context)
                .getSupportMediaController();
        if (controller != null && controller.getMetadata() != null) {
            String currentPlayingMediaId = controller.getMetadata().getDescription()
                    .getMediaId();
            String itemMusicId = MediaIDHelper.extractMusicIDFromMediaID(
                    mediaItem.getDescription().getMediaId());
            if (currentPlayingMediaId != null
                    && TextUtils.equals(currentPlayingMediaId, itemMusicId)) {
                return true;
            }
        }
        return false;
    }
}
