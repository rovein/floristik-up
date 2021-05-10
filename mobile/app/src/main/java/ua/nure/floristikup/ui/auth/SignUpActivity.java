package ua.nure.floristikup.ui.auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.nure.floristikup.R;
import ua.nure.floristikup.data.Address;
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.util.LoadingDialog;
import ua.nure.floristikup.util.InternetConnection;
import ua.nure.floristikup.util.ValidationUtils;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";

    EditText name, email, phoneNumber, password, confirmPassword, country;
    Button confirmButton;
    LinearLayout mGoToLogin;
    Context context;
    LoadingDialog loadingDialog = new LoadingDialog(SignUpActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signup);

        context = this;

        init();
    }

    private void init() {
        name = findViewById(R.id.name);

        name.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ValidationUtils.verifyName(this, name);
            }
        });

        email = findViewById(R.id.email);
        email.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ValidationUtils.verifyEmail(this, email);
            }
        });

        phoneNumber = findViewById(R.id.phone);
        phoneNumber.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ValidationUtils.verifyPhone(this, phoneNumber);
            }
        });

        password = findViewById(R.id.password);
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ValidationUtils.verifyPassword(this, password);
            }
        });

        confirmPassword = findViewById(R.id.confirm_password);
        confirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ValidationUtils.verifyPasswords(this, password, confirmPassword);
            }
        });

        country = findViewById(R.id.country);
        country.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ValidationUtils.verifyName(this, country);
            }
        });

        confirmButton = findViewById(R.id.signup_button);
        confirmButton.setOnClickListener(v -> signUp(
                name.getText().toString(),
                email.getText().toString(),
                phoneNumber.getText().toString(),
                password.getText().toString(),
                country.getText().toString()
                ));

        mGoToLogin = findViewById(R.id.go_to_signin);
        mGoToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
        });
    }

    private void signUp(String name, String email, String phone, String password, String country) {
        if (!ValidationUtils.verifyName(this, this.name)
                || !ValidationUtils.verifyEmail(this, this.email)
                || !ValidationUtils.verifyPhone(this, phoneNumber)
                || !ValidationUtils.verifyPassword(this, this.password)
                || !ValidationUtils.verifyPasswords(this, this.password, this.confirmPassword)) {
            return;
        } else if (!InternetConnection.checkConnection(getApplicationContext())) {
            Toast.makeText(this, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
            return;
        }
        loadingDialog.start();

        FloristShop floristShop = FloristShop.getInstance()
                .setName(name)
                .setEmail(email)
                .setPhoneNumber(phone)
                .setPassword(password)
                .setCountry(country)
                .setUserRole("USER");

        NetworkService.getInstance().getApiService()
                .signUpFloristShop(floristShop).enqueue(signUpCallback);
    }

    private Callback<FloristShop> signUpCallback = new Callback<FloristShop>() {
        @Override
        public void onResponse(Call<FloristShop> call, Response<FloristShop> response) {
            if(!response.isSuccessful()) {
                Log.i(TAG, response.message() + " " + response.code());
                Toast.makeText(
                        SignUpActivity.this,
                        response.message(),
                        Toast.LENGTH_LONG
                ).show();
            } else {
                Intent intent = new Intent(SignUpActivity.this,
                        SignInActivity.class);
                startActivity(intent);
            }
            loadingDialog.dismiss();
        }

        @Override
        public void onFailure(Call<FloristShop> call, Throwable t) {
            Log.i(TAG, t.getMessage());
            Toast.makeText(
                    SignUpActivity.this,
                    t.getMessage(),
                    Toast.LENGTH_LONG
            ).show();
            loadingDialog.dismiss();
        }
    };

}

