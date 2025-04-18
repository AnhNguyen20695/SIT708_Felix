package com.example.newsappv2;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kwabenaberko.newsapilib.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsRecyclerAdapter extends RecyclerView.Adapter<NewsRecyclerAdapter.NewsViewHolder> {
    List<Article> articleList;
    String listType;
    private final NewsRecyclerViewInterface newsRecyclerViewInterface;
    NewsRecyclerAdapter(List<Article> articleList,
                        String listType,
                        NewsRecyclerViewInterface newsRecyclerViewInterface) {
        this.articleList = articleList;
        this.listType = listType;
        this.newsRecyclerViewInterface = newsRecyclerViewInterface;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_recycler_row, parent, false);
        return new NewsViewHolder(view,
                                  newsRecyclerViewInterface,
                                  this.listType);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleList.get(position);
        holder.titleTextView.setText(article.getTitle());
        holder.sourceTextView.setText(article.getSource().getName());
        Picasso.get().load(article.getUrlToImage())
                    .error(R.drawable.no_image_icon)
                    .placeholder(R.drawable.no_image_icon)
                    .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    void updateData(List<Article> data) {
        articleList.clear();
        articleList.addAll(data);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView, sourceTextView;
        ImageView imageView;

        public NewsViewHolder(@NonNull View itemView,
                              NewsRecyclerViewInterface newsRecyclerViewInterface,
                              String listType) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.article_title);
            sourceTextView = itemView.findViewById(R.id.article_source);
            imageView = itemView.findViewById(R.id.article_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (newsRecyclerViewInterface != null) {
                        int position = getAdapterPosition();

                        if (position != RecyclerView.NO_POSITION) {
                            newsRecyclerViewInterface.onNewsClick(position, listType);
                        }
                    }
                }
            });
        }
    }
}
