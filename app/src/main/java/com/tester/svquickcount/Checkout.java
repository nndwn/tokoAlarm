package com.tester.svquickcount;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tester.svquickcount.Adapter.CartAdapter;
import com.tester.svquickcount.Adapter.PaymentAdapter;
import com.tester.svquickcount.Dialog.AlertConfirm;
import com.tester.svquickcount.Dialog.AlertError;
import com.tester.svquickcount.Dialog.LoadingDialog;
import com.tester.svquickcount.Model.ListCart;
import com.tester.svquickcount.Model.ListPayment;
import com.tester.svquickcount.Session.SessionSetting;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import static com.tester.svquickcount.Config.Config.BASE_URL;

public class Checkout extends AppCompatActivity implements AlertConfirm.ConfirmCallback {


    AlertConfirm alertConfirm;
    @BindView(R.id.tvInvoice)
    TextView tvInvoice;
    @BindView(R.id.tvTanggal)
    TextView tvTanggal;
    @BindView(R.id.tvTotalItem)
    TextView tvTotalItem;
    @BindView(R.id.tvTotalBayar)
    TextView tvTotalBayar;
    @BindView(R.id.tvAlamatKirim)
    TextView tvAlamatKirim;
    String invoice,tanggal,total_item,total_bayar,alamat_kirim;
    @BindView(R.id.tvKodePromo)
    EditText tvKodePromo;

    BottomSheetBehavior sheetBehavior;
    BottomSheetDialog sheetDialog;
    @BindView(R.id.bottom_sheet)
    View bottom_sheet;


    private ArrayList<ListPayment> listPayment;
    //private PromoAdapter adapterPromo;
    LinearLayoutManager linearLayoutManagerPayment;
    RecyclerView.Adapter recyclerViewadapterPayment;
    JSONArray jsonArray;

    LoadingDialog loadingDialog;
    AlertError alertError;

    JSONArray dataitem;

