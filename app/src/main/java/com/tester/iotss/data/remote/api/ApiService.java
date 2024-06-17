package com.tester.iotss.data.remote.api;

import com.tester.iotss.data.remote.request.PerangkatRequest;
import com.tester.iotss.data.remote.response.PerangkatResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("perangkat")
    Call<PerangkatResponse> fetchPerangkat(@Body PerangkatRequest request);
}