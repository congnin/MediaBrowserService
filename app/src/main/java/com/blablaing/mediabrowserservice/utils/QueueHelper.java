package com.blablaing.mediabrowserservice.utils;

import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;

import com.blablaing.mediabrowserservice.model.MusicProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.blablaing.mediabrowserservice.utils.MediaIDHelper.MEDIA_ID_MUSICS_BY_GENRE;
import static com.blablaing.mediabrowserservice.utils.MediaIDHelper.MEDIA_ID_MUSICS_BY_SEARCH;

/**
 * Created by Linh on 3/24/2017.
 */

public class QueueHelper {
    private static final String TAG = LogHelper.makeLogTag(QueueHelper.class);

    public static List<MediaSessionCompat.QueueItem> getPlayingQueue(String mediaId,
                                                               MusicProvider musicProvider) {
        String[] hierarchy = MediaIDHelper.getHierarchy(mediaId);
        if (hierarchy.length != 2) {
            LogHelper.e(TAG, "Could not build a playing queue for this mediaId: ", mediaId);
            return null;
        }

        String categoryType = hierarchy[0];
        String categoryValue = hierarchy[1];
        LogHelper.d(TAG, "Creating playing queue for ", categoryType, ", ", categoryValue);

        Iterable<MediaMetadataCompat> tracks = null;
        // This sample only supports genre and by_search category types.
        if (categoryType.equals(MEDIA_ID_MUSICS_BY_GENRE)) {
            tracks = musicProvider.getMusicsByGenre(categoryValue);
        } else if (categoryType.equals(MEDIA_ID_MUSICS_BY_SEARCH)) {
            tracks = musicProvider.searchMusic(categoryValue);
        }

        if (tracks == null) {
            LogHelper.e(TAG, "Unrecognized category type: ", categoryType, " for mediaId ", mediaId);
            return null;
        }

        return convertToQueue(tracks, hierarchy[0], hierarchy[1]);
    }

    public static List<MediaSessionCompat.QueueItem> getPlayingQueueFromSearch(String query,
                                                                         MusicProvider musicProvider) {
        LogHelper.d(TAG, "Creating playing queue for musics from search ", query);
        return convertToQueue(musicProvider.searchMusic(query), MEDIA_ID_MUSICS_BY_SEARCH, query);
    }

    public static int getMusicIndexOnQueue(Iterable<MediaSessionCompat.QueueItem> queue,
                                           String mediaId) {
        int index = 0;
        for (MediaSessionCompat.QueueItem item : queue) {
            if (mediaId.equals(item.getDescription().getMediaId())) {
                return index;
            }
            index++;
        }
        return -1;
    }

    public static int getMusicIndexOnQueue(Iterable<MediaSessionCompat.QueueItem> queue, long queueId) {
        int index = 0;
        for (MediaSessionCompat.QueueItem item : queue) {
            if (queueId == item.getQueueId()) {
                return index;
            }
            index++;
        }
        return -1;
    }

    private static List<MediaSessionCompat.QueueItem> convertToQueue(
            Iterable<MediaMetadataCompat> tracks, String... categories) {
        List<MediaSessionCompat.QueueItem> queue = new ArrayList<>();
        int count = 0;
        for (MediaMetadataCompat track : tracks) {
            String hierarchyAwareMediaID = MediaIDHelper.createMediaID(
                    track.getDescription().getMediaId(), categories);
            MediaMetadataCompat trackCopy = new MediaMetadataCompat.Builder(track)
                    .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, hierarchyAwareMediaID)
                    .build();

            MediaSessionCompat.QueueItem item = new MediaSessionCompat.QueueItem(
                    trackCopy.getDescription(), count++);
            queue.add(item);
        }
        return queue;
    }

    public static List<MediaSessionCompat.QueueItem> getRandomQueue(MusicProvider musicProvider) {
        Iterator<String> genres = musicProvider.getGenres().iterator();
        if (!genres.hasNext()) {
            return Collections.emptyList();
        }
        String genre = genres.next();
        Iterable<MediaMetadataCompat> tracks = musicProvider.getMusicsByGenre(genre);
        return convertToQueue(tracks, MEDIA_ID_MUSICS_BY_GENRE, genre);
    }

    public static boolean isIndexPlayable(int index, List<MediaSessionCompat.QueueItem> queue) {
        return (queue != null && index >= 0 && index < queue.size());
    }
}
