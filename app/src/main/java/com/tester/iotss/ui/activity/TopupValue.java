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
import java.util.Random;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.tester.iotss.R;
import com.tester.iotss.utils.helpers.AppHelper;
import com.tester.iotss.utils.sessions.SessionLogin;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TopupValue extends AppCompatActivity{
    private TextView tvValue;
    private TextView tvPaymentInfo;
    private AutoCompleteTextView spinnerPayment;
    private TextView tvPaymentName;
    private String nomor_cs = "";
    private String selectedPayment = "";
    private String resultValue ="";
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

        tvValue = findViewById(R.id.tvValue);
        tvPaymentInfo = findViewById(R.id.tvPaymentInfo);
        spinnerPayment = findViewById(R.id.spinnerPayment);
        tvPaymentName = findViewById(R.id.tvPaymentName);

        Intent intent = getIntent();
        String value = intent.getStringExtra("value");

        Random random = new Random();
        int randomDigits = random.nextInt(1000);

        String[] paymentMethods = getResources().getStringArray(R.array.payment_methods);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, paymentMethods);
        spinnerPayment.setAdapter(adapter);

        spinnerPayment.setOnItemClickListener((parent, view, position, id) -> {
            selectedPayment = parent.getItemAtPosition(position).toString();
            switch (selectedPayment) {
                case "Bank Mandiri":
                    tvPaymentInfo.setText("1060012526581");
                    break;
                case "Bank Jago":
                    tvPaymentInfo.setText("507381262547");
                    break;
                case "Super Bank":
                    tvPaymentInfo.setText("000000077305");
                    break;
                case "Ovo":
                case "Dana":
                case "Gopay":
                    tvPaymentInfo.setText("081264628242");
                    break;
                default:
                    tvPaymentInfo.setText("");
                    break;
            }

            tvPaymentName.setVisibility(View.VISIBLE);
            tvPaymentInfo.setVisibility(View.VISIBLE);
        });

        if (value != null) {
            resultValue = String.format("%s.%03d", value, randomDigits);
            tvValue.setText(resultValue);

        }
    }



    private void getData() {
        AndroidNetworking.get(BASE_URL + "users/pusatbantuan")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(final JSONObject person) {
                        Log.d("PUSATBANTUANLOG", person.toString());
                        try {

                            boolean status = person.getBoolean("status");
                            if (status) {
                                JSONObject data = person.getJSONObject("data");
                                nomor_cs =
                                        data.getString("nomor_cs");
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("PUSATBANTUANLOG", e.getMessage());
                            Toast.makeText(getApplicationContext(), "Terjadi Kesalahan " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(ANError error) {
                        Log.d("PUSATBANTUANLOG", error.getMessage());
                        Toast.makeText(getApplicationContext(), "Terjadi Kesalahan " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    @OnClick(R.id.lnChatCs)
    void lnChatCs() {
        String countryCode = "62";
        sessionLogin = new SessionLogin();
        String message = "Top Up: \n" + "username: "+sessionLogin.getNama(getApplicationContext()) +"\nmetode : " + selectedPayment + "\nPembayaran: " + resultValue;
        String formattedNumber = nomor_cs.replaceFirst("^0", countryCode);
        AppHelper.openWhatsApp(context, formattedNumber, message);

    }
}
