package com.example.resmith.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.resmith.R;
import com.google.zxing.Result;

public class BarcodeActivity extends AppCompatActivity {

    CodeScannerView codeScannerView;
    CodeScanner codeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);

        codeScannerView = findViewById(R.id.scannerBox);
        codeScannerView.isAutoFocusButtonVisible();

        codeScanner = new CodeScanner(this, codeScannerView);
        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                Toast.makeText(BarcodeActivity.this, "Barcode Value :" + result.getText(), Toast.LENGTH_SHORT);
                Log.d("Barcode Value :", "Barcode Value :" + result.getText());
//                codeScanner.startPreview();

                checkBarcodeResult(result.getText());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Barcode Value :" + result.getText(), Toast.LENGTH_SHORT);
                        checkBarcodeResult(result.getText());
                    }
                });

            }
        });

        codeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                codeScanner.startPreview();
            }
        });


    }

    private void checkBarcodeResult(String text) {
        Activity activity = BarcodeActivity.this;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "Barcode Value :" + text, Toast.LENGTH_SHORT);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(codeScanner != null) {
            codeScanner.startPreview();
        }

    }

    @Override
    protected void onPause() {

        if(codeScanner != null) {
            codeScanner.releaseResources();
        }
        super.onPause();
    }
}