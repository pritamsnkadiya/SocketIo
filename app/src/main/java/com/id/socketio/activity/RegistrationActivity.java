package com.id.socketio.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.id.socketio.R;
import com.id.socketio.api.ApiClient;
import com.id.socketio.model.UserRegistrationRequest;
import com.id.socketio.model.UserRegistrationResponse;
import com.kaopiz.kprogresshud.KProgressHUD;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();
    public EditText name, dob, gender, address, email, mobile, password;
    public Button registration;

    public UserRegistrationRequest request;
    public UserRegistrationResponse registrationResponse;
    public KProgressHUD hud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = (EditText) findViewById(R.id.name);
        dob = (EditText) findViewById(R.id.dob);
        gender = (EditText) findViewById(R.id.gender);
        address = (EditText) findViewById(R.id.address);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        password = (EditText) findViewById(R.id.password);
        registration = (Button) findViewById(R.id.registration);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logInProgressDialogCall();
                loginCredentials();
            }
        });
    }

    private void loginCredentials() {

        request = new UserRegistrationRequest();

        request.setCityId("1");
        request.setStateId("1");
        request.setName(name.getText().toString());
        request.setDob(dob.getText().toString());
        request.setGender(gender.getText().toString());
        request.setAddress(address.getText().toString());
        request.setEmail(email.getText().toString());
        request.setMobile(mobile.getText().toString());
        request.setPassword(password.getText().toString());

        Log.d(TAG, "Peeru API UserRegi Request :" + request.toString());
        try {
            ApiClient.getSingletonApiClient().userLoginRequest(request, new Callback<UserRegistrationResponse>() {
                @Override
                public void onResponse(Call<UserRegistrationResponse> call, Response<UserRegistrationResponse> response) {
                    Log.e("REQUEST USER MYPOOL", new Gson().toJson(response));
                    registrationResponse = response.body();
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Peeru Data " + response.toString());
                        try {
                            hud.dismiss();
                            Toast toast = Toast.makeText(getApplicationContext(), "Registration Successfully !", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            Intent intent = new Intent(RegistrationActivity.this, UserLoginActivity.class);
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
                    }
                }

                @Override
                public void onFailure(Call<UserRegistrationResponse> call, Throwable t) {
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
                .setDetailsLabel("Registering....")
                .setDimAmount(0.5f)
                .setCancellable(true);
        hud.show();
    }
}
