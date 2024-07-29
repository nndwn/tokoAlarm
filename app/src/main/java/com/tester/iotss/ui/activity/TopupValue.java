package com.tester.iotss.ui.activity;
import static com.tester.iotss.data.config.Config.BASE_URL;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tester.iotss.R;
import com.tester.iotss.domain.model.PaymentMetode;
import com.tester.iotss.utils.Utils;
import com.tester.iotss.utils.helpers.AppHelper;
import com.tester.iotss.utils.sessions.SessionLogin;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopupValue extends AppCompatActivity{
    private TextView rekening;
    private TextView pemilik;
    private String nomor_cs = "";
    private String price = "";
    private String selectedPayment = "";
    private Context context;
    SessionLogin sessionLogin;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_topup_value);
        ButterKnife.bind(this);
        getData();
        PaymentManual();
    }

    private static class paymentMetode{
        public List<PaymentMetode> payment;
    }

    private List<PaymentMetode> DataPayment() {
        String jsonStr = Utils.readRawResource( getResources().openRawResource(R.raw.payment_metode));
        Gson gson = new Gson();
        Type responseType = new TypeToken<paymentMetode>() {}.getType();
        paymentMetode response = gson.fromJson(jsonStr, responseType);
        return response.payment;
    }

    private void DropDownPayment() {
        AutoCompleteTextView spinnerPayment = findViewById(R.id.spinnerPayment);
        List<String> nameMetode = new ArrayList<>();
        for (int i = 0 ; i < DataPayment().size() ; i++) {
            nameMetode.add(DataPayment().get(i).GetName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, nameMetode);
        spinnerPayment.setAdapter(adapter);

        spinnerPayment.setOnItemClickListener((parent, view, position, id) -> {
            selectedPayment = parent.getItemAtPosition(position).toString();
            if (selectedPayment.isBlank() || selectedPayment.isEmpty())
            {
                pemilik.setText("");
                rekening.setText("");
                return;
            }
            for (int i = 0; i < nameMetode.size(); i++) {
                if (nameMetode.get(i).equals(selectedPayment)) {
                    rekening.setText(String.valueOf(DataPayment().get(i).GetNomor()));
                    pemilik.setText(DataPayment().get(i).GetPemilik());
                    break;
                }
            }
            pemilik.setVisibility(View.VISIBLE);
            rekening.setVisibility(View.VISIBLE);

        });
    }

    private void PaymentManual() {
        TextView textPayment = findViewById(R.id.tvValue);
        rekening = findViewById(R.id.tvPaymentInfo);
        pemilik = findViewById(R.id.tvPaymentName);

        DropDownPayment();

        Intent intent = getIntent();
        price = intent.getStringExtra("priceTopUp");
        textPayment.setText(price);

    }

    private void getData() {
        AndroidNetworking.initialize(getApplicationContext());
        AndroidNetworking.get(BASE_URL + "users/pusatbantuan")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        try {
                            boolean status = person.getBoolean("status");
                            if (status) {
                                JSONObject data = person.getJSONObject("data");
                                nomor_cs = data.getString("nomor_cs");
                            }
                        } catch (JSONException e) {
                            Log.d("PUSATBANTUANLOG", Objects.requireNonNull(e.getMessage()));
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.d("PUSATBANTUANLOG", Objects.requireNonNull(error.getMessage()));
                    }
                });
    }

    @OnClick(R.id.lnChatCs)
    void lnChatCs() {
        String countryCode = "62";
        sessionLogin = new SessionLogin();
        String message = "Top Up: \n" + "username: "+sessionLogin.getNama(getApplicationContext()) +"\nmetode : " + selectedPayment + "\nPembayaran: " + price;
        String formattedNumber = nomor_cs.replaceFirst("^0", countryCode);
        AppHelper.openWhatsApp(context, formattedNumber, message);

    }
}
