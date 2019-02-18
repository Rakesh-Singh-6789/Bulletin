package com.rakeshSingh.bulletin;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.rakeshSingh.bulletin.adapters.TrendingTopicsAdapter;
import com.rakeshSingh.bulletin.database.AppDatabase;
import com.rakeshSingh.bulletin.fragments.CategoryFragment;
import com.rakeshSingh.bulletin.models.Article;
import com.rakeshSingh.bulletin.models.Topics;
import com.rakeshSingh.bulletin.utils.UserLocale;
import com.rakeshSingh.bulletin.viewmodels.MainViewModel;
import com.rakeshSingh.bulletin.viewmodels.TopicsViewModelFactory;
import com.rakeshSingh.bulletin.widget.NewsWidget;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.trendingViewPager)
    ViewPager trendingViewPager;
    @BindView(R.id.topicsViewPager)
    ViewPager topicsViewPager;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.appBarLayout)
    AppBarLayout appBarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appName)
    TextView appName;
    @BindView(R.id.trendingTopicsTxt)
    TextView trendingTopicsTxt;
    @BindView(R.id.progressLayout)
    LinearLayout progressLayout;
    @BindView(R.id.country)
    TextView country;

    //Fragment Objects
    CategoryFragment businessFragment;
    CategoryFragment entertainmentFragment;
    CategoryFragment healthFragment;
    CategoryFragment scienceFragment;
    CategoryFragment sportsFragment;
    CategoryFragment technologyFragment;

    public static AppDatabase database;

    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        database = AppDatabase.getInstance(MainActivity.this);

        Intent widgetUpdate = new Intent(NewsWidget.ACTION_UPDATE);
        sendBroadcast(widgetUpdate);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("");
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/volkorn_sb.ttf");
        appName.setText(getResources().getString(R.string.app_name));
        appName.setTypeface(typeface);
        trendingTopicsTxt.setTypeface(typeface);

        country.setText(UserLocale.getUserCountry(MainActivity.this));

        setUpTopicsViewPager();

        showLoading(true);
        MainViewModel mainViewModel = ViewModelProviders.of(MainActivity.this,
                new TopicsViewModelFactory(getApplication(), MainActivity.this, null))
                .get(MainViewModel.class);
        mainViewModel.getTrendingLivaData()
                .observe(MainActivity.this, new Observer<Topics>() {
                    @Override
                    public void onChanged(Topics topics) {
                        setUpTrendingViewPager(topics.getArticles());
                    }
                });
    }


    private void showLoading(boolean shouldShow) {
        if (shouldShow) {
            appBarLayout.setVisibility(View.GONE);
            progressLayout.setVisibility(View.VISIBLE);
            topicsViewPager.setVisibility(View.GONE);
        } else {
            appBarLayout.setVisibility(View.VISIBLE);
            appBarLayout.animate().alpha(1f).setDuration(200).start();
            progressLayout.setVisibility(View.GONE);
            topicsViewPager.setVisibility(View.VISIBLE);
            topicsViewPager.animate().alpha(1f).setDuration(200).start();
        }
    }

    private void setUpTopicsViewPager() {
        topicsViewPager.setOffscreenPageLimit(6);
        topicsViewPager.setAdapter(new TopicsAdapter(getSupportFragmentManager()));
        topicsViewPager.setCurrentItem(0, true);
        tabLayout.setupWithViewPager(topicsViewPager);
    }

    private void setUpTrendingViewPager(List<Article> articles) {
        trendingViewPager.setAdapter(new TrendingTopicsAdapter(
                articles, MainActivity.this));
        trendingViewPager.setClipToPadding(false);
        trendingViewPager.setPadding(80, 8, 80, 8);
        trendingViewPager.setPageMargin(30);
        showLoading(false);
    }

    private class TopicsAdapter extends FragmentPagerAdapter {

        TopicsAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            switch (position) {
                case 0:
                    if (technologyFragment == null) {
                        technologyFragment = new CategoryFragment();
                        bundle.putString("category", "technology");
                        technologyFragment.setArguments(bundle);
                        return technologyFragment;
                    }
                    return technologyFragment;
                case 1:
                    if (businessFragment == null) {
                        businessFragment = new CategoryFragment();
                        bundle.putString("category", "business");
                        businessFragment.setArguments(bundle);
                        return businessFragment;
                    }
                    return businessFragment;
                case 2:
                    if (entertainmentFragment == null) {
                        entertainmentFragment = new CategoryFragment();
                        bundle.putString("category", "entertainment");
                        entertainmentFragment.setArguments(bundle);
                        return entertainmentFragment;
                    }
                    return entertainmentFragment;
                case 3:
                    if (healthFragment == null) {
                        healthFragment = new CategoryFragment();
                        bundle.putString("category", "health");
                        healthFragment.setArguments(bundle);
                        return healthFragment;
                    }
                    return healthFragment;
                case 4:
                    if (scienceFragment == null) {
                        scienceFragment = new CategoryFragment();
                        bundle.putString("category", "science");
                        scienceFragment.setArguments(bundle);
                        return scienceFragment;
                    }
                    return scienceFragment;
                case 5:
                    if (sportsFragment == null) {
                        sportsFragment = new CategoryFragment();
                        bundle.putString("category", "sports");
                        sportsFragment.setArguments(bundle);
                        return sportsFragment;
                    }
                    return sportsFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 6;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Technology";
                case 1:
                    return "Business";
                case 2:
                    return "Entertainment";
                case 3:
                    return "Health";
                case 4:
                    return "Science";
                case 5:
                    return "Sports";
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_saved:
                Intent bookmarks = new Intent(MainActivity.this,
                        BookmarksActivity.class);
                startActivity(bookmarks);
                return true;
            case R.id.menu_search:
                Intent search = new Intent(MainActivity.this,
                        SearchActivity.class);
                startActivity(search);
                return true;
        }
        return true;
    }
}
