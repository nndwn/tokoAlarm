package com.tester.iotss.utils;

import androidx.appcompat.app.AppCompatActivity;

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
}
