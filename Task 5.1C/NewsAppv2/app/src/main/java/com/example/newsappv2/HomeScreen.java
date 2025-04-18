package com.example.newsappv2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeScreen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeScreen extends Fragment implements NewsRecyclerViewInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    RecyclerView topNewsRecyclerView;
    NewsRecyclerAdapter adapter;
    NewsRecyclerAdapter topNewsAdapter;
    List<Article> articleList = new ArrayList<>();
    List<Article> topArticleList = new ArrayList<>();
    LinearProgressIndicator progressIndicator;
    private String NewsLogKeySuccess = "API SUCCESS";
    private String NewsLogKeyFailure = "API FAILURE";

    public HomeScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeScreen.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeScreen newInstance(String param1, String param2) {
        HomeScreen fragment = new HomeScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.news_recycler_view);
        topNewsRecyclerView = view.findViewById(R.id.top_news_recycler_view);
        progressIndicator = view.findViewById(R.id.progress_bar);

        setupNewsRecyclerView();
        getNews();
    }

    void setupNewsRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NewsRecyclerAdapter(articleList, "news", this);
        topNewsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        topNewsAdapter = new NewsRecyclerAdapter(topArticleList, "topNews", this);

        recyclerView.setAdapter(adapter);
        topNewsRecyclerView.setAdapter(topNewsAdapter);
    }

    void changeInProgress(boolean show) {
        if(show) {
            progressIndicator.setVisibility(View.VISIBLE);
        } else {
            progressIndicator.setVisibility(View.INVISIBLE);
        }
    }

    void getNews() {
        changeInProgress(true);
        NewsApiClient newsApiClient = new NewsApiClient("e787a5fb8dcd4c1f97d9e55ae754fafa");
        // /v2/everything
        newsApiClient.getEverything(
                new EverythingRequest.Builder()
                        .q("trump")
                        .build(),
                new NewsApiClient.ArticlesResponseCallback() {
                    @Override
                    public void onSuccess(ArticleResponse response) {
                        getActivity().runOnUiThread(()->{
                            changeInProgress(false);
                            articleList = response.getArticles().subList(10, 20);
                            topArticleList = response.getArticles().subList(0, 10);
                            adapter.updateData(articleList);
                            adapter.notifyDataSetChanged();
                            topNewsAdapter.updateData(topArticleList);
                            topNewsAdapter.notifyDataSetChanged();
                        });
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        Log.i(NewsLogKeyFailure, throwable.getMessage());
                    }
                }
        );
    }

    @Override
    public void onNewsClick(int position, String newsType) {
        Log.i(NewsLogKeySuccess, "Clicked on "+newsType);
        if (newsType.equals("news")) {
            Log.i(NewsLogKeySuccess,"Getting support fragment.");
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            Log.i(NewsLogKeySuccess,"Got support fragment.");
            NewsDetailsFragment newsDetailsFragment = new NewsDetailsFragment(this.articleList.get(position),
                                                                              this.articleList);
            Log.i(NewsLogKeySuccess,"Begin replacing.");
            fragmentTransaction.replace(R.id.newsPlaceholder,newsDetailsFragment, null)
                                .addToBackStack(null)
                                .commit();
        } else {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            NewsDetailsFragment newsDetailsFragment = new NewsDetailsFragment(this.topArticleList.get(position),
                                                                              this.topArticleList);
            fragmentTransaction.replace(R.id.newsPlaceholder,newsDetailsFragment, null)
                    .addToBackStack(null)
                    .commit();
        }
    }
}