package com.rakeshSingh.bulletin;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.rakeshSingh.bulletin.adapters.TopicsAdapter;
import com.rakeshSingh.bulletin.constants.AppConstants;
import com.rakeshSingh.bulletin.models.Article;
import com.rakeshSingh.bulletin.viewmodels.MainViewModel;
import com.rakeshSingh.bulletin.viewmodels.TopicsViewModelFactory;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BookmarksActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.bookmarksRv)
    RecyclerView bookmarksRv;
    @BindView(R.id.noBookmarksRoot)
    LinearLayout noBookmarks;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        unbinder = ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Bookmarks");
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
            getSupportActionBar().setElevation(6f);
            toolbar.setElevation(6f);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        MainViewModel viewModel = ViewModelProviders.of(this,
                new TopicsViewModelFactory(getApplication(), this, null))
                .get(MainViewModel.class);
        viewModel.getBookmarkedArticles().observe(BookmarksActivity.this, new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                if (articles.size() != 0) {
                    noBookmarks.setVisibility(View.GONE);
                    bookmarksRv.setLayoutManager(new LinearLayoutManager(BookmarksActivity.this));
                    bookmarksRv.setHasFixedSize(true);
                    bookmarksRv.setAdapter(new TopicsAdapter(articles,
                            BookmarksActivity.this,
                            MainActivity.database, AppConstants.PARENT_TOPIC));
                } else noBookmarks.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
