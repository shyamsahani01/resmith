package com.example.resmith.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    String baseUrl = "http://reports.pinkcityindia.com/";

    @POST("api/web/checkResmithOrder")
    @FormUrlEncoded
    Call<JsonElement> checkResmithOrder(@Field("barcode") String barcode);

//    @POST("api/web/checkResmithOrder")
//    @Multipart
//    Call<JsonElement> uploadResmithProductVideo(@Part MultipartBody.Part video, @Field("barcode") String barcode);

    @POST("api/web/checkResmithOrder")
    @Multipart
    Call<JsonElement> uploadResmithProductVideo(@Part MultipartBody.Part video);
}
