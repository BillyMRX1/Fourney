package com.bearbrand.fourney;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.bearbrand.fourney.activity.AuthActivity;

import com.bearbrand.fourney.activity.SplashActivity;
import com.bearbrand.fourney.drawer.DrawerAdapter;
import com.bearbrand.fourney.drawer.DrawerItem;
import com.bearbrand.fourney.drawer.SimpleItem;
import com.bearbrand.fourney.drawer.SpaceItem;
import com.bearbrand.fourney.model.HistoryModel;
import com.bearbrand.fourney.model.UserModel;
import com.bearbrand.fourney.ui.history.HistoryFragment;
import com.bearbrand.fourney.ui.home.HomeFragment;
import com.bearbrand.fourney.ui.leaderboard.LeaderboardFragment;
import com.bearbrand.fourney.ui.profile.ProfileFragment;
import com.bearbrand.fourney.ui.reward.RewardFragment;
import com.bearbrand.fourney.ui.splash.screen.StartAuthFragment;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.OnProgressListener;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.jetbrains.annotations.NotNull;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class MenuActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener, OnCompleteListener<Void> {
    private static final int POS_CLOSE = 0;
    private static final int POS_HOME = 1;
    private static final int POS_HISTORY = 2;
    private static final int POS_REWARD = 3;
    private static final int POS_LEADERBOARD = 4;
    private static final int POS_PROFIL = 5;
    private static final int POS_LOGOUT = 7;

    private NotificationAlarmManager alarmReceiver;
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "channel_01";
    private static final CharSequence CHANNEL_NAME = "dicoding channel";


    private String[] screenTitles;
    private Drawable[] screenIcons;
    String temp = "";

    private SlidingRootNav slidingRootNav;
    FirebaseUser user;
    DocumentReference ref;
    CollectionReference reference;

    //GEOFENCE
    private static final String TAG = MenuActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private enum PendingGeofenceTask {
        ADD, REMOVE, NONE
    }

    private GeofencingClient mGeofencingClient;
    private ArrayList<Geofence> mGeofenceList;
    private PendingIntent mGeofencePendingIntent;
    private PendingIntent mGeofencePendingIntentPositive;

    private PendingGeofenceTask mPendingGeofenceTask = PendingGeofenceTask.NONE;
    static HashMap<String, LatLng> BAY_AREA_LANDMARKS = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mGeofenceList = new ArrayList<>();

        user = FirebaseAuth.getInstance().getCurrentUser();
        new FetchFeed().execute();

        alarmReceiver = new NotificationAlarmManager();

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            checkUserLocation();

        }
    }

    //DIPANGGIL
    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(mGeofenceList);

        return builder.build();
    }


    //DIPANGGIL
    private void addGeofences() {
        if (!checkPermissions()) {
            showSnackbar(getString(R.string.insufficient_permissions));
            return;
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions();
            return;
        }
        mGeofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnCompleteListener(this);
    }


    @Override
    public void onComplete(@NonNull Task<Void> task) {
        mPendingGeofenceTask = PendingGeofenceTask.NONE;
        if (task.isSuccessful()) {
            updateGeofencesAdded(!getGeofencesAdded());

            user = FirebaseAuth.getInstance().getCurrentUser();
            ref = FirebaseFirestore.getInstance().collection("users").document(user.getUid());
            ref.update("location", "");


        } else {
            // Get the status code for the error and log it using a user-friendly message.
            String errorMessage = GeofenceErrorMessages.getErrorString(this, task.getException());
            Log.w(TAG, errorMessage);
        }
    }


    //DIPANGGIL
    private PendingIntent getGeofencePendingIntent() {
        //update location in firestore
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        //BUAT NAMPILIN NOTIFIKASI
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        mGeofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    //DIPANGGIL
    private void populateGeofenceList(HashMap<String, LatLng> dataLandmarks) {

        for (Map.Entry<String, LatLng> entry : dataLandmarks.entrySet()) {

            mGeofenceList.add(new Geofence.Builder()
                    // Set the request ID of the geofence. This is a string to identify this
                    // geofence.
                    .setRequestId(entry.getKey())

                    // Set the circular region of this geofence.
                    .setCircularRegion(
                            entry.getValue().latitude,
                            entry.getValue().longitude,
                            GeoConstants.GEOFENCE_RADIUS_IN_METERS
                    )

                    // Set the expiration duration of the geofence. This geofence gets automatically
                    // removed after this period of time.
                    .setExpirationDuration(GeoConstants.GEOFENCE_EXPIRATION_IN_MILLISECONDS)

                    // Set the transition types of interest. Alerts are only generated for these
                    // transition. We track entry and exit transitions in this sample.
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER |
                            Geofence.GEOFENCE_TRANSITION_EXIT)

                    // Create the geofence.
                    .build());
        }

    }

    //DIPANGGIL
    private void performPendingGeofenceTask() {
        if (mPendingGeofenceTask == PendingGeofenceTask.ADD) {
            addGeofences();
        }
    }

    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    private boolean getGeofencesAdded() {
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean(
                GeoConstants.GEOFENCES_ADDED_KEY, false);
    }


    private void updateGeofencesAdded(boolean added) {
        PreferenceManager.getDefaultSharedPreferences(this)
                .edit()
                .putBoolean(GeoConstants.GEOFENCES_ADDED_KEY, added)
                .apply();
    }


    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(MenuActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i(TAG, "Requesting permission");
            ActivityCompat.requestPermissions(MenuActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Permission granted.");
                performPendingGeofenceTask();
            } else {
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
                mPendingGeofenceTask = PendingGeofenceTask.NONE;
            }
        }
    }


    //DRAWER

    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (user != null) {
            if (position == POS_CLOSE) {
                slidingRootNav.closeMenu();
            } else if (position == POS_HOME) {
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.container, homeFragment);
            } else if (position == POS_HISTORY) {
                HistoryFragment historyFragment = new HistoryFragment();
                transaction.replace(R.id.container, historyFragment);
            } else if (position == POS_REWARD) {
                RewardFragment rewardFragment = new RewardFragment();
                transaction.replace(R.id.container, rewardFragment);
            } else if (position == POS_LEADERBOARD) {
                LeaderboardFragment leaderBoardFragment = new LeaderboardFragment();
                transaction.replace(R.id.container, leaderBoardFragment);
            } else if (position == POS_PROFIL) {
                ProfileFragment profilFragment = new ProfileFragment();
                transaction.replace(R.id.container, profilFragment);
            } else if (position == POS_LOGOUT) {
                FirebaseAuth.getInstance().signOut();
                Intent in = new Intent(this, AuthActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(in);
                finish();
            }
        } else {
            if (position == POS_CLOSE) {
                slidingRootNav.closeMenu();
            } else if (position == POS_HOME) {
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.container, homeFragment);
            } else if (position == POS_HISTORY) {
                startActivity(new Intent(this, AuthActivity.class));
            } else if (position == POS_REWARD) {
                startActivity(new Intent(this, AuthActivity.class));
            } else if (position == POS_LEADERBOARD) {
                startActivity(new Intent(this, AuthActivity.class));
            } else if (position == POS_PROFIL) {
                startActivity(new Intent(this, AuthActivity.class));
            }
        }
        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.gray))
                .withTextTint(color(R.color.gray))
                .withSelectedIconTint(color(R.color.blue))
                .withSelectedTextTint(color(R.color.blue));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity(); // or finish();
    }

    private void checkUser() {

        if (user != null) {
            DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_CLOSE),
                    createItemFor(POS_HOME).setChecked(true),
                    createItemFor(POS_HISTORY),
                    createItemFor(POS_REWARD),
                    createItemFor(POS_LEADERBOARD),
                    createItemFor(POS_PROFIL),
                    new SpaceItem(100),
                    createItemFor(POS_LOGOUT)
            ));

            adapter.setListener(this);

            RecyclerView list = findViewById(R.id.list);
            list.setNestedScrollingEnabled(false);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);

            adapter.setSelected(POS_HOME);

        } else {
            DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_CLOSE),
                    createItemFor(POS_HOME).setChecked(true),
                    createItemFor(POS_HISTORY),
                    createItemFor(POS_REWARD),
                    createItemFor(POS_LEADERBOARD),
                    createItemFor(POS_PROFIL),
                    new SpaceItem(100)
            ));

            adapter.setListener(this);

            RecyclerView list = findViewById(R.id.list);
            list.setNestedScrollingEnabled(false);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);

            adapter.setSelected(POS_HOME);
        }
    }

    private void checkUserLocation() {
        if (user != null) {
            checkNotification();
            reference = FirebaseFirestore.getInstance().collection("place");
            reference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                    if (!checkPermissions()) {
                        mPendingGeofenceTask = PendingGeofenceTask.ADD;
                        requestPermissions();
                        return;
                    }

                    for (DocumentSnapshot document : task.getResult()) {
                        BAY_AREA_LANDMARKS.put(document.getString("title"), new LatLng(Double.valueOf(document.getString("latitude")), Double.valueOf(document.getString("longitude"))));
                    }

                    //GEOFENCE
                    populateGeofenceList(BAY_AREA_LANDMARKS);

                    Log.d(TAG, BAY_AREA_LANDMARKS.toString());

                    addGeofences();
                    performPendingGeofenceTask();

                }
            });
        }
    }


    private class FetchFeed extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... params) {
            return null;
        }

        protected void onPostExecute(Void param) {
            checkUser();
        }

    }

     private void checkNotification() {
        CollectionReference data = FirebaseFirestore.getInstance().collection("users");
        Query query = data.whereEqualTo("status", "Positive");
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                for (DocumentSnapshot document : value) {
                    Log.d("Status Positive", document.getString("uid"));
                    DocumentReference historyRef = FirebaseFirestore.getInstance().collection("history").document(document.getString("uid"));
                    historyRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot doc = task.getResult();
                            List<Object> mapPositive = new ArrayList<>();
                            Map<String, Object> mapPlace = null;
                            Map<String, Object> map = doc.getData();
                            List<Object> arrayDataPositive = new ArrayList<>();

                            if (doc.exists()) {
                                List<Map<String, Object>> place = (List<Map<String, Object>>) doc.get("place");
                                for (Map<String, Object> i : place) {
                                    mapPositive.add(i);
                                }
                                for (int i = 0; i < mapPositive.size(); i++) {
                                    //send notif
                                    mapPlace = (HashMap<String, Object>) mapPositive.get(i);
                                    mapPlace.remove("idObject");
                                    arrayDataPositive.add(mapPlace);
                                }
                                Log.d("SEND NOTIF", arrayDataPositive.toString());

                                DocumentReference historyUser = FirebaseFirestore.getInstance().collection("history").document(user.getUid());
                                historyUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot userDoc = task.getResult();
                                        List<Object> mapUser = new ArrayList<>();
                                        List<Object> arrayDataUser = new ArrayList<>();
                                        Map<String, Object> mapPlaceUser = null;
                                        if (userDoc.exists()) {
                                            List<Map<String, Object>> place = (List<Map<String, Object>>) userDoc.get("place");
                                            for (Map<String, Object> i : place) {
                                                mapUser.add(i);
                                            }
                                            for (int i = 0; i < mapUser.size(); i++) {
                                                //send notif
                                                mapPlaceUser = (HashMap<String, Object>) mapUser.get(i);
                                                mapPlaceUser.remove("idObject");
                                                arrayDataUser.add(mapPlaceUser);
                                            }

                                            Log.d("SEND NOTIF", arrayDataUser.toString());

                                            for(int i = 0; i < arrayDataPositive.size(); i++){
                                                for (int j = 0; j<arrayDataUser.size(); j++){
                                                    if (mapPositive.get(i).equals(mapUser.get(j))){
                                                        //send notif
                                                        sendNotification(arrayDataPositive.toString());

                                                    }
                                                }
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });

                }
            }
        });
    }

    public void sendNotification(String data) {
        if(!data.equals(temp)) {
            Intent intent = new Intent(this, MenuActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo_blue)
                    .setColor(getResources().getColor(R.color.blue))
                    .setContentTitle("Peringatan Karantina Mandiri")
                    .setContentText("Berdasarkan kunjunganmu tanggal 10 Juni 2021 di Batu Night Spectaculer, terindikasi pengunjung mengalami gejala Covid 19. Segera lakukan karantina mandiri.")
                    .setAutoCancel(true);

        /*
        Untuk android Oreo ke atas perlu menambahkan notification channel
        */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                /* Create or update. */
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);

                mBuilder.setChannelId(CHANNEL_ID);

                if (mNotificationManager != null) {
                    mNotificationManager.createNotificationChannel(channel);
                }
            }

            Notification notification = mBuilder.build();

            if (mNotificationManager != null) {
                mNotificationManager.notify(NOTIFICATION_ID, notification);
            }

            temp = data;
        }
    }



}