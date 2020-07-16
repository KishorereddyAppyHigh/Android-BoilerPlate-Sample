package com.appyhigh.sampleboilerplateapplication.Views.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appyhigh.sampleboilerplateapplication.Api.ApiClient;
import com.appyhigh.sampleboilerplateapplication.Api.ApiInterface;
import com.appyhigh.sampleboilerplateapplication.Model.Article;
import com.appyhigh.sampleboilerplateapplication.Model.News;
import com.appyhigh.sampleboilerplateapplication.R;
import com.appyhigh.sampleboilerplateapplication.Views.Fragments.dummy.DummyContent;
import com.appyhigh.sampleboilerplateapplication.Views.Listeners.NewsItemClickListener;
import com.appyhigh.sampleboilerplateapplication.utility.SharedPreferenceUtil;
import com.appyhigh.sampleboilerplateapplication.utility.Util;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 */
public class ItemFragment extends Fragment {
    public static final String API_KEY = "3cb9160c1ef346d6a7303e6ed4b623b0";
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Article> articleList = new ArrayList<>();
    private MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;
    private SharedPreferenceUtil sharedPreferenceUtil;

    public ItemFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        sharedPreferenceUtil = new SharedPreferenceUtil(requireContext());
        recyclerView = view.findViewById(R.id.newsRecyclerView);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(articleList , requireContext());
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String defaultCountryName = sharedPreferenceUtil.getStringValue("country_Name");
        if (defaultCountryName == null){
            defaultCountryName = "INDIA";
        }
        String countryName = null;
        if (defaultCountryName.equalsIgnoreCase("CANADA")){
            countryName = "ca";
        }
        if (defaultCountryName.equalsIgnoreCase("USA")){
            countryName = "us";
        }
        if (defaultCountryName.equalsIgnoreCase("INDIA")){
            countryName = "in";
        }
        if (defaultCountryName.equalsIgnoreCase("UK")){
            countryName = "gb";
        }
        if (defaultCountryName.equalsIgnoreCase("AUSTRALIA")){
            countryName = "au";
        }
        loadJson(countryName);
        setOnItemClickListener();
    }


    private void setOnItemClickListener() {
        myItemRecyclerViewAdapter.setOnclickListener(new NewsItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                WebViewFragment webViewFragment = new WebViewFragment();
                Bundle bundle = new Bundle();
                Article article = articleList.get(position);
                bundle.putString("url",article.getUrl());
                webViewFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container , webViewFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    public void loadJson(String countryName){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        String country = countryName;
        Call<News> call = apiInterface.getNews(country,API_KEY);
        call.enqueue(new Callback<News>(){
            @Override
            public void onResponse(Call<News> call, Response<News> response) {
                if (response.isSuccessful() && response.body().getArticle() != null){
                    if (!articleList.isEmpty()){
                        articleList.clear();
                    }
                    articleList = response.body().getArticle();
//                    myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(articleList,requireContext());
//                    recyclerView.setAdapter(myItemRecyclerViewAdapter);
//                    myItemRecyclerViewAdapter.notifyDataSetChanged();
                    myItemRecyclerViewAdapter.updateNews(articleList);
                }
            }

            @Override
            public void onFailure(Call<News> call, Throwable t) {

            }
        });
    }
}