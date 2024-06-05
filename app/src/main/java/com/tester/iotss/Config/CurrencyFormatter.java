package com.tester.iotss.Config;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import java.text.NumberFormat;
import java.util.Currency;

public class CurrencyFormatter implements TextWatcher {

    private EditText editText;
    private boolean isEditing = false;

    public CurrencyFormatter(EditText editText) {
        this.editText = editText;
        this.editText.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        // Tidak diperlukan
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        // Tidak diperlukan
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (isEditing) return;

        isEditing = true;

        // Mengambil input pengguna tanpa format
        String originalInput = editable.toString().replaceAll("[^\\d]", "");

        // Format mata uang
        String formattedCurrency = formatCurrency(originalInput);

        // Setel kembali ke EditText
        editText.setText(formattedCurrency);
        editText.setSelection(formattedCurrency.length());

        isEditing = false;
    }

    private String formatCurrency(String value) {
        try {
            // Konversi string ke angka
            double parsed = Double.parseDouble(value) / 100; // Ubah ke nilai yang sesuai dengan mata uang Anda

            // Format ke dalam format mata uang
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
            currencyFormat.setCurrency(Currency.getInstance("USD")); // Ganti dengan kode mata uang yang sesuai
            return currencyFormat.format(parsed);
        } catch (NumberFormatException e) {
            // Handle kesalahan parsing jika diperlukan
            e.printStackTrace();
            return "";
        }
    }
}

