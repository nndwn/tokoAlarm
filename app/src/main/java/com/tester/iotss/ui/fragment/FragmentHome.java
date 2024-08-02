package com.tester.iotss.ui.fragment;


import static com.tester.iotss.data.config.Config.BASE_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.iotss.R;
import com.tester.iotss.domain.model.ListPromo;
import com.tester.iotss.ui.activity.BeliPaket;
import com.tester.iotss.ui.activity.History;
import com.tester.iotss.ui.activity.Setting;
import com.tester.iotss.ui.activity.Topup;
import com.tester.iotss.ui.activity.WebViewActivity;
import com.tester.iotss.ui.adapter.PromoAdapter;
import com.tester.iotss.utils.sessions.SessionLogin;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentHome extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public FragmentHome() {
        // Required empty public constructor
    }

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;


    @BindView(R.id.tvSaldo)
    TextView tvSaldo;

    @BindView(R.id.viewPager)
    ViewPager viewPager;

    SessionLogin sessionLogin;

    private ArrayList<ListPromo> listPromos;
    private PromoAdapter sliderAdapter;
    private Runnable runnable;
    private Handler handler;
    private int currentPage = 0;

    String URL_TUTORIAL = "";
    String URL_PESAN_ALARM = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        FirebaseApp.initializeApp(requireActivity());
        sessionLogin = new SessionLogin();
        FirebaseMessaging.getInstance().subscribeToTopic(sessionLogin.getNohp(requireContext()));

        viewPager = view.findViewById(R.id.viewPager);
        listPromos = new ArrayList<>();

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getData();
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (listPromos != null && !listPromos.isEmpty()) {
                    currentPage = (currentPage + 1) % listPromos.size();
                    viewPager.setCurrentItem(currentPage, true);
                }
                handler.postDelayed(this, 3000); // Change page every 3 seconds
            }
        };
        return view;
    }


    @Override
    public void onRefresh() {
        requireActivity().runOnUiThread(() -> {
            swipeRefreshLayout.setRefreshing(true);
            getData();
        });
    }

    private void LinkBanner( int i)
    {
        List<String> banner = Arrays.asList(
                "https://tokoalarm.com/promo/",
                "https://tokoalarm.com/informasi-update-apk/");
        String url = banner.get(i);
        Log.d("Banner", banner.get(i));
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("URL", url);
        startActivity(intent);

    }

    private void getData() {
        if (isAdded()) {
            SessionLogin sessionLogin = new SessionLogin();
            String userId = sessionLogin.getId(requireActivity());

            AndroidNetworking.post(BASE_URL + "users/datapelanggan")
                    .addBodyParameter("id_users", userId)
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(final JSONObject person) {
                            if (isAdded() && getView() != null) { // Check if the fragment is still attached
                                swipeRefreshLayout.setRefreshing(false);
                                listPromos.clear();
                                Log.d("FRAGMENTHOMELOG", person.toString());
                                try {
                                    boolean status = person.getBoolean("status");
                                    if (status) {
                                        tvSaldo.setText(person.getString("saldo"));
                                        Type listType = new TypeToken<ArrayList<ListPromo>>() {}.getType();
                                        listPromos = new Gson().fromJson(String.valueOf(person.getJSONArray("data")), listType);
                                        sliderAdapter = new PromoAdapter(listPromos, requireContext(), view -> {
                                            int position = (int) view.getTag();
                                            LinkBanner(position);
                                        });
                                        viewPager.setAdapter(sliderAdapter);
                                        sliderAdapter.notifyDataSetChanged();
                                        URL_TUTORIAL = person.getJSONObject("config").getString("link_tutorial");
                                        URL_PESAN_ALARM = person.getJSONObject("config").getString("link_pesan_alarm");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("FRAGMENTHOMELOG", e.getMessage());
                                }
                            } else {
                                Log.d("FRAGMENTHOMELOG", "Fragment not attached to context during response.");
                            }
                        }

                        @Override
                        public void onError(ANError error) {
                            if (isAdded() && getView() != null) { // Check if the fragment is still attached
                                swipeRefreshLayout.setRefreshing(false);
                                Log.d("FRAGMENTHOMELOG", error.getMessage());
                            } else {
                                Log.d("FRAGMENTHOMELOG", "Fragment not attached to context during error.");
                            }
                        }
                    });
        } else {
            // Handle the fragment not being attached
            Log.d("FRAGMENTHOMELOG", "Fragment not attached to context.");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable, 3000); // Start the automatic page change
        onRefresh();
    }
    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable); // Stop the automatic page change
    }

    @OnClick(R.id.btnTopup)
    void btnTopup() {
        Intent intent = new Intent(getActivity(), Topup.class);
        startActivity(intent);
    }

    @OnClick(R.id.lnPesanAlarm)
    void lnPesanAlarm() {
        // Example of launching WebViewActivity from another activity
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("URL", URL_PESAN_ALARM);
        startActivity(intent);
    }

    @OnClick(R.id.lnSetting)
    void lnSetting() {
        Intent intent = new Intent(getActivity(), Setting.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnHistory)
    void btnHistory() {
        Intent intent = new Intent(getActivity(), History.class);
        startActivity(intent);
    }

    @OnClick(R.id.lnBeliPaket)
    void lnBeliPaket() {
        Intent intent = new Intent(getActivity(), BeliPaket.class);
        startActivity(intent);
    }

    @OnClick(R.id.lnCaraPenggunaan)
    void lnCaraPenggunaan() {
        String urlTutorial = "https://tokoalarm.com/cara-penggunaan/";
        Intent intent = new Intent(getContext(), WebViewActivity.class);
        intent.putExtra("URL", urlTutorial);
        startActivity(intent);
    }
}

