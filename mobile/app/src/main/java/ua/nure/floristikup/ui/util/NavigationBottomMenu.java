package ua.nure.floristikup.ui.util;

import android.annotation.SuppressLint;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ua.nure.floristikup.R;
import ua.nure.floristikup.ui.profile.FlowersActivity;
import ua.nure.floristikup.ui.profile.PlacementsActivity;
import ua.nure.floristikup.ui.profile.ProfileActivity;

public class NavigationBottomMenu {

    @SuppressLint("NonConstantResourceId")
    public static BottomNavigationView.OnNavigationItemSelectedListener
    getOnNavigationItemSelectedListener(AppCompatActivity activity) {
        return item -> {
            switch (item.getItemId()) {
            case R.id.navigation_placements:
                navigateToScreen(activity, PlacementsActivity.class);
                return true;
            case R.id.navigation_flowers:
                navigateToScreen(activity, FlowersActivity.class);
                return true;
            case R.id.navigation_profile:
                navigateToScreen(activity, ProfileActivity.class);
                return true;
            }
            return false;
        };
    }

    private static void navigateToScreen(AppCompatActivity activity, Class cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }
}
