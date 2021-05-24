package ua.nure.floristikup.ui.auth;

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
import ua.nure.floristikup.data.FloristShop;
import ua.nure.floristikup.network.NetworkService;
import ua.nure.floristikup.ui.profile.PlacementsActivity;
import ua.nure.floristikup.ui.util.LoadingDialog;
import ua.nure.floristikup.util.InternetConnection;
import ua.nure.floristikup.util.ValidationUtils;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignIn Activity";

    EditText email, password;
    Button confirmButton;
    LinearLayout goToSignUp;
    LoadingDialog loadingDialog = new LoadingDialog(SignInActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_signin);
        init();
    }

    private void init() {
        email = findViewById(R.id.email);
        email.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ValidationUtils.verifyEmail(this, email);
            }
        });

        password = findViewById(R.id.password);
        password.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                ValidationUtils.verifyPassword(this, password);
            }
        });

        confirmButton = findViewById(R.id.signup_button);
        confirmButton.setOnClickListener(v -> signIn(email.getText().toString(),
                password.getText().toString()));

        goToSignUp = findViewById(R.id.go_to_signin);
        goToSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this,
                    SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void signIn(String email, String password) {
        if (!ValidationUtils.verifyEmail(this, this.email) ||
                !ValidationUtils.verifyPassword(this, this.password)) {
            return;
        } else if (!InternetConnection.checkConnection(getApplicationContext())) {
            Toast.makeText(
                    this,
                    R.string.no_internet_connection,
                    Toast.LENGTH_LONG).show();
        } else {
            loadingDialog.start();
            FloristShop floristShop = FloristShop.getInstance()
                    .setEmail(email)
                    .setPassword(password);

            NetworkService.getInstance()
                    .getApiService()
                    .login(floristShop)
                    .enqueue(loginCallback);
        }
    }

    private final Callback<FloristShop> loginCallback = new Callback<FloristShop>() {
        @Override
        public void onResponse(Call<FloristShop> call, Response<FloristShop> response) {
            if (!response.isSuccessful()) {
                Log.i(TAG, response.message());
                System.out.println(response.message());
                Toast.makeText(
                        SignInActivity.this,
                        response.message(),
                        Toast.LENGTH_LONG).show();
            } else {
                System.out.println(response.body().getToken());
                FloristShop.getInstance()
                        .setToken(response.body().getToken())
                        .setUserRole(response.body().getUserRole());

                Intent intent = new Intent(SignInActivity.this, PlacementsActivity.class);
                startActivity(intent);
            }
            loadingDialog.dismiss();
        }

        @Override
        public void onFailure(Call<FloristShop> call, Throwable t) {
            Log.i(TAG, t.toString());
            Toast.makeText(
                    SignInActivity.this,
                    t.getMessage(),
                    Toast.LENGTH_LONG).show();
            loadingDialog.dismiss();
        }
    };
}



