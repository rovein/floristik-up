package ua.nure.floristikup.ui.edit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.Flower;
import ua.nure.floristikup.network.JSONPlaceHolderApi;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.profile.MenuActivity;
import ua.nure.floristikup.ui.util.LoadingDialog;

public class EditFlowerActivity extends AppCompatActivity {

    private static final String TAG = "EditFlowerActivity";
    Button mCancelButton, mSaveButton;
    Flower mFlower;
    TextView labelTV;
    EditText mNameET, colorET, shelfLifeET, minTempET, maxTempET;
    private JSONPlaceHolderApi apiService;
    String token;
    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flower);

        mCancelButton = findViewById(R.id.cancel_add_flower_btn);
        mSaveButton = findViewById(R.id.save_add_flower_btn);

        labelTV = findViewById(R.id.add_service_label);
        mNameET = findViewById(R.id.add_name_input);
        colorET = findViewById(R.id.add_city_label);
        shelfLifeET = findViewById(R.id.add_shelf_life_input);
        minTempET = findViewById(R.id.add_min_temp_input);
        maxTempET = findViewById(R.id.add_max_temp_input);

        labelTV.setText(R.string.edit_flower);
        token = "Bearer " + FloristShop.getInstance().getToken();

        apiService = NetworkService.getInstance().getApiService();

        initializeFlower();

        mSaveButton.setOnClickListener(v -> {
            saveFlower();
        });

        mCancelButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void initializeFlower() {
        loadingDialog.start();
        apiService
                .getFlower(token, getIntent().getIntExtra("fId", -1))
                .enqueue(flowerCallback);
    }

    Callback<Flower> flowerCallback = new Callback<Flower>() {
        @Override
        public void onResponse(Call<Flower> call, Response<Flower> response) {
            if (response.isSuccessful()) {
                Flower flower = response.body();

                mNameET.setText(flower.getName());
                colorET.setText(flower.getColor());
                String shelfLife = Integer.toString(flower.getShelfLife());
                shelfLifeET.setText(shelfLife);
                minTempET.setText(Integer.toString(flower.getMinTemperature()));
                maxTempET.setText(Integer.toString(flower.getMaxTemperature()));

                mFlower = new Flower()
                        .setName(flower.getName())
                        .setColor(flower.getColor())
                        .setShelfLife(flower.getShelfLife())
                        .setMinTemperature(flower.getMinTemperature())
                        .setMaxTemperature(flower.getMaxTemperature());
            }
            loadingDialog.dismiss();
        }

        @Override
        public void onFailure(Call<Flower> call, Throwable t) {
            Log.i(TAG, t.getMessage());
            loadingDialog.dismiss();
        }
    };

    private void saveFlower() {
        mFlower.setId(getIntent().getIntExtra("fId", -1));
        mFlower.setName(mNameET.getText().toString());
        mFlower.setColor(colorET.getText().toString());
        mFlower.setShelfLife(Integer.parseInt(shelfLifeET.getText().toString()));
        mFlower.setMinTemperature(Integer.parseInt(minTempET.getText().toString()));
        mFlower.setMaxTemperature(Integer.parseInt(maxTempET.getText().toString()));

        loadingDialog.start();
        apiService.updateFlower(token, mFlower).enqueue(editFlowerCallback);
        Intent intent = new Intent(EditFlowerActivity.this, MenuActivity.class);
        startActivity(intent);
    }

    Callback<Flower> editFlowerCallback = new Callback<Flower>() {
        @Override
        public void onResponse(Call<Flower> call, Response<Flower> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                loadingDialog.dismiss();
                finish();
            }
        }

        @Override
        public void onFailure(Call<Flower> call, Throwable t) {
            Log.i(TAG, t.getMessage());
            loadingDialog.dismiss();
        }
    };

}
