package com.rakeshSingh.bulletin;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.rakeshSingh.bulletin.adapters.TopicsAdapter;
import com.rakeshSingh.bulletin.constants.AppConstants;
import com.rakeshSingh.bulletin.models.Article;
import com.rakeshSingh.bulletin.models.Topics;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @BindView(R.id.searchRv)
    RecyclerView recyclerView;
    @BindView(R.id.queryEditText)
    EditText queryEditText;
    @BindView(R.id.placeholder)
    ImageView placeholder;
    @BindView(R.id.progressRoot)
    LinearLayout progress;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.noResults)
    TextView noResults;

    private SearchTask searchTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        queryEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) v.getContext()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                    //Build URL
                    Uri.Builder builder = new Uri.Builder();
                    builder.scheme("https")
                            .authority("newsapi.org")
                            .appendPath("v2")
                            .appendPath("everything")
                            .appendQueryParameter("q", v.getText().toString())
                            .appendQueryParameter("apiKey", BuildConfig.NewsAPIKey);
                    searchTask = new SearchTask();
                    searchTask.execute(builder.build().toString());
                    return true;
                }
                return false;
            }
        });
    }

    class SearchTask extends AsyncTask<String, Void, Void> {

        private List<Article> articles;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoading(true);
        }

        @Override
        protected Void doInBackground(String... strings) {
            Log.d("Query", strings[0]);
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
                StringBuilder builder = new StringBuilder();

                String inputString;
                while ((inputString = bufferedReader.readLine()) != null) {
                    builder.append(inputString);
                }

                articles = new Gson().fromJson(builder.toString(), Topics.class).getArticles();
                Log.d("Data", String.valueOf(articles.size()));

                urlConnection.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (articles.size() != 0) {
                showLoading(false);
                TopicsAdapter topicsAdapter = new TopicsAdapter(articles,
                        SearchActivity.this,
                        MainActivity.database, AppConstants.PARENT_CATEGORY);
                LayoutAnimationController controller =
                        AnimationUtils.loadLayoutAnimation(SearchActivity.this,
                                R.anim.item_anim_from_bottom);

                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(topicsAdapter);
                recyclerView.setVisibility(View.VISIBLE);
                placeholder.setVisibility(View.INVISIBLE);
                recyclerView.setLayoutAnimation(controller);
                recyclerView.scheduleLayoutAnimation();
                topicsAdapter.notifyDataSetChanged();
            } else {
                noResults.setVisibility(View.VISIBLE);
                placeholder.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
            }
        }
    }

    private void showLoading(boolean show) {
        if (show) {
            placeholder.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            noResults.setVisibility(View.GONE);
            recyclerView.setVisibility(View.INVISIBLE);
        } else {
            placeholder.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            noResults.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchTask != null) {
            searchTask.cancel(true);
        }
    }
}
