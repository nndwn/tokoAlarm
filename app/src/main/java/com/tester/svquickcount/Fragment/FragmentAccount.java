package com.tester.svquickcount.Fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.ImageViewer;
import com.tester.svquickcount.Login;
import com.tester.svquickcount.R;
import com.tester.svquickcount.Session.SessionLogin;
import com.tester.svquickcount.Session.SessionSetting;
import com.tester.svquickcount.UbahPassword;
import com.tester.svquickcount.UbahProfile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static com.tester.svquickcount.Config.Config.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentAccount extends Fragment {


    public FragmentAccount() {
        // Required empty public constructor
    }


    @BindView(R.id.tvNama)
    TextView tvNama;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.ivFotoProfile)
    RoundedImageView ivFotoProfile;
    SessionLogin sessionLogin;

    File fileGambar;
    SessionSetting sessionSetting;

    @BindView(R.id.tvKodeTps)
    TextView tvKodeTps;
    @BindView(R.id.tvNamaTps)
    TextView tvNamaTps;
    @BindView(R.id.tvProvinsi)
    TextView tvProvinsi;
    @BindView(R.id.tvKabupaten)
    TextView tvKabupaten;
    @BindView(R.id.tvKecamatan)
    TextView tvKecamatan;
    @BindView(R.id.tvKelurahan)
    TextView tvKelurahan;
    @BindView(R.id.tvAlamat)
    TextView tvAlamat;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this,view);
        sessionLogin = new SessionLogin();
        tvNama.setText(sessionLogin.getNama_pelanggan(getContext()));
        tvEmail.setText(sessionLogin.getEmail_pelanggan(getContext()));
        sessionSetting = new SessionSetting();
        Picasso.with(getContext()).load(BASE_URL+sessionLogin.getFoto_pelanggan(getContext()))
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .placeholder(R.drawable.user)
                .into(ivFotoProfile);
        try{
            JSONObject penugasan = new JSONObject(sessionLogin.getPenugasan(getActivity()));
            boolean status = penugasan.getBoolean("status");
            if(status){
                JSONObject data = penugasan.getJSONObject("data");
                tvKodeTps.setText("#"+data.getString("id_tps"));
                tvNamaTps.setText(data.getString("nama_tps"));
                tvProvinsi.setText(data.getString("provinsi"));
                tvKabupaten.setText(data.getString("kabupaten"));
                tvKecamatan.setText(data.getString("kecamatan"));
                tvKelurahan.setText(data.getString("kelurahan"));
                tvAlamat.setText(data.getString("alamat"));
            }else{

            }
        }catch (Exception e){

        }
        return view;
    }


    @OnClick(R.id.ivFotoProfile) void ivFotoProfile(){
        Intent intent = new Intent(getActivity(), ImageViewer.class);
        intent.putExtra("gambar",BASE_URL+sessionLogin.getFoto_pelanggan(getContext()));
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
    }

    @OnClick(R.id.btnLogout) void btnLogout(){
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

    @OnClick(R.id.btnAturProfile) void btnAturProfile(){
        Intent intent = new Intent(getActivity(), UbahProfile.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
    }

    @OnClick(R.id.changePicture) void changePicture(){
        ImagePicker.Companion.with(this).cropSquare().galleryOnly().start();
    }

    @Override
    public void onResume() {
        super.onResume();
        tvNama.setText(sessionLogin.getNama_pelanggan(getContext()));
        tvEmail.setText(sessionLogin.getEmail_pelanggan(getContext()));
    }


    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if(resultCode == RESULT_OK){
            final Uri selectedImage = imageReturnedIntent.getData();
            ivFotoProfile.setImageURI(selectedImage);
            fileGambar = new File(selectedImage.getPath());
            prosesUpload();
        }
    }




    private void prosesUpload(){
        SessionSetting sessionSetting = new SessionSetting();
        AndroidNetworking.upload(BASE_URL+"webservice/auth/ubahfoto")
                .addMultipartParameter("id",sessionLogin.getId_pelanggan(getContext()))
                .addMultipartFile("gambar",fileGambar)
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .setUploadProgressListener(new UploadProgressListener() {
                    @Override
                    public void onProgress(long bytesUploaded, long totalBytes) {
                        // do anything with progress
                    }
                })
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject person) {
                        try{
                            boolean status = person.getBoolean("status");
                            if(status){
                                sessionLogin.setFoto_pelanggan(person.getString("foto"),getActivity());
                            }
                        }catch (JSONException e){

                        }
                    }
                    @Override
                    public void onError(ANError error) {
                    }
                });
    }
}
