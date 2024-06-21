package com.tester.iotss.data.remote.api;

import com.tester.iotss.data.remote.request.GetUserScheduleRequest;
import com.tester.iotss.data.remote.request.PerangkatRequest;
import com.tester.iotss.data.remote.request.ScheduleEnableDisableRequest;
import com.tester.iotss.data.remote.request.ScheduleRequest;
import com.tester.iotss.data.remote.response.CommonApiResponse;
import com.tester.iotss.data.remote.response.PerangkatResponse;
import com.tester.iotss.data.remote.response.ScheduleResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiService {
    @POST("schedule/alat")
    Call<PerangkatResponse> fetchPerangkat(@Body PerangkatRequest request);

    @POST("schedule")
    Call<CommonApiResponse> sendSchedule(@Body ScheduleRequest request);

    @POST("schedule/update")
    Call<CommonApiResponse> updateSchedule(@Body ScheduleRequest request);

    @POST("schedule/list")
    Call<ScheduleResponse> getUserSchedules(@Body GetUserScheduleRequest request);

    @POST("schedule/setting")
    Call<CommonApiResponse> setScheduleStatus(@Body ScheduleEnableDisableRequest request);

    @POST("schedule/delete")
    Call<CommonApiResponse> deleteSchedule(@Body ScheduleRequest request);
}