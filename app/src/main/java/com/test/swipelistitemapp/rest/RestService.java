package com.test.swipelistitemapp.rest;

import com.test.swipelistitemapp.Constants;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Zoran on 09.03.2016.
 */
public class RestService {

    private static RestServiceInterface apiService;

    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Constants.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient())
            .build();

    public static RestServiceInterface getInstance() {
        if (apiService == null) {
            apiService = retrofit.create(RestServiceInterface.class);
        }
        return apiService;
    }

}
