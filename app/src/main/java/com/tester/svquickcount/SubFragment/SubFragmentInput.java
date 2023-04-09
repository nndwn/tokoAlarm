package com.tester.svquickcount.SubFragment;


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
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.tester.svquickcount.Adapter.HistoryAdapter;
import com.tester.svquickcount.Model.ListHistory;
import com.tester.svquickcount.R;
import com.tester.svquickcount.Session.SessionLogin;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.view.View.GONE;
import static com.tester.svquickcount.Config.Config.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubFragmentInput extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public SubFragmentInput() {
        // Required empty public constructor
    }

    private ArrayList<ListHistory> listHistory;
    private HistoryAdapter adapter;
    @BindView(R.id.list_history)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

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
        View view =  inflater.inflate(R.layout.fragment_sub_fragment_input, container, false);
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
                getData();
            }
        });


        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
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
                getData();
            }
        });
    }



    private void getData(){
        SessionSetting sessionSetting = new SessionSetting();
        AndroidNetworking.post(BASE_URL+"webservice/transaksi/gettransaksiaktif")
                .addBodyParameter("id_pelanggan",sessionLogin.getId_pelanggan(getContext()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        swipeRefreshLayout.setRefreshing(false);
                        try {
                            shimmerHistory.setVisibility(View.GONE);
                            ShowHistory(person.getJSONArray("data"),person);
                        }catch (JSONException e){
                            Log.d("SUBFRAGMENTBAYARLOG",e.getMessage());
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
    }




    private void ShowHistory(JSONArray jsonArray,JSONObject person){

        listHistory.clear();
        if(jsonArray.length()>0) {
            lnData.setVisibility(View.VISIBLE);
            lnNoData.setVisibility(GONE);
            try {
                for (int j = 0; j < jsonArray.length(); j++) {
                    ListHistory memberData = new ListHistory();
                    JSONObject dataObject = jsonArray.getJSONObject(j);
                    memberData.setId_transaksi(dataObject.getString("noindex"));
                    memberData.setInvoice(dataObject.getString("invoice"));
                    memberData.setTotal_bayar(dataObject.getString("total_bayar"));
                    memberData.setTotal_item(dataObject.getString("total_item"));
                    memberData.setBiaya_kirim(dataObject.getString("biaya_kirim"));
                    memberData.setAlamat_kirim(dataObject.getString("alamat_kirim"));
                    memberData.setPengirim(dataObject.getString("pengirim"));
                    memberData.setStatus_bayar(dataObject.getString("status_bayar"));
                    memberData.setTanggal(dataObject.getString("tanggal"));
                    memberData.setId_payment(dataObject.getString("id_payment"));
                    memberData.setNama_payment(dataObject.getString("nama_payment"));
                    memberData.setNorek(dataObject.getString("norek"));
                    memberData.setIscod(dataObject.getString("is_cod"));
                    memberData.setKode_promo(dataObject.getString("kodepromo"));
                    memberData.setPotongan(dataObject.getString("potongan"));
                    memberData.setStatus_kirim(dataObject.getString("status_kirim"));
                    memberData.setStatus_transaksi(dataObject.getString("status_transaksi"));
                    memberData.setGrandtotal(dataObject.getString("grandtotal"));
                    memberData.setGambar_payment(dataObject.getString("gambar_payment"));
                    memberData.setPemilik_rekening(dataObject.getString("pemilik_rekening"));
                    if(person.has("item")){
                        if(person.getJSONObject("item").has(dataObject.getString("noindex"))){
                            memberData.setDataitem(person.getJSONObject("item").getJSONArray(dataObject.getString("noindex")));
                        }
                    }

                    listHistory.add(memberData);
                    recyclerViewadapter = new HistoryAdapter(listHistory, getActivity(), getActivity(),"aktif");
                    recyclerView.setAdapter(recyclerViewadapter);
                }
            }catch (Exception e){
                Log.d("SUBFRAGMENTBAYARLOG",e.getMessage());
            }
        }else{
            lnData.setVisibility(View.GONE);
            lnNoData.setVisibility(View.VISIBLE);
        }


    }


    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }
}
