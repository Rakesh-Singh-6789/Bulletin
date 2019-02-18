package com.rakeshSingh.bulletin.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rakeshSingh.bulletin.NewsDetailActivity;
import com.rakeshSingh.bulletin.R;
import com.rakeshSingh.bulletin.models.Article;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TrendingTopicsAdapter extends PagerAdapter {

    @BindView(R.id.trendingImage)
    ImageView trendingImage;
    @BindView(R.id.trendingAuthor)
    TextView trendingAuthor;
    @BindView(R.id.trendingTitle)
    TextView trendingTitle;
    @BindView(R.id.authorIcon)
    ImageView icon;

    private List<Article> articles;
    private Activity activity;
    private RequestOptions options;

    public TrendingTopicsAdapter(List<Article> articles, Activity activity) {
        this.articles = articles;
        this.activity = activity;
        options = new RequestOptions()
                .error(R.drawable.news_placeholder);
    }

    @Override
    public int getCount() {
        return articles != null ? articles.size() : 0;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.item_trending, container, false);
        ButterKnife.bind(this, view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent details = new Intent(activity, NewsDetailActivity.class);
                details.putExtra("url", articles.get(position)
                        .getUrl());
                details.putExtra("source", articles.get(position)
                        .getSource().getName());
                activity.startActivity(details);
            }
        });

        Glide.with(activity)
                .load(articles.get(position).getUrlToImage())
                .apply(options)
                .transition(withCrossFade())
                .into(trendingImage);

        trendingTitle.setText(articles.get(position).getTitle());
        String author = articles.get(position).getAuthor();
        if (author != null) {
            trendingAuthor.setText(author);
        } else {
            trendingAuthor.setText(activity.getResources()
                    .getString(R.string.author_unknown));
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((CardView) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

}
