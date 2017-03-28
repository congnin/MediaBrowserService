package com.blablaing.mediabrowserservice.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.blablaing.mediabrowserservice.utils.LogHelper;

/**
 * Created by Linh on 3/28/2017.
 */

public class ActionBarCastActivity extends AppCompatActivity {
    private static final String TAG = LogHelper.makeLogTag(ActionBarCastActivity.class);
    private static final int DELAY_MILLIS = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogHelper.d(TAG, "Activity onCreate");
    }
}
