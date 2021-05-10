package ua.nure.floristikup.ui.edit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.Placement;
import ua.nure.floristikup.network.JSONPlaceHolderApi;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.profile.MenuActivity;
import ua.nure.floristikup.ui.util.LoadingDialog;

public class EditPlacementActivity extends AppCompatActivity {

    private static final String TAG = "EditPlacementActivity";
    Button mCancelButton, mSaveButton;
    Placement mPlacement;
    TextView labelTV;
    EditText cityET, streetET, houseET, maxCapacityET;
    private JSONPlaceHolderApi apiService;
    String token;
    LoadingDialog loadingDialog = new LoadingDialog(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        mCancelButton = findViewById(R.id.cancel_add_room_btn);
        mSaveButton = findViewById(R.id.save_add_room_btn);

        labelTV = findViewById(R.id.add_room_label);
        cityET = findViewById(R.id.add_city_label);
        streetET = findViewById(R.id.add_street_label);
        houseET = findViewById(R.id.add_house_label);
        maxCapacityET = findViewById(R.id.add_max_capacity_input);

        labelTV.setText(R.string.edit_room);
        token = "Bearer " + FloristShop.getInstance().getToken();

        apiService = NetworkService.getInstance().getApiService();

        getRoom();

        mSaveButton.setOnClickListener(v -> saveRoom());
        mCancelButton.setOnClickListener(v -> finish());
    }

    private void getRoom() {
        loadingDialog.start();
        apiService.getPlacement(token, getIntent().getIntExtra("rId", -1)).enqueue(roomCallBack);
    }

    Callback<Placement> roomCallBack = new Callback<Placement>() {
        @SuppressLint("SetTextI18n")
        @Override
        public void onResponse(Call<Placement> call, Response<Placement> response) {
            if (response.isSuccessful()) {
                Placement body = response.body();

                cityET.setText(body.getCity());
                streetET.setText(body.getStreet());
                houseET.setText(body.getHouse());
                maxCapacityET.setText(Integer.toString(body.getMaxCapacity()));
                mPlacement = new Placement()
                        .setId(body.getId())
                        .setCity(body.getCity())
                        .setStreet(body.getStreet())
                        .setHouse(body.getHouse())
                        .setMaxCapacity(body.getMaxCapacity())
                        .setSmartDevice(body.getSmartDevice());
            }
            loadingDialog.dismiss();
        }

        @Override
        public void onFailure(Call<Placement> call, Throwable t) {
            Log.i(TAG, t.getMessage());
            loadingDialog.dismiss();
        }
    };

    private void saveRoom() {
        mPlacement.setCity(cityET.getText().toString());
        mPlacement.setStreet(streetET.getText().toString());
        mPlacement.setHouse(houseET.getText().toString());
        mPlacement.setMaxCapacity(Integer.parseInt(maxCapacityET.getText().toString()));

        String email = FloristShop.getInstance().getEmail();
        loadingDialog.start();
        apiService.updatePlacement(token, email, mPlacement).enqueue(editRoomCallback);
    }

    Callback<Placement> editRoomCallback = new Callback<Placement>() {
        @Override
        public void onResponse(Call<Placement> call, @NotNull Response<Placement> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                loadingDialog.dismiss();
                Intent intent = new Intent(EditPlacementActivity.this, MenuActivity.class);
                startActivity(intent);
                finish();
            }
        }

        @Override
        public void onFailure(Call<Placement> call, Throwable t) {
            Log.i(TAG, t.getMessage());
            loadingDialog.dismiss();
        }
    };

}
