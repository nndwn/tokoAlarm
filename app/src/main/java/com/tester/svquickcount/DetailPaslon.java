package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.Adapter.MonitoringAdapter;
import com.tester.svquickcount.Adapter.PaslonAdapter;
import com.tester.svquickcount.Adapter.PaymentAdapter;
import com.tester.svquickcount.Dialog.AlertError;
import com.tester.svquickcount.Dialog.AlertSuccess;
import com.tester.svquickcount.Dialog.LoadingDialog;
import com.tester.svquickcount.Model.ListMonitoring;
import com.tester.svquickcount.Model.ListPaslon;
import com.tester.svquickcount.Model.ListPayment;
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

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog sheetDialog;
    @BindView(R.id.bottom_sheet)
    View bottom_sheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_paslon);
        ButterKnife.bind(this);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
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

    @OnClick(R.id.lnInputSuara) void lnInputSuara(){
        showBottomSheetDialog();
    }


    @SuppressLint("WrongConstant")
    private void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.sheet_inputsuara, null);

        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        EditText edSuaraSah,edSuaraTidakSah;
        Button btnSimpan;
        edSuaraSah = (EditText) view.findViewById(R.id.edSuaraSah);
        edSuaraTidakSah=(EditText)view.findViewById(R.id.edSuaraTidakSah);
        btnSimpan=(Button)view.findViewById(R.id.btnSimpan);
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prosesInputSuara(edSuaraSah.getText().toString(),edSuaraTidakSah.getText().toString());
            }
        });

        sheetDialog = new BottomSheetDialog(this);
        sheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            sheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        sheetDialog.show();
        sheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                sheetDialog = null;
            }
        });
    }


    public void dismissBottomSheetDialog(){
        sheetDialog.dismiss();
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


    public void prosesInputSuara(String suarasah,String suaratidaksah){
        loadingDialog.startLoadingDialog();
        SessionSetting sessionSetting = new SessionSetting();
        SessionLogin sessionLogin = new SessionLogin();
        String id_tps = "",nama_tps="",provinsi="",kabupaten="",kecamatan="",kelurahan="";
        try{
            JSONObject penugasan = new JSONObject(sessionLogin.getPenugasan(getApplicationContext()));
            boolean status = penugasan.getBoolean("status");
            if(status){
                JSONObject data = penugasan.getJSONObject("data");
                id_tps = data.getString("id_tps");
                nama_tps = data.getString("nama_tps");
                provinsi = data.getString("kode_provinsi");
                kabupaten=data.getString("kode_kabupaten");
                kecamatan = data.getString("kode_kecamatan");
                kelurahan = data.getString("kode_kelurahan");
            }else{

            }
        }catch (Exception e){

        }
        AndroidNetworking.post(BASE_URL+"webservice/paslon/inputsuara")
                .addBodyParameter("paslon",tvNamaPaslon.getText().toString())
                .addBodyParameter("kode_tps",id_tps)
                .addBodyParameter("nama_tps",nama_tps)
                .addBodyParameter("provinsi",provinsi)
                .addBodyParameter("kabupaten",kabupaten)
                .addBodyParameter("kecamatan",kecamatan)
                .addBodyParameter("kelurahan",kelurahan)
                .addBodyParameter("suara_sah", suarasah)
                .addBodyParameter("suara_tidaksah", suaratidaksah)
                .addBodyParameter("userinput",sessionLogin.getEmail_pelanggan(getApplicationContext()))
                .addBodyParameter("useredit",sessionLogin.getEmail_pelanggan(getApplicationContext()))
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject person) {
                        loadingDialog.dismissDialog();
                        try {
                            boolean isSuccess = person.getBoolean("status");
                            if (isSuccess) {
                                alertSuccess.startDialog("Berhasil","Input Suara Berhasil");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        onBackPressed();
                                    }
                                },1500);
                            } else {
                                alertError.startDialog("Input Suara Gagal", person.getString("message"));
                            }
                        } catch (JSONException e) {
                            alertError.startDialog("Input Suara Gagal", e.getMessage());
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        loadingDialog.dismissDialog();
                        alertError.startDialog("Input Suara Gagal", anError.getMessage());
                    }
                });
    }
}
