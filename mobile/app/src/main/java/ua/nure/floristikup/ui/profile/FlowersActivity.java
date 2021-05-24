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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.Flower;
import ua.nure.floristikup.network.JSONPlaceHolderApi;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.add.AddFlowerActivity;
import ua.nure.floristikup.ui.rva.FlowersRVA;
import ua.nure.floristikup.ui.util.LoadingDialog;
import ua.nure.floristikup.ui.util.NavigationBottomMenu;

public class FlowersActivity extends AppCompatActivity {

    private static final String TAG = "ServicesActivity";

    private List<Flower> flowers;
    private RecyclerView recyclerView;
    private JSONPlaceHolderApi apiService;
    private final LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flowers);

        recyclerView = findViewById(R.id.flowers_rv);

        ImageButton mBackButton = findViewById(R.id.back_btn);
        ImageButton mAddButton = findViewById(R.id.add_flower_btn);

        apiService = NetworkService.getInstance().getApiService();

        LinearLayoutManager llm = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);

        mAddButton.setOnClickListener((v) -> {
            navigateToScreen(AddFlowerActivity.class);
            finish();
        });

        BottomNavigationView navigation = findViewById(R.id.bottom_navigation_flowers);
        navigation.setOnNavigationItemSelectedListener(NavigationBottomMenu.getOnNavigationItemSelectedListener(FlowersActivity.this));
        NavigationBottomMenu.setCheckedItem(navigation.getMenu(), R.id.navigation_flowers);
    }

    @Override
    protected void onResume() {
        super.onResume();
        flowers = new ArrayList<>();
        initializeData();
    }

    private void initializeData() {
        loadingDialog.start();
        String token = "Bearer " + FloristShop.getInstance().getToken();
        apiService.getFlowersData(token).enqueue(serviceCallback);
    }

    Callback<ArrayList<Flower>> serviceCallback = new Callback<ArrayList<Flower>>() {
        @Override
        public void onResponse(Call<ArrayList<Flower>> call, Response<ArrayList<Flower>> response) {
            if (!response.isSuccessful()) {
                System.out.println(response.code());
                return;
            }
            ArrayList<Flower> flowerList = response.body();
            for (Flower flower : flowerList) {
                flowers.add(
                        new Flower()
                                .setId(flower.getId())
                                .setName(flower.getName())
                                .setColor(flower.getColor())
                                .setShelfLife(flower.getShelfLife())
                                .setMinTemperature(flower.getMinTemperature())
                                .setMaxTemperature(flower.getMaxTemperature())
                );
            }
            initializeAdapter();
            loadingDialog.dismiss();
        }

        @Override
        public void onFailure(Call<ArrayList<Flower>> call, Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
            loadingDialog.dismiss();
        }
    };

    private void initializeAdapter() {
        FlowersRVA adapter = new FlowersRVA(this, flowers);
        recyclerView.setAdapter(adapter);
        loadingDialog.dismiss();
    }

    private void navigateToScreen(Class cls) {
        Intent intent = new Intent(FlowersActivity.this, cls);
        startActivity(intent);
    }
}
