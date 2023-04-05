package com.tester.svquickcount;

import static com.tester.svquickcount.Config.Config.BASE_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.Adapter.CartAdapter;
import com.tester.svquickcount.Dialog.AlertError;
import com.tester.svquickcount.Dialog.LoadingDialog;
import com.tester.svquickcount.Model.ListCart;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Pembayaran extends AppCompatActivity {


    @BindView(R.id.tvInvoice)
    TextView tvInvoice;
    @BindView(R.id.tvKodePromo)
    TextView tvKodePromo;
    @BindView(R.id.tvTotalItem)
    TextView tvTotalItem;
    @BindView(R.id.tvTotalBayar)
    TextView tvTotalBayar;
    @BindView(R.id.tvPotongan)
    TextView tvPotongan;
    @BindView(R.id.tvGrandTotal)
    TextView tvGrandTotal;
    @BindView(R.id.ivGambarPayment)
    ImageView ivGambarPayment;
    @BindView(R.id.tvNorek)
    TextView tvNorek;
    @BindView(R.id.tvPemilikRekening)
    TextView tvPemilikRekening;

    String id_transaksi,invoice,kodepromo,total_item,total_bayar,potongan,grandtotal,norek,pemilik_rekening,is_cod,status_bayar,status_transaksi,status_kirim;
    SessionSetting sessionSetting;

    @BindView(R.id.lnBank)
    LinearLayout lnBank;
    @BindView(R.id.lnCod)
    LinearLayout lnCod;
    @BindView(R.id.lnStatus)
    LinearLayout lnStatus;
    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.btnKonfirmasi)
    LinearLayout btnKonfirmasi;
    @BindView(R.id.btnTerima)
    LinearLayout btnTerima;

    JSONArray dataitem;

    private ArrayList<ListCart> listCart;
    private CartAdapter adapter;
    @BindView(R.id.list_produk)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    LoadingDialog loadingDialog;
    AlertError alertError;
    @SuppressLint("WrongConstant")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembayaran);
        ButterKnife.bind(this);
        sessionSetting = new SessionSetting();
        loadingDialog = new LoadingDialog(Pembayaran.this);
        alertError = new AlertError(Pembayaran.this);
        if(getIntent().hasExtra("id_transaksi")){
            id_transaksi = getIntent().getStringExtra("id_transaksi");
            invoice = getIntent().getStringExtra("invoice");
            kodepromo = getIntent().getStringExtra("kodepromo");
            total_bayar = getIntent().getStringExtra("total_bayar");
            total_item = getIntent().getStringExtra("total_item");
            potongan = getIntent().getStringExtra("potongan");
            grandtotal = getIntent().getStringExtra("grandtotal");
            norek = getIntent().getStringExtra("norek");
            pemilik_rekening = getIntent().getStringExtra("pemilik_rekening");
            is_cod = getIntent().getStringExtra("is_cod");
            status_bayar = getIntent().getStringExtra("status_bayar");
            status_transaksi = getIntent().getStringExtra("status_transaksi");
            status_kirim = getIntent().getStringExtra("status_kirim");
            try {
                dataitem = new JSONArray(getIntent().getStringExtra("item"));
                Log.d("PEMBAYARANLOGJSON",dataitem.toString());
            } catch (JSONException e) {
            }
            if(is_cod.equals("1")){
                if(status_transaksi.equals("1")){
                    lnBank.setVisibility(View.GONE);
                    lnCod.setVisibility(View.GONE);
                    lnStatus.setVisibility(View.VISIBLE);
                    btnKonfirmasi.setVisibility(View.GONE);
                    tvStatus.setText("Transaksi ini sudah dinyatakan selesai!");
                    tvStatus.setEnabled(true);
                }else {
                    lnBank.setVisibility(View.GONE);
                    lnCod.setVisibility(View.VISIBLE);
                    lnStatus.setVisibility(View.GONE);
                    btnKonfirmasi.setVisibility(View.GONE);
                    if (status_kirim.equals("PROCESS")) {
                        lnStatus.setVisibility(View.VISIBLE);
                        tvStatus.setText("Transaksi ini dalam proses pengiriman!");
                        tvStatus.setEnabled(false);
                        tvStatus.setHovered(true);
                        btnTerima.setVisibility(View.VISIBLE);
                    } else if (status_kirim.equals("SUCCESS")) {
                        if (!status_bayar.equals("SUCCESS")) {
                            lnStatus.setVisibility(View.VISIBLE);
                            lnCod.setVisibility(View.GONE);
                            tvStatus.setText("Pesanan sudah diterima silahkan lakukan pembayaran COD!");
                            tvStatus.setEnabled(true);
                        }
                    }
                }
            }else{
                if(status_transaksi.equals("1")){
                    lnBank.setVisibility(View.GONE);
                    lnCod.setVisibility(View.GONE);
                    lnStatus.setVisibility(View.VISIBLE);
                    btnKonfirmasi.setVisibility(View.GONE);
                    tvStatus.setText("Transaksi ini sudah dinyatakan selesai!");
                    tvStatus.setEnabled(true);
                }else{
                    if(status_bayar.equals("PROCESS")) {
                        lnBank.setVisibility(View.GONE);
                        lnCod.setVisibility(View.GONE);
                        lnStatus.setVisibility(View.VISIBLE);
                        btnKonfirmasi.setVisibility(View.GONE);
                        tvStatus.setText("Proses pembayaran sedang diverifikasi oleh admin!");
                        tvStatus.setEnabled(false);
                        tvStatus.setHovered(true);
                    }else if(status_bayar.equals("SUCCESS")){
                        lnBank.setVisibility(View.GONE);
                        lnCod.setVisibility(View.GONE);
                        lnStatus.setVisibility(View.VISIBLE);
                        btnKonfirmasi.setVisibility(View.GONE);
                        if(status_kirim.equals("PROCESS")){
                            tvStatus.setText("Transaksi ini dalam proses pengiriman!");
                            tvStatus.setEnabled(false);
                            tvStatus.setHovered(true);
                            btnTerima.setVisibility(View.VISIBLE);
                        }else if(status_kirim.equals("SUCCESS")){
                            tvStatus.setText("Barang / Produk pada transaksi ini sudah diterima!");
                            tvStatus.setEnabled(true);
                        }else{
                            tvStatus.setText("Transaksi ini sudah dilakukan pembayaran\n mohon tunggu barang sampai!");
                            tvStatus.setEnabled(true);
                        }

                    }else{
                        lnBank.setVisibility(View.VISIBLE);
                        lnCod.setVisibility(View.GONE);
                        lnStatus.setVisibility(View.GONE);
                        btnKonfirmasi.setVisibility(View.VISIBLE);
                    }
                }


            }
            String strpotongan = new DecimalFormat("##,##0").format(Long.parseLong(getIntent().getStringExtra("potongan")));
            String strtotalbayar = new DecimalFormat("##,##0").format(Long.parseLong(getIntent().getStringExtra("total_bayar")));
            String strgrandtotal = new DecimalFormat("##,##0").format(Long.parseLong(getIntent().getStringExtra("grandtotal")));
            tvInvoice.setText("#"+invoice);
            if(kodepromo.equals("")){
                tvKodePromo.setText("-");
            }else{
                tvKodePromo.setText(kodepromo);
            }

            tvTotalItem.setText(total_item);
            tvNorek.setText(norek);
            tvPemilikRekening.setText("Atas Nama "+pemilik_rekening);
            tvPotongan.setText("Rp "+strpotongan);
            tvTotalBayar.setText("Rp "+strtotalbayar);
            tvGrandTotal.setText("Rp "+strgrandtotal);

            Picasso.with(Pembayaran.this).load(BASE_URL+getIntent().getStringExtra("gambar_payment"))
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.bannerplaceholder)
                    .into(ivGambarPayment);
        }

        listCart = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(Pembayaran.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ShowItem();
    }


    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }


    @OnClick(R.id.btnKonfirmasi) void btnKonfirmasi(){
        Intent intent = new Intent(Pembayaran.this,KonfirmasiPembayaran.class);
        intent.putExtra("id_transaksi",id_transaksi);
        intent.putExtra("invoice",invoice);
        intent.putExtra("total_bayar",total_bayar);
        intent.putExtra("total_item",total_item);
        intent.putExtra("potongan",potongan);
        intent.putExtra("grandtotal",grandtotal);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
    }



    @OnClick(R.id.btnTerima) void btnTerima(){
        loadingDialog.startLoadingDialog();
        SessionSetting sessionSetting = new SessionSetting();
        AndroidNetworking.post(BASE_URL+"webservice/transaksi/terimapesanan")
                .addBodyParameter("id_transaksi", id_transaksi)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject person) {
                        loadingDialog.dismissDialog();
                        try {
                            boolean isSuccess = person.getBoolean("status");
                            if (isSuccess) {
                                Intent intent = new Intent(Pembayaran.this,SuccessKonfirmasi.class);
                                intent.putExtra("invoice",invoice);
                                intent.putExtra("total_bayar",total_bayar);
                                intent.putExtra("total_item",total_item);
                                intent.putExtra("potongan",potongan);
                                intent.putExtra("grandtotal",grandtotal);
                                intent.putExtra("message","Pesanan Sudah Diterima");
                                startActivity(intent);
                                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
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
                        alertError.startDialog("Gagal", anError.getMessage());
                    }
                });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);

    }


    private void ShowItem(){
        listCart.clear();
        try {
            Log.d("PEMBAYARANLOGJSON", String.valueOf(dataitem.length()));
            if (dataitem.length() > 0) {
                for (int j = 0; j < dataitem.length(); j++) {

                    try {
                        SessionSetting sessionSetting = new SessionSetting();
                        JSONObject dataObject = dataitem.getJSONObject(j);
                        ListCart memberData = new ListCart();
                        memberData.setIdcart(dataObject.getString("id"));
                        memberData.setGambar(BASE_URL + dataObject.getString("gambar"));
                        memberData.setHarga(dataObject.getString("harga_jual"));
                        memberData.setNamaproduk(dataObject.getString("nama_produk"));
                        memberData.setKuantitas(dataObject.getString("kuantitas"));
                        memberData.setIdpelanggan(dataObject.getString("id_pelanggan"));
                        memberData.setIdproduk(dataObject.getString("id_produk"));
                        listCart.add(memberData);
                        recyclerViewadapter = new CartAdapter(listCart, Pembayaran.this, Pembayaran.this, R.layout.list_item);
                        recyclerView.setAdapter(recyclerViewadapter);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        }catch (Exception e){

        }
    }
}
