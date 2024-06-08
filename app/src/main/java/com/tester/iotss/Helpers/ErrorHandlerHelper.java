package com.tester.iotss.Helpers;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONException;
import com.androidnetworking.common.ANConstants;
import com.androidnetworking.error.ANError;

public class ErrorHandlerHelper {
    private static boolean errorHandled = false;

    public static void resetErrorHandledFlag() {
        errorHandled = false;
    }

    public static void handleANError(Context context, ANError error) {
        if (!errorHandled) {
            if (error.getErrorDetail().equals(ANConstants.CONNECTION_ERROR)) {
                Toast.makeText(context, "Tidak ada koneksi internet", Toast.LENGTH_LONG).show();
            } else {
                Log.d("ERROR_LOG", error.getMessage());
                Toast.makeText(context, "ON ERROR " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
            errorHandled = true;
        }
    }

    public static void handleJSONException(Context context, JSONException e) {
        if (!errorHandled) {
            Log.d("FRAGMENTHISTORYLOG", e.getMessage());
            Toast.makeText(context, "Terjadi kesalahan " + e.getMessage(), Toast.LENGTH_SHORT).show();
            errorHandled = true;
        }
    }
}
