package com.blablaing.mediabrowserservice;

import android.app.Activity;
import android.media.session.MediaSession;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by congnc on 3/26/17.
 */

public class QueueAdapter extends ArrayAdapter<MediaSession.QueueItem> {
    private long mActiveQueueItemId = MediaSession.QueueItem.UNKNOWN_ID;

    public QueueAdapter(Activity context) {
        super(context, R.layout.media_list_item, new ArrayList<MediaSession.QueueItem>());
    }

    public void setActiveQueueItemId(long id) {
        this.mActiveQueueItemId = id;
    }

    private static class ViewHolder {
        ImageView mImageView;
        TextView mTitleView;
        TextView mDescriptionView;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.media_list_item, parent, false);
            holder = new ViewHolder();
            holder.mImageView = (ImageView) convertView.findViewById(R.id.play_eq);
            holder.mTitleView = (TextView) convertView.findViewById(R.id.title);
            holder.mDescriptionView = (TextView) convertView.findViewById(R.id.description);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MediaSession.QueueItem item = getItem(position);
        holder.mTitleView.setText(item.getDescription().getTitle());
        if (item.getDescription().getDescription() != null) {
            holder.mDescriptionView.setText(item.getDescription().getDescription());
        }

        // If the itemId matches the active Id then use a different icon
        if (mActiveQueueItemId == item.getQueueId()) {
            holder.mImageView.setImageDrawable(
                    getContext().getDrawable(R.drawable.ic_equalizer_white_24dp));
        } else {
            holder.mImageView.setImageDrawable(
                    getContext().getDrawable(R.drawable.ic_play_arrow_white_24dp));
        }
        return convertView;
    }
}
