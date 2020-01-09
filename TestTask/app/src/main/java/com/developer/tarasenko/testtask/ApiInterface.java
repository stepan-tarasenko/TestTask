package com.developer.tarasenko.testtask;

import com.developer.tarasenko.testtask.entity.DisplayModel;
import com.developer.tarasenko.testtask.entity.ReceiveModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    String API_BASE_URL = "https://demo0040494.mockable.io/api/v1/";

    @GET("trending")
    Call<List<ReceiveModel>> getReceiveModels();

    @GET("object/{id}")
    Call<DisplayModel> getReceiveModelById(@Path("id") int objectId);
}
