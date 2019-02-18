package com.rakeshSingh.bulletin.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.rakeshSingh.bulletin.BuildConfig;
import com.rakeshSingh.bulletin.NewsDetailActivity;
import com.rakeshSingh.bulletin.R;
import com.rakeshSingh.bulletin.api.RetrofitClient;
import com.rakeshSingh.bulletin.api.RetrofitService;
import com.rakeshSingh.bulletin.models.Article;
import com.rakeshSingh.bulletin.models.Topics;
import com.rakeshSingh.bulletin.utils.DateFormatter;
import com.rakeshSingh.bulletin.utils.UserLocale;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions.withCrossFade;

public class NewsWidgetDataProvider implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Intent intent;
    private List<Article> articles;
    private long token;
    private int appWidgetId;

    NewsWidgetDataProvider(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        this.articles = new ArrayList<>();
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        loadData();
    }

    @Override
    public void onDataSetChanged() {
        //notifyAll();
        loadData();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Log.d(NewsWidget.TAG, "getCount: " + articles.size());
        return 20;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (articles.size() > 0) {
            RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.item_widget);
            String title = articles.get(position).getTitle();
            String author = articles.get(position).getAuthor();
            if (author == null) {
                author = context.getString(R.string.author_unknown);
            }
            row.setTextViewText(R.id.newsTitle, title);
            row.setTextViewText(R.id.newsAuthor, author);
            row.setTextViewText(R.id.newsPublishedAt, DateFormatter
                    .getFormattedTime(articles.get(position).getPublishedAt()));
            try {
                Bitmap bitmap = Glide.with(context)
                        .asBitmap()
                        .load(articles.get(position).getUrlToImage())
                        .transition(withCrossFade())
                        .submit(100, 100)
                        .get();

                row.setImageViewBitmap(R.id.widgetImg, bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent details = new Intent();
            Bundle b = new Bundle();
            b.putString("url", articles.get(position).getUrl());
            b.putString("source", articles.get(position).getSource().getName());
            details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            details.putExtras(b);

            row.setOnClickFillInIntent(R.id.widgetRoot, details);
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtras(b);
            PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,0);
            row.setOnClickPendingIntent(R.id.widgetList, pendingIntent);

            return row;
        }
        return null;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void loadData() {
        token = Binder.clearCallingIdentity();
        Log.d(NewsWidget.TAG, "run: Executed");
        RetrofitClient.getRetrofitClient().create(RetrofitService.class)
                .getTopArticles(UserLocale.getUserLocale(context.getApplicationContext()), BuildConfig.NewsAPIKey)
                .enqueue(new Callback<Topics>() {
                    @Override
                    public void onResponse(Call<Topics> call, Response<Topics> response) {
                        if (response.body() != null) {
                            articles = response.body().getArticles();
                            Binder.restoreCallingIdentity(token);
                            Log.d(NewsWidget.TAG, "run: Finished - " + articles.size());
                        }
                    }

                    @Override
                    public void onFailure(Call<Topics> call, Throwable t) {
                        call.cancel();
                    }
                });
    }

}
