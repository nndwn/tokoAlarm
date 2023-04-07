package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.Dialog.AlertError;
import com.tester.svquickcount.Dialog.AlertSuccess;
import com.tester.svquickcount.Dialog.LoadingDialog;
import com.tester.svquickcount.Session.SessionLogin;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import static android.view.View.GONE;
import static com.tester.svquickcount.Config.Config.BASE_URL;

public class DetailProduk extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.ivGambar)
    ImageView ivGambar;
    @BindView(R.id.tvNamaProduk)
    TextView tvNamaProduk;
    @BindView(R.id.tvHarga)
    TextView tvHarga;
    @BindView(R.id.tvStok)
    TextView tvStok;
    @BindView(R.id.tvTerjual)
    TextView tvTerjual;
    @BindView(R.id.tvDeskripsi)
    TextView tvDeskripsi;
    @BindView(R.id.tvMisi)
    TextView tvMisi;

    String id_produk="";
    @BindView(R.id.btnKeranjang)
    LinearLayout btnKeranjang;

    int kuantitas = 0;
    int stok = 0;
    @BindView(R.id.shimmerKuantitas)
    ShimmerFrameLayout shimmerKuantitas;

    @BindView(R.id.lnKuantitas)
    LinearLayout lnKuantitas;

    SessionLogin sessionLogin;
    SessionSetting sessionSetting;
    AlertError alertError;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.tvKuantitas)
    TextView tvKuantitas;

    LoadingDialog loadingDialog;
    AlertSuccess alertSuccess;

    @BindView(R.id.tvKeranjang)
    TextView tvKeranjang;

    @BindView(R.id.areachart)
    WebView areaChart;
    @BindView(R.id.piechart)
    WebView pieChart;
    @BindView(R.id.columnchart)
    WebView columnChart;

    int num1, num2, num3, num4, num5;
    String item1, item2, item3, item4, item5, title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_produk);
        ButterKnife.bind(this);
        btnKeranjang.setEnabled(false);

        sessionLogin = new SessionLogin();
        sessionSetting = new SessionSetting();
        alertError = new AlertError(DetailProduk.this);
        loadingDialog = new LoadingDialog(DetailProduk.this);
        alertSuccess = new AlertSuccess(DetailProduk.this);
        if(getIntent().hasExtra("gambar")){
            id_produk = getIntent().getStringExtra("id_produk");
//            tvTerjual.setText(getIntent().getStringExtra("terjual")+getIntent().getStringExtra("satuan"));
//            tvStok.setText(getIntent().getStringExtra("stok")+getIntent().getStringExtra("satuan"));
            tvTerjual.setText("23.000");
            tvStok.setText("1.000");
            stok = Integer.parseInt(getIntent().getStringExtra("stok"));
            tvDeskripsi.setText(getIntent().getStringExtra("deskripsi"));
            tvMisi.setText(getIntent().getStringExtra("deskripsi"));
            String harga = new DecimalFormat("##,##0").format(Long.parseLong(getIntent().getStringExtra("harga")));
            tvHarga.setText("Rp "+harga);
            tvNamaProduk.setText(getIntent().getStringExtra("namaproduk"));
            Picasso.with(DetailProduk.this).load(getIntent().getStringExtra("gambar"))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(ivGambar);
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getKuantitas();
            }
        });


        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                swipeRefreshLayout.setEnabled(scrollView.getScrollY() == 0);
            }
        });



        num1 = 34;
        num2 = 50;
        num3 = 25;
        num4 = 45;
        num5 = 55;
        item1 = "DKI";
        item2 = "Banten";
        item3 = "Jawa Barat";
        item4 = "Jawa Tengah";
        item5 = "Jawa Timur";
        title = "";

        areaChart.loadUrl("file:///android_asset/area_chart.html");
        areaChart.addJavascriptInterface(new WebAppInterface(), "Android");
        areaChart.getSettings().setJavaScriptEnabled(true);

        pieChart.loadUrl("file:///android_asset/pie_chart.html");
        pieChart.addJavascriptInterface(new WebAppInterface(), "Android");
        pieChart.getSettings().setJavaScriptEnabled(true);

        columnChart.loadUrl("file:///android_asset/column_chart.html");
        columnChart.addJavascriptInterface(new WebAppInterface(), "Android");
        columnChart.getSettings().setJavaScriptEnabled(true);
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
    }

    @OnClick(R.id.btnKeranjang) void btnKeranjang(){
        addCart();
    }


    @OnClick(R.id.lnCart) void lnCart(){
        Intent intent = new Intent(DetailProduk.this, Cart.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
    }

    private void ShowLoadingKreanjang(){
        swipeRefreshLayout.setRefreshing(true);
        shimmerKuantitas.setVisibility(View.VISIBLE);
        lnKuantitas.setVisibility(View.GONE);
    }

    private void HideLoadingKreanjang(){
        swipeRefreshLayout.setRefreshing(false);
        shimmerKuantitas.setVisibility(View.GONE);
        lnKuantitas.setVisibility(View.VISIBLE);
    }

    private void getKuantitas(){
        ShowLoadingKreanjang();
        AndroidNetworking.post(BASE_URL+"webservice/produk/getjumlahitem")
                .addBodyParameter("id_produk",id_produk)
                .addBodyParameter("id_pelanggan",sessionLogin.getId_pelanggan(getApplicationContext()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject person) {
                        HideLoadingKreanjang();
                        try {
                            boolean isSuccess = person.getBoolean("status");
                            if (isSuccess) {
                                kuantitas = Integer.parseInt(person.getString("kuantitas"));
                                if(kuantitas>0){
                                    btnKeranjang.setEnabled(true);
                                }
                                tvKuantitas.setText(String.valueOf(kuantitas));

                                if(!person.getJSONObject("cart").getString("total_item").equals("0")){
                                    tvKeranjang.setVisibility(View.VISIBLE);
                                    tvKeranjang.setText(person.getJSONObject("cart").getString("total_item"));
                                }else{
                                    tvKeranjang.setVisibility(GONE);
                                }

                            }else{
                                alertError.startDialog("Terjadi Kesalahan",person.getString("message"));
                            }
                        } catch (JSONException e) {
                            alertError.startDialog("Terjadi Kesalahan", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        HideLoadingKreanjang();
                        alertError.startDialog("Terjadi Kesalahan", anError.getMessage());
                    }
                });
    }



    private void addCart(){
        loadingDialog.startLoadingDialog();
        AndroidNetworking.post(BASE_URL+"webservice/cart/add")
                .addBodyParameter("id_produk",id_produk)
                .addBodyParameter("id_pelanggan",sessionLogin.getId_pelanggan(getApplicationContext()))
                .addBodyParameter("kuantitas", String.valueOf(kuantitas))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject person) {
                        loadingDialog.dismissDialog();
                        try {
                            boolean isSuccess = person.getBoolean("status");
                            if (isSuccess) {
                                Intent intent = new Intent(DetailProduk.this, Cart.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
                            }else{
                                alertError.startDialog("Terjadi Kesalahan",person.getString("message"));
                            }
                        } catch (JSONException e) {
                            alertError.startDialog("Terjadi Kesalahan", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        HideLoadingKreanjang();
                        alertError.startDialog("Terjadi Kesalahan", anError.getMessage());
                    }
                });
    }

    @Override
    public void onRefresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getKuantitas();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }


    @OnClick(R.id.btnPlus) void btnPlus(){

        if((kuantitas+1) > stok){
            btnKeranjang.setEnabled(false);
        }else{
            kuantitas++;
            tvKuantitas.setText(String.valueOf(kuantitas));
            btnKeranjang.setEnabled(true);
        }
    }

    @OnClick(R.id.btnMin) void btnMin(){
        if(kuantitas == 0){
            btnKeranjang.setEnabled(false);
        }else{
            kuantitas--;
            tvKuantitas.setText(String.valueOf(kuantitas));
            if(kuantitas == 0) {
                btnKeranjang.setEnabled(false);
            }else{
                btnKeranjang.setEnabled(true);
            }
        }
    }


    public class WebAppInterface {

        @JavascriptInterface
        public int getNum1() {
            return num1;
        }

        @JavascriptInterface
        public int getNum2() {
            return num2;
        }

        @JavascriptInterface
        public int getNum3() {
            return num3;
        }

        @JavascriptInterface
        public int getNum4() {
            return num4;
        }

        @JavascriptInterface
        public int getNum5() {
            return num5;
        }

        @JavascriptInterface
        public String getItem1() {
            return item1;
        }

        @JavascriptInterface
        public String getItem2() {
            return item2;
        }

        @JavascriptInterface
        public String getItem3() {
            return item3;
        }

        @JavascriptInterface
        public String getItem4() {
            return item4;
        }

        @JavascriptInterface
        public String getItem5() {
            return item5;
        }

        @JavascriptInterface
        public String getChartTitle() {
            return title;
        }
    }
}
