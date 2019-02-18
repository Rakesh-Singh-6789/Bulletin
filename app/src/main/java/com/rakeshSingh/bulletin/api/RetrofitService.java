package com.rakeshSingh.bulletin.api;

import com.rakeshSingh.bulletin.constants.AppConstants;
import com.rakeshSingh.bulletin.models.Topics;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitService {

    @GET(AppConstants.TOP_HEADLINES_URL)
    Call<Topics> getTopArticles(@Query("country") String country,
                                @Query("apiKey") String apiKey);

    //https://newsapi.org/v2/top-headlines?country=in&category=[category]&apiKey=[ApiKey]
    @GET(AppConstants.TOP_HEADLINES_URL)
    Call<Topics> getCategoryTopics(@Query("country") String country,
                                   @Query("category") String category,
                                   @Query("apiKey") String apiKey);

}
