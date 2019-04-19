package com.id.socketio.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.id.socketio.R;
import com.id.socketio.api.ApiClient;
import com.id.socketio.model.UserLoginRequest;
import com.id.socketio.model.UserLoginResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission_group.CAMERA;

public class UserLoginActivity extends AppCompatActivity {

    private static final String TAG = UserLoginActivity.class.getSimpleName();
    public EditText email, password;
    public Button login;
    public UserLoginRequest request;
    public UserLoginResponse loginResponse;
    public TextView click_me;
    public KProgressHUD hud;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        click_me = (TextView) findViewById(R.id.click_me);
        click_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoginActivity.this, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInProgressDialogCall();
                loginCredentials();
            }
        });
    }

    private void loginCredentials() {

        request = new UserLoginRequest();
        request.setEmail(email.getText().toString());
        request.setPassword(password.getText().toString());

        Log.d(TAG, "Peeru API UserRegi Request :" + request.toString());
        try {
            ApiClient.getSingletonApiClient().userLoginRequestcall(request, new Callback<UserLoginResponse>() {
                @Override
                public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                    Log.e("REQUEST USER MYPOOL", new Gson().toJson(response));
                    loginResponse = response.body();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Peeru Data " + loginResponse.toString());
                        try {
                            hud.dismiss();
                            Toast toast = Toast.makeText(getApplicationContext(), "Login Successfully !", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();

                            SharedPreferences sharedPreferences = PreferenceManager
                                    .getDefaultSharedPreferences(UserLoginActivity.this);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("uuid", loginResponse.getResult().getUser().getUuid());
                            editor.apply();

                            Intent intent = new Intent(UserLoginActivity.this, AddUserActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        } catch (Exception e) {
                            hud.dismiss();
                            Toast toast = Toast.makeText(getApplicationContext(), "Enter Valid credentials", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                        }
                    } else {
                        hud.dismiss();
                        hud = KProgressHUD.create(UserLoginActivity.this)
                                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                                .setDimAmount(0.5f)
                                .setLabel("Alert")
                                .setDetailsLabel("Please Enter Valid credentials !")
                                .setCancellable(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface
                                                                 dialogInterface) {
                                        Toast.makeText(UserLoginActivity.this, "You " +
                                                "cancelled manually!", Toast
                                                .LENGTH_SHORT).show();
                                        hud.dismiss();
                                    }
                                });
                        hud.show();
                    }
                }

                @Override
                public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                    hud.dismiss();
                    Toast toast = Toast.makeText(getApplicationContext(), "Enter Valid credentials", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    Log.d("Login Request", "Peeru Failed request : " + t.getMessage());
                    t.printStackTrace();
                }
            });
        } catch (Exception e) {
            hud.dismiss();
            Log.d(TAG, "Peeru Error Notifications :" + e.getMessage());
        }
    }
    public void logInProgressDialogCall() {
        hud = KProgressHUD.create(this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setDetailsLabel("Signing up....")
                .setDimAmount(0.5f)
                .setCancellable(true);
        hud.show();
    }
}
