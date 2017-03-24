package com.blablaing.mediabrowserservice.utils;

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
}
