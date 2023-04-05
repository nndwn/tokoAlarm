package com.tester.svquickcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
import com.github.ybq.android.spinkit.SpinKitView;
import com.tester.svquickcount.Adapter.ProdukAdapter;
import com.tester.svquickcount.Model.ListProduk;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.tester.svquickcount.Config.Config.BASE_URL;

public class Produk extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private ArrayList<ListProduk> listProduk;
    private ProdukAdapter adapter;
    @BindView(R.id.list_produk)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;


    SessionSetting sessionSetting;

    @BindView(R.id.lnData)
    LinearLayout lnData;

    @BindView(R.id.lnNoData)
    LinearLayout lnNoData;

    @BindView(R.id.shimmerProduk)
    ShimmerFrameLayout shimmerProduk;

    @BindView(R.id.edSearch)
    SearchView edSearch;

    int next;
    int total_page;

    boolean isLoading=false;

    @BindView(R.id.ivloadMore)
    SpinKitView ivloadMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produk);
        ButterKnife.bind(this);
        sessionSetting = new SessionSetting();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                shimmerProduk.setVisibility(View.VISIBLE);
                lnData.setVisibility(GONE);
                lnNoData.setVisibility(GONE);
                getData();
            }
        });






        listProduk = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(Produk.this, 2));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
                if (!isLoading) {
                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == listProduk.size() - 1) {
                        //bottom of list!
                        if(next<=total_page) {
                            loadMore();
                            isLoading = true;
                        }
                    }
                }
            }
        });
        edSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onRefresh();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.equals("")){
                    onRefresh();
                }
                return false;
            }
        });

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
                lnData.setVisibility(GONE);
                lnNoData.setVisibility(GONE);
                getData();
            }
        });
    }



    private void getData(){
        AndroidNetworking.post(BASE_URL+"webservice/produk")
                .addBodyParameter("search",edSearch.getQuery().toString())
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
                                        next = person.getInt("next");
                                        total_page = person.getInt("total_page");
                                        ShowProduk(person.getJSONArray("data"));
                                    }else{

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("FRAGMENTHOMELOG",e.getMessage());
                                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("FRAGMENTHOMELOG",error.getMessage());
                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }



    private void ShowProduk(JSONArray jsonArray){

        listProduk.clear();

        for (int j = 0; j < jsonArray.length(); j++) {

            try {
                JSONObject dataObject = jsonArray.getJSONObject(j);
                ListProduk memberData = new ListProduk();
                memberData.setIdproduk(dataObject.getString("id"));
                memberData.setGambar("https://pbs.twimg.com/media/EtYNdtIVoAMrhxm.jpg");
                memberData.setHarga(dataObject.getString("harga_jual"));
                memberData.setNamaproduk("H. Ismet Roni, SH");
                memberData.setSatuan(dataObject.getString("satuan"));
                memberData.setStok(dataObject.getString("stok"));
                memberData.setDeskripsi(dataObject.getString("deskripsi"));
                memberData.setTerjual(dataObject.getString("terjual"));

                listProduk.add(memberData);
                recyclerViewadapter = new ProdukAdapter(listProduk, Produk.this, Produk.this,R.layout.list_produk_large);
                recyclerView.setAdapter(recyclerViewadapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if(jsonArray.length()>0){
            lnData.setVisibility(View.VISIBLE);
            lnNoData.setVisibility(View.GONE);
        }else{
            lnData.setVisibility(GONE);
            lnNoData.setVisibility(View.VISIBLE);
        }


    }



    private void loadMore(){


        ivloadMore.setVisibility(View.VISIBLE);

        AndroidNetworking.post(BASE_URL+"webservice/produk")
                .addBodyParameter("search",edSearch.getQuery().toString())
                .addBodyParameter("page", String.valueOf(next))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        isLoading=false;
                        ivloadMore.setVisibility(GONE);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("FRAGMENTHOMELOG",person.toString());
                                try {

                                    boolean status = person.getBoolean("status");
                                    if(status){
                                        next = person.getInt("next");
                                        JSONArray jsonArray = person.getJSONArray("data");
                                        for (int j = 0; j < jsonArray.length(); j++) {

                                            try {
                                                JSONObject dataObject = jsonArray.getJSONObject(j);
                                                ListProduk memberData = new ListProduk();
                                                memberData.setIdproduk(dataObject.getString("id"));
                                                memberData.setGambar(BASE_URL+dataObject.getString("gambar"));
                                                memberData.setHarga(dataObject.getString("harga_jual"));
                                                memberData.setNamaproduk(dataObject.getString("nama_produk"));
                                                memberData.setSatuan(dataObject.getString("satuan"));
                                                memberData.setStok(dataObject.getString("stok"));
                                                memberData.setDeskripsi(dataObject.getString("deskripsi"));
                                                memberData.setTerjual(dataObject.getString("terjual"));

                                                listProduk.add(memberData);
                                                recyclerViewadapter = new ProdukAdapter(listProduk, Produk.this, Produk.this,R.layout.list_produk_large);
                                                recyclerView.setAdapter(recyclerViewadapter);
                                                recyclerViewadapter.notifyDataSetChanged();
                                                isLoading = false;

                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }else{

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("FRAGMENTHOMELOG",e.getMessage());
                                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }

                    @Override
                    public void onError(ANError error) {
                        isLoading=false;
                        ivloadMore.setVisibility(GONE);
                        Log.d("FRAGMENTHOMELOG",error.getMessage());
                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}
