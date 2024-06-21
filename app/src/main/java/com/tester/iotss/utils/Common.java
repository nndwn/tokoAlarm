package com.tester.iotss.utils;

import com.tester.iotss.data.remote.api.ApiService;
import com.tester.iotss.data.remote.network.RetrofitClient;
import com.tester.iotss.domain.model.Schedule;
import com.tester.iotss.ui.adapter.ScheduleAdapter;
import com.tester.iotss.ui.fragment.ScheduleFragment;
import com.tester.iotss.utils.sessions.SessionLogin;

import java.util.ArrayList;
import java.util.List;

public class Common {

    public static ApiService apiService = RetrofitClient.getClient().create(ApiService.class);

    public static SessionLogin sessionLogin = new SessionLogin();

    public static ScheduleFragment scheduleFragment;

    public static ScheduleAdapter scheduleAdapter;
    public static List<Schedule> scheduleList = new ArrayList<>();

    public static String convertToDayNames(String dayNumbers) {
        StringBuilder dayNamesBuilder = new StringBuilder();

        // Split day numbers string by commas
        String[] numbersArray = dayNumbers.split(",");

        // Array of day names corresponding to day numbers (1 for Senin, 2 for Selasa, etc.)
        String[] dayNames = {"Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu", "Minggu"};

        for (String number : numbersArray) {
            try {
                int index = Integer.parseInt(number.trim()) - 1; // Convert to zero-based index
                if (index >= 0 && index < dayNames.length) {
                    if (dayNamesBuilder.length() > 0) {
                        dayNamesBuilder.append(", ");
                    }
                    dayNamesBuilder.append(dayNames[index]);
                }
            } catch (NumberFormatException e) {
                // Handle the case where number.trim() cannot be parsed to an integer
                // For example, log the error or skip this number
                System.err.println("Failed to parse: " + number.trim() + ". Skipping...");
            }
        }

        return dayNamesBuilder.toString();
    }


}
