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
import android.widget.ImageView;
import android.widget.TextView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewsDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsDetailsFragment extends Fragment implements NewsRecyclerViewInterface{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    Article article;
    List<Article> articleList = new ArrayList<>();
    RecyclerView recyclerView;
    ImageView imageView;
    TextView newsTitleView;
    TextView newsContentView;
    NewsRecyclerAdapter adapter;
    private String NewsLogKeySuccess = "API SUCCESS";

    // TODO: Rename and change types of parameters

    public NewsDetailsFragment(Article article,
                               List<Article> articleList) {
        // Required empty public constructor
        Log.i(NewsLogKeySuccess, "Initializing.");
        this.article = article;
        Log.i(NewsLogKeySuccess, "Received article: "+article);
        this.articleList = articleList;
        Log.i(NewsLogKeySuccess, "Received articleList of size: "+articleList.size());
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment NewsDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public NewsDetailsFragment newInstance(Article article, List<Article> articleList) {
        NewsDetailsFragment fragment = new NewsDetailsFragment(article,articleList);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.i(NewsLogKeySuccess,"Fragment on view created.");
        super.onViewCreated(view, savedInstanceState);
        Log.i(NewsLogKeySuccess,"Setting recycler view adapter.");
        recyclerView = view.findViewById(R.id.related_news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Log.i(NewsLogKeySuccess,"Setting articles list of size: "+articleList.size());
        adapter = new NewsRecyclerAdapter(articleList, "news", this);
        recyclerView.setAdapter(adapter);
//        adapter.updateData(articleList);
//        adapter.notifyDataSetChanged();

        imageView = view.findViewById(R.id.news_image);
        newsContentView = view.findViewById(R.id.news_content);
        newsTitleView = view.findViewById(R.id.news_title);

        Log.i(NewsLogKeySuccess,"Setting contents.");
        newsContentView.setText(this.article.getContent());
        Log.i(NewsLogKeySuccess,"Setting title contents.");
        newsTitleView.setText(this.article.getTitle());
        Log.i(NewsLogKeySuccess,"Setting image.");
        Picasso.get().load(this.article.getUrlToImage())
                .error(R.drawable.no_image_icon)
                .placeholder(R.drawable.no_image_icon)
                .into(imageView);
        Log.i(NewsLogKeySuccess,"All set.");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(NewsLogKeySuccess,"Inflating fragment.");
        return inflater.inflate(R.layout.fragment_news_details, container, false);
    }

    @Override
    public void onNewsClick(int position, String newsType) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        NewsDetailsFragment newsDetailsFragment = new NewsDetailsFragment(this.articleList.get(position),
                                                                          this.articleList);
        fragmentTransaction.replace(R.id.newsPlaceholder,newsDetailsFragment, null)
                .addToBackStack(null)
                .commit();
    }
}