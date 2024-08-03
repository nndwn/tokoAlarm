package com.tester.iotss.ui.adapter;

import static com.tester.iotss.utils.Common.sessionLogin;
import static com.tester.iotss.utils.services.ScheduleService.BrokerUri;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.materialswitch.MaterialSwitch;
import com.tester.iotss.R;
import com.tester.iotss.data.AppConstant;
import com.tester.iotss.data.remote.request.ScheduleEnableDisableRequest;
import com.tester.iotss.data.remote.response.CommonApiResponse;
import com.tester.iotss.domain.model.Schedule;
import com.tester.iotss.ui.activity.FormJadwalActivity;
import com.tester.iotss.utils.Common;
import com.tester.iotss.utils.helpers.MqttHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder> {

    private final List<Schedule> scheduleList;
    private final Context context;
    private MqttHelper mqttHelper;

    public ScheduleAdapter(Context context, List<Schedule> scheduleList) {
        this.context = context;
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
        holder.bind(schedule, context);
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), FormJadwalActivity.class);
            intent.putExtra("schedule", schedule);
            v.getContext().startActivity(intent);
        });

        holder.materialSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ScheduleEnableDisableRequest requestBody = new ScheduleEnableDisableRequest(schedule.getId(), schedule.getId_book(), isChecked);
            if (isChecked)
            {
                startMqtt(schedule, context);
            }
            Call<CommonApiResponse> call = Common.apiService.setScheduleStatus(requestBody);
            call.enqueue(new Callback<>() {
                @Override
                public void onResponse(@NonNull Call<CommonApiResponse> call, @NonNull Response<CommonApiResponse> response) {
                    assert response.body() != null;

                }

                @Override
                public void onFailure(@NonNull Call<CommonApiResponse> call, @NonNull Throwable t) {}
            });
        });
    }


    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    public static class ScheduleViewHolder extends RecyclerView.ViewHolder {

        private final MaterialSwitch materialSwitch;
        private final TextView tvTime;
        private final TextView tvDays;
        private final TextView tvTitle;
        private final TextView tvSensorSwitch;
        private final TextView tvSensorOhm;
        private final TextView tvSensorRf;



        public ScheduleViewHolder(@NonNull View itemView) {
            super(itemView);
            materialSwitch = itemView.findViewById(R.id.materialSwitch);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvDays = itemView.findViewById(R.id.tv_days);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSensorSwitch = itemView.findViewById(R.id.tvSensorSwitch);
            tvSensorOhm = itemView.findViewById(R.id.tvSensorOhm);
            tvSensorRf = itemView.findViewById(R.id.tvSensorRf);
        }

        public void bind(@NonNull Schedule schedule, Context context) {
            String dayNames = Common.convertToDayNames(schedule.getDays());
            tvDays.setText(dayNames);
            tvTime.setText(schedule.getStart_time() + " - " + schedule.getEnd_time());

            materialSwitch.setChecked(schedule.getIs_active().equals("1"));

            if (!schedule.getNama_paket().equals("-")) {
                tvTitle.setText(schedule.getNama_paket());
            } else {
                tvTitle.setText(schedule.getId_alat());
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
    private void disconnectMqtt() {
        if (mqttHelper != null) {
            if (mqttHelper.mqttAndroidClient != null) {
                if (mqttHelper.mqttAndroidClient.isConnected()) {
                    Log.d("FragmentHomeLog", "berhasil diskonek mqtt");
                    try {
                        mqttHelper.disconnect();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("FragmentHomeLog", "gagal diskonek mqtt");
                    }
                }
                mqttHelper = null;
            }
        }
    }

    public void startMqtt(Schedule jadwal, Context context) {
        disconnectMqtt();
        mqttHelper = new MqttHelper(context, BrokerUri, AppConstant.MQTT_USER,AppConstant.MQTT_PASSWORD);
        mqttHelper.connect(sessionLogin.getNohp(context.getApplicationContext()), sessionLogin.getPassword(context.getApplicationContext()));
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                String id = jadwal.getId_alat().toUpperCase();
                sendToServer(id+"/statin1");
                sendToServer(id+"/statin2");
                sendToServer(id+"/statin3");

                Log.d("abah", "SERVER MQTT KONEK");
            }

            @Override
            public void connectionLost(Throwable throwable) {}
            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {}
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {}
        });
    }

    private void sendToServer( String topic) {

        if (mqttHelper.mqttAndroidClient.isConnected()) {
            MqttMessage message = new MqttMessage();
            message.setPayload("0".getBytes());
            message.setQos(0);
            message.setRetained(false);
            mqttHelper.mqttAndroidClient.publish(topic, message);
        } else {
            Log.d("Publish", "Enggak Bisa publish");
        }
    }


}