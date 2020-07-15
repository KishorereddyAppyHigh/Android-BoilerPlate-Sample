package com.appyhigh.sampleboilerplateapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.hardware.input.InputManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appyhigh.sampleboilerplateapplication.notifications.NotificationService;
import com.appyhigh.sampleboilerplateapplication.utility.Notes;
import com.appyhigh.sampleboilerplateapplication.utility.SharedPreferenceUtil;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText titleEditText, descriptionEditText;
    private Button saveButton;
    private TextView notesTextView;
    private CoordinatorLayout coordinatorLayout;
    public static final String channel = "MyNotifications";
    private MaterialToolbar materialToolbar;
    private SharedPreferenceUtil sharedPreferenceUtil;
    private InterstitialAd interstitialAd;
    private FirebaseAnalytics firebaseAnalytics;
    private FirebaseFirestore firebaseFirestore;
    boolean doubleBackToExitPressedOnce = false;
    //private DocumentReference documentReference = firebaseFirestore.collection("NoteBook").document("Note");
    private AdView adView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        materialToolbar = findViewById(R.id.topToolBar);
        titleEditText = findViewById(R.id.titleEditText);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        saveButton = findViewById(R.id.saveButton);
        notesTextView = findViewById(R.id.textView);
        sharedPreferenceUtil = new SharedPreferenceUtil(this);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        setSupportActionBar(materialToolbar);
        adView = findViewById(R.id.adView);
        FirebaseAnalytics.getInstance(this).logEvent("main_screen_opened", null);
        FirebaseInAppMessaging.getInstance().triggerEvent("main_screen_opened");
        loadBannerAdd();
        createNotification();
        checkForDynamicLinks();
        saveNotes();
        hideKeyBoardWhenClickOutsideView();
    }

    private void hideKeyBoardWhenClickOutsideView() {
        coordinatorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager) v.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce){
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press Back Button to Exit ", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        },5000);
    }

    private void saveNotes() {
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleEditText.getText().toString().isEmpty()){
                    titleEditText.setError("Title Cannot Be Empty ");
                    return;
                }
                if (descriptionEditText.getText().toString().isEmpty()){
                    descriptionEditText.setError("Description Cannot Be Empty");
                    return;
                }
                titleEditText.setError(null);
                descriptionEditText.setError(null);
                String title = titleEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                Notes notes = new Notes(title, description);
                Bundle bundle = new Bundle();
                bundle.putString("title", notes.getTitle());
                bundle.putString("description", notes.getDescription());
                firebaseAnalytics.logEvent("save_notes", bundle);
                firebaseFirestore.collection("NoteBook").document("Note").set(notes)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Note Added Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void checkForDynamicLinks() {
        FirebaseDynamicLinks.getInstance().getDynamicLink(getIntent()).
                addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                        }
                        if (deepLink != null) {
                            String currentPage = deepLink.getQueryParameter("curPage");
                            assert currentPage != null;
                            int curPage = Integer.parseInt(currentPage);
//                    ViewPager.setCurrentItem(curPage);
                        }
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void createNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NotificationService.channelId, channel,
                    NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic("weather")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String message = "Success";
                        if (!task.isSuccessful()) {
                            message = "failed";
                        }
                        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseFirestore.collection("NoteBook").document("Note")
                .addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(MainActivity.this, "Error While Loading", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        assert value != null;
                        if (value.exists()) {
                            String title = value.getString("title");
                            String description = value.getString("description");
                            Notes notes = new Notes(title, description);
                            notesTextView.setText("Title:   " + notes.getTitle() + "\n" + "Description:   " + notes.getDescription());
                        } else {
                            Toast.makeText(MainActivity.this, "Document Does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        checkForDynamicLinks();
    }

    private void loadBannerAdd() {
        List<String> testDevices = new ArrayList<>();
        testDevices.add(AdRequest.DEVICE_ID_EMULATOR);
        testDevices.add("2D1A1ED41BC6E3DFBC9B9C5CAF0D2E27");
        RequestConfiguration requestConfiguration = new RequestConfiguration.Builder()
                .setTestDeviceIds(testDevices)
                .build();
        MobileAds.setRequestConfiguration(requestConfiguration);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                Toast.makeText(MainActivity.this, "add clicked ", Toast.LENGTH_SHORT).show();
            }

        });
    }
}