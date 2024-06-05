package com.tester.iotss;

import static com.tester.iotss.Config.Config.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.iotss.Model.HistoryList;
import com.tester.iotss.Session.SessionLogin;
import com.tester.iotss.adapter.HistoryAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class History extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener {
    private List<HistoryList> historyLists;
    private HistoryAdapter adapter;
    @BindView(R.id.list_history)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });

        historyLists = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(History.this,LinearLayoutManager.VERTICAL,false);
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
        AndroidNetworking.post(BASE_URL+"users/historisaldo")
                .addBodyParameter("id_users",sessionLogin.getId(getApplicationContext()))
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
                                        Type listType = new TypeToken<ArrayList<HistoryList>>(){}.getType();
                                        historyLists = new Gson().fromJson(String.valueOf(person.getJSONArray("data")), listType);
                                        Log.d("TESTINGHITAPI",String.valueOf(person.getJSONArray("data")));
                                        recyclerViewadapter = new HistoryAdapter(History.this,historyLists,History.this);
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
}