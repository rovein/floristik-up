package ua.nure.floristikup.ui.storages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.Flower;
import ua.nure.floristikup.data.FlowerStorage;
import ua.nure.floristikup.data.Placement;
import ua.nure.floristikup.data.RedistributionResponseDto;
import ua.nure.floristikup.data.SmartDevice;
import ua.nure.floristikup.network.JSONPlaceHolderApi;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.profile.MenuActivity;
import ua.nure.floristikup.ui.profile.PlacementsActivity;
import ua.nure.floristikup.ui.rva.StoragesRVA;
import ua.nure.floristikup.ui.util.LoadingDialog;

public class StoragesActivity extends AppCompatActivity {

    private static final String TAG = "StoragesActivity";

    private List<FlowerStorage> mFlowerStorages;
    private RecyclerView mRecyclerView;
    private JSONPlaceHolderApi apiService;
    private ImageButton mBackButton;
    private ImageButton addButton;
    private Button redistributeButton;
    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storages);

        mRecyclerView = (RecyclerView) findViewById(R.id.storages_rv);
        mBackButton = findViewById(R.id.back_btn);
        addButton =  findViewById(R.id.create_storage_btn);
        redistributeButton = findViewById(R.id.redistribute_button);

        apiService = NetworkService.getInstance().getApiService();

        LinearLayoutManager llm = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);

        mBackButton.setOnClickListener((v) -> {
            navigateToMenuScreen();
            finish();
        });

        redistributeButton.setOnClickListener(view -> new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.redistribute))
                    .setMessage(getString(R.string.are_you_sure_redistribute))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        loadingDialog.start();
                        FlowerStorage storage = mFlowerStorages.get(0);
                        apiService.redistribute(
                                FloristShop.getInstance().getToken(),
                                new SmartDevice()
                                        .setId(storage.getStorageRoomId())
                                        .setTemperature(storage.getTemperature())
                                        .setHumidity(storage.getHumidity())
                                        .setSatisfactionFactor(storage.getSatisfactionFactor())
                                        .setAirQuality(storage.getAirQuality())
                        ).enqueue(redistributionCallback);
                    }
                    ).setNegativeButton(android.R.string.no, null).show());

        addButton.setOnClickListener((v) -> {
            Intent intent = new Intent(StoragesActivity.this, CreateStorageActivity.class);
            intent.putExtra("rId",  getIntent().getIntExtra("rId", -1));
            startActivity(intent);
            finish();
        });

    }

    Callback<ArrayList<RedistributionResponseDto>> redistributionCallback = new Callback<ArrayList<RedistributionResponseDto>>() {
        @SuppressLint("DefaultLocale")
        @Override
        public void onResponse(
                Call<ArrayList<RedistributionResponseDto>> call,
                Response<ArrayList<RedistributionResponseDto>> response
        ) {
            ArrayList<RedistributionResponseDto> jsonArray = response.body();

            StringBuilder resultMessage = new StringBuilder();
            Flower flower;
            Placement placement;

            for (RedistributionResponseDto storage : jsonArray) {
                flower = storage.getFlower();
                placement = storage.getStorageRoom();
                resultMessage.append(String.format(
                        "Квітку %s (%s) у кількості %d перозподілено до приміщення %d (%s, %s %s).\n",
                        flower.getName(), flower.getColor(), storage.getAmount(),
                        placement.getId(), placement.getCity(), placement.getStreet(), placement.getHouse())
                );
            }

            String result = resultMessage.toString();
            if (result.isEmpty()) {
                result = "Перерозподіл не виконано";
            }

            loadingDialog.dismiss();
            new AlertDialog.Builder(StoragesActivity.this)
                    .setTitle(getString(R.string.redistribute))
                    .setMessage(result)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                        startActivity(new Intent(StoragesActivity.this, PlacementsActivity.class));
                    }).show();
        }

        @Override
        public void onFailure(Call<ArrayList<RedistributionResponseDto>> call,
                Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
            loadingDialog.dismiss();
            loadingDialog.dismiss();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        initializeData();
    }

    private void initializeData() {
        loadingDialog.start();
        mFlowerStorages = new ArrayList<>();
        FloristShop floristShop = FloristShop.getInstance();
        String token = "Bearer " + floristShop.getToken();

        apiService.getFlowerStoragesByRoom(token,
                getIntent().getIntExtra("rId", -1)).enqueue(storagesCallback);
    }

    Callback<ArrayList<FlowerStorage>> storagesCallback = new Callback<ArrayList<FlowerStorage>>() {

        @Override
        public void onResponse(Call<ArrayList<FlowerStorage>> call,
                Response<ArrayList<FlowerStorage>> response) {
            if (!response.isSuccessful()) {
                System.out.println(response.code());
                loadingDialog.dismiss();
                return;
            }
            List<FlowerStorage> flowerStorages = response.body();
            for (FlowerStorage storage : flowerStorages) {
                mFlowerStorages.add(new FlowerStorage()
                        .setId(storage.getId())
                        .setStartDate(storage.getStartDate())
                        .setFormattedDate(storage.getFormattedDate())
                        .setFlowerId(storage.getFlowerId())
                        .setStorageRoomId(storage.getStorageRoomId())
                        .setAmount(storage.getAmount())
                        .setFlowerName(storage.getFlowerName())
                        .setFlowerColor(storage.getFlowerColor())
                        .setFlowerShelfLife(storage.getFlowerShelfLife())
                        .setMinTemperature(storage.getMinTemperature())
                        .setMaxTemperature(storage.getMaxTemperature())
                        .setCity(storage.getCity())
                        .setStreet(storage.getStreet())
                        .setHouse(storage.getHouse())
                        .setActualCapacity(storage.getActualCapacity())
                        .setMaxCapacity(storage.getMaxCapacity())
                        .setTemperature(storage.getTemperature())
                        .setHumidity(storage.getHumidity())
                        .setAirQuality(storage.getAirQuality())
                        .setSatisfactionFactor(storage.getSatisfactionFactor())
                );
            }
            initializeAdapter();
        }

        @Override
        public void onFailure(Call<ArrayList<FlowerStorage>> call, Throwable t) {
            System.out.println(t);
            Log.i(TAG, t.getMessage());
            loadingDialog.dismiss();
        }
    };

    @SuppressLint("DefaultLocale")
    private void initializeAdapter() {
        StoragesRVA adapter = new StoragesRVA(this, mFlowerStorages);
        mRecyclerView.setAdapter(adapter);
        loadingDialog.dismiss();

        if (!mFlowerStorages.isEmpty()) {
            TextView header = findViewById(R.id.name_tv);

            FlowerStorage storage = mFlowerStorages.get(0);

            Long storageRoomId = storage.getStorageRoomId();
            String city = storage.getCity();
            String street = storage.getStreet();
            String house = storage.getHouse();
            Integer actualCapacity = storage.getActualCapacity();
            Long maxCapacity = storage.getMaxCapacity();
            String temperature = Double.toString(storage.getTemperature());
            String humidity = Double.toString(storage.getHumidity());

            header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
            header.setText(String.format(
                    "| %s %d \n| %s, %s %s \n| %s %d/%d \n| %s %s °C / %s %s %%",
                    getString(R.string.placement_number), storageRoomId,
                    city, street, house,
                    getString(R.string.capacity), actualCapacity, maxCapacity,
                    getString(R.string.temperature), temperature,
                    getString(R.string.humidity), humidity)
            );
        }
    }

    private void navigateToMenuScreen() {
        Intent intent = new Intent(StoragesActivity.this, MenuActivity.class);
        startActivity(intent);
    }


    private void navigateToScreen(Class cls) {
        Intent intent = new Intent(StoragesActivity.this, cls);
        startActivity(intent);
    }
}
