package com.appyhigh.sampleboilerplateapplication.Views.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appyhigh.sampleboilerplateapplication.Model.Article;
import com.appyhigh.sampleboilerplateapplication.R;
import com.appyhigh.sampleboilerplateapplication.Views.Fragments.dummy.DummyContent.DummyItem;
import com.appyhigh.sampleboilerplateapplication.Views.Listeners.NewsItemClickListener;
import com.appyhigh.sampleboilerplateapplication.utility.Util;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  List<Article> articles;
    private Context context;
    private NewsItemClickListener newsItemClickListener;


    public MyItemRecyclerViewAdapter(List<Article> items , Context context) {
        this.context = context;
        this.articles = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new NewsViewHolder(view);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final NewsViewHolder viewHolder = (NewsViewHolder) holder;
        Article article = articles.get(position);
        Glide.with(context)
                .load(article.getUrlToImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(viewHolder.imageView);
        viewHolder.title.setText(article.getTitle());
        viewHolder.desc.setText(article.getDescription());
        viewHolder.author.setText(article.getAuthor());
        viewHolder.source.setText(article.getSource().getName());
        viewHolder.time.setText(Util.DateFormat(article.getPublishedAt()));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public void updateNews(List<Article> list){
        this.articles = list;
        notifyDataSetChanged();
    }

    public void setOnclickListener(NewsItemClickListener newsItemClickListener){
        this.newsItemClickListener = newsItemClickListener;
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        TextView title, desc , author , source , time;
        ImageView imageView;


        public NewsViewHolder(View view) {
            super(view);
            mView = view;
            title = view.findViewById(R.id.title);
            desc = view.findViewById(R.id.desc);
            author = view.findViewById(R.id.author);
            source = view.findViewById(R.id.source);
            time = view.findViewById(R.id.time);
            imageView = view.findViewById(R.id.img);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position  = getAdapterPosition();
                    if (newsItemClickListener != null ){
                        newsItemClickListener.onItemClick(v , position);
                    }
                }
            });
        }

    }
}