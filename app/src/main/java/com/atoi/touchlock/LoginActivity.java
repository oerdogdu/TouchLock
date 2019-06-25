package com.atoi.touchlock;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.atoi.touchlock.Data.Remote.AuthApiCall;
import com.atoi.touchlock.Data.Remote.AuthService;
import com.atoi.touchlock.Data.Local.UserRepository;
import com.atoi.touchlock.Utils.SessionSharedPreferances;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private AppCompatButton loginButton, signupButton;
    private TextInputEditText emailET, passwordET;
    private UserRepository userRepository;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        userRepository = UserRepository.getInstance(getApplication());

        emailET = findViewById(R.id.emailET);
        passwordET = findViewById(R.id.passwordET);

        signupButton = findViewById(R.id.registerButton);
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //testCall();
                @Nullable final String email = emailET.getText().toString().trim();
                @Nullable String password = passwordET.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(LoginActivity.this, "Please fill in all areas",
                            Toast.LENGTH_LONG).show();
                } else {
                    AuthService service = AuthApiCall.prepareCall();
                    Call<ResponseBody> authCall = service.loginUser(email, password);
                    authCall.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            String responseAuth = null;
                            try {
                                responseAuth = response.body().string();
                                if (responseAuth.equalsIgnoreCase("Successful")) {
                                    SessionSharedPreferances session = new SessionSharedPreferances(getApplicationContext());
                                    session.setEmailSession(email);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(LoginActivity.this, "Wrong credentials",
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "Login failed: Try again later",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }

    public void testCall() {
        SessionSharedPreferances session = new SessionSharedPreferances(getApplicationContext());
        session.setEmailSession("atoi1907@gmail.com");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }
}

