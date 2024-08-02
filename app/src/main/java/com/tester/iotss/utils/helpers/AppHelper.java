package com.tester.iotss.utils.helpers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class AppHelper {
    public static void openWhatsApp(Context context, String phoneNumber, String message) {
        try {
            // Attempt to open WhatsApp using the WhatsApp URI
            Uri uri = Uri.parse("whatsapp://send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8"));
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // Handle encoding exception
            Toast.makeText(context, "Error encoding message", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            // If WhatsApp is not installed, open the web link
            try {
                Uri uri = Uri.parse("https://api.whatsapp.com/send?phone=" + phoneNumber + "&text=" + URLEncoder.encode(message, "UTF-8"));
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                context.startActivity(intent);
            } catch (Exception ex) {
                // Handle exceptions as needed
                Toast.makeText(context, "Error: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
            }
        }
    }
}
