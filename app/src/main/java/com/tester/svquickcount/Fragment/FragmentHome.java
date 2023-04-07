package com.tester.svquickcount.Fragment;


import static android.view.View.GONE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.svquickcount.Adapter.PaslonAdapter;
import com.tester.svquickcount.Adapter.PromoAdapter;
import com.tester.svquickcount.Model.ListPaslon2;
import com.tester.svquickcount.Model.ListPromo;
import com.tester.svquickcount.Paslon;
import com.tester.svquickcount.Promo;
import com.tester.svquickcount.R;
import com.tester.svquickcount.Session.SessionLogin;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzahg.runOnUiThread;
import static com.tester.svquickcount.Config.Config.BASE_URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public FragmentHome() {
        // Required empty public constructor
    }



    List<String> colorName;

    private ArrayList<ListPaslon2> listPaslon;
    private PaslonAdapter adapter;
    @BindView(R.id.list_paslon)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;


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

    @BindView(R.id.shimmerHome)
    ShimmerFrameLayout shimmerHome;
    @BindView(R.id.lnHome)
    LinearLayout lnHome;

    SessionSetting sessionSetting;
    SessionLogin sessionLogin;
    @BindView(R.id.tvKodeTps)
    TextView tvKodeTps;
    @BindView(R.id.tvNamaTps)
    TextView tvNamaTps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        sessionLogin = new SessionLogin();
        sessionSetting = new SessionSetting();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                shimmerHome.setVisibility(View.VISIBLE);
                lnHome.setVisibility(GONE);
                getData();
            }
        });





        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
            }
        });

        listPaslon = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        rv.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    case MotionEvent.ACTION_SCROLL:
                        // Disallow ScrollView to intercept touch events.
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }

                // Handle RecyclerView touch events.
//                rv.onTouchEvent(e);
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // Handle RecyclerView touch events.
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                // Do nothing.
            }
        });


        //PROMO

        listPromo = new ArrayList<>();
        recyclerViewPromo.setHasFixedSize(true);
        recyclerViewPromo.setNestedScrollingEnabled(false);
        linearLayoutManagerPromo = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        recyclerViewPromo.setLayoutManager(linearLayoutManagerPromo);

        try{
            JSONObject penugasan = new JSONObject(sessionLogin.getPenugasan(getActivity()));
            boolean status = penugasan.getBoolean("status");
            if(status){
                JSONObject data = penugasan.getJSONObject("data");
                tvKodeTps.setText("#"+data.getString("id_tps"));
                tvNamaTps.setText(data.getString("nama_tps"));
            }else{

            }
        }catch (Exception e){

        }
        return  view;
    }

    @Override
    public void onRefresh() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                shimmerHome.setVisibility(View.VISIBLE);
                lnHome.setVisibility(View.VISIBLE);
                getData();
            }
        });
    }


    @OnClick(R.id.btnPromo) void btnPromo(){
        Intent intent = new Intent(getActivity(), Promo.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
    }

    @OnClick(R.id.btnPaslon) void btnPaslon(){
        Intent intent = new Intent(getActivity(), Paslon.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
    }

    private void getData(){
        SessionLogin sessionLogin = new SessionLogin();
        AndroidNetworking.post(BASE_URL+"webservice/paslon")
                .addBodyParameter("id_pelanggan",sessionLogin.getId_pelanggan(getContext()))
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
                                        shimmerHome.setVisibility(GONE);
                                        lnHome.setVisibility(View.VISIBLE);
                                        ShowPaslon(person.getJSONArray("paslon"));
//                                        ShowPromo(person.getJSONArray("promo"));
                                    }else{

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("FRAGMENTHOMELOG",e.getMessage());
                                    Toast.makeText(getActivity().getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("FRAGMENTHOMELOG",error.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }




    private void ShowPaslon(JSONArray jsonArray){

        listPaslon.clear();
        try {
            Type listType = new TypeToken<ArrayList<ListPaslon2>>(){}.getType();
            listPaslon = new Gson().fromJson(String.valueOf(jsonArray), listType);
            Log.d("TESTINGHITAPI",String.valueOf(jsonArray));
            recyclerViewadapter = new PaslonAdapter(listPaslon, getActivity(), getActivity(),R.layout.list_paslon);
            recyclerView.setAdapter(recyclerViewadapter);
            recyclerViewadapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        for (int j = 0; j < jsonArray.length(); j++) {
//
//            try {
//                JSONObject dataObject = jsonArray.getJSONObject(j);
//                ListPaslon memberData = new ListPaslon();
//                memberData.setIdproduk(dataObject.getString("id"));
////                memberData.setGambar(BASE_URL+dataObject.getString("gambar"));
//                memberData.setGambar("https://pbs.twimg.com/media/EtYNdtIVoAMrhxm.jpg");
//                memberData.setHarga(dataObject.getString("harga_jual"));
////                memberData.setNamaproduk(dataObject.getString("nama_produk"));
//                memberData.setNamaproduk("H. Ismet Roni, SH");
//                memberData.setSatuan(dataObject.getString("satuan"));
//                memberData.setStok(dataObject.getString("stok"));
//                memberData.setDeskripsi(dataObject.getString("deskripsi"));
//                memberData.setTerjual(dataObject.getString("terjual"));
//
//                listPaslon.add(memberData);
//                recyclerViewadapter = new ProdukAdapter(listPaslon, getActivity(), getActivity(),R.layout.list_produk);
//                recyclerView.setAdapter(recyclerViewadapter);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }


    }


    private void ShowPromo(JSONArray jsonArray){
        listPromo.clear();

        for (int j = 0; j < jsonArray.length(); j++) {

            try {
                JSONObject dataObject = jsonArray.getJSONObject(j);
                ListPromo memberDataPromo = new ListPromo();
                memberDataPromo.setId_promo(dataObject.getString("id_promo"));
                memberDataPromo.setJudul(dataObject.getString("judul"));
                memberDataPromo.setDeskripsi(dataObject.getString("deskripsi"));
                memberDataPromo.setKodepromo(dataObject.getString("kodepromo"));
                memberDataPromo.setTanggalberakhir(dataObject.getString("tanggalberakhir"));
                memberDataPromo.setGambar(BASE_URL+dataObject.getString("gambar"));

                listPromo.add(memberDataPromo);
                recyclerViewadapterPromo = new PromoAdapter(listPromo, getActivity(), getActivity(),R.layout.list_promo,"fragmenthome");
                recyclerViewPromo.setAdapter(recyclerViewadapterPromo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }


}
