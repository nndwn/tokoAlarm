package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.Dialog.AlertError;
import com.tester.svquickcount.Dialog.AlertSuccess;
import com.tester.svquickcount.Dialog.LoadingDialog;
import com.tester.svquickcount.Session.SessionLogin;
import com.tester.svquickcount.Session.SessionSetting;

import static com.tester.svquickcount.Config.Config.BASE_URL;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailPaslon extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.ivGambar)
    ImageView ivGambar;
    @BindView(R.id.tvNamaPaslon)
    TextView tvNamaPaslon;
    @BindView(R.id.tvJenisPaslon)
    TextView tvJenisPaslon;
    @BindView(R.id.tvSuaraSah)
    TextView tvSuaraSah;
    @BindView(R.id.tvSuaraTidakSah)
    TextView tvSuaraTidakSah;
    @BindView(R.id.tvVisi)
    TextView tvVisi;
    @BindView(R.id.tvMisi)
    TextView tvMisi;

    String id_paslon="";


    SessionLogin sessionLogin;
    SessionSetting sessionSetting;
    AlertError alertError;

    @BindView(R.id.containerSwipe)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.scrollView)
    ScrollView scrollView;


    LoadingDialog loadingDialog;
    AlertSuccess alertSuccess;


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
        setContentView(R.layout.activity_detail_paslon);
        ButterKnife.bind(this);

        sessionLogin = new SessionLogin();
        sessionSetting = new SessionSetting();
        alertError = new AlertError(DetailPaslon.this);
        loadingDialog = new LoadingDialog(DetailPaslon.this);
        alertSuccess = new AlertSuccess(DetailPaslon.this);
        if(getIntent().hasExtra("gambar")){
            id_paslon = getIntent().getStringExtra("id_produk");
            tvSuaraSah.setText(getIntent().getStringExtra("suarasah"));
            tvSuaraTidakSah.setText(getIntent().getStringExtra("tidaksah"));
            tvVisi.setText(getIntent().getStringExtra("visi"));
            tvMisi.setText(getIntent().getStringExtra("misi"));
            tvNamaPaslon.setText(getIntent().getStringExtra("nama_paslon"));
            tvJenisPaslon.setText("Calon "+getIntent().getStringExtra("jenis_paslon"));
            Picasso.with(DetailPaslon.this).load(BASE_URL+"assets/paslon/"+getIntent().getStringExtra("gambar"))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(ivGambar);
        }

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                getData();
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
        swipeRefreshLayout.setRefreshing(false);
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
                getData();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
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


    private void getData(){
        AndroidNetworking.post(BASE_URL+"webservice/paslon")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        swipeRefreshLayout.setRefreshing(false);

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }
}
