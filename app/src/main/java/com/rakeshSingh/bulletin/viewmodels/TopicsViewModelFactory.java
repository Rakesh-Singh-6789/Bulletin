package com.rakeshSingh.bulletin.viewmodels;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class TopicsViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private Context context;
    private Application application;
    private String category;

    public TopicsViewModelFactory(Application application, Context context, String category) {
        this.application = application;
        this.context = context;
        this.category = category;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MainViewModel(application, context, category);
    }
}
