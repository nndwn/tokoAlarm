package com.tester.iotss.Fragment;


import android.content.Intent;
import android.net.Uri;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.iotss.BeliPaket;
import com.tester.iotss.History;
import com.tester.iotss.Model.ListPromo;
import com.tester.iotss.PusatBantuan;
import com.tester.iotss.R;
import com.tester.iotss.Session.SessionLogin;
import com.tester.iotss.Setting;
import com.tester.iotss.Topup;
import com.tester.iotss.Tutorial;
import com.tester.iotss.Adapters.PromoAdapter;
import org.json.JSONException;
import org.json.JSONObject;
import static com.tester.iotss.Configs.Config.BASE_URL;
import java.lang.reflect.Type;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public FragmentHome() {
        // Required empty public constructor
    }

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.tvSaldo)
    TextView tvSaldo;

    SessionLogin sessionLogin;

    private ArrayList<ListPromo> listPromos;
    private PromoAdapter adapter;
    @BindView(R.id.list_promo)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    String URL_TUTORIAL = "";
    String URL_PESAN_ALARM = "";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        FirebaseApp.initializeApp(getActivity());
        sessionLogin = new SessionLogin();
        FirebaseMessaging.getInstance().subscribeToTopic(sessionLogin.getNohp(getContext()));
        sessionLogin = new SessionLogin();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getData();
        });

        listPromos = new ArrayList<>();
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

        return  view;
    }

    @Override
    public void onRefresh() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
            }
        });
    }


    private void getData(){
        SessionLogin sessionLogin = new SessionLogin();
        AndroidNetworking.post(BASE_URL+"users/datapelanggan")
                .addBodyParameter("id_users",sessionLogin.getId(getActivity()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        swipeRefreshLayout.setRefreshing(false);
                        listPromos.clear();
                        Log.d("FRAGMENTHOMELOG",person.toString());
                        try {
                            boolean status = person.getBoolean("status");
                            if(status){
                                tvSaldo.setText(person.getString("saldo"));
                                Type listType = new TypeToken<ArrayList<ListPromo>>(){}.getType();
                                listPromos = new Gson().fromJson(String.valueOf(person.getJSONArray("data")), listType);
                                Log.d("TESTINGHITAPI",String.valueOf(person.getJSONArray("data")));
                                recyclerViewadapter = new PromoAdapter(listPromos,getActivity(),getActivity());
                                recyclerView.setAdapter(recyclerViewadapter);
                                recyclerViewadapter.notifyDataSetChanged();
                                URL_TUTORIAL = person.getJSONObject("config").getString("link_tutorial");
                                URL_PESAN_ALARM = person.getJSONObject("config").getString("link_pesan_alarm");
                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("FRAGMENTHOMELOG",e.getMessage());
                            Toast.makeText(getActivity().getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("FRAGMENTHOMELOG",error.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @OnClick(R.id.lnPusatBantuan) void pusatBantuan(){
        Intent intent = new Intent(getActivity(), PusatBantuan.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnTopup) void btnTopup(){
        Intent intent = new Intent(getActivity(), Topup.class);
        startActivity(intent);
    }

    @OnClick(R.id.lnPesanAlarm) void lnPesanAlarm(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(URL_PESAN_ALARM));
        startActivity(intent);
//        Intent intent = new Intent(getActivity(), PesanAlarm.class);
//        startActivity(intent);
    }
    @OnClick(R.id.lnSetting) void lnSetting(){
        Intent intent = new Intent(getActivity(), Setting.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnHistory) void btnHistory(){
        Intent intent = new Intent(getActivity(), History.class);
        startActivity(intent);
    }

    @OnClick(R.id.lnBeliPaket) void lnBeliPaket(){
        Intent intent = new Intent(getActivity(), BeliPaket.class);
        startActivity(intent);
    }

    @OnClick(R.id.lnCaraPenggunaan) void lnCaraPenggunaan(){
        Intent intent = new Intent(getActivity(), Tutorial.class);
        startActivity(intent);
    }
}

