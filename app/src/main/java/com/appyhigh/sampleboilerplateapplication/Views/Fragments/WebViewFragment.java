package com.appyhigh.sampleboilerplateapplication.Views.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.appyhigh.sampleboilerplateapplication.R;

public class WebViewFragment extends Fragment {
    private WebView webView;


    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = view.findViewById(R.id.webView);
        Bundle bundle = getArguments();
        if (bundle != null){
            String url = bundle.getString("url");
            webView.loadUrl(url);
        }
        return view;
    }
}