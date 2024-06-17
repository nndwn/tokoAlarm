package com.tester.iotss.ui.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.tester.iotss.R;
import com.tester.iotss.data.model.Schedule;
import com.tester.iotss.ui.activity.FormJadwalActivity;
import com.tester.iotss.utils.Common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private List<Schedule> scheduleList;

    public ScheduleAdapter(List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
    }
    @NonNull
    @Override
    public ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ScheduleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ScheduleViewHolder holder, int position) {
        Schedule schedule = scheduleList.get(position);
        holder.bind(schedule);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FormJadwalActivity.class);
            intent.putExtra("schedule", schedule);
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public class ScheduleViewHolder extends RecyclerView.ViewHolder {

        private MaterialSwitch materialSwitch;
        private TextView tvTime, tvDays, tvDevice, tvSensorSwitch, tvSensorOhm, tvSensorRf;

        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            materialSwitch = itemView.findViewById(R.id.materialSwitch);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvDays = itemView.findViewById(R.id.tv_days);
            tvDevice = itemView.findViewById(R.id.tv_device);
            tvSensorSwitch = itemView.findViewById(R.id.tvSensorSwitch);
            tvSensorOhm = itemView.findViewById(R.id.tvSensorOhm);
            tvSensorRf = itemView.findViewById(R.id.tvSensorRf);
        }

        public void bind(Schedule schedule) {
            materialSwitch.setText(schedule.getName());
            tvTime.setText(schedule.getStart_time() + " - " + schedule.getEnd_time());

            String dayNames = Common.convertToDayNames(schedule.getDays());
            tvDays.setText(dayNames);

            if (schedule.getIs_active().equals("1")) {
                materialSwitch.setChecked(true);
            }

            if (!schedule.getNama_paket().equals("-")) {
                tvDevice.setText(schedule.getNama_paket());
            } else {
                tvDevice.setText(schedule.getId_alat());
            }

            if (schedule.getSensor_switch().equals("1")) {
                tvSensorSwitch.setText("Switch On");
                tvSensorSwitch.setEnabled(true);
            } else {
                tvSensorSwitch.setText("Switch Off");
                tvSensorSwitch.setEnabled(false);
            }

            if (schedule.getSensor_ohm().equals("1")) {
                tvSensorOhm.setText("Ohm On");
                tvSensorOhm.setEnabled(true);
            } else {
                tvSensorOhm.setText("Ohm Off");
                tvSensorOhm.setEnabled(false);
            }

            if (schedule.getSensor_rf().equals("1")) {
                tvSensorRf.setText("RF On");
                tvSensorRf.setEnabled(true);
            } else {
                tvSensorRf.setText("RF Off");
                tvSensorRf.setEnabled(false);
            }
        }

    }
}