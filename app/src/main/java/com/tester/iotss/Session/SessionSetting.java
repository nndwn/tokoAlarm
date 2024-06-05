package com.tester.iotss.Session;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionSetting {

    String endpoint;

    public String getEndpoint(Context context) {
        SharedPreferences sharedPreferences2=context.getSharedPreferences("endpoint", Context.MODE_PRIVATE);
        endpoint=sharedPreferences2.getString("endpoint","");
        return endpoint;
    }

    public void setEndpoint(String endpoint,Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("endpoint", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("endpoint", endpoint);
        editor.commit();
        this.endpoint = endpoint;
    }


}
