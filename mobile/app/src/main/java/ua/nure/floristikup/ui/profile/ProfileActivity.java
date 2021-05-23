package ua.nure.floristikup.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.Address;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.auth.SignInActivity;
import ua.nure.floristikup.ui.util.LoadingDialog;
import ua.nure.floristikup.ui.util.NavigationBottomMenu;
import ua.nure.floristikup.util.ValidationUtils;

public class ProfileActivity extends AppCompatActivity {

    TextView mNameTV, mEmailTV, mPhoneTV, mCountryTV;
    EditText mNameET, mEmailET, mPhoneET, mCountryET, mPasswordET, confirmPasswordET;
    Button editButton;

    ImageView mBack, mLogOut;

    Context context;
    LoadingDialog loadingDialog = new LoadingDialog(this);

    private static final String TAG = "ProfileActivity";
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loadingDialog.start();
        init();
        getData();
    }

    private void init() {
        context = this;
        token = "Bearer " + FloristShop.getInstance().getToken();

        mEmailTV = findViewById(R.id.profile_email);
        mNameTV = findViewById(R.id.profile_name);
        mPhoneTV = findViewById(R.id.profile_phone);
        mCountryTV = findViewById(R.id.profile_country);

        mNameET = findViewById(R.id.profile_name_edit);
        mEmailET = findViewById(R.id.profile_email_edit);
        mPhoneET = findViewById(R.id.profile_phone_edit);
        mCountryET = findViewById(R.id.profile_country_edit);
        mPasswordET = findViewById(R.id.profile_password_edit);
        confirmPasswordET = findViewById(R.id.profile_confirm_password_edit);

        confirmPasswordET.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ValidationUtils.verifyPasswords(this, mPasswordET, confirmPasswordET);
            }
        });

        editButton = findViewById(R.id.edit_profile_btn);
        editButton.setOnClickListener(v -> startEditing());

        mLogOut = findViewById(R.id.exit_btn);
        mLogOut.setOnClickListener(v -> {
            FloristShop.getInstance().setToken(null).setName(null);
            startActivity(new Intent(ProfileActivity.this, SignInActivity.class));
        });

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(NavigationBottomMenu.getOnNavigationItemSelectedListener(ProfileActivity.this));
        navigation.clearFocus();
        showTV();
    }

    private void getData() {
        final FloristShop floristShop = FloristShop.getInstance();
        String email = floristShop.getEmail();

        NetworkService.getInstance()
                .getApiService()
                .getFloristShopData(token, email)
                .enqueue(new Callback<FloristShop>() {
                    @Override
                    public void onResponse(Call<FloristShop> call, Response<FloristShop> response) {
                        if (!response.isSuccessful()) {
                            Log.i(TAG, response.message());
                            System.out.println(response);
                        } else {
                            floristShop.setName(response.body().getName());
                            floristShop.setPhoneNumber(response.body().getPhoneNumber());
                            floristShop.setCountry(response.body().getCountry());

                            mNameTV.setText(floristShop.getName());
                            mPhoneTV.setText(floristShop.getPhoneNumber());
                            mEmailTV.setText(floristShop.getEmail());
                            mCountryTV.setText(floristShop.getCountry());
                        }
                        loadingDialog.dismiss();
                    }

                    @Override
                    public void onFailure(Call<FloristShop> call, Throwable t) {
                        Log.i(TAG, t.toString());
                        System.out.println(t.toString());
                        loadingDialog.dismiss();
                    }
                });
    }

    private void showTV() {
        editButton.setText(R.string.edit);

        mNameET.setVisibility(View.GONE);
        mEmailET.setVisibility(View.GONE);
        mPhoneET.setVisibility(View.GONE);
        mCountryET.setVisibility(View.GONE);
        mPasswordET.setVisibility(View.GONE);
        confirmPasswordET.setVisibility(View.GONE);

        mNameTV.setVisibility(View.VISIBLE);
        mEmailTV.setVisibility(View.VISIBLE);
        mPhoneTV.setVisibility(View.VISIBLE);
        mCountryTV.setVisibility(View.VISIBLE);
    }

    private void showET() {
        editButton.setText(R.string.done);

        mNameTV.setVisibility(View.GONE);
        mEmailTV.setVisibility(View.GONE);
        mPhoneTV.setVisibility(View.GONE);
        mCountryTV.setVisibility(View.GONE);

        mNameET.setVisibility(View.VISIBLE);
        mEmailET.setVisibility(View.VISIBLE);
        mPhoneET.setVisibility(View.VISIBLE);
        mCountryET.setVisibility(View.VISIBLE);
        mPasswordET.setVisibility(View.VISIBLE);
        confirmPasswordET.setVisibility(View.VISIBLE);

        mNameET.setText(FloristShop.getInstance().getName());
        mEmailET.setText(FloristShop.getInstance().getEmail());
        mPhoneET.setText(FloristShop.getInstance().getPhoneNumber());
        mCountryET.setText(FloristShop.getInstance().getCountry());
    }

    private void startEditing() {
        showET();
        getData();
        editButton.setOnClickListener(v -> submitEditing());
    }

    private void submitEditing() {
        if(!ValidationUtils.verifyPasswords(this, mPasswordET, confirmPasswordET)) {
            return;
        }

        loadingDialog.start();

        Callback<FloristShop> callback = new Callback<FloristShop>() {
            @Override
            public void onResponse(Call<FloristShop> call, Response<FloristShop> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                } else {
                    editButton.setOnClickListener(v -> startEditing());
                    getData();
                    showTV();
                }
                loadingDialog.dismiss();
            }

            @Override
            public void onFailure(Call<FloristShop> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                loadingDialog.dismiss();
            }
        };

        mNameET.setVisibility(View.GONE);
        mEmailET.setVisibility(View.GONE);
        mPhoneET.setVisibility(View.GONE);
        mCountryET.setVisibility(View.GONE);
        mPasswordET.setVisibility(View.GONE);
        confirmPasswordET.setVisibility(View.GONE);

        FloristShop.getInstance()
                .setName(mNameET.getText().toString())
                .setEmail(mEmailET.getText().toString())
                .setPhoneNumber(mPhoneET.getText().toString())
                .setCountry(mCountryET.getText().toString())
                .setPassword(mPasswordET.getText().toString());

        NetworkService.getInstance().getApiService()
                .updateFloristShop(token, FloristShop.getInstance())
                .enqueue(callback);
    }
}