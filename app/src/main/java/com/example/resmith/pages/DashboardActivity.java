package com.example.resmith.pages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.resmith.R;
import com.example.resmith.api.RetrofitInstance;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallback;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    ImageView barcodeScanner;
    TextInputEditText orderSearchTextInputEditText;
    Button SearchButton;


    Integer check_value = 1;
    String orderSearchTextInputEditTextValue;




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

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkForOrderNo();
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

    private void checkForOrderNo() {

        check_value = 1;

        orderSearchTextInputEditTextValue = orderSearchTextInputEditText.getText().toString();

        if(orderSearchTextInputEditText.getText().toString().isEmpty()) {
            Toast.makeText(DashboardActivity.this, "Please enter order no.", Toast.LENGTH_SHORT).show();
            check_value = 0;
        }

        if(check_value == 1) {
            Call<JsonElement> checkDataForOrderNo = RetrofitInstance.getRetrofitInstance().getApi().checkResmithOrder(orderSearchTextInputEditTextValue);

            checkDataForOrderNo.enqueue(new Callback<JsonElement>() {
                @Override
                public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                    if(response.isSuccessful()) {

                        JsonObject jsonObject = response.body().getAsJsonObject();
                        Boolean getStatus = jsonObject.get("status").getAsBoolean();
                        String getMsg = jsonObject.get("msg").getAsString();

                        if(getStatus) {
                            Toast.makeText(DashboardActivity.this, getMsg, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(DashboardActivity.this, UploadVideoActivity.class);
                            intent.putExtra("barcode", orderSearchTextInputEditTextValue);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(DashboardActivity.this, getMsg, Toast.LENGTH_SHORT).show();
                        }
                        Log.e("APICheck 22", "Something 22 1133 " + response.body().toString());
                    }
                    else {
                        try {
                            Log.e("APICheck 22", "Something 22 1122 " + response.errorBody().string());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }

                @Override
                public void onFailure(Call<JsonElement> call, Throwable t) {
                    Log.e("APICheck 22", "Something went wrong 22 " + t.getMessage().toString());
                }
            });


        }


    }



}