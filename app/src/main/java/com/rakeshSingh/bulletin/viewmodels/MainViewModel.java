package com.rakeshSingh.bulletin.viewmodels;

import android.app.Application;
import android.content.Context;

import com.rakeshSingh.bulletin.database.DataRepository;
import com.rakeshSingh.bulletin.models.Article;
import com.rakeshSingh.bulletin.models.Topics;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {

    private LiveData<Topics> trendingLiveData, categoryLiveData;
    private LiveData<List<Article>> articles;

    MainViewModel(@NonNull Application application, Context context, String category) {
        super(application);
        loadData(context, category);
    }

    private void loadData(Context context, String category) {
        categoryLiveData = DataRepository.getInstance().getCategoryTopics(context, category);
        trendingLiveData = DataRepository.getInstance().getTrendingTopics(context);
        articles = DataRepository.getInstance().getBookmarkedArticles();
    }

    public LiveData<Topics> getTrendingLivaData() {
        return trendingLiveData;
    }

    public LiveData<Topics> getCategoryLiveData() {
        return categoryLiveData;
    }

    public LiveData<List<Article>> getBookmarkedArticles() {
        return articles;
    }
}
