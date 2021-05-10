package ua.nure.floristikup.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ua.nure.floristikup.R;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        init();
    }

    private void init() {
        findViewById(R.id.rooms_btn).setOnClickListener(v -> goTo(PlacementsActivity.class));
        findViewById(R.id.profile_btn).setOnClickListener(v -> goTo(ProfileActivity.class));
        findViewById(R.id.flowers_btn).setOnClickListener(v -> goTo(FlowersActivity.class));
    }

    private void goTo(Class cls) {
        Intent intent = new Intent(MenuActivity.this, cls);
        startActivity(intent);
    }
}