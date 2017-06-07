package com.example.android.userinfocall;

import com.example.android.userinfocall.Entities.RandomAPI;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitService {

    @GET("api")
    Call<RandomAPI> getRandomUser();
}
