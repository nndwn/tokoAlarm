package com.tester.svquickcount;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.os.Bundle;
import android.widget.RadioButton;

import com.tester.svquickcount.Fragment.FragmentAccount;
import com.tester.svquickcount.Fragment.FragmentHistory;
import com.tester.svquickcount.Fragment.FragmentHome;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, new FragmentHome()).commit();
    }


    @OnClick({R.id.rbHome, R.id.rbAccount,R.id.rbPesanan})
    public void onRadioButtonClicked(RadioButton radioButton) {
        // Is the button now checked?
        boolean checked = radioButton.isChecked();

        // Check which radio button was clicked
        switch (radioButton.getId()) {
            case R.id.rbHome:
                if (checked) {
                    // 1 clicked
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentHome()).commit();
                }
                break;
            case R.id.rbAccount:
                if (checked) {
                    // 2 clicked
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentAccount()).commit();
                }
                break;
            case R.id.rbPesanan:
                if (checked) {
                    // 2 clicked
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new FragmentHistory()).commit();
                }
                break;
        }
    }
}
