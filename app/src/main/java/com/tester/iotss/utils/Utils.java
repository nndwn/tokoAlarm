package com.tester.iotss.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Utils  {

    public static String readRawResource(InputStream input) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
        StringBuilder stringBuilder = new StringBuilder();

        String line;
        try {
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return stringBuilder.toString();
    }

    public static String getDayNumber(String dayName) {
        return switch (dayName.toLowerCase()) {
            case "senin" -> "1";
            case "selasa" -> "2";
            case "rabu" -> "3";
            case "kamis" -> "4";
            case "jumat" -> "5";
            case "sabtu" -> "6";
            case "minggu" -> "7";
            default -> ""; // Handle unknown day names if necessary
        };
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }



}
