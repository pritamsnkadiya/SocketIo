
package com.id.socketio.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserLoginResponse {

    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("result")
    @Expose
    private UserLoginResult result;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public UserLoginResult getResult() {
        return result;
    }

    public void setResult(UserLoginResult result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "UserLoginResponse{" +
                "status=" + status +
                ", result=" + result +
                '}';
    }
}
