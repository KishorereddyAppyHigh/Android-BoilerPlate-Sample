package com.appyhigh.sampleboilerplateapplication.Views.Fragments;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Spinner;

import com.appyhigh.sampleboilerplateapplication.R;
import com.google.android.material.appbar.MaterialToolbar;

public class WebViewFragment extends Fragment {
    private WebView webView;
    private MaterialToolbar materialToolbar ;
    private Spinner spinner;

    public WebViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        webView = view.findViewById(R.id.webView);
        spinner = requireActivity().findViewById(R.id.countrySelection);
        Bundle bundle = getArguments();
        if (bundle != null){
            String url = bundle.getString("url");
            webView.loadUrl(url);
           // getActivity().onBackPressed();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        spinner.setVisibility(View.GONE);
        materialToolbar = requireActivity().findViewById(R.id.topNewsToolBar);
        materialToolbar.setNavigationIcon(R.drawable.ic_back_icon);
        materialToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        spinner.setVisibility(View.VISIBLE);
        materialToolbar.setNavigationIcon(null);
    }
}