package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.tester.svquickcount.Adapter.PromoAdapter;
import com.tester.svquickcount.Model.ListPromo;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.tester.svquickcount.Config.Config.BASE_URL;

public class Promo extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private ArrayList<ListPromo> listPromo;
    private PromoAdapter adapterPromo;
    @BindView(R.id.list_promo)
    RecyclerView recyclerViewPromo;
    LinearLayoutManager linearLayoutManagerPromo;
    RecyclerView.Adapter recyclerViewadapterPromo;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.shimmerProduk)
    ShimmerFrameLayout shimmerProduk;

    @BindView(R.id.lnData)
    LinearLayout lnData;
    @BindView(R.id.lnNoData)
    LinearLayout lnNoData;

    SessionSetting sessionSetting;

    String fromcheckout="0";
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promo);
        ButterKnife.bind(this);

        if(getIntent().hasExtra("fromcheckout")){
            fromcheckout = getIntent().getStringExtra("fromcheckout");
        }
        sessionSetting = new SessionSetting();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                shimmerProduk.setVisibility(View.VISIBLE);
                lnNoData.setVisibility(GONE);
                lnData.setVisibility(GONE);
                getData();
            }
        });

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
            }
        });

        listPromo = new ArrayList<>();
        recyclerViewPromo.setHasFixedSize(true);
        linearLayoutManagerPromo = new LinearLayoutManager(Promo.this,LinearLayoutManager.VERTICAL,false);
        recyclerViewPromo.setLayoutManager(linearLayoutManagerPromo);
    }


    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
    }

    @Override
    public void onRefresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                shimmerProduk.setVisibility(View.VISIBLE);
                lnNoData.setVisibility(GONE);
                lnData.setVisibility(GONE);
                getData();
            }
        });
    }



    private void getData(){
        AndroidNetworking.get(BASE_URL+"webservice/promo")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        swipeRefreshLayout.setRefreshing(false);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("FRAGMENTHOMELOG",person.toString());
                                try {

                                    boolean status = person.getBoolean("status");
                                    if(status){
                                        shimmerProduk.setVisibility(View.GONE);
                                        lnData.setVisibility(View.VISIBLE);
                                        ShowPromo(person.getJSONArray("data"));
                                    }else{

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("PROMOLOG",e.getMessage());
                                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("PROMOLOG",error.getMessage());
                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }


    private void ShowPromo(JSONArray jsonArray){
        listPromo.clear();

        if(jsonArray.length()>0) {
            for (int j = 0; j < jsonArray.length(); j++) {

                try {
                    JSONObject dataObject = jsonArray.getJSONObject(j);
                    ListPromo memberDataPromo = new ListPromo();
                    memberDataPromo.setId_promo(dataObject.getString("id_promo"));
                    memberDataPromo.setJudul(dataObject.getString("judul"));
                    memberDataPromo.setDeskripsi(dataObject.getString("deskripsi"));
                    memberDataPromo.setKodepromo(dataObject.getString("kodepromo"));
                    memberDataPromo.setTanggalberakhir(dataObject.getString("tanggalberakhir"));
                    memberDataPromo.setGambar(BASE_URL + dataObject.getString("gambar"));


                    if(fromcheckout.equals("1")){
                        listPromo.add(memberDataPromo);
                        recyclerViewadapterPromo = new PromoAdapter(listPromo, Promo.this, Promo.this, R.layout.list_promo_detail, "checkout");
                        recyclerViewPromo.setAdapter(recyclerViewadapterPromo);
                    }else {
                        listPromo.add(memberDataPromo);
                        recyclerViewadapterPromo = new PromoAdapter(listPromo, Promo.this, Promo.this, R.layout.list_promo_detail, "promo");
                        recyclerViewPromo.setAdapter(recyclerViewadapterPromo);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else{
            lnData.setVisibility(GONE);
            lnNoData.setVisibility(View.VISIBLE);
        }

    }


}
