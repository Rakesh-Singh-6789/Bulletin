package com.rakeshSingh.bulletin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rakeshSingh.bulletin.NewsDetailActivity;
import com.rakeshSingh.bulletin.R;
import com.rakeshSingh.bulletin.constants.AppConstants;
import com.rakeshSingh.bulletin.database.AppDatabase;
import com.rakeshSingh.bulletin.models.Article;
import com.rakeshSingh.bulletin.utils.AppExecutors;
import com.rakeshSingh.bulletin.utils.DateFormatter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class TopicsAdapter extends RecyclerView.Adapter {

    private List<Article> articles;
    private Context context;
    private RequestOptions options;
    private AppDatabase database;
    private String parent;

    public TopicsAdapter(List<Article> articles, Context context, AppDatabase database, String parent) {
        this.articles = articles;
        this.context = context;
        this.database = database;
        options = new RequestOptions().error(R.drawable.news_placeholder);
        this.parent = parent;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
        return new NewsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        int p = holder.getAdapterPosition();
        String author = articles.get(p).getAuthor();
        if (author != null) {
            ((NewsHolder) holder).newsAuthor.setText(author);
        } else {
            ((NewsHolder) holder).newsAuthor.setText(context.getResources()
                    .getString(R.string.author_unknown));
        }
        ((NewsHolder) holder).newsTitle.setText(articles
                .get(p)
                .getTitle());
        Glide.with(context)
                .load(articles.get(p).getUrlToImage())
                .transition(withCrossFade())
                .apply(options)
                .into(((NewsHolder) holder).newsImage);
        ((NewsHolder) holder).publishedAt.setText(
                DateFormatter.getFormattedTime(articles
                        .get(p)
                        .getPublishedAt()));

        ((NewsHolder) holder).share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_TEXT, articles.get(position).getUrl());
                share.setType("text/plain");
                context.startActivity(share);
            }
        });

        if (parent.equals(AppConstants.PARENT_TOPIC)) {
            ((NewsHolder) holder).bookmark.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.ic_bookmark_black));
            ((NewsHolder) holder).bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppExecutors.getInstance().getExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.articlesDAO().deleteArticle(articles
                                    .get(holder.getAdapterPosition()));
                            articles.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                    });
                }
            });
        } else if (parent.equals(AppConstants.PARENT_CATEGORY)) {
            ((NewsHolder) holder).bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppExecutors.getInstance().getExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            database.articlesDAO().insertArticle(articles
                                    .get(holder.getAdapterPosition()));
                        }
                    });
                    ((NewsHolder) holder).bookmark.setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_bookmark_black));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return articles != null ? articles.size() : 0;
    }

    class NewsHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.newsTitle)
        TextView newsTitle;
        @BindView(R.id.newsAuthor)
        TextView newsAuthor;
        @BindView(R.id.newsImage)
        ImageView newsImage;
        @BindView(R.id.newsPublishedAt)
        TextView publishedAt;
        @BindView(R.id.bookmark)
        ImageButton bookmark;
        @BindView(R.id.share)
        ImageButton share;

        NewsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Intent details = new Intent(context, NewsDetailActivity.class);
            details.putExtra("url", articles.get(position)
                    .getUrl());
            details.putExtra("source", articles.get(position)
                    .getSource().getName());

            context.startActivity(details);
        }
    }
}
