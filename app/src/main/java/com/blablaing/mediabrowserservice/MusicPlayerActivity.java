package com.blablaing.mediabrowserservice;

import android.media.browse.MediaBrowser;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MusicPlayerActivity extends AppCompatActivity
        implements BrowseFragment.FragmentDataHelper {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, BrowseFragment.newInstance(null))
                    .commit();
        }
    }

    @Override
    public void onMediaItemSelected(MediaBrowser.MediaItem item) {
        if (item.isPlayable()) {
            getMediaController().getTransportControls().playFromMediaId(item.getMediaId(), null);
            QueueFragment queueFragment = QueueFragment.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, queueFragment)
                    .addToBackStack(null)
                    .commit();
        } else if (item.isBrowsable()) {
            getFragmentManager().beginTransaction()
                    .replace(R.id.container, BrowseFragment.newInstance(item.getMediaId()))
                    .addToBackStack(null)
                    .commit();
        }
    }
}
