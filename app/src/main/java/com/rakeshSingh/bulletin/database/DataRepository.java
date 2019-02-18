package com.rakeshSingh.bulletin.database;

import android.content.Context;
import android.util.Log;

import com.rakeshSingh.bulletin.BuildConfig;
import com.rakeshSingh.bulletin.MainActivity;
import com.rakeshSingh.bulletin.api.RetrofitClient;
import com.rakeshSingh.bulletin.api.RetrofitService;
import com.rakeshSingh.bulletin.models.Article;
import com.rakeshSingh.bulletin.models.Topics;
import com.rakeshSingh.bulletin.utils.AppExecutors;
import com.rakeshSingh.bulletin.utils.UserLocale;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DataRepository {

    private static DataRepository instance;
    private final String TAG = DataRepository.class.getSimpleName();

    public static DataRepository getInstance() {
        if (instance == null) {
            instance = new DataRepository();
            return instance;
        }
        return instance;
    }

    public LiveData<Topics> getTrendingTopics(Context context) {
        final MutableLiveData<Topics> results = new MutableLiveData<>();
        RetrofitClient.getRetrofitClient().create(RetrofitService.class)
                .getTopArticles(UserLocale.getUserLocale(context), BuildConfig.NewsAPIKey)
                .enqueue(new Callback<Topics>() {
                    @Override
                    public void onResponse(Call<Topics> call, Response<Topics> response) {
                        results.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Topics> call, Throwable t) {
                        call.cancel();
                        Log.d(TAG, t.getMessage());
                    }
                });
        return results;
    }

    public LiveData<Topics> getCategoryTopics(Context context, String category) {
        final MutableLiveData<Topics> results = new MutableLiveData<>();
        RetrofitClient.getRetrofitClient().create(RetrofitService.class)
                .getCategoryTopics(UserLocale.getUserLocale(context), category,
                        BuildConfig.NewsAPIKey)
                .enqueue(new Callback<Topics>() {
                    @Override
                    public void onResponse(Call<Topics> call, Response<Topics> response) {
                        results.setValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<Topics> call, Throwable t) {
                        call.cancel();
                    }
                });
        return results;
    }

    public LiveData<List<Article>> getBookmarkedArticles() {
        Log.d("Room", "Room call");
        final MutableLiveData<List<Article>> articles = new MutableLiveData<>();
        AppExecutors.getInstance().getExecutor().execute(new Runnable() {
            @Override
            public void run() {
                articles.postValue(MainActivity.database.articlesDAO().getAllArticles());
            }
        });
        return articles;
    }
}
