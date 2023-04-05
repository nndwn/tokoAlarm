package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.tester.svquickcount.Adapter.CartAdapter;
import com.tester.svquickcount.Dialog.AlertError;
import com.tester.svquickcount.Dialog.AlertSuccess;
import com.tester.svquickcount.Dialog.LoadingDialog;
import com.tester.svquickcount.Model.ListCart;
import com.tester.svquickcount.Session.SessionLogin;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static android.view.View.GONE;
import static com.tester.svquickcount.Config.Config.BASE_URL;

public class Cart extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private ArrayList<ListCart> listCart;
    private CartAdapter adapter;
    @BindView(R.id.list_produk)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    SessionSetting sessionSetting;
    SessionLogin sessionLogin;

    @BindView(R.id.lnData)
    LinearLayout lnData;

    @BindView(R.id.lnNoData)
    LinearLayout lnNoData;

    @BindView(R.id.shimmerProduk)
    ShimmerFrameLayout shimmerProduk;

    long total = 0;

    @BindView(R.id.tvTotal)
    TextView tvTotal;

    @BindView(R.id.lnCheckout)
    FrameLayout lnCheckout;

    @BindView(R.id.btnCheckout)
    LinearLayout btnCheckout;

    @BindView(R.id.edAlamatPengiriman)
    EditText edAlamatPengiriman;

    LoadingDialog loadingDialog;
    AlertError alertError;
    AlertSuccess alertSuccess;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this);
        btnCheckout.setEnabled(false);
        sessionSetting = new SessionSetting();
        sessionLogin = new SessionLogin();

        loadingDialog = new LoadingDialog(Cart.this);
        alertError = new AlertError(Cart.this);
        alertSuccess = new AlertSuccess(Cart.this);

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





        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
            }
        });

        listCart = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(Cart.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

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
                btnCheckout.setEnabled(false);
                getData();
            }
        });
    }


    private void getData(){
        SessionLogin sessionLogin = new SessionLogin();
        AndroidNetworking.post(BASE_URL+"webservice/cart")
                .addBodyParameter("id_pelanggan",sessionLogin.getId_pelanggan(getApplicationContext()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        swipeRefreshLayout.setRefreshing(false);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("FRAGMENTCARTLOG",person.toString());
                                try {

                                    boolean status = person.getBoolean("status");
                                    if(status){
                                        shimmerProduk.setVisibility(View.GONE);
                                        ShowProduk(person.getJSONArray("data"));
                                    }else{
                                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+person.getString("message"),Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("FRAGMENTCARTLOG",e.getMessage());
                                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("FRAGMENTCARTLOG",error.getMessage());
                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }



    public void cartPlus(String id_produk,int kuantitas){
        AndroidNetworking.post(BASE_URL+"webservice/cart/add")
                .addBodyParameter("id_produk",id_produk)
                .addBodyParameter("id_pelanggan",sessionLogin.getId_pelanggan(getApplicationContext()))
                .addBodyParameter("kuantitas", String.valueOf(kuantitas+1))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject person) {
                        onRefresh();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(),anError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void cartMin(String id_produk,int kuantitas){
        AndroidNetworking.post(BASE_URL+"webservice/cart/add")
                .addBodyParameter("id_produk",id_produk)
                .addBodyParameter("id_pelanggan",sessionLogin.getId_pelanggan(getApplicationContext()))
                .addBodyParameter("kuantitas", String.valueOf(kuantitas-1))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject person) {
                        onRefresh();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(getApplicationContext(),anError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }



    private void ShowProduk(JSONArray jsonArray){

        listCart.clear();
        total = 0;
        if(jsonArray.length()>0) {
            lnData.setVisibility(View.VISIBLE);
            lnNoData.setVisibility(GONE);
            for (int j = 0; j < jsonArray.length(); j++) {

                try {
                    JSONObject dataObject = jsonArray.getJSONObject(j);
                    ListCart memberData = new ListCart();
                    memberData.setIdcart(dataObject.getString("id"));
                    memberData.setGambar(BASE_URL + dataObject.getString("gambar"));
                    memberData.setHarga(dataObject.getString("harga_jual"));
                    memberData.setNamaproduk(dataObject.getString("nama_produk"));
                    memberData.setKuantitas(dataObject.getString("kuantitas"));
                    memberData.setIdpelanggan(dataObject.getString("id_pelanggan"));
                    memberData.setIdproduk(dataObject.getString("id_produk"));
                    memberData.setStok(dataObject.getString("stok"));
                    total += Long.valueOf(dataObject.getString("total"));
                    listCart.add(memberData);
                    recyclerViewadapter = new CartAdapter(listCart, Cart.this, Cart.this, R.layout.list_cart);
                    recyclerView.setAdapter(recyclerViewadapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            String strtotal = new DecimalFormat("##,##0").format(total);
            tvTotal.setText("Rp "+strtotal);
            lnCheckout.setVisibility(View.VISIBLE);
            btnCheckout.setEnabled(true);
        }else{
            lnData.setVisibility(View.GONE);
            lnNoData.setVisibility(View.VISIBLE);
            lnCheckout.setVisibility(GONE);
            btnCheckout.setEnabled(false);
        }


    }



    private Boolean validateAlamat(){
        String val=edAlamatPengiriman.getText().toString();
        if(val.isEmpty()){
            edAlamatPengiriman.setError("Harap Isi Alamat Anda");
            return false;
        }else{
            edAlamatPengiriman.setError(null);
            return true;
        }
    }


    @OnClick(R.id.btnCheckout) void btnCheckout(){
        if (!validateAlamat()) {
            return;
        } else {
            loadingDialog.startLoadingDialog();
            SessionSetting sessionSetting = new SessionSetting();
            AndroidNetworking.post(BASE_URL+"webservice/cart/checkout")
                    .addBodyParameter("id_pelanggan", sessionLogin.getId_pelanggan(getApplicationContext()))
                    .addBodyParameter("alamat_kirim", edAlamatPengiriman.getText().toString())
                    .addBodyParameter("nama_pelanggan",sessionLogin.getNama_pelanggan(getApplicationContext()))
                    .setPriority(Priority.HIGH)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject person) {
                            loadingDialog.dismissDialog();
                            try {
                                boolean isSuccess = person.getBoolean("status");
                                if (isSuccess) {
                                    JSONObject data = person.getJSONObject("data");
                                    Intent intent = new Intent(Cart.this,Checkout.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.putExtra("alamat_kirim",data.getString("alamat_kirim"));
                                    intent.putExtra("total_item",data.getString("total_item"));
                                    intent.putExtra("total_bayar",data.getString("total_bayar"));
                                    intent.putExtra("invoice",data.getString("invoice"));
                                    intent.putExtra("tanggal",data.getString("tanggal"));
                                    intent.putExtra("item",person.getJSONArray("item").toString());
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                                    finish();
                                } else {
                                    alertError.startDialog("Gagal", person.getString("message"));
                                }
                            } catch (JSONException e) {
                                alertError.startDialog("Gagal", e.getMessage());
                            }
                        }

                        @Override
                        public void onError(ANError anError) {
                            loadingDialog.dismissDialog();
                            alertError.startDialog("Login Gagal", anError.getMessage());
                        }
                    });
        }
    }
}
