package com.tester.iotss.ui.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.iotss.R;
import com.tester.iotss.domain.model.ListTopUp;
import com.tester.iotss.ui.adapter.TopUpAdapter;
import com.tester.iotss.utils.Utils;
import java.lang.reflect.Type;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class Topup extends AppCompatActivity {

    private NumberFormat formatRupiah;
    private static class Json {
        public String countryCode;
        public String languageCode;
        public List<ListTopUp> top_up ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);
        ButterKnife.bind(this);

        RecyclerView recyclerView = findViewById(R.id.btnListTopUp);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String jsonStr = Utils.readRawResource( getResources().openRawResource(R.raw.local));
        TopUpAdapter topUpAdapter = getAdapter(jsonStr);

        recyclerView.setAdapter(topUpAdapter);
    }

    private @NonNull TopUpAdapter getAdapter(String jsonStr) {
        Gson gson = new Gson();
        Type responseType = new TypeToken<Json>() {}.getType();
        Json response = gson.fromJson(jsonStr, responseType);

        List<ListTopUp> topupList = response.top_up;

        formatRupiah = NumberFormat.getCurrencyInstance(new Locale(response.languageCode,  response.countryCode));
        return new TopUpAdapter(response.top_up,formatRupiah, view -> {
            int position = (int) view.getTag();
            showPriceTopUp(topupList.get(position).GetPrice());});
    }

    private void showPriceTopUp(int value) {
        Intent intent = new Intent(Topup.this, TopupValue.class);
        int price;
        Random random = new Random();
        int contRangeRandom = 500;
        int rangeRandomLast = random.nextInt(contRangeRandom);
        price = value != 0 ? value + rangeRandomLast : 0;

        String priceFormat =  formatRupiah.format(price);
        intent.putExtra("priceTopUp", priceFormat);
        startActivity(intent);
    }

    @OnClick(R.id.backButton)
    void backButton() {
        onBackPressed();
    }
}