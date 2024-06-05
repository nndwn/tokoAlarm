package com.tester.iotss;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.google.zxing.Result;
import com.tester.iotss.Dialog.AlertError;
import com.tester.iotss.Dialog.LoadingDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import me.dm7.barcodescanner.core.IViewFinder;
import me.dm7.barcodescanner.core.ViewFinderView;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Tutorial extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ObjectAnimator animator;

    @BindView(R.id.scannerLayout)
    View scannerLayout;
    @BindView(R.id.scannerBar)
    View scannerBar;
    @BindView(R.id.cameraView)
    FrameLayout cameraPreview;
    @BindView(R.id.tvHasilScan)
    TextView tvHasilScan;
    private ZXingScannerView mScannerView;
    LoadingDialog loadingDialog;
    AlertError alertError;
    boolean isForResult = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_tutorial);
        ButterKnife.bind(this);
        isForResult = getIntent().getBooleanExtra("isForResult", false);
        loadingDialog = new LoadingDialog(Tutorial.this);
        alertError = new AlertError(Tutorial.this);

        animator = null;

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.cameraView);
        mScannerView = new ZXingScannerView(this) {
            @Override
            protected IViewFinder createViewFinderView(Context context) {
                return new CustomViewFinderView(context);
            }
        };

        contentFrame.addView(mScannerView);

        ViewTreeObserver vto = scannerLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                scannerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    scannerLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    scannerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }


                float startY = scannerLayout.getY() - scannerLayout.getHeight();
                float destinationY = scannerLayout.getY() + scannerLayout.getHeight();

                ObjectAnimator animator = ObjectAnimator.ofFloat(scannerBar, "translationY", startY, destinationY);
                animator.setRepeatMode(ValueAnimator.REVERSE);
                animator.setRepeatCount(ValueAnimator.INFINITE);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(2000);

                final boolean[] isAnimationUpwards = {false};
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        // Tindakan yang diambil saat animasi dimulai
                        scannerBar.setBackgroundResource(R.drawable.scanning_background_flip);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        // Tindakan yang diambil saat animasi dibatalkan
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                        isAnimationUpwards[0] = !isAnimationUpwards[0];
                        if (isAnimationUpwards[0]) {
                            scannerBar.setBackgroundResource(R.drawable.scanning_background);
                        } else {
                            scannerBar.setBackgroundResource(R.drawable.scanning_background_flip);
                        }
                    }
                });

                animator.start();





            }
        });
    }

    @OnClick(R.id.backButton) void backButton(){
        onBackPressed();
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @OnCheckedChanged(R.id.checkFlash)
    void onFlashChecked(CheckBox button, boolean checked) {
        //do your stuff.
        if(checked){
            mScannerView.setFlash(true);
        }else{
            mScannerView.setFlash(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_normal,R.anim.slide_out_down);
    }

    @Override
    public void handleResult(Result rawResult) {
        String url = rawResult.getText();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private static class CustomViewFinderView extends ViewFinderView {

        public CustomViewFinderView(Context context) {
            super(context);
            init();
        }

        public CustomViewFinderView(Context context, AttributeSet attrs) {
            super(context, attrs);
            init();
        }

        private void init() {
            setSquareViewFinder(false);
            setBorderColor(Color.parseColor("#FF9800"));
            setBackgroundColor(Color.TRANSPARENT);
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
        }

    }
}