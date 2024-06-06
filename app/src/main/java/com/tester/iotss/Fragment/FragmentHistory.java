package com.tester.iotss.Fragment;


import static android.view.View.GONE;

import static com.tester.iotss.Configs.Config.BASE_URL;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.iotss.Dialog.AlertError;
import com.tester.iotss.Dialog.AlertSuccess;
import com.tester.iotss.Dialog.LoadingDialog;
import com.tester.iotss.Model.AlatList;
import com.tester.iotss.R;
import com.tester.iotss.Session.SessionLogin;
import com.tester.iotss.Adapters.AlatAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHistory extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AlatAdapter.PerpanjangClickListener {
    public FragmentHistory() {
        // Required empty public constructor
    }

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.shimmerLog)
    ShimmerFrameLayout shimmerLog;
    @BindView(R.id.lnData)
    LinearLayout lnData;

    SessionLogin sessionLogin = new SessionLogin();

    private List<AlatList> alatLists;
    private AlatAdapter adapter;
    @BindView(R.id.alat_list)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog sheetDialog;
    @BindView(R.id.bottom_sheet)
    View bottom_sheet;

    LoadingDialog loadingDialog;
    AlertSuccess alertSuccess;
    AlertError alertError;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this,view);
        loadingDialog = new LoadingDialog(getActivity());
        alertSuccess = new AlertSuccess(getActivity());
        alertError = new AlertError(getActivity());
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        swipeRefreshLayout.setOnRefreshListener(this);
        alatLists = new ArrayList<>();
        recyclerViewadapter = new AlatAdapter(getActivity(),alatLists, getActivity(),this);
        recyclerView.setAdapter(recyclerViewadapter);
        recyclerView.setHasFixedSize(false);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            shimmerLog.setVisibility(View.VISIBLE);
            lnData.setVisibility(GONE);
            getData();
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
            }
        });
        return  view;
    }


    @Override
    public void onRefresh() {
        getActivity().runOnUiThread(() -> {
            swipeRefreshLayout.setRefreshing(true);
            shimmerLog.setVisibility(View.VISIBLE);
            lnData.setVisibility(GONE);
            getData();
        });
    }


    private void getData(){
        AndroidNetworking.post(BASE_URL+"users/getalat")
                .setPriority(Priority.HIGH)
                .addBodyParameter("nohp", sessionLogin.getNohp(getContext()))
                .addBodyParameter("status","Aktif")
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("FRAGMENTHISTORYLOG",person.toString());
                        try {

                            boolean status = person.getBoolean("status");
                            if(status) {
                                shimmerLog.setVisibility(GONE);
                                lnData.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = person.getJSONArray("data");
                                Type listType = new TypeToken<ArrayList<AlatList>>(){}.getType();
                                alatLists = new Gson().fromJson(String.valueOf(jsonArray), listType);
                                Log.d("TESTINGHITAPI",String.valueOf(jsonArray));
                                recyclerViewadapter = new AlatAdapter(getActivity(),alatLists, getActivity(),FragmentHistory.this::onPerpanjangClick);
                                recyclerView.setAdapter(recyclerViewadapter);
                                recyclerViewadapter.notifyDataSetChanged();
                            }else{
                                Toast.makeText(getActivity().getApplicationContext(),person.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("FRAGMENTHISTORYLOG",e.getMessage());
                            Toast.makeText(getActivity().getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("FRAGMENTHISTORYLOG",error.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onPerpanjangClick(int position) {
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
        Button btnSimpan;
        LinearLayout lnIdAlat;

        lnIdAlat = (LinearLayout) view.findViewById(R.id.lnIdAlat);
        tvNamaPaket = (TextView) view.findViewById(R.id.tvNamaPaket);
        tvDurasi = (TextView) view.findViewById(R.id.tvDurasi);
        tvHarga = (TextView) view.findViewById(R.id.tvHarga);
        btnSimpan = (Button) view.findViewById(R.id.btnSimpan);
        tvNamaPaket.setText(alatLists.get(position).getPeriode());
        tvDurasi.setText(alatLists.get(position).getDayConvertion()+" Hari");
        tvHarga.setText(alatLists.get(position).getBiayaRupiah());
        lnIdAlat.setVisibility(GONE);

        btnSimpan.setText("Perpanjang Sekarang");
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perpanjangPaket(position);
            }
        });
        sheetDialog = new BottomSheetDialog(getActivity());
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


    private void perpanjangPaket(int position){
        loadingDialog.startLoadingDialog();
        SessionLogin sessionLogin = new SessionLogin();
        AndroidNetworking.post(BASE_URL+"users/perpanjang")
                .addBodyParameter("nomor_paket", alatLists.get(position).getNomorPaket())
                .addBodyParameter("id_users",sessionLogin.getId(getActivity()))
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
                                sheetDialog.dismiss();
                                onRefresh();
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

