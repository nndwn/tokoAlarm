package com.tester.iotss.utils;

import com.tester.iotss.Session.SessionLogin;

public class Common {
    public static SessionLogin sessionLogin = new SessionLogin();

    public static String convertToDayNames(String dayNumbers) {
        StringBuilder dayNamesBuilder = new StringBuilder();

        // Split day numbers string by commas
        String[] numbersArray = dayNumbers.split(",");

        // Array of day names corresponding to day numbers (1 for Senin, 2 for Selasa, etc.)
        String[] dayNames = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};

        for (String number : numbersArray) {
            int index = Integer.parseInt(number.trim()) - 1; // Convert to zero-based index
            if (index >= 0 && index < dayNames.length) {
                if (dayNamesBuilder.length() > 0) {
                    dayNamesBuilder.append(", ");
                }
                dayNamesBuilder.append(dayNames[index]);
            }
        }

        return dayNamesBuilder.toString();
    }

}
