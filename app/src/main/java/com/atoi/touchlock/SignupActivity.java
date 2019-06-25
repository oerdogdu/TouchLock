package com.atoi.touchlock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atoi.touchlock.Data.Remote.AuthApiCall;
import com.atoi.touchlock.Data.Remote.AuthService;
import com.atoi.touchlock.Data.Local.UserRepository;
import com.atoi.touchlock.POJO.User;
import com.atoi.touchlock.Utils.Callbacks;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {

    private AppCompatButton signButton;
    private TextInputEditText emailET, nameET, passwordET, confirmET;
    private ProgressBar pbLoading;

    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);

        userRepository = UserRepository.getInstance(getApplication());

        emailET = findViewById(R.id.emailET);
        nameET = findViewById(R.id.nameET);
        passwordET = findViewById(R.id.passwordET);
        confirmET = findViewById(R.id.confirmET);

        signButton = findViewById(R.id.registerButton);

        signButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               @Nullable final String email = emailET.getText().toString().trim();
               @Nullable final String name = nameET.getText().toString().trim();
               @Nullable final String password = passwordET.getText().toString().trim();
               @Nullable String confimPassword = confirmET.getText().toString().trim();

               if (TextUtils.isEmpty(email) || TextUtils.isEmpty(name) || TextUtils.isEmpty(password)
                       || TextUtils.isEmpty(confimPassword)) {
                   Toast.makeText(SignupActivity.this, "Please fill in all areas",
                           Toast.LENGTH_LONG).show();
               } else if (!confimPassword.equalsIgnoreCase(password)) {
                   Toast.makeText(SignupActivity.this, "Password values are different",
                           Toast.LENGTH_LONG).show();
               } else {
                   AuthService service = AuthApiCall.prepareCall();
                   Call<ResponseBody> authCall = service.registerUser(email, name, password);
                   authCall.enqueue(new Callback<ResponseBody>() {
                       @Override
                       public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                           try {
                               String responseAuth = response.body().string();
                               if(responseAuth.equalsIgnoreCase("Successful")) {
                                   Toast.makeText(SignupActivity.this, "Registration successful",
                                           Toast.LENGTH_LONG).show();
                                   User user = new User(email, name, password);
                                   userRepository.insertUser(user, new Callbacks.OnUserInserted() {
                                       @Override
                                       public void onUserInserted(Long userId) {

                                       }
                                   });
                                   Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                   startActivity(intent);
                                   finish();
                               }
                               else {
                                   Toast.makeText(SignupActivity.this, "User already exists",
                                           Toast.LENGTH_LONG).show();
                               }
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }

                       @Override
                       public void onFailure(Call<ResponseBody> call, Throwable t) {
                           Toast.makeText(SignupActivity.this, "Registration failed: Try again later",
                                   Toast.LENGTH_LONG).show();
                       }
                   });
               }
           }
       });
    }
}
