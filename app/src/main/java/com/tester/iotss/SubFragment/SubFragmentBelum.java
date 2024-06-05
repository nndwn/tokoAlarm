package com.tester.iotss.SubFragment;


import android.annotation.SuppressLint;
import android.os.Bundle;

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
import android.widget.LinearLayout;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.iotss.Model.ListHistoryInput;
import com.tester.iotss.R;
import com.tester.iotss.Session.SessionLogin;
import com.tester.iotss.Session.SessionSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.tester.iotss.Config.Config.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubFragmentBelum extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public SubFragmentBelum() {
        // Required empty public constructor
    }


    private ArrayList<ListHistoryInput> listHistory;
    @BindView(R.id.list_history)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    SessionLogin sessionLogin;

    @BindView(R.id.lnData)
    LinearLayout lnData;

    @BindView(R.id.lnNoData)
    LinearLayout lnNoData;

    @BindView(R.id.shimmerHistory)
    ShimmerFrameLayout shimmerHistory;
    @SuppressLint("WrongConstant")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sub_fragment_belum, container, false);
        ButterKnife.bind(this,view);

        sessionLogin = new SessionLogin();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                shimmerHistory.setVisibility(View.VISIBLE);
                lnData.setVisibility(GONE);
                lnNoData.setVisibility(GONE);
//                getData();
            }
        });

        listHistory = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }

    @Override
    public void onRefresh() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                shimmerHistory.setVisibility(View.VISIBLE);
                lnData.setVisibility(GONE);
                lnNoData.setVisibility(GONE);
//                getData();
            }
        });
    }



//    private void getData(){
//        SessionSetting sessionSetting = new SessionSetting();
//        SessionLogin sessionLogin = new SessionLogin();
//        String kode_tps="";
//        String provinsi="";
//        String kabupaten="";
//        String kecamatan="";
//        String kelurahan="";
//        try{
//            JSONObject penugasan = new JSONObject(sessionLogin.getPenugasan(getContext()));
//            boolean status = penugasan.getBoolean("status");
//            if(status){
//                JSONObject data = penugasan.getJSONObject("data");
//                kode_tps = data.getString("id_tps");
//                provinsi = data.getString("kode_provinsi");
//                kabupaten = data.getString("kode_kabupaten");
//                kecamatan = data.getString("kode_kecamatan");
//                kelurahan = data.getString("kode_kelurahan");
//            }else{
//
//            }
//        }catch (Exception e){
//
//        }
//        AndroidNetworking.post(BASE_URL+"webservice/paslon/historybeluminput")
//                .addBodyParameter("kode_tps",kode_tps)
//                .addBodyParameter("provinsi",provinsi)
//                .addBodyParameter("kabupaten",kabupaten)
//                .addBodyParameter("kecamatan",kecamatan)
//                .addBodyParameter("kelurahan",kelurahan)
//                .setPriority(Priority.HIGH)
//                .build()
//                .getAsJSONObject(new JSONObjectRequestListener() {
//                    @Override
//                    public void onResponse(final JSONObject person) {
//                        swipeRefreshLayout.setRefreshing(false);
//                        try {
//                            shimmerHistory.setVisibility(View.GONE);
//                            ShowHistory(person.getJSONArray("paslon"),person);
//                        }catch (JSONException e){
//                            Log.d("SUBFRAGMENTBAYARLOG",e.getMessage());
//                        }
//
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//                });
//    }
//
//
//
//
//    private void ShowHistory(JSONArray jsonArray,JSONObject person){
//
//        listHistory.clear();
//        if(jsonArray.length()>0) {
//            lnData.setVisibility(View.VISIBLE);
//            lnNoData.setVisibility(GONE);
//            try {
//                Type listType = new TypeToken<ArrayList<ListHistoryInput>>(){}.getType();
//                listHistory = new Gson().fromJson(String.valueOf(jsonArray), listType);
//                Log.d("TESTINGHITAPI",String.valueOf(jsonArray));
//                recyclerViewadapter = new HistoryInputAdapter(listHistory, getActivity(), getActivity());
//                recyclerView.setAdapter(recyclerViewadapter);
//                recyclerViewadapter.notifyDataSetChanged();
//            }catch (Exception e){
//                Log.d("SUBFRAGMENTBAYARLOG",e.getMessage());
//            }
//        }else{
//            lnData.setVisibility(View.GONE);
//            lnNoData.setVisibility(View.VISIBLE);
//        }
//
//
//    }


    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

}
