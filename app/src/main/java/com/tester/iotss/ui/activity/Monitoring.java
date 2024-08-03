package com.tester.iotss.ui.activity;

import static com.tester.iotss.data.config.Config.BASE_URL;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.tester.iotss.R;
import com.tester.iotss.data.AppConstant;
import com.tester.iotss.domain.model.Schedule;
import com.tester.iotss.ui.dialog.AlertError;
import com.tester.iotss.ui.dialog.AlertSuccess;
import com.tester.iotss.ui.dialog.LoadingDialog;
import com.tester.iotss.utils.helpers.MqttHelper;
import com.tester.iotss.utils.sessions.SessionLogin;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Monitoring extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    String id_alat = "", nomor_paket = "";
    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    SessionLogin sessionLogin;
    @BindView(R.id.tvIdAlat)
    TextView tvIdAlat;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.tvStatusAlat)
    TextView tvStatusAlat;

    @BindView(R.id.ivSensor1)
    ImageView ivSensor1;
    @BindView(R.id.ivSensor2)
    ImageView ivSensor2;
    @BindView(R.id.ivSensor3)
    ImageView ivSensor3;
    @BindView(R.id.ivSocket)
    ImageView ivSocket;

    @BindView(R.id.tvIn1)
    TextView tvIn1;
    @BindView(R.id.tvIn2)
    TextView tvIn2;
    @BindView(R.id.tvIn3)
    TextView tvIn3;
    @BindView(R.id.tvAlarm)
    TextView tvAlarm;
    @BindView(R.id.txtSwIn1)
    TextView txtSwIn1;
    @BindView(R.id.tvSensor1)
    TextView tvSensor1;
    @BindView(R.id.tvSensor2)
    TextView tvSensor2;
    @BindView(R.id.tvSensor3)
    TextView tvSensor3;

    @BindView(R.id.tvSocket)
    TextView tvSocket;

    @BindView(R.id.swIn1)
    Switch swIn1;

    @BindView(R.id.swIn2)
    Switch swIn2;

    @BindView(R.id.txtSwIn2)
    TextView txtSwIn2;

    @BindView(R.id.swIn3)
    Switch swIn3;
    @BindView(R.id.txtSwIn3)
    TextView txtSwIn3;

    @BindView(R.id.swMode)
    Switch swMode;

    @BindView(R.id.tvTanggalMulai)
    TextView tvTanggalMulai;
    @BindView(R.id.tvTanggalExpired)
    TextView tvTanggalExpired;
    @BindView(R.id.tvPeriode)
    TextView tvPeriode;

    @BindView(R.id.tvDelay)
    TextView tvDelay;
    @BindView(R.id.btnAlarm)
    LinearLayout btnAlarm;

    @BindView(R.id.btnCekAlat)
    Button btnCekAlat;
    boolean isCekAlatButton = false;
    private MqttHelper mqttHelper;

    private static final String BrokerUri = AppConstant.MQTT_SERVER_PROTOCOL + AppConstant.MQTT_SERVER_HOST + ":" + AppConstant.MQTT_SERVER_PORT;

    boolean checkeddaridata = false;

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog sheetDialog;
    @BindView(R.id.bottom_sheet)
    View bottom_sheet;

    BottomSheetBehavior sheetBehaviorDelay;
    BottomSheetDialog sheetDialogDelay;
    @BindView(R.id.bottom_sheet_delay)
    View bottom_sheet_delay;

    LoadingDialog loadingDialog;
    AlertSuccess alertSuccess;
    AlertError alertError;

    @BindView(R.id.tvLogIn1)
    TextView tvLogIn1;
    @BindView(R.id.tvLogIn2)
    TextView tvLogIn2;
    @BindView(R.id.tvLogIn3)
    TextView tvLogIn3;
    @BindView(R.id.tvLogValIn1)
    TextView tvLogValIn1;
    @BindView(R.id.tvLogValIn2)
    TextView tvLogValIn2;
    @BindView(R.id.tvLogValIn3)
    TextView tvLogValIn3;

    @BindView(R.id.ivButtonHidupkanAlarm)
    ImageView ivButtonHidupkanAlarm;

    @BindView(R.id.tvButtonTextHidupkanAlarm)
    TextView tvButtonTextHidupkanAlarm;

    @BindView(R.id.tvDelayAlarm)
    TextView tvDelayAlarm;

    @BindView(R.id.lnListOfSensor)
    LinearLayout lnListOfSensor;

    private Schedule schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring);
        ButterKnife.bind(this);
        initializeUI();

        setupFirebase();
        setupSwipeRefresh();
        setupScrollListener();
        setupSwitchListeners();

    }

    private void setupFirebase() {
        FirebaseApp.initializeApp(this);
        sessionLogin = new SessionLogin();
        FirebaseMessaging.getInstance().subscribeToTopic(sessionLogin.getNohp(getApplicationContext()));
        loadingDialog = new LoadingDialog(this);
        alertSuccess = new AlertSuccess(this);
        alertError = new AlertError(this);
    }

    private void initializeUI() {
        if (getIntent().hasExtra("id_alat")) {
            id_alat = Objects.requireNonNull(getIntent().getStringExtra("id_alat")).toUpperCase();
            nomor_paket = getIntent().getStringExtra("nomor_paket");
            tvIdAlat.setText(id_alat);

        }

        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        sheetBehaviorDelay = BottomSheetBehavior.from(bottom_sheet_delay);
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getData();
        });
    }

    private void setupScrollListener() {
        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
        });
    }

    private void setupSensorSwitchListener(String sensor, CompoundButton sw , String topic , TextView txt)
    {
        if (schedule != null)
        {
            if (Objects.equals(schedule.getIs_active(), "0") || Objects.equals(Objects.requireNonNull(schedule).getIs_active(), "1") && Objects.equals(sensor, "1"))
            {
                sw.setChecked(false);
            }else
            {
                getData();
            }
        }

        sw.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (schedule != null && Objects.equals(schedule.getIs_active(), "1") && Objects.equals(sensor, "1"))
            {
                Toast.makeText(Monitoring.this, "Tidak Dapat Melakukan setting manual , periksa pengaturan jadwal"  , Toast.LENGTH_LONG).show();
                sw.setChecked(!isChecked);
            }else if (!checkeddaridata ) {
                if (id_alat.length() > 1) {
                    String url = id_alat + topic;
                    String value = isChecked ? "1" : "0";
                    if (sendToServer(url, value)) {
                        txt.setText(isChecked ? "Enable" : "Disable");
                    } else {
                        sw.setChecked(!isChecked);
                        txt.setText(isChecked ? "Disable" : "Enable");
                    }
                } else {
                    sw.setChecked(!isChecked);
                    txt.setText(isChecked ? "Disable" : "Enable");
                }
            }
        });
    }

    private void setupSwitchListeners() {
        if (schedule != null)
        {
            schedule = (Schedule) getIntent().getSerializableExtra("schedule");
        }
        setupAlarmSwitchListener(swMode);
        setupSensorSwitchListener(schedule != null ? schedule.getSensor_switch() : "",swIn1, "/statin1",txtSwIn1);
        setupSensorSwitchListener(schedule != null ? schedule.getSensor_ohm(): "",swIn2, "/statin2",txtSwIn2);
        setupSensorSwitchListener(schedule != null ? schedule.getSensor_rf(): "",swIn3, "/statin3",txtSwIn3);

    }

    private void setupAlarmSwitchListener(CompoundButton switchButton) {
        switchButton.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (!checkeddaridata) {
                if (id_alat.length() > 1) {
                    String url = id_alat + "/mode";
                    String value = isChecked ? "otomatis" : "manual";
                    if (sendToServer(url, value)) {
                        switchButton.setText(isChecked ? "Mode: Otomatis" : "Mode: Manual");
                        btnAlarm.setVisibility(isChecked ? View.GONE : View.VISIBLE);
                    } else {
                        switchButton.setChecked(!isChecked);
                        switchButton.setText(isChecked ? "Mode: Manual" : "Mode: Otomatis");
                    }
                } else {
                    switchButton.setChecked(!isChecked);
                    switchButton.setText(isChecked ? "Mode: Manual" : "Mode: Otomatis");
                }
            }
        });
    }

    @OnClick(R.id.backButton)
    void backButton() {
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        runOnUiThread(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getData();
        });
    }

    private void getData() {
        SessionLogin sessionLogin = new SessionLogin();
        AndroidNetworking.post(BASE_URL + "users/bookalatdetail")
                .setPriority(Priority.HIGH)
                .addBodyParameter("nohp", sessionLogin.getNohp(getApplicationContext()))
                .addBodyParameter("id_alat", id_alat)
                .addBodyParameter("nomor_paket", nomor_paket)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        swipeRefreshLayout.setRefreshing(false);
                        runOnUiThread(() -> {
                            Log.d("LOGJSONMONITORING", person.toString());
                            try {
                                boolean status = person.getBoolean("status");
                                if (status) {
                                    JSONObject data = person.getJSONObject("data");
                                    tvIdAlat.setText(data.getString("id_alat"));
                                    tvStatus.setText(data.getString("status"));
                                    if (data.getString("status").equals("Aktif")) {
                                        tvStatus.setEnabled(true);
                                        lnListOfSensor.setVisibility(View.VISIBLE);
                                    } else if (data.getString("status").equals("Non Aktif")) {
                                        tvStatus.setEnabled(false);
                                        tvStatus.setHovered(false);
                                        lnListOfSensor.setVisibility(View.GONE);
                                    } else if (data.getString("status").equals("Pending")) {
                                        tvStatus.setEnabled(false);
                                        tvStatus.setHovered(true);
                                    }
                                    tvPeriode.setText(data.getString("periode"));
                                    tvTanggalMulai.setText(data.getString("tanggal_mulai"));
                                    tvTanggalExpired.setText(data.getString("tanggal_selesai"));
                                    if (person.getJSONObject("last_alat").getBoolean("status")) {
                                        checkeddaridata = true;
                                        JSONObject data_last = person.getJSONObject("last_alat").getJSONObject("data");

                                        if (data_last.getString("stsin1").equals("1")) {
                                            tvSensor1.setText("Mendeteksi");
                                            ivSensor1.setImageResource(R.drawable.circle_danger);
                                        } else {
                                            tvSensor1.setText("Normal");
                                            ivSensor1.setImageResource(R.drawable.circle_success);
                                        }

                                        if (data_last.getString("stsin2").equals("1")) {
                                            tvSensor2.setText("Mendeteksi");
                                            ivSensor2.setImageResource(R.drawable.circle_danger);
                                        } else {
                                            tvSensor2.setText("Normal");
                                            ivSensor2.setImageResource(R.drawable.circle_success);
                                        }

                                        if (data_last.getString("stsin3").equals("1")) {
                                            tvSensor3.setText("Mendeteksi");
                                            ivSensor3.setImageResource(R.drawable.circle_danger);
                                        } else {
                                            tvSensor3.setText("Normal");
                                            ivSensor3.setImageResource(R.drawable.circle_success);
                                        }

                                        if (data_last.getString("stsstatin1").equals("1")) {
                                            swIn1.setChecked(true);
                                            txtSwIn1.setText("Enable");
                                        } else {
                                            swIn1.setChecked(false);
                                            txtSwIn1.setText("Disable");
                                        }

                                        if (data_last.getString("stsstatin2").equals("1")) {
                                            swIn2.setChecked(true);
                                            txtSwIn2.setText("Enable");
                                        } else {
                                            swIn2.setChecked(false);
                                            txtSwIn2.setText("Disable");
                                        }

                                        if (data_last.getString("stsstatin3").equals("1")) {
                                            swIn3.setChecked(true);
                                            txtSwIn3.setText("Enable");
                                        } else {
                                            swIn3.setChecked(false);
                                            txtSwIn3.setText("Disable");
                                        }

                                        if (data_last.getString("mode").equals("otomatis")) {
                                            swMode.setChecked(true);
                                            swMode.setText("Mode: Otomatis");
                                            btnAlarm.setVisibility(View.GONE);
                                        } else {
                                            swMode.setChecked(false);
                                            swMode.setText("Mode: Manual");
                                            btnAlarm.setVisibility(View.VISIBLE);
                                        }

                                        if (data_last.getString("is_aktif").equals("1")) {
                                            tvStatusAlat.setText("Aktif");
                                            tvStatusAlat.setEnabled(true);
                                            lnListOfSensor.setVisibility(View.VISIBLE);
                                        } else {
                                            tvStatusAlat.setText("Non Aktif");
                                            tvStatusAlat.setEnabled(false);
                                            tvStatusAlat.setHovered(false);
                                            lnListOfSensor.setVisibility(View.GONE);
                                        }


                                        checkeddaridata = false;
                                        tvDelay.setText(data_last.getString("delay"));
                                        tvDelayAlarm.setText(data_last.getString("delay") + " Detik");
                                        tvLogValIn1.setText(data_last.getString("in1"));
                                        tvLogValIn2.setText(data_last.getString("in2"));
                                        tvLogValIn3.setText(data_last.getString("in3"));
                                    }

                                    if (person.getJSONObject("renamed").getBoolean("status")) {
                                        JSONArray renamedArray = person.getJSONObject("renamed").getJSONArray("data");
                                        for (int j = 0; j < renamedArray.length(); j++) {
                                            try {
                                                JSONObject renamedObject = renamedArray.getJSONObject(j);
                                                if (renamedObject.getString("before_rename").equals("in1")) {
                                                    tvIn1.setText(renamedObject.getString("after_rename"));
                                                    tvLogIn1.setText(renamedObject.getString("after_rename"));
                                                } else if (renamedObject.getString("before_rename").equals("in2")) {
                                                    tvIn2.setText(renamedObject.getString("after_rename"));
                                                    tvLogIn2.setText(renamedObject.getString("after_rename"));
                                                } else if (renamedObject.getString("before_rename").equals("in3")) {
                                                    tvIn3.setText(renamedObject.getString("after_rename"));
                                                    tvLogIn3.setText(renamedObject.getString("after_rename"));
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else {
                                    Toast.makeText(Monitoring.this.getApplicationContext(), person.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d("FRAGMENTHOMELOG", Objects.requireNonNull(e.getMessage()));
                                Toast.makeText(Monitoring.this, "Terjadi Kesalahan " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            konekMQTT();
                        });

                    }

                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        konekMQTT();
                        Log.d("FRAGMENTHOMELOG", Objects.requireNonNull(error.getMessage()));
                        Toast.makeText(Monitoring.this, "Terjadi Kesalahan " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }


    @OnClick(R.id.ivEditSensor1)
    void ivEditSensor1() {
        showBottomSheetDialog("in1", tvIn1.getText().toString());
    }

    @OnClick(R.id.ivEditSensor2)
    void ivEditSensor2() {
        showBottomSheetDialog("in2", tvIn2.getText().toString());
    }

    @OnClick(R.id.ivEditSensor3)
    void ivEditSensor3() {
        showBottomSheetDialog("in3", tvIn3.getText().toString());
    }

    @OnClick(R.id.ivEditAlarm)
    void ivEditAlarm() {
        showBottomSheetDialogDelay(tvDelay.getText().toString());
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    private void konekMQTT() {
        diskonekMQTT();
        mqttHelper = new MqttHelper(Monitoring.this, BrokerUri,AppConstant.MQTT_USER,AppConstant.MQTT_PASSWORD);
        mqttHelper.connect(sessionLogin.getNohp(getApplicationContext()), sessionLogin.getPassword(getApplicationContext()));
        startMqtt();
        Log.d("reza", "sudah konek");
    }

    public void startMqtt() {
        mqttHelper.mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.d("FragmentHomeLog", "Main Mqtt Connected: " + s);
                tvSocket.setText("Server Connected");
                ivSocket.setImageResource(R.drawable.circle_success);

                subscribeToTopic(id_alat + "/in1");
                subscribeToTopic(id_alat + "/in2");
                subscribeToTopic(id_alat + "/in3");

                subscribeToTopic(id_alat + "/statin1");
                subscribeToTopic(id_alat + "/statin2");
                subscribeToTopic(id_alat + "/statin3");
                subscribeToTopic(id_alat + "/alarm");
                subscribeToTopic(id_alat + "/delay");
                subscribeToTopic(id_alat + "/active");
                Log.d("abah", "SERVER MQTT KONEK");
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.d("FragmentHomeLog", "Main Mqtt lost:");
                tvSocket.setText("Server Disconnected");
                ivSocket.setImageResource(R.drawable.circle_danger);
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) {
                Log.d("NETWORK MQTT", "Message size: " + mqttMessage.getPayload().length + " bytes");
                Log.d("FragmentHomeLog", mqttMessage.toString());
                if (topic.equals(id_alat + "/in1")) {
                    if (mqttMessage.toString().equals("1")) {
                        tvSensor1.setText("Mendeteksi");
                        ivSensor1.setImageResource(R.drawable.circle_danger);
                    } else {
                        tvSensor1.setText("Normal");
                        ivSensor1.setImageResource(R.drawable.circle_success);
                    }
                } else if (topic.equals(id_alat + "/in2")) {
                    if (mqttMessage.toString().equals("1")) {
                        tvSensor2.setText("Mendeteksi");
                        ivSensor2.setImageResource(R.drawable.circle_danger);
                    } else {
                        tvSensor2.setText("Normal");
                        ivSensor2.setImageResource(R.drawable.circle_success);
                    }
                } else if (topic.equals(id_alat + "/in3")) {
                    if (mqttMessage.toString().equals("1")) {
                        tvSensor3.setText("Mendeteksi");
                        ivSensor3.setImageResource(R.drawable.circle_danger);
                    } else {
                        tvSensor3.setText("Normal");
                        ivSensor3.setImageResource(R.drawable.circle_success);
                    }
//                } else if (topic.equals(id_alat + "/statin1")) {
//                    if (mqttMessage.toString().equals("1")) {
//                        swIn1.setChecked(true);
//                        txtSwIn1.setText("Enable");
//                    } else {
//                        swIn1.setChecked(false);
//                        txtSwIn1.setText("Disable");
//                    }
//
//                } else if (topic.equals(id_alat + "/statin2")) {
//                    if (mqttMessage.toString().equals("1")) {
//                        swIn2.setChecked(true);
//                        txtSwIn2.setText("Enable");
//                    } else {
//                        swIn2.setChecked(false);
//                        txtSwIn2.setText("Disable");
//                    }
//
//                } else if (topic.equals(id_alat + "/statin3")) {
//                    if (mqttMessage.toString().equals("1")) {
//                        swIn3.setChecked(true);
//                        txtSwIn3.setText("Enable");
//                    } else {
//                        swIn3.setChecked(false);
//                        txtSwIn3.setText("Disable");
//                    }

                } else if (topic.equals(id_alat + "/alarm")) {
                    if (mqttMessage.toString().equals("0")) {
                        btnAlarm.setEnabled(true);
                        ivButtonHidupkanAlarm.setImageResource(R.drawable.alarm_aktif);
                    }
                } else if (topic.equals(id_alat + "/delay")) {
                    tvDelay.setText(mqttMessage.toString());
                    tvDelayAlarm.setText(mqttMessage + " Detik");

                } else if (topic.equals(id_alat + "/active")) {
                    if (mqttMessage.toString().equals("1")) {
                        tvStatusAlat.setText("Aktif");
                        tvStatusAlat.setEnabled(true);
                        if (isCekAlatButton) {
                            alertSuccess.startDialog("Sukses", "Alat dalam keadaan aktif");
                        }
                        lnListOfSensor.setVisibility(View.VISIBLE);
                    } else {
                        tvStatusAlat.setText("Non Aktif");
                        tvStatusAlat.setEnabled(false);
                        tvStatusAlat.setHovered(false);
                        lnListOfSensor.setVisibility(View.GONE);
                    }


                    isCekAlatButton = false;
                    btnCekAlat.setEnabled(true);

                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    private void diskonekMQTT() {
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

    private boolean sendToServer(String topic, String datanya) {

        if (mqttHelper.mqttAndroidClient.isConnected()) {
            MqttMessage message = new MqttMessage();
            message.setPayload(datanya.getBytes());
            message.setQos(0);
            message.setRetained(false);
            mqttHelper.mqttAndroidClient.publish(topic, message);
            return true;
        } else {
            Log.d("Publish", "Enggak Bisa publish");
            return false;
        }
    }


    private void subscribeToTopic(final String topic) {
        if (mqttHelper != null) {
            if (mqttHelper.mqttAndroidClient != null) {
                try {
                    mqttHelper.mqttAndroidClient.subscribe(topic, 0, null, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            Log.d("FragmentHomeLog", topic + " Subscribed!");
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            Log.d("FragmentHomeLog", "Subscribed fail!");
                        }
                    });
                } catch (Exception e) {
                    Log.d("MQTT SUBSCRIBE", e.getMessage());
                }
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void showBottomSheetDialog(String before_rename, String label) {
        View view = getLayoutInflater().inflate(R.layout.sheet_rename, null);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Handle sliding
            }
        });

        EditText edRename;
        Button btnSimpan;

        edRename = view.findViewById(R.id.edRename);
        btnSimpan = view.findViewById(R.id.btnSimpan);

        edRename.setText(label);

        btnSimpan.setOnClickListener(view1 -> {
            sheetDialog.dismiss();
            prosesRename(before_rename, edRename.getText().toString());
        });


        sheetDialog = new BottomSheetDialog(Monitoring.this);
        sheetDialog.setContentView(view);
        Objects.requireNonNull(sheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });


    }

    @SuppressLint("WrongConstant")
    private void showBottomSheetDialogDelay(String before_delay) {
        View view = getLayoutInflater().inflate(R.layout.sheet_alarm, null);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Handle state changes
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Handle sliding
            }
        });

        EditText edDelay;
        Button btnSimpan;

        edDelay = view.findViewById(R.id.edDelay);
        btnSimpan = view.findViewById(R.id.btnSimpan);

        edDelay.setText(before_delay);

        btnSimpan.setOnClickListener(view1 -> {
            sheetDialog.dismiss();
            sendToServer(id_alat + "/delay", edDelay.getText().toString());
            tvDelay.setText(edDelay.getText().toString());
            tvDelayAlarm.setText(edDelay.getText().toString() + " Detik");
        });


        sheetDialog = new BottomSheetDialog(Monitoring.this);
        sheetDialog.setContentView(view);
        sheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });
    }


    private void prosesRename(String before_rename, String after_rename) {
        loadingDialog.startLoadingDialog();
        AndroidNetworking.post(BASE_URL + "users/rename")
                .setPriority(Priority.HIGH)
                .addBodyParameter("nohp", sessionLogin.getNohp(getApplicationContext()))
                .addBodyParameter("id_alat", id_alat)
                .addBodyParameter("before_rename", before_rename)
                .addBodyParameter("after_rename", after_rename)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        loadingDialog.dismissDialog();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("FRAGMENTHOMELOG", person.toString());
                                try {

                                    boolean status = person.getBoolean("status");
                                    if (status) {
                                        onRefresh();
                                    } else {
                                        alertError.startDialog("Gagal", person.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("FRAGMENTHOMELOG", e.getMessage());
                                    alertError.startDialog("Gagal", "Terjadi Kesalahan " + e.getMessage());
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(ANError error) {
                        loadingDialog.dismissDialog();
                        Log.d("FRAGMENTHOMELOG", Objects.requireNonNull(error.getMessage()));
                        alertError.startDialog("Gagal", "Terjadi Kesalahan " + error.getMessage());
                    }
                });
    }


    @OnClick(R.id.btnAlarm)
    void btnAlarm() {
        btnAlarm.setEnabled(false);
        ivButtonHidupkanAlarm.setImageResource(R.drawable.alarm_nonaktif);
        sendToServer(id_alat + "/alarm", "1");
    }

    @OnClick(R.id.btnCekAlat)
    void btnCekAlat() {

        btnCekAlat.setEnabled(false);
        isCekAlatButton = true;
        sendToServer(id_alat + "/active", "0");
    }
}