package com.appyhigh.sampleboilerplateapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Bundle;
import android.widget.Toast;

import com.appyhigh.sampleboilerplateapplication.notifications.NotificationService;
import com.appyhigh.sampleboilerplateapplication.utility.SharedPreferenceUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

public class MainActivity extends AppCompatActivity {
    public static final String channel = "MyNotifications";
    private MaterialToolbar materialToolbar;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private InterstitialAd interstitialAd;
    private FirebaseAnalytics firebaseAnalytics;
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materialToolbar = findViewById(R.id.topToolBar);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        setSupportActionBar(materialToolbar);
        adView = findViewById(R.id.adView);
//        FirebaseAnalytics.getInstance(this).logEvent("main_screen_opened", null);
//        FirebaseInAppMessaging.getInstance().triggerEvent("main_screen_opened");
        loadInterstitialAdd();
        loadBannerAdd();
        createNotification();
    }

    private void createNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NotificationService.channelId,channel,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String message = "Success";
                        if (!task.isSuccessful()){
                            message = "failed";
                        }
                        Toast.makeText(MainActivity.this,message ,Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadInterstitialAdd() {
        interstitialAd = new InterstitialAd(this);

        if (BuildConfig.DEBUG) {
            interstitialAd.setAdUnitId(getString(R.string.interstitial_test_id));
        } else {
            interstitialAd.setAdUnitId(getString(R.string.intertitialId));
        }
        interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                interstitialAd.show();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
    }

    private void loadBannerAdd() {
        AdRequest adRequest = new AdRequest.Builder().build();
//        if (BuildConfig.DEBUG) {
//            adView.setAdUnitId(getString(R.string.banner_test_id));
//        } else {
//            adView.setAdUnitId(getString(R.string.bannerSmallId));
//        }
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Toast.makeText(MainActivity.this, "add clicked ",Toast.LENGTH_SHORT).show();
            }

        });
    }
}