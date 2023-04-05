package com.tester.svquickcount.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.DetailProduk;
import com.tester.svquickcount.Model.ListProduk;
import com.tester.svquickcount.R;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ProdukAdapter extends RecyclerView.Adapter<ProdukAdapter.ViewHolder> {
    Context context;
    Activity activity;

    List<ListProduk> listRiwayats;

    int layout;
    public ProdukAdapter(List<ListProduk> getDataAdapter, Context context, Activity activity,int layout) {

        super();

        this.listRiwayats = getDataAdapter;
        this.context = context;
        this.activity = activity;
        this.layout = layout;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ListProduk getDataAdapter1 = listRiwayats.get(position);
        activity.runOnUiThread((Runnable) (new Runnable() {
            public final void run() {
                try {
                    String str1 = getDataAdapter1.getNamaproduk();
                    if (str1.length() > 50) {
                        str1 = str1.substring(0, 50);
                        str1 += "...";
                    }
                    holder.tvNamaProduk.setText(StringUtils.capitalize(str1.toLowerCase()));
                    String harga = new DecimalFormat("##,##0").format(Long.parseLong(getDataAdapter1.getHarga()));
                    holder.tvHarga.setText("Rp " + harga);
                    Picasso.with(context).load(getDataAdapter1.getGambar())
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(holder.ivGambar);
                }catch (Exception e){

                }

            }
        }));


        holder.lnProduk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, DetailProduk.class);
                intent.putExtra("gambar",getDataAdapter1.getGambar());
                intent.putExtra("harga",getDataAdapter1.getHarga());
                intent.putExtra("namaproduk",getDataAdapter1.getNamaproduk());
                intent.putExtra("stok",getDataAdapter1.getStok());
                intent.putExtra("satuan",getDataAdapter1.getSatuan());
                intent.putExtra("terjual",getDataAdapter1.getTerjual());
                intent.putExtra("id_produk",getDataAdapter1.getIdproduk());
                intent.putExtra("deskripsi",getDataAdapter1.getDeskripsi());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_up,R.anim.slide_normal);
            }
        });



    }

    @Override
    public int getItemCount() {

        return listRiwayats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaProduk, tvHarga;
        public RoundedImageView ivGambar;
        public LinearLayout lnProduk;
        public ViewHolder(View itemView) {

            super(itemView);
            tvNamaProduk = (TextView) itemView.findViewById(R.id.tvNamaProduk);
            tvHarga = (TextView) itemView.findViewById(R.id.tvHarga);
            ivGambar = (RoundedImageView) itemView.findViewById(R.id.ivGambar);
            lnProduk = (LinearLayout) itemView.findViewById(R.id.lnProduk);
        }
    }





}
