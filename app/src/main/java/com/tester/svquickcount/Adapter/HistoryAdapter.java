package com.tester.svquickcount.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tester.svquickcount.Checkout;
import com.tester.svquickcount.Model.ListHistory;
import com.tester.svquickcount.Pembayaran;
import com.tester.svquickcount.R;

import java.text.DecimalFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    Context context;
    Activity activity;

    List<ListHistory> listRiwayats;

    String from;
    public HistoryAdapter(List<ListHistory> getDataAdapter, Context context, Activity activity,String from) {

        super();
        this.from=from;
        this.listRiwayats = getDataAdapter;
        this.context = context;
        this.activity = activity;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_history, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ListHistory getDataAdapter1 = listRiwayats.get(position);
        activity.runOnUiThread((Runnable) (new Runnable() {
            public final void run() {
                holder.tvInvoice.setText("Pesanan #"+getDataAdapter1.getInvoice());
                holder.tvTanggal.setText(getDataAdapter1.getTanggal());
                if(getDataAdapter1.getStatus_transaksi().equals("1")){
                    holder.tvStatus.setText("Transaksi Selesai");
                    holder.tvStatus.setEnabled(true);
                    holder.lnItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, Pembayaran.class);
                            intent.putExtra("id_transaksi",getDataAdapter1.getId_transaksi());
                            intent.putExtra("invoice",getDataAdapter1.getInvoice());
                            intent.putExtra("kodepromo",getDataAdapter1.getKode_promo());
                            intent.putExtra("total_bayar",getDataAdapter1.getTotal_bayar());
                            intent.putExtra("total_item",getDataAdapter1.getTotal_item());
                            intent.putExtra("potongan",getDataAdapter1.getPotongan());
                            intent.putExtra("grandtotal",getDataAdapter1.getGrandtotal());
                            intent.putExtra("norek",getDataAdapter1.getNorek());
                            intent.putExtra("pemilik_rekening",getDataAdapter1.getPemilik_rekening());
                            intent.putExtra("gambar_payment",getDataAdapter1.getGambar_payment());
                            intent.putExtra("is_cod",getDataAdapter1.getIscod());
                            intent.putExtra("item",getDataAdapter1.getDataitem().toString());
                            intent.putExtra("status_bayar",getDataAdapter1.getStatus_bayar());
                            intent.putExtra("status_kirim",getDataAdapter1.getStatus_kirim());
                            intent.putExtra("status_transaksi",getDataAdapter1.getStatus_transaksi());
                            activity.startActivity(intent);
                            activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                        }
                    });
                }else{
                    if(from.equals("aktif")){
                        if(getDataAdapter1.getStatus_bayar().equals("PENDING")) {
                            holder.tvStatus.setText("Belum dibayar");
                            holder.tvStatus.setEnabled(false);
                            holder.tvStatus.setHovered(true);
                            holder.lnItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(getDataAdapter1.getId_payment() != null && !getDataAdapter1.getId_payment().isEmpty() && !getDataAdapter1.getId_payment().equals("null")) {
                                        Intent intent = new Intent(activity, Pembayaran.class);
                                        intent.putExtra("id_transaksi",getDataAdapter1.getId_transaksi());
                                        intent.putExtra("invoice",getDataAdapter1.getInvoice());
                                        intent.putExtra("kodepromo",getDataAdapter1.getKode_promo());
                                        intent.putExtra("total_bayar",getDataAdapter1.getTotal_bayar());
                                        intent.putExtra("total_item",getDataAdapter1.getTotal_item());
                                        intent.putExtra("potongan",getDataAdapter1.getPotongan());
                                        intent.putExtra("grandtotal",getDataAdapter1.getGrandtotal());
                                        intent.putExtra("norek",getDataAdapter1.getNorek());
                                        intent.putExtra("pemilik_rekening",getDataAdapter1.getPemilik_rekening());
                                        intent.putExtra("gambar_payment",getDataAdapter1.getGambar_payment());
                                        intent.putExtra("is_cod",getDataAdapter1.getIscod());
                                        intent.putExtra("item",getDataAdapter1.getDataitem().toString());
                                        intent.putExtra("status_bayar",getDataAdapter1.getStatus_bayar());
                                        intent.putExtra("status_kirim",getDataAdapter1.getStatus_kirim());
                                        intent.putExtra("status_transaksi",getDataAdapter1.getStatus_transaksi());
                                        activity.startActivity(intent);
                                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                                    }else{
                                        Intent intent = new Intent(activity, Checkout.class);
                                        intent.putExtra("alamat_kirim", getDataAdapter1.getAlamat_kirim());
                                        intent.putExtra("total_item", getDataAdapter1.getTotal_item());
                                        intent.putExtra("total_bayar", getDataAdapter1.getTotal_bayar());
                                        intent.putExtra("invoice", getDataAdapter1.getInvoice());
                                        intent.putExtra("tanggal", getDataAdapter1.getTanggal());
                                        intent.putExtra("fromhistory", "1");
                                        intent.putExtra("id_transaksi", getDataAdapter1.getId_transaksi());
                                        intent.putExtra("item",getDataAdapter1.getDataitem().toString());
                                        intent.putExtra("status_bayar",getDataAdapter1.getStatus_bayar());
                                        intent.putExtra("status_kirim",getDataAdapter1.getStatus_kirim());
                                        intent.putExtra("status_transaksi",getDataAdapter1.getStatus_transaksi());
                                        activity.startActivity(intent);
                                        activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                                    }
                                }
                            });
                        }else if(getDataAdapter1.getStatus_bayar().equals("PROCESS")){
                            holder.tvStatus.setText("Menunggu verifikasi pembayaran");
                            holder.tvStatus.setEnabled(false);
                            holder.tvStatus.setHovered(true);
                            holder.lnItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(activity, Pembayaran.class);
                                    intent.putExtra("id_transaksi",getDataAdapter1.getId_transaksi());
                                    intent.putExtra("invoice",getDataAdapter1.getInvoice());
                                    intent.putExtra("kodepromo",getDataAdapter1.getKode_promo());
                                    intent.putExtra("total_bayar",getDataAdapter1.getTotal_bayar());
                                    intent.putExtra("total_item",getDataAdapter1.getTotal_item());
                                    intent.putExtra("potongan",getDataAdapter1.getPotongan());
                                    intent.putExtra("grandtotal",getDataAdapter1.getGrandtotal());
                                    intent.putExtra("norek",getDataAdapter1.getNorek());
                                    intent.putExtra("pemilik_rekening",getDataAdapter1.getPemilik_rekening());
                                    intent.putExtra("gambar_payment",getDataAdapter1.getGambar_payment());
                                    intent.putExtra("is_cod",getDataAdapter1.getIscod());
                                    intent.putExtra("item",getDataAdapter1.getDataitem().toString());
                                    intent.putExtra("status_bayar",getDataAdapter1.getStatus_bayar());
                                    intent.putExtra("status_kirim",getDataAdapter1.getStatus_kirim());
                                    intent.putExtra("status_transaksi",getDataAdapter1.getStatus_transaksi());
                                    activity.startActivity(intent);
                                    activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                                }
                            });

                        }else if(getDataAdapter1.getStatus_bayar().equals("SUCCESS")) {
                            holder.tvStatus.setText("Sudah dibayar");
                            holder.tvStatus.setEnabled(true);
                            holder.lnItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(activity, Pembayaran.class);
                                    intent.putExtra("id_transaksi",getDataAdapter1.getId_transaksi());
                                    intent.putExtra("invoice",getDataAdapter1.getInvoice());
                                    intent.putExtra("kodepromo",getDataAdapter1.getKode_promo());
                                    intent.putExtra("total_bayar",getDataAdapter1.getTotal_bayar());
                                    intent.putExtra("total_item",getDataAdapter1.getTotal_item());
                                    intent.putExtra("potongan",getDataAdapter1.getPotongan());
                                    intent.putExtra("grandtotal",getDataAdapter1.getGrandtotal());
                                    intent.putExtra("norek",getDataAdapter1.getNorek());
                                    intent.putExtra("pemilik_rekening",getDataAdapter1.getPemilik_rekening());
                                    intent.putExtra("gambar_payment",getDataAdapter1.getGambar_payment());
                                    intent.putExtra("is_cod",getDataAdapter1.getIscod());
                                    intent.putExtra("item",getDataAdapter1.getDataitem().toString());
                                    intent.putExtra("status_bayar",getDataAdapter1.getStatus_bayar());
                                    intent.putExtra("status_kirim",getDataAdapter1.getStatus_kirim());
                                    intent.putExtra("status_transaksi",getDataAdapter1.getStatus_transaksi());
                                    activity.startActivity(intent);
                                    activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                                }
                            });
                        }
                    }else if(from.equals("dikirim")){
                        if(getDataAdapter1.getStatus_kirim().equals("PENDING")){
                            holder.tvStatus.setText("Belum dikirim");
                            holder.tvStatus.setEnabled(false);
                            holder.tvStatus.setHovered(true);
                        }else if(getDataAdapter1.getStatus_kirim().equals("PROCESS")){
                            holder.tvStatus.setText("Sedang proses pengiriman");
                            holder.tvStatus.setEnabled(false);
                            holder.tvStatus.setHovered(true);
                            holder.lnItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(activity, Pembayaran.class);
                                    intent.putExtra("id_transaksi",getDataAdapter1.getId_transaksi());
                                    intent.putExtra("invoice",getDataAdapter1.getInvoice());
                                    intent.putExtra("kodepromo",getDataAdapter1.getKode_promo());
                                    intent.putExtra("total_bayar",getDataAdapter1.getTotal_bayar());
                                    intent.putExtra("total_item",getDataAdapter1.getTotal_item());
                                    intent.putExtra("potongan",getDataAdapter1.getPotongan());
                                    intent.putExtra("grandtotal",getDataAdapter1.getGrandtotal());
                                    intent.putExtra("norek",getDataAdapter1.getNorek());
                                    intent.putExtra("pemilik_rekening",getDataAdapter1.getPemilik_rekening());
                                    intent.putExtra("gambar_payment",getDataAdapter1.getGambar_payment());
                                    intent.putExtra("is_cod",getDataAdapter1.getIscod());
                                    intent.putExtra("item",getDataAdapter1.getDataitem().toString());
                                    intent.putExtra("status_bayar",getDataAdapter1.getStatus_bayar());
                                    intent.putExtra("status_kirim",getDataAdapter1.getStatus_kirim());
                                    intent.putExtra("status_transaksi",getDataAdapter1.getStatus_transaksi());
                                    activity.startActivity(intent);
                                    activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                                }
                            });
                        }else if(getDataAdapter1.getStatus_kirim().equals("SUCCESS")){
                            holder.tvStatus.setText("Barang sudah diterima");
                            holder.tvStatus.setEnabled(true);
                            holder.lnItem.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(activity, Pembayaran.class);
                                    intent.putExtra("id_transaksi",getDataAdapter1.getId_transaksi());
                                    intent.putExtra("invoice",getDataAdapter1.getInvoice());
                                    intent.putExtra("kodepromo",getDataAdapter1.getKode_promo());
                                    intent.putExtra("total_bayar",getDataAdapter1.getTotal_bayar());
                                    intent.putExtra("total_item",getDataAdapter1.getTotal_item());
                                    intent.putExtra("potongan",getDataAdapter1.getPotongan());
                                    intent.putExtra("grandtotal",getDataAdapter1.getGrandtotal());
                                    intent.putExtra("norek",getDataAdapter1.getNorek());
                                    intent.putExtra("pemilik_rekening",getDataAdapter1.getPemilik_rekening());
                                    intent.putExtra("gambar_payment",getDataAdapter1.getGambar_payment());
                                    intent.putExtra("is_cod",getDataAdapter1.getIscod());
                                    intent.putExtra("item",getDataAdapter1.getDataitem().toString());
                                    intent.putExtra("status_bayar",getDataAdapter1.getStatus_bayar());
                                    intent.putExtra("status_kirim",getDataAdapter1.getStatus_kirim());
                                    intent.putExtra("status_transaksi",getDataAdapter1.getStatus_transaksi());
                                    activity.startActivity(intent);
                                    activity.overridePendingTransition(R.anim.slide_in_up, R.anim.slide_normal);
                                }
                            });
                        }
                    }
                }
                holder.tvTotalItem.setText(": "+getDataAdapter1.getTotal_item());
                String strTarif = new DecimalFormat("##,##0").format(Long.parseLong(getDataAdapter1.getGrandtotal()));
                holder.tvTotalBayar.setText(": Rp "+strTarif);
            }
        }));



    }

    @Override
    public int getItemCount() {

        return listRiwayats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvInvoice, tvTanggal, tvTotalItem,tvTotalBayar,tvStatus;
        public Button btnAction;
        public LinearLayout lnItem;
        public ViewHolder(View itemView) {

            super(itemView);
            tvInvoice = (TextView) itemView.findViewById(R.id.tvInvoice);
            tvTotalItem = (TextView) itemView.findViewById(R.id.tvTotalItem);
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            tvTotalBayar = (TextView) itemView.findViewById(R.id.tvTotalBayar);
            tvTanggal = (TextView) itemView.findViewById(R.id.tvTanggal);
            btnAction = (Button) itemView.findViewById(R.id.btnAction);
            lnItem = (LinearLayout) itemView.findViewById(R.id.lnItem);
        }
    }





}
