package com.appyhigh.sampleboilerplateapplication.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.appyhigh.sampleboilerplateapplication.R;
import com.appyhigh.sampleboilerplateapplication.Views.Fragments.ItemFragment;
import com.appyhigh.sampleboilerplateapplication.utility.SharedPreferenceUtil;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetNewsActivity extends AppCompatActivity {
    private MaterialToolbar materialToolbar;
    private Spinner spinner;
    private SharedPreferenceUtil sharedPreferenceUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_news);
        materialToolbar = findViewById(R.id.topNewsToolBar);
        spinner = findViewById(R.id.countrySelection);
        sharedPreferenceUtil = new SharedPreferenceUtil(GetNewsActivity.this);
        setSupportActionBar(materialToolbar);
        addNewsFragment();
        showDropDownMenu();
    }

    private void showDropDownMenu() {
        List<String> list = new ArrayList<>();
        list.add("in");
        list.add("ca");

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(GetNewsActivity.this ,
                R.layout.support_simple_spinner_dropdown_item,getResources().getStringArray(R.array.countryNames));
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        String name = sharedPreferenceUtil.getStringValue("country_Name");
        int position = arrayAdapter.getPosition(name);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(position);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GetNewsActivity.this, spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                sharedPreferenceUtil.saveString("country_Name",spinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void addNewsFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        ItemFragment itemFragment = new ItemFragment();
        fragmentTransaction.replace(R.id.fragment_container,itemFragment);
        fragmentTransaction.commit();
    }
}