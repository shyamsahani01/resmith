package com.example.resmith.pages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.resmith.R;
import com.google.android.material.textfield.TextInputEditText;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;

import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    ImageView barcodeScanner;
    TextInputEditText orderSearchTextInputEditText;
    Button SearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        barcodeScanner = findViewById(R.id.barcodeScanner);
        orderSearchTextInputEditText = findViewById(R.id.orderSearchTextInputEditText);
        SearchButton = findViewById(R.id.SearchButton);

        barcodeScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, BarcodeActivity.class);
                startActivity(intent);
            }
        });

        String[] permission = {Manifest.permission.INTERNET,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.CAMERA};

        PermissionX.init(DashboardActivity.this).permissions(permission)
                .explainReasonBeforeRequest()
//                .onForwardToSettings(new ForwardToSettingsCallback() {
//                    @Override
//                    public void onForwardToSettings(@NonNull ForwardScope forwardScope, @NonNull List<String> list) {
//                        forwardScope.showForwardToSettingsDialog(list, "This Permission is required", "Ok", "Cancel");
//                    }
//                })
                .onExplainRequestReason(new ExplainReasonCallback() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope explainScope, @NonNull List<String> list) {
                        explainScope.showRequestReasonDialog(list, "Permission is required", "Ok", "Cancel");
                    }
                }).request(new RequestCallback() {
                    @Override
                    public void onResult(boolean b, @NonNull List<String> list, @NonNull List<String> list1) {
                        if(b) {
//                            Toast.makeText(DashboardActivity.this, "All Permission Given.", Toast.LENGTH_SHORT).show();
                        }
                        else {
//                            Toast.makeText(DashboardActivity.this, "No Permission Given.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}