package com.tester.iotss.domain.model;

import android.net.Uri;

import androidx.multidex.BuildConfig;

public class RingtoneList {
    String title;
    Uri uri;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setUriFromResource(int resId) {
        this.uri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID + "/" + resId);
    }
}
