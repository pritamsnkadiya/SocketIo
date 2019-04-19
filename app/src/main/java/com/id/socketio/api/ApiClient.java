package com.id.socketio.api;

import android.content.Context;
import android.util.Log;

import com.id.socketio.model.UserLoginRequest;
import com.id.socketio.model.UserLoginResponse;
import com.id.socketio.model.UserRegistrationRequest;
import com.id.socketio.model.UserRegistrationResponse;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient implements Serializable {

    private static final String TAG = ApiClient.class.getSimpleName();

    private static final boolean production = false;//BuildConfig.DEBUG;


   // public static final String BASE_URL = "http://192.168.9.116:50003/";
    public static final String BASE_URL = "http://lsa.ckmeout.com:50008/";
    public static boolean isProduction() {
        return production;
    }

    private static Retrofit retrofit = null;

    private static ApiClient apiClient;

    private Context context;

    private static final Object mLock = new Object();

    public ApiClient() {
    }

    public ApiClient(Context context) {
        this.context = context;
    }

    public static ApiClient getSingletonApiClient() {
        synchronized (mLock) {
            if (apiClient == null)
                apiClient = new ApiClient();

            return apiClient;
        }
    }

    private static Retrofit getClient() {
        if (retrofit == null) {
            //OkHttpClient.Builder client = new OkHttpClient.Builder();

            OkHttpClient.Builder okHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(60 * 5, TimeUnit.SECONDS)
                    .readTimeout(60 * 5, TimeUnit.SECONDS)
                    .writeTimeout(60 * 5, TimeUnit.SECONDS);

            GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient.build())
                    .addConverterFactory(gsonConverterFactory)
                    .build();

        }
        return retrofit;
    }
/*
    public void getLotteryDetailsCall(String token, Callback<ResponseModel> callback) {
        Call<ResponseModel> call = null;
        try {
            SharedPreferences sharedPreferences = PreferenceManager
                    .getDefaultSharedPreferences(getAppContext());
            String COUNTRY_ID = sharedPreferences.getString("COUNTRY_ID", "DE");

            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Log.d(TAG, "getLotteryDetailsCall Request URL : " + AppConstants.LOTTERY_RESULT_BASE_URL + COUNTRY_ID);
            //Log.d(TAG, "OrderDetail Request URL : "+BASE_URL + Constant.API_ORDERS+id);
            call = apiService.getLotteryDetails(AppConstants.LOTTERY_RESULT_BASE_URL + COUNTRY_ID, token);
            call.enqueue(callback);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            callback.onFailure(call, e);
        }
    }*/

    public void userLoginRequest(UserRegistrationRequest request, Callback<UserRegistrationResponse> callback) {
        Call<UserRegistrationResponse> call = null;
        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Log.d(TAG, "userRegistration Request URL : " + BASE_URL);
            call = apiService.userRegistration(request);
            call.enqueue(callback);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            callback.onFailure(call, e);
        }
    }
    public void userLoginRequestcall(UserLoginRequest request, Callback<UserLoginResponse> callback) {
        Call<UserLoginResponse> call = null;
        try {
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Log.d(TAG, "UserLoginResponse Request URL : " + BASE_URL);
            call = apiService.userLogin(request);
            call.enqueue(callback);
        } catch (Exception e) {
            Log.e(TAG, e.toString(), e);
            callback.onFailure(call, e);
        }
    }
}