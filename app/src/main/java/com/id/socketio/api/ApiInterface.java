package com.id.socketio.api;

import com.id.socketio.model.UserLoginRequest;
import com.id.socketio.model.UserLoginResponse;
import com.id.socketio.model.UserRegistrationRequest;
import com.id.socketio.model.UserRegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {

    public static final int DEFAULT_PAGE_SIZE = 10;

/*    @Headers({"Accept: application/json"})
    @GET
    Call<ResponseModel> getLotteryDetails(@Url String url, @Header("Authorization") String tokenId);*/

    @Headers({"Content-Type: application/json"})
    @POST("/v1/users")
    Call<UserRegistrationResponse> userRegistration(@Body UserRegistrationRequest request);

    @Headers({"Content-Type: application/json"})
    @POST("/v1/users/login")
    Call<UserLoginResponse> userLogin(@Body UserLoginRequest request);

}
