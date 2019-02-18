package com.rakeshSingh.bulletin.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rakeshSingh.bulletin.MainActivity;
import com.rakeshSingh.bulletin.R;
import com.rakeshSingh.bulletin.adapters.TopicsAdapter;
import com.rakeshSingh.bulletin.constants.AppConstants;
import com.rakeshSingh.bulletin.models.Article;
import com.rakeshSingh.bulletin.models.Topics;
import com.rakeshSingh.bulletin.viewmodels.MainViewModel;
import com.rakeshSingh.bulletin.viewmodels.TopicsViewModelFactory;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CategoryFragment extends Fragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String category;
    private Unbinder unbinder;

    private final String TAG = CategoryFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        assert getArguments() != null;
        category = getArguments().getString("category");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        unbinder = ButterKnife.bind(this, view);

        //Get Data from ViewModel
        MainViewModel mainViewModel = ViewModelProviders.of(
                CategoryFragment.this,
                new TopicsViewModelFactory(getActivity().getApplication(),
                        getContext(), category))
                .get(MainViewModel.class);
        mainViewModel.getCategoryLiveData().observe(CategoryFragment.this, new Observer<Topics>() {
            @Override
            public void onChanged(Topics topics) {
                setUpRecyclerView(topics.getArticles());
            }
        });
        return view;
    }

    private void setUpRecyclerView(List<Article> articles) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TopicsAdapter topicsAdapter = new TopicsAdapter(articles, getContext(),
                MainActivity.database, AppConstants.PARENT_CATEGORY);
        recyclerView.setAdapter(topicsAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
