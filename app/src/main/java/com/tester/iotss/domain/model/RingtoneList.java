package com.tester.iotss.domain.model;

import android.content.Context;
import android.net.Uri;

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

    public void setUriFromResource(Context context, int resId) {
        this.uri = Uri.parse("android.resource://" + context.getPackageName() + "/" + resId);
    }
}
