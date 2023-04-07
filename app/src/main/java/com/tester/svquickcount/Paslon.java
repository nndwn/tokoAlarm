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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.svquickcount.Adapter.PaslonAdapter;
import com.tester.svquickcount.Model.ListPaslon2;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.tester.svquickcount.Config.Config.BASE_URL;

public class Paslon extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private ArrayList<ListPaslon2> listPaslon;
    private PaslonAdapter adapter;
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
        setContentView(R.layout.activity_paslon);
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






        listPaslon = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(Paslon.this, 2));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
//                if (!isLoading) {
//                    if (gridLayoutManager != null && gridLayoutManager.findLastCompletelyVisibleItemPosition() == listPaslon.size() - 1) {
//                        //bottom of list!
//                        if(next<=total_page) {
//                            loadMore();
//                            isLoading = true;
//                        }
//                    }
//                }
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
        AndroidNetworking.post(BASE_URL+"webservice/paslon")
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
                                        ShowPaslon(person.getJSONArray("paslon"));
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



    private void ShowPaslon(JSONArray jsonArray){

        listPaslon.clear();

        try {
            Type listType = new TypeToken<ArrayList<ListPaslon2>>(){}.getType();
            listPaslon = new Gson().fromJson(String.valueOf(jsonArray), listType);
            Log.d("TESTINGHITAPI",String.valueOf(jsonArray));
            recyclerViewadapter = new PaslonAdapter(listPaslon, Paslon.this, Paslon.this,R.layout.list_paslon_large);
            recyclerView.setAdapter(recyclerViewadapter);
            recyclerViewadapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(jsonArray.length()>0){
            lnData.setVisibility(View.VISIBLE);
            lnNoData.setVisibility(View.GONE);
        }else{
            lnData.setVisibility(GONE);
            lnNoData.setVisibility(View.VISIBLE);
        }


    }




}
