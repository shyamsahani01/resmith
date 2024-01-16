package com.example.resmith.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    ApiInterface apiInterface;

    public static RetrofitInstance retrofitInstance;

    private RetrofitInstance() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiInterface.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiInterface = retrofit.create(ApiInterface.class);
    }

    public static synchronized RetrofitInstance getRetrofitInstance() {
        if(retrofitInstance == null) {
            retrofitInstance = new RetrofitInstance();
        }
        return  retrofitInstance;
    }

    public ApiInterface getApi() {
        return apiInterface;
    }
}
