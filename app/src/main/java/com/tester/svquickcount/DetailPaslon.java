package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
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
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.Adapter.MonitoringAdapter;
import com.tester.svquickcount.Adapter.PaslonAdapter;
import com.tester.svquickcount.Dialog.AlertError;
import com.tester.svquickcount.Dialog.AlertSuccess;
import com.tester.svquickcount.Dialog.LoadingDialog;
import com.tester.svquickcount.Model.ListMonitoring;
import com.tester.svquickcount.Model.ListPaslon;
import com.tester.svquickcount.Session.SessionLogin;
import com.tester.svquickcount.Session.SessionSetting;

import static com.tester.svquickcount.Config.Config.BASE_URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

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


    @BindView(R.id.piechart)
    WebView pieChart;
    @BindView(R.id.columnchart)
    WebView columnChart;

    String jsonArray;

    @BindView(R.id.shimmerPieChart)
    ShimmerFrameLayout shimmerPieChart;
    @BindView(R.id.shimmerColumnChart)
    ShimmerFrameLayout shimmerColumnChart;
    @BindView(R.id.shimmerTable)
    ShimmerFrameLayout shimmerTable;

    @BindView(R.id.lnPieChart)
    LinearLayout lnPieChart;
    @BindView(R.id.lnColumnChart)
    LinearLayout lnColumnChart;
    @BindView(R.id.lnTable)
    LinearLayout lnTable;
    @BindView(R.id.tvTotalSah)
    TextView tvTotalSah;
    @BindView(R.id.tvTotalTidakSah)
    TextView tvTotalTidakSah;

    private ArrayList<ListMonitoring> listMonitorings;
    private PaslonAdapter adapter;
    @BindView(R.id.list_monitoring)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;

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

        listMonitorings = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(DetailPaslon.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);

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
                shimmerPieChart.setVisibility(View.VISIBLE);
                shimmerColumnChart.setVisibility(View.VISIBLE);
                shimmerTable.setVisibility(View.VISIBLE);
                lnPieChart.setVisibility(View.GONE);
                lnColumnChart.setVisibility(View.GONE);
                lnTable.setVisibility(View.GONE);
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
        public String getJSOnArray() {
            return jsonArray;
        }
    }


    private void getData(){
        AndroidNetworking.post(BASE_URL+"webservice/paslon/detail")
                .addBodyParameter("nama_paslon",getIntent().getStringExtra("nama_paslon"))
                .addBodyParameter("tingkatan",getIntent().getStringExtra("tingkatan"))
                .addBodyParameter("nama_dapil",getIntent().getStringExtra("nama_dapil"))
                .addBodyParameter("multi_dapil",getIntent().getStringExtra("multi_dapil"))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        Log.d("DETAILPASLONLOG",person.toString());
                        swipeRefreshLayout.setRefreshing(false);
                        shimmerPieChart.setVisibility(View.GONE);
                        shimmerColumnChart.setVisibility(View.GONE);
                        shimmerTable.setVisibility(View.GONE);
                        lnPieChart.setVisibility(View.VISIBLE);
                        lnColumnChart.setVisibility(View.VISIBLE);
                        lnTable.setVisibility(View.VISIBLE);
                        try {
                            if (person.getBoolean("status")) {
                                jsonArray = person.getJSONArray("data").toString();
                                pieChart.loadUrl("file:///android_asset/pie_chart.html");
                                pieChart.addJavascriptInterface(new WebAppInterface(), "Android");
                                pieChart.getSettings().setJavaScriptEnabled(true);
                                columnChart.loadUrl("file:///android_asset/column_chart.html");
                                columnChart.addJavascriptInterface(new WebAppInterface(), "Android");
                                columnChart.getSettings().setJavaScriptEnabled(true);
                                pieChart.setWebChromeClient(new WebChromeClient() {
                                    @Override
                                    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                                        android.util.Log.d("WebViewTESTPieChart", consoleMessage.message());
                                        return true;
                                    }
                                });
                                columnChart.setWebChromeClient(new WebChromeClient(){
                                    @Override
                                    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                                        android.util.Log.d("WebViewTESTColumnChart", consoleMessage.message());
                                        return true;
                                    }
                                });

                                tvTotalSah.setText(person.getString("total_sah"));
                                tvTotalTidakSah.setText(person.getString("total_tidaksah"));


                                listMonitorings.clear();

                                try {
                                    Type listType = new TypeToken<ArrayList<ListMonitoring>>(){}.getType();
                                    listMonitorings = new Gson().fromJson(String.valueOf(person.getJSONArray("data")), listType);
                                    Log.d("TESTINGHITAPI",String.valueOf(person.getJSONArray("data")));
                                    recyclerViewadapter = new MonitoringAdapter(listMonitorings, DetailPaslon.this, DetailPaslon.this,R.layout.list_monitoring);
                                    recyclerView.setAdapter(recyclerViewadapter);
                                    recyclerViewadapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }catch (Exception e){

                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        swipeRefreshLayout.setRefreshing(false);
                        Log.d("DETAILPASLONLOG",error.toString());
                    }
                });
    }
}
