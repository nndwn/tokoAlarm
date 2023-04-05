package com.tester.svquickcount.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tester.svquickcount.Checkout;
import com.tester.svquickcount.Model.ListPayment;
import com.tester.svquickcount.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {
    Context context;
    Activity activity;

    List<ListPayment> listRiwayats;

    public PaymentAdapter(List<ListPayment> getDataAdapter, Context context, Activity activity) {

        super();

        this.listRiwayats = getDataAdapter;
        this.context = context;
        this.activity = activity;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_payment, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ListPayment getDataAdapter1 = listRiwayats.get(position);
        activity.runOnUiThread((Runnable) (new Runnable() {
            public final void run() {
                try {
                    holder.tvNamaPayment.setText(getDataAdapter1.getNama_payment());
                    Picasso.with(context).load(getDataAdapter1.getGambar())
                            .networkPolicy(NetworkPolicy.NO_CACHE)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(holder.ivGambar);

                    holder.btnPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (activity instanceof Checkout) {
                                ((Checkout) activity).submitPembayaran(getDataAdapter1.getId_payment(),getDataAdapter1.getNama_payment(),getDataAdapter1.getGambar(),getDataAdapter1.getNorek(),getDataAdapter1.getPemilik(),getDataAdapter1.getIs_cod());
                            }
                        }
                    });
                }catch (Exception e){

                }

            }
        }));





    }

    @Override
    public int getItemCount() {

        return listRiwayats.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvNamaPayment;
        public ImageView ivGambar;
        public LinearLayout btnPay;
        public ViewHolder(View itemView) {

            super(itemView);
            tvNamaPayment = (TextView) itemView.findViewById(R.id.tvNamaPayment);
            ivGambar = (ImageView) itemView.findViewById(R.id.ivGambar);
            btnPay = (LinearLayout) itemView.findViewById(R.id.btnPay);
        }
    }





}