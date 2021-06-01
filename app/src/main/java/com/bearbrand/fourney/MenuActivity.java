package com.bearbrand.fourney;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.bearbrand.fourney.activity.AuthActivity;
import com.bearbrand.fourney.activity.SplashActivity;
import com.bearbrand.fourney.drawer.DrawerAdapter;
import com.bearbrand.fourney.drawer.DrawerItem;
import com.bearbrand.fourney.drawer.SimpleItem;
import com.bearbrand.fourney.drawer.SpaceItem;
import com.bearbrand.fourney.ui.history.HistoryFragment;
import com.bearbrand.fourney.ui.home.HomeFragment;
import com.bearbrand.fourney.ui.leaderboard.LeaderboardFragment;
import com.bearbrand.fourney.ui.profile.ProfileFragment;
import com.bearbrand.fourney.ui.reward.RewardFragment;
import com.bearbrand.fourney.ui.splash.screen.StartAuthFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

public class MenuActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {
    private static final int POS_CLOSE = 0;
    private static final int POS_HOME = 1;
    private static final int POS_HISTORY = 2;
    private static final int POS_REWARD = 3;
    private static final int POS_LEADERBOARD = 4;
    private static final int POS_PROFIL = 5;
    private static final int POS_LOGOUT = 7;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        checkUser();




    }

    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

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

        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();


    }

    private void showFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Toast.makeText(this, "Ada user", Toast.LENGTH_LONG).show();
            DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_CLOSE),
                    createItemFor(POS_HOME).setChecked(true),
                    createItemFor(POS_HISTORY),
                    createItemFor(POS_REWARD),
                    createItemFor(POS_LEADERBOARD),
                    createItemFor(POS_PROFIL),
                    new SpaceItem(260),
                    createItemFor(POS_LOGOUT)
            ));

            adapter.setListener(this);

            RecyclerView list = findViewById(R.id.list);
            list.setNestedScrollingEnabled(false);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);

            adapter.setSelected(POS_HOME);
        } else {
            Toast.makeText(this, "Tidak ada user", Toast.LENGTH_LONG).show();
            DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(POS_CLOSE),
                    createItemFor(POS_HOME).setChecked(true),
                    createItemFor(POS_HISTORY),
                    createItemFor(POS_REWARD),
                    createItemFor(POS_LEADERBOARD),
                    createItemFor(POS_PROFIL),
                    new SpaceItem(260)
            ));

            adapter.setListener(this);

            RecyclerView list = findViewById(R.id.list);
            list.setNestedScrollingEnabled(false);
            list.setLayoutManager(new LinearLayoutManager(this));
            list.setAdapter(adapter);

            adapter.setSelected(POS_HOME);
        }
    }
}