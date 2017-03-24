package com.blablaing.mediabrowserservice.model;

import android.media.MediaMetadata;
import android.text.TextUtils;

/**
 * Created by Linh on 3/24/2017.
 */

public class MutableMediaMetadata {
    public MediaMetadata metadata;
    public final String trackId;

    public MutableMediaMetadata(String trackId, MediaMetadata metadata) {
        this.metadata = metadata;
        this.trackId = trackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || o.getClass() != MutableMediaMetadata.class) {
            return false;
        }

        MutableMediaMetadata that = (MutableMediaMetadata) o;

        return TextUtils.equals(trackId, that.trackId);
    }

    @Override
    public int hashCode() {
        return trackId.hashCode();
    }
}
