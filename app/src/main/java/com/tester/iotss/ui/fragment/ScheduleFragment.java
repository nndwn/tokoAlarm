package com.tester.iotss.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.MaterialToolbar;
import com.tester.iotss.R;
import com.tester.iotss.data.remote.api.ApiService;
import com.tester.iotss.data.remote.network.RetrofitClient;
import com.tester.iotss.data.remote.request.GetUserScheduleRequest;
import com.tester.iotss.data.remote.response.ScheduleResponse;
import com.tester.iotss.databinding.FragmentScheduleBinding;
import com.tester.iotss.domain.model.Schedule;
import com.tester.iotss.ui.activity.FormJadwalActivity;
import com.tester.iotss.ui.adapter.ScheduleAdapter;
import com.tester.iotss.utils.Common;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ScheduleAdapter adapter;
    private final List<Schedule> scheduleList = new ArrayList<>();
    private ApiService apiService;
    private SwipeRefreshLayout swipeRefreshLayout;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //private ScheduleViewModel mViewModel;
        com.tester.iotss.databinding.FragmentScheduleBinding fragmentScheduleBinding = FragmentScheduleBinding.inflate(inflater, container, false);
        View view = fragmentScheduleBinding.getRoot();

        //mViewModel = new ViewModelProvider(this).get(ScheduleViewModel.class);

        MaterialToolbar toolbar = fragmentScheduleBinding.topAppBar;

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.tambah_jadwal) {
                startActivity(new Intent(requireContext(), FormJadwalActivity.class));
                return true;
            }
            return false;
        });

        RecyclerView recyclerView = fragmentScheduleBinding.rvSchedule;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ScheduleAdapter(getContext(), scheduleList);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout = fragmentScheduleBinding.swipeRefreshLayout;
        swipeRefreshLayout.setOnRefreshListener(this);

        apiService = RetrofitClient.getClient().create(ApiService.class);

        getData();

        Common.scheduleFragment = this;

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        getData();
    }

    public void refreshData() {
        getData();
    }



    private void getData() {
        // Call API to fetch schedules
        GetUserScheduleRequest requestBody = new GetUserScheduleRequest(Common.sessionLogin.getNohp(requireContext()));
        Call<ScheduleResponse> call = apiService.getUserSchedules(requestBody);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NonNull Call<ScheduleResponse> call, @NonNull Response<ScheduleResponse> response) {
                swipeRefreshLayout.setRefreshing(false); // Hide refresh indicator
                if (response.isSuccessful()) {
                    ScheduleResponse scheduleResponse = response.body();
                    if (scheduleResponse != null) {
                        scheduleList.clear(); // Clear old data
                        scheduleList.addAll(scheduleResponse.getData());
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    scheduleList.clear();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScheduleResponse> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false); // Hide refresh indicator
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}