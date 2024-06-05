package com.tester.iotss;

import static com.tester.iotss.Config.Config.BASE_URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.iotss.Dialog.AlertError;
import com.tester.iotss.Dialog.AlertSuccess;
import com.tester.iotss.Dialog.LoadingDialog;
import com.tester.iotss.Model.PaketList;
import com.tester.iotss.Session.SessionLogin;
import com.tester.iotss.adapter.PaketAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeliPaket extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, PaketAdapter.OnItemClickListener {

    private List<PaketList> paketLists;
    @BindView(R.id.list_paket)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog sheetDialog;
    @BindView(R.id.bottom_sheet)
    View bottom_sheet;

    AlertError alertError;
    AlertSuccess alertSuccess;
    LoadingDialog loadingDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beli_paket);
        ButterKnife.bind(this);
        alertError = new AlertError(BeliPaket.this);
        alertSuccess = new AlertSuccess(BeliPaket.this);
        loadingDialog = new LoadingDialog(BeliPaket.this);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });

        paketLists = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(BeliPaket.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }



    private void getData(){
        SessionLogin sessionLogin = new SessionLogin();
        AndroidNetworking.get(BASE_URL+"users/paket")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        swipeRefreshLayout.setRefreshing(false);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("HISTORYLOG",person.toString());
                                try {

                                    boolean status = person.getBoolean("status");
                                    if(status){
                                        Type listType = new TypeToken<ArrayList<PaketList>>(){}.getType();
                                        paketLists = new Gson().fromJson(String.valueOf(person.getJSONArray("data")), listType);
                                        Log.d("TESTINGHITAPI",String.valueOf(person.getJSONArray("data")));
                                        recyclerViewadapter = new PaketAdapter(BeliPaket.this,paketLists,BeliPaket.this,BeliPaket.this);
                                        recyclerView.setAdapter(recyclerViewadapter);
                                        recyclerViewadapter.notifyDataSetChanged();
                                    }else{

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("HISTORYLOG",e.getMessage());
                                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("HISTORYLOG",error.getMessage());
                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onItemClick(int position) {
        showBottomSheetDialog(position);
    }


    private void showBottomSheetDialog(int position) {
        View view = getLayoutInflater().inflate(R.layout.sheet_pembayaran, null);
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // Handle state changes
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // Handle sliding
            }
        });

        TextView tvNamaPaket,tvDurasi,tvHarga;
        EditText edIdAlat;
        Button btnSimpan;

        tvNamaPaket = (TextView) view.findViewById(R.id.tvNamaPaket);
        tvDurasi = (TextView) view.findViewById(R.id.tvDurasi);
        tvHarga = (TextView) view.findViewById(R.id.tvHarga);
        btnSimpan = (Button) view.findViewById(R.id.btnSimpan);
        edIdAlat = (EditText) view.findViewById(R.id.edIdAlat);
        tvNamaPaket.setText(paketLists.get(position).getPeriode());
        tvDurasi.setText(paketLists.get(position).getDayConvertion()+" Hari");
        tvHarga.setText(paketLists.get(position).getBiayaRupiah());


        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertTransaksi(position,edIdAlat.getText().toString());
            }
        });
        sheetDialog = new BottomSheetDialog(BeliPaket.this);
        sheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });
    }



    private void insertTransaksi(int position,String id_alat){
        loadingDialog.startLoadingDialog();
        SessionLogin sessionLogin = new SessionLogin();
        AndroidNetworking.post(BASE_URL+"users/belipaket")
                .addBodyParameter("nohp", sessionLogin.getNohp(getApplicationContext()))
                .addBodyParameter("periode", paketLists.get(position).getPeriode())
                .addBodyParameter("day_convertion", paketLists.get(position).getDayConvertion())
                .addBodyParameter("cutoff_day", paketLists.get(position).getCutoffDay())
                .addBodyParameter("biaya", paketLists.get(position).getBiaya())
                .addBodyParameter("id_users",sessionLogin.getId(getApplicationContext()))
                .addBodyParameter("id_alat",id_alat)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject person) {
                        loadingDialog.dismissDialog();
                        Log.d("BELIPAKETLOG",person.toString());
                        try {
                            boolean isSuccess = person.getBoolean("status");
                            if (isSuccess) {
                                alertSuccess.startDialog("Berhasil",person.getString("message"));
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(BeliPaket.this, Home.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                },2000);
                            } else {
                                alertError.startDialog("Gagal", person.getString("message"));
                            }
                        } catch (JSONException e) {
                            alertError.startDialog("Gagal", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        loadingDialog.dismissDialog();
                        alertError.startDialog("Gagal", anError.getMessage());
                    }
                });
    }
}