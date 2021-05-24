package ua.nure.floristikup.ui.add;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.data.Placement;
import ua.nure.floristikup.network.JSONPlaceHolderApi;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.profile.PlacementsActivity;
import ua.nure.floristikup.ui.util.LoadingDialog;

public class AddPlacementActivity extends AppCompatActivity {

    private static final String TAG = "AddRoomActivity";
    Button mCancelButton, mSaveButton;
    Placement mPlacement;
    TextView labelTV;
    EditText cityET, streetET, houseET, maxCapacityET;
    private JSONPlaceHolderApi mApi;
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

        labelTV.setText(R.string.add_room);
        token = "Bearer " + FloristShop.getInstance().getToken();

        mApi = NetworkService.getInstance().getApiService();
        mPlacement = new Placement();

        mCancelButton.setOnClickListener(v -> finish());

        mSaveButton.setOnClickListener(v -> addRoom());
    }

    private void addRoom() {
        mPlacement.setCity(cityET.getText().toString());
        mPlacement.setStreet(streetET.getText().toString());
        mPlacement.setHouse(houseET.getText().toString());
        mPlacement.setMaxCapacity(Integer.parseInt(maxCapacityET.getText().toString()));

        String email = FloristShop.getInstance().getEmail();
        loadingDialog.start();
        mApi.addPlacement(token, email, mPlacement).enqueue(addRoomCallback);
    }

    Callback<Placement> addRoomCallback = new Callback<Placement>() {
        @Override
        public void onResponse(Call<Placement> call, @NotNull Response<Placement> response) {
            if (response.isSuccessful()) {
                System.out.println(response.body());
                loadingDialog.dismiss();
                Intent intent = new Intent(AddPlacementActivity.this, PlacementsActivity.class);
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
