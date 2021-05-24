package ua.nure.floristikup.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.Placement;
import ua.nure.floristikup.network.JSONPlaceHolderApi;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.add.AddPlacementActivity;
import ua.nure.floristikup.ui.rva.PlacementsRVA;
import ua.nure.floristikup.ui.util.LoadingDialog;
import ua.nure.floristikup.ui.util.NavigationBottomMenu;

public class PlacementsActivity extends AppCompatActivity {

    private static final String TAG = "ServicesActivity";

    private List<Placement> mPlacements;
    private RecyclerView mRecyclerView;
    private JSONPlaceHolderApi apiService;
    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placements);

        mRecyclerView = findViewById(R.id.rooms_rv);
        ImageButton backButton = findViewById(R.id.back_btn);
        ImageButton addButton = findViewById(R.id.create_placement_btn);

        apiService = NetworkService.getInstance().getApiService();

        LinearLayoutManager llm = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

        addButton.setOnClickListener((v) -> {
            navigateToScreen(AddPlacementActivity.class);
            finish();
        });

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_placements);
        navigation.setOnNavigationItemSelectedListener(NavigationBottomMenu.getOnNavigationItemSelectedListener(PlacementsActivity.this));
        NavigationBottomMenu.setCheckedItem(navigation.getMenu(), R.id.navigation_placements);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadingDialog.start();
        initializeData();
    }

    private void initializeData(){
        mPlacements = new ArrayList<>();
        String email = FloristShop.getInstance().getEmail();
        String token = "Bearer " + FloristShop.getInstance().getToken();
        apiService.getPlacementsData(token, email).enqueue(roomCallback);
    }


    Callback<ArrayList<Placement>> roomCallback = new Callback<ArrayList<Placement>>() {
        @Override
        public void onResponse(Call<ArrayList<Placement>> call, Response<ArrayList<Placement>> response) {
            if(!response.isSuccessful()) {
                System.out.println(response.code());
                loadingDialog.dismiss();
                return;
            }

            ArrayList<Placement> placementList = response.body();
            for (Placement placement : placementList) {
                mPlacements.add(new Placement()
                        .setId(placement.getId())
                        .setCity(placement.getCity())
                        .setStreet(placement.getStreet())
                        .setHouse(placement.getHouse())
                        .setActualCapacity(placement.getActualCapacity())
                        .setMaxCapacity(placement.getMaxCapacity())
                        .setSmartDevice(placement.getSmartDevice())
                );
            }
            initializeAdapter();
        }

        @Override
        public void onFailure(Call<ArrayList<Placement>> call, Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
            loadingDialog.dismiss();
        }
    };

    private void initializeAdapter(){
        Collections.sort(mPlacements,
                ((placement, anotherPlacement) ->  placement.getId() - anotherPlacement.getId()));
        PlacementsRVA adapter = new PlacementsRVA(this, mPlacements);
        mRecyclerView.setAdapter(adapter);
        loadingDialog.dismiss();
    }

    private void navigateToScreen(Class cls) {
        Intent intent = new Intent(PlacementsActivity.this, cls);
        startActivity(intent);
    }
}
