package com.tester.svquickcount.Adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.Cart;
import com.tester.svquickcount.Model.ListCart;
import com.tester.svquickcount.R;

import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    Context context;
    Activity activity;

    List<ListCart> listRiwayats;

    int layout;
    public CartAdapter(List<ListCart> getDataAdapter, Context context, Activity activity,int layout) {

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

        final ListCart getDataAdapter1 = listRiwayats.get(position);
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
                    if(layout==R.layout.list_item){
                        holder.tvKuantitas.setText(": "+getDataAdapter1.getKuantitas());
                    }else {
                        holder.tvKuantitas.setText(getDataAdapter1.getKuantitas());
                    }
                    Picasso.with(context).load(getDataAdapter1.getGambar())
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(holder.ivGambar);
                }catch (Exception e){
                    Log.d("CARTADAPTERLOG",e.getMessage());
                }

            }
        }));

        if(layout==R.layout.list_cart) {
            holder.btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof Cart) {
                        if ((Integer.valueOf(getDataAdapter1.getKuantitas()) + 1) > Integer.valueOf(getDataAdapter1.getStok())) {
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(activity, "Stok tidak mencukupi", Toast.LENGTH_LONG).show();
                                }
                            });
                        } else {
                            ((Cart) activity).cartPlus(getDataAdapter1.getIdproduk(), Integer.valueOf(getDataAdapter1.getKuantitas()));
                        }
                    }
                }
            });


            holder.btnMin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (activity instanceof Cart) {
                        ((Cart) activity).cartMin(getDataAdapter1.getIdproduk(), Integer.valueOf(getDataAdapter1.getKuantitas()));
                    }
                }
            });
        }





    }

    @Override
    public int getItemCount() {

        return listRiwayats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaProduk, tvHarga,tvKuantitas;
        public RoundedImageView ivGambar;
        public LinearLayout lnProduk;
        public Button btnPlus,btnMin;
        public ViewHolder(View itemView) {

            super(itemView);
            tvNamaProduk = (TextView) itemView.findViewById(R.id.tvNamaProduk);
            tvKuantitas = (TextView) itemView.findViewById(R.id.tvKuantitas);
            tvHarga = (TextView) itemView.findViewById(R.id.tvHarga);
            ivGambar = (RoundedImageView) itemView.findViewById(R.id.ivGambar);
            lnProduk = (LinearLayout) itemView.findViewById(R.id.lnProduk);
            if(layout==R.layout.list_cart) {
                btnPlus = (Button) itemView.findViewById(R.id.btnPlus);
                btnMin = (Button) itemView.findViewById(R.id.btnMin);
            }
        }
    }





}