    private ArrayList<ListCart> listCart;
    private CartAdapter adapter;
    @BindView(R.id.list_produk)
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    RecyclerView.Adapter recyclerViewadapter;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(Checkout.this);
        alertError = new AlertError(Checkout.this);
        sheetBehavior = BottomSheetBehavior.from(bottom_sheet);
        alertConfirm = new AlertConfirm(Checkout.this,this);
        if(getIntent().hasExtra("invoice")){
            invoice = getIntent().getStringExtra("invoice");
            tanggal = getIntent().getStringExtra("tanggal");
            total_item = getIntent().getStringExtra("total_item");
            total_bayar = getIntent().getStringExtra("total_bayar");
            alamat_kirim = getIntent().getStringExtra("alamat_kirim");
            try {
                dataitem = new JSONArray(getIntent().getStringExtra("item"));
                Log.d("CHECKOUTLOGJSON",dataitem.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            tvInvoice.setText("Pesanan #"+invoice);
            tvTanggal.setText(tanggal);
            tvTotalItem.setText(total_item);
            tvAlamatKirim.setText(alamat_kirim);
            String strbayar = new DecimalFormat("##,##0").format(Long.parseLong(total_bayar));
            tvTotalBayar.setText("Rp "+strbayar);
        }

        getPayment();

        listPayment = new ArrayList<>();

        listCart = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(Checkout.this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        ShowItem();
    }


    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        if(getIntent().hasExtra("fromhistory")){
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
        }else{
            alertConfirm.startDialog("Konfirmasi","Apakah yakin ingin kembali ke home ?");
        }

    }

    @Override
    public void onConfirmYa() {
        alertConfirm.dismissDialog();
        Intent intent = new Intent(Checkout.this,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
        finish();
    }

    @OnClick(R.id.btnPromo) void btnPromo(){
        Intent intent = new Intent(Checkout.this, Promo.class);
        intent.putExtra("fromcheckout","1");
        startActivityForResult(intent,1);
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
    }

    @Override
    public void onConfirmNo() {
        alertConfirm.dismissDialog();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1){
            if(resultCode == RESULT_OK){
                String result = data.getStringExtra("kodepromo");
                tvKodePromo.setText(result);
            }
        }
    }


    @OnClick(R.id.btnCheckout) void btnCheckout(){
        showBottomSheetDialog();
    }


    @SuppressLint("WrongConstant")
    private void showBottomSheetDialog() {
        View view = getLayoutInflater().inflate(R.layout.sheet_payment, null);

        if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }


        RecyclerView recyclerViewPayment;

        recyclerViewPayment = (RecyclerView) view.findViewById(R.id.list_payment);

        recyclerViewPayment.setHasFixedSize(true);
        linearLayoutManagerPayment = new LinearLayoutManager(Checkout.this,LinearLayoutManager.VERTICAL,false);
        recyclerViewPayment.setLayoutManager(linearLayoutManagerPayment);

        try{
            listPayment.clear();
            SessionSetting sessionSetting = new SessionSetting();
            if(jsonArray.length()>0) {
                for (int j = 0; j < jsonArray.length(); j++) {

                    try {
                        JSONObject dataObject = jsonArray.getJSONObject(j);
                        ListPayment memberDataPromo = new ListPayment();
                        memberDataPromo.setId_payment(dataObject.getString("id_payment"));
                        memberDataPromo.setGambar(BASE_URL + dataObject.getString("gambar"));
                        memberDataPromo.setNama_payment(dataObject.getString("nama_payment"));
                        memberDataPromo.setIs_cod(dataObject.getString("is_cod"));
                        memberDataPromo.setNorek(dataObject.getString("norek"));
                        memberDataPromo.setPemilik(dataObject.getString("pemilik"));

                        listPayment.add(memberDataPromo);
                        recyclerViewadapterPayment = new PaymentAdapter(listPayment, Checkout.this, Checkout.this);
                        recyclerViewPayment.setAdapter(recyclerViewadapterPayment);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }catch (Exception e){

        }
        //sheetDialog.dismiss();

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



    private void getPayment(){
        SessionSetting sessionSetting = new SessionSetting();
        AndroidNetworking.get(BASE_URL+"webservice/transaksi/getpayment")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.d("CHECKOUTLOG",person.toString());
                                try {

                                    boolean status = person.getBoolean("status");
                                    if(status){
                                        jsonArray = person.getJSONArray("data");
                                    }else{

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("CHECKOUTLOG",e.getMessage());
                                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("CHECKOUTLOG",error.getMessage());
                        Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
    }



    public void submitPembayaran(String id_payment,String nama_payment,String gambar_payment,String norek,String pemilik_rekening,String is_cod){
        dismissBottomSheetDialog();
        loadingDialog.startLoadingDialog();
        SessionSetting sessionSetting = new SessionSetting();
        AndroidNetworking.post(BASE_URL+"webservice/transaksi/pembayaran")
                .addBodyParameter("id_payment",id_payment)
                .addBodyParameter("gambar_payment",gambar_payment)
                .addBodyParameter("nama_payment",nama_payment)
                .addBodyParameter("norek",norek)
                .addBodyParameter("is_cod",is_cod)
                .addBodyParameter("kodepromo",tvKodePromo.getText().toString())
                .addBodyParameter("total_bayar",total_bayar)
                .addBodyParameter("total_item",total_item)
                .addBodyParameter("pemilik_rekening",pemilik_rekening)
                .addBodyParameter("invoice",invoice)
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                                Log.d("CHECKOUTLOG",person.toString());
                                try {

                                    boolean status = person.getBoolean("status");
                                    if(status){
                                        JSONObject data = person.getJSONObject("data");
                                        Intent intent = new Intent(Checkout.this,Pembayaran.class);
                                        intent.putExtra("id_transaksi",data.getString("noindex"));
                                        intent.putExtra("invoice",data.getString("invoice"));
                                        intent.putExtra("kodepromo",data.getString("kodepromo"));
                                        intent.putExtra("total_bayar",data.getString("total_bayar"));
                                        intent.putExtra("total_item",data.getString("total_item"));
                                        intent.putExtra("potongan",data.getString("potongan"));
                                        intent.putExtra("grandtotal",data.getString("grandtotal"));
                                        intent.putExtra("norek",data.getString("norek"));
                                        intent.putExtra("pemilik_rekening",data.getString("pemilik_rekening"));
                                        intent.putExtra("gambar_payment",data.getString("gambar_payment"));
                                        intent.putExtra("is_cod",data.getString("is_cod"));
                                        intent.putExtra("item",dataitem.toString());
                                        intent.putExtra("status_bayar",data.getString("status_bayar"));
                                        intent.putExtra("status_kirim",data.getString("status_kirim"));
                                        intent.putExtra("status_transaksi",data.getString("status_transaksi"));
                                        startActivity(intent);
                                        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                                    }else{
                                        alertError.startDialog("Gagal",person.getString("message"));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("CHECKOUTLOG",e.getMessage());
                                    Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+e.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                    }
                    @Override
                    public void onError(ANError error) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadingDialog.dismissDialog();
                                Log.d("CHECKOUTLOG",error.getMessage());
                                Toast.makeText(getApplicationContext(),"Terjadi Kesalahan "+error.getMessage(),Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                });
    }



    private void ShowItem(){
        listCart.clear();
        Log.d("CHECKOUTLOGJSON", String.valueOf(dataitem.length()));
        if(dataitem.length()>0) {
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
                    recyclerViewadapter = new CartAdapter(listCart, Checkout.this, Checkout.this, R.layout.list_item);
                    recyclerView.setAdapter(recyclerViewadapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
