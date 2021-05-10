package ua.nure.floristikup.ui.storages;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.FlowerStorage;
import ua.nure.floristikup.data.Flower;
import ua.nure.floristikup.data.Placement;
import ua.nure.floristikup.network.JSONPlaceHolderApi;
import ua.nure.floristikup.network.NetworkService;

public class CreateStorageActivity extends AppCompatActivity {
//
//    private static final String TAG = "SignContractActivity";
//
//    private List<FlowerStorage> mFlowerStorages;
//    private JSONPlaceHolderApi mApi;
//    ImageView mBack;
//    Button mSignContractButton;
//    Spinner mServicesSpinner, mRoomsSpinner;
//    FlowerStorage mFlowerStorage;
//
//    int roomId;
//    int serviceId;
//    int[] servicesId;
//    List<Flower> mFlowers;
//    List<Placement> mPlacements;
//    String token;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.create_storage_activity);
//
//        mBack = findViewById(R.id.back_btn);
//        mSignContractButton = findViewById(R.id.sign_contract_btn);
//        mSignContractButton.setOnClickListener(v -> {
//            signContract();
//        });
//
//        mApi = NetworkService.getInstance().getApiService();
//        mFlowerStorage = new FlowerStorage();
//
//        mServicesSpinner = findViewById(R.id.get_service_spinner);
//        mServicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                serviceId = position;
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        mRoomsSpinner = findViewById(R.id.get_room_spinner);
//        mRoomsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                roomId = mPlacements.get(position).getId();
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        getData();
//    }
//
//    Callback<ArrayList<Flower>> serviceCallback = new Callback<ArrayList<Flower>>() {
//        @Override
//        public void onResponse(Call<ArrayList<Flower>> call, Response<ArrayList<Flower>> response) {
//            if (!response.isSuccessful()) {
//                System.out.println(response.code());
//                return;
//            }
//            mFlowers = response.body();
//            String[] names = new String[mFlowers.size()];
//            servicesId = new int[mFlowers.size()];
//            int i = 0;
//            for (Flower flower : mFlowers) {
//                names[i] = flower.getName();
//                servicesId[i] = flower.getId();
//                i++;
//            }
//            initServicesAdapter(names);
//        }
//
//        @Override
//        public void onFailure(Call<ArrayList<Flower>> call, Throwable t) {
//            System.out.println(t);
//            Log.i(TAG, t.getMessage());
//        }
//    };
//
//    Callback<ArrayList<Placement>> roomsCallback = new Callback<ArrayList<Placement>>() {
//        @Override
//        public void onResponse(Call<ArrayList<Placement>> call, Response<ArrayList<Placement>> response) {
//            if (!response.isSuccessful()) {
//                System.out.println(response.code());
//                return;
//            }
//            mPlacements = response.body();
//            Integer[] ids = new Integer[mPlacements.size()];
//            int i = 0;
//            for (Placement placement : mPlacements) {
//                ids[i] = placement.getId();
//                i++;
//            }
//            initRoomsAdapter(ids);
//        }
//
//        @Override
//        public void onFailure(Call<ArrayList<Placement>> call, Throwable t) {
//            System.out.println(t);
//            Log.i(TAG, t.getMessage());
//        }
//    };
//
//    Callback<FlowerStorage> signContractCallback = new Callback<FlowerStorage>() {
//        @Override
//        public void onResponse(Call<FlowerStorage> call, Response<FlowerStorage> response) {
//            if (!response.isSuccessful()) {
//                System.out.println(response.code());
//                return;
//            }
//            finish();
//        }
//
//        @Override
//        public void onFailure(Call<FlowerStorage> call, Throwable t) {
//            System.out.println(t);
//            Log.i(TAG, t.getMessage());
//        }
//    };
//
//    private void getData() {
//        String email = FloristShop.getInstance().getEmail();
//        token = "Bearer " + FloristShop.getInstance().getToken();
//
//        mApi.getFlowersData(token, getIntent().getStringExtra("cEmail")).enqueue(serviceCallback);
//        mApi.getPlacementsData(token, email).enqueue(roomsCallback);
//    }
//
//    private void initServicesAdapter(String[] servicesData){
//        ArrayAdapter<String> adapter = new ArrayAdapter(this,
//                android.R.layout.simple_list_item_1, servicesData);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mServicesSpinner.setAdapter(adapter);
//    }
//
//    private void initRoomsAdapter(Integer[] roomsData){
//        ArrayAdapter<String> adapter = new ArrayAdapter(this,
//                android.R.layout.simple_list_item_1, roomsData);
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        mRoomsSpinner.setAdapter(adapter);
//    }
//
//    private void signContract() {
//        mFlowerStorage.setCleaningServiceId(servicesId[serviceId]);
//        mFlowerStorage.setRoomId(roomId);
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
//        mFlowerStorage.setDate(dateFormat.format(new Date()));
//
//            NetworkService.getInstance()
//                    .getApiService()
//                    .createFlowerStorage(token, mFlowerStorage)
//                    .enqueue(signContractCallback);
//
//    }
//
}