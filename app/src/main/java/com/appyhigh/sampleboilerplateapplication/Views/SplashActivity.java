package com.appyhigh.sampleboilerplateapplication.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.os.Handler;

import com.appyhigh.sampleboilerplateapplication.BuildConfig;
import com.appyhigh.sampleboilerplateapplication.R;
import com.appyhigh.sampleboilerplateapplication.utility.SharedPreferenceUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private boolean showAds = true;
    private String showAdsString = "yes";
    private InterstitialAd interstitialAd;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private FirebaseRemoteConfig firebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash);
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                nextActivity();
//            }
//        },1500);
        loadInterstitialAdd();
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        getRemoteConfig();
    }

    private void loadInterstitialAdd() {
        interstitialAd = new InterstitialAd(this);

        if (BuildConfig.DEBUG) {
            interstitialAd.setAdUnitId(getString(R.string.interstitial_test_id));
        } else {
            interstitialAd.setAdUnitId(getString(R.string.intertitialId));
        }
        //interstitialAd.loadAd(new AdRequest.Builder().build());

        interstitialAd.setAdListener(new AdListener() {
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
                nextActivity();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                nextActivity();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
    }

    private void getRemoteConfig() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings firebaseRemoteConfigSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(1500)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(firebaseRemoteConfigSettings);
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);
        firebaseRemoteConfig.fetchAndActivate()
                .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            boolean updated = task.getResult();
                            firebaseRemoteConfig.activate();
                        }
                        showAdsString = firebaseRemoteConfig.getString("display_ads");
                        showAds = showAdsString.equals("yes");
                        sharedPreferenceUtil.saveBoolean("ADS", showAds);
                        if (showAds) {
                            List<String> testDevices = new ArrayList<>();
                            testDevices.add(AdRequest.DEVICE_ID_EMULATOR);
                            testDevices.add("2D1A1ED41BC6E3DFBC9B9C5CAF0D2E27");
                            RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
                                    .setTestDeviceIds(testDevices)
                                    .build();
                            MobileAds.setRequestConfiguration(requestConfiguration);
                            interstitialAd.loadAd(new AdRequest.Builder().build());
                        }else {
                            nextActivity();
                        }
                    }
                });

    }

    public void nextActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
//
//    private void checkForUpdates(){
//        if (BuildConfig.DEBUG){
//            nextActivity();
//        }else {
//            final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(this);
//            appUpdateManager.getAppUpdateInfo()
//            .addOnSuccessListener(appUpdateInfo -> {
//                if (appUpdateInfo.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS){
//                    try {
//                        appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.IMMEDIATE,this,200);
//                    } catch (IntentSender.SendIntentException e) {
//                        e.printStackTrace();
//                        nextActivity();
//                    }
//                }else {
//                    com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
//                    appUpdateInfoTask.addOnSuccessListener(appUpdateInfoNew -> {
//                        if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
//                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
//                            appUpdateManager.startUpdateFlowForResult(
//                                    appUpdateInfoNew ,
//
//                            )
//                        }
//                    });
//                }
//            });
//        }
//    }

}