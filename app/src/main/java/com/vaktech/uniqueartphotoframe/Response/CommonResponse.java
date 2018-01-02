package com.vaktech.uniqueartphotoframe.Response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by vaksys-android-52 on 21/7/17.
 */

public class CommonResponse {
    public boolean isError() {
        return error;
    }

    @SerializedName("error")
    private boolean error;

    @SerializedName("message")
    private String message;

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
