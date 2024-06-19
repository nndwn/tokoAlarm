package com.tester.iotss.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tester.iotss.ui.activity.Login;
import com.tester.iotss.R;
import com.tester.iotss.utils.sessions.SessionLogin;
import com.tester.iotss.utils.sessions.SessionSetting;
import com.tester.iotss.ui.activity.UbahPassword;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;

import static com.tester.iotss.data.config.Config.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccount extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public FragmentAccount() {}

    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvNoHp)
    TextView tvNoHp;
    @BindView(R.id.ivFotoProfile)
    RoundedImageView ivFotoProfile;
    SessionLogin sessionLogin;

    File fileGambar;
    SessionSetting sessionSetting;

    @BindView(R.id.tvTanggalDaftar)
    TextView tvTanggalDaftar;
    @BindView(R.id.tvStatus)
    TextView tvStatus;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this,view);
        sessionLogin = new SessionLogin();
        tvNama.setText(sessionLogin.getNama(getContext()));
        tvNoHp.setText(sessionLogin.getNohp(getContext()));
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                getData();
            }
        });





        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
            }
        });
        return view;
    }




    @OnClick(R.id.btnLogout) void btnLogout(){
        FirebaseApp.initializeApp(getActivity());
        FirebaseMessaging.getInstance().unsubscribeFromTopic(sessionLogin.getNohp(getContext()));
        sessionLogin.logout(getContext());
        Intent intent = new Intent(getActivity(), Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().finish();
    }


    @OnClick(R.id.lnUbahPassword) void lnUbahPassword(){
        Intent intent = new Intent(getActivity(), UbahPassword.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onRefresh() {
        getActivity().runOnUiThread(() -> getData());
    }

    private void getData(){
        SessionLogin sessionLogin = new SessionLogin();
        try {
            AndroidNetworking.post(BASE_URL+"users/auth")
                    .setPriority(Priority.HIGH)
                    .addBodyParameter("nohp", sessionLogin.getNohp(getContext()))
                    .addBodyParameter("password", sessionLogin.getPassword(getContext()))
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(final JSONObject person) {
                            swipeRefreshLayout.setRefreshing(false);
                            getActivity().runOnUiThread(() -> {
                                Log.d("FRAGMENTHOMELOG",person.toString());
                                try {

                                    boolean status = person.getBoolean("status");
                                    if(status){
                                        JSONObject data = person.getJSONObject("data");
                                        tvTanggalDaftar.setText(data.getString("tanggal_daftar"));
                                        tvStatus.setText(data.getString("status"));
                                    }else{
                                        Toast.makeText(getActivity().getApplicationContext(),"Terjadi Kesalahan "+person.getString("message"),Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("FRAGMENTHOMELOG",e.getMessage());
                                    Toast.makeText(getActivity().getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
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
        }catch (Exception e){
            Log.d("FRAGMENTACCOUNT", e.getMessage().toString());
        }
    }
}
