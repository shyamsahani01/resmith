package com.example.resmith.pages;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.resmith.R;
import com.example.resmith.api.RetrofitInstance;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadVideoActivity extends AppCompatActivity {

    Button recordButton, uploadButton;
    VideoView videoView;
    MediaController mediaController;
    String barcode;
    Integer check_value = 1;
    File videoFile;
    RequestBody videoBody;

    MultipartBody.Part videoFilePart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        recordButton = findViewById(R.id.recordButton);
        uploadButton = findViewById(R.id.uploadButton);
        videoView = findViewById(R.id.videoView);

        Intent intent = getIntent();
        barcode = intent.getStringExtra("barcode");

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                check_value = 1;

                if(videoFile.isFile()) {
                    Toast.makeText(UploadVideoActivity.this, "Please record video.", Toast.LENGTH_SHORT).show();
                    check_value = 0;
                }

                if(barcode.isEmpty()) {
                    Toast.makeText(UploadVideoActivity.this, "No order found", Toast.LENGTH_SHORT).show();
                    check_value = 0;
                }

                if(check_value == 1) {
//                    Call<JsonElement> uploadVideoApi = RetrofitInstance.getRetrofitInstance().getApi().uploadResmithProductVideo(videoFilePart, barcode);

                    Call<JsonElement> uploadVideoApi = RetrofitInstance.getRetrofitInstance().getApi().uploadResmithProductVideo(videoFilePart);

                    uploadVideoApi.enqueue(new Callback<JsonElement>() {
                        @Override
                        public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                            if(response.isSuccessful()) {

                                JsonObject jsonObject = response.body().getAsJsonObject();
                                Boolean getStatus = jsonObject.get("status").getAsBoolean();
                                String getMsg = jsonObject.get("msg").getAsString();

                                if(getStatus) {
                                    Toast.makeText(UploadVideoActivity.this, getMsg, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(UploadVideoActivity.this, getMsg, Toast.LENGTH_SHORT).show();
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
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 1) {
            videoView.setVideoURI(data.getData());
            videoView.start();

            Long timstamp_long = System.currentTimeMillis()/1000;
            String current_timestamp = timstamp_long.toString();

//            videoFile = new File(data.getData().getPath() + File.separator + "vid_" + current_timestamp + ".mp4");

            Log.e("APICheck 22", "getPath " + data.getData().getPath() );
            Log.e("APICheck 22", "getScheme " + data.getData().getScheme() );


            Log.e("APICheck 22", "getData " + data.getData() );

            videoFile = new File(data.getData().getPath(),  "vid_" + current_timestamp + ".mp4");
            videoBody = RequestBody.create(MediaType.parse("video/*"), videoFile);
            videoFilePart = MultipartBody.Part.createFormData("video", videoFile.getName(), videoBody);

        }
    }
}