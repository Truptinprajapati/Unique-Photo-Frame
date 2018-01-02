package com.vaktech.uniqueartphotoframe.Utilities;


import com.vaktech.uniqueartphotoframe.Response.AppDialogResponse;
import com.vaktech.uniqueartphotoframe.Response.AppOnlineDialogResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by vaksys-android-52 on 21/7/17.
 */

public interface ApiInterface {



    @GET(AppConfig.MORE_APPS)
    Call<AppOnlineDialogResponse>MORE_APPS_CALL();

    @GET(AppConfig.TOP_APPS)
    Call<AppOnlineDialogResponse>TOP_APPS_CALL();

    @GET(AppConfig.EXIT_APPS)
    Call<AppOnlineDialogResponse>EXIT_APPS_CALL();

    @GET(AppConfig.ALERT_APPS)
    Call<AppDialogResponse> ALERT_APPS_CALL();
}
