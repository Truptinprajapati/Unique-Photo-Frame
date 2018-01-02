package com.vaktech.uniqueartphotoframe.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.vaktech.uniqueartphotoframe.Adaptor.RecyclerdownAdapter;
import com.vaktech.uniqueartphotoframe.Adaptor.TopsAppRecyclerView;
import com.vaktech.uniqueartphotoframe.R;
import com.vaktech.uniqueartphotoframe.Response.AppDialogResponse;
import com.vaktech.uniqueartphotoframe.Response.AppOnlineDialogResponse;
import com.vaktech.uniqueartphotoframe.Utilities.ApiInterface;
import com.vaktech.uniqueartphotoframe.Utilities.Apiclient2;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppHubActivity extends AppCompatActivity {
    private ImageView appIconImg;
    private TextView appnameTv;
    private TextView appDescriptionTv;
    private Button cancelBtn;
    private Button okBtn;
    private Call<AppDialogResponse> apiResponseCall;
    private ApiInterface apiInterface1;
    private String appLink;
    private ProgressDialog pDialog;
    private Apiclient2 apiclient2;
    public RecyclerView appsRecyclerview, recycler_2;
    private TopsAppRecyclerView recyclerView1Adapter;
    private RecyclerdownAdapter moreAppsRecyclerViewAdapter;
    private Call<AppOnlineDialogResponse> appListResponseCall;
    private InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.actionbarcentertext);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_hub);
        pDialog = new ProgressDialog(this);
        pDialog = pDialog.show(AppHubActivity.this, null, "Please Wait", false, false);
        apiInterface1 = apiclient2.getClient().create(ApiInterface.class);
        interstitialAd = new InterstitialAd(AppHubActivity.this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest1 = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest1);
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });
        horizontalApiCall();
        verticalApiCall();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home, menu);

        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.home) {


            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("EXIT", true);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void horizontalApiCall() {
        appsRecyclerview = (RecyclerView) findViewById(R.id.recycler1);
        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(AppHubActivity.this, LinearLayoutManager.HORIZONTAL, false);
        appsRecyclerview.setLayoutManager(verticalLayoutManagaer);
        appsRecyclerview.setItemAnimator(new DefaultItemAnimator());
        appListResponseCall = apiInterface1.TOP_APPS_CALL();
        appListResponseCall.enqueue(new Callback<AppOnlineDialogResponse>() {
            @Override
            public void onResponse(Call<AppOnlineDialogResponse> call, Response<AppOnlineDialogResponse> response) {
                Log.d("response code", String.valueOf(response.code()));
                if (!response.body().isError()) {
                    pDialog.dismiss();
                    List<AppOnlineDialogResponse.AppList> data = response.body().getRow();
                    recyclerView1Adapter = new TopsAppRecyclerView(AppHubActivity.this, data);
                    appsRecyclerview.setAdapter(recyclerView1Adapter);
                } else {
                    Toast.makeText(AppHubActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppOnlineDialogResponse> call, Throwable t) {
                Toast.makeText(AppHubActivity.this, "Failure Connection..!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void verticalApiCall() {
        recycler_2 = (RecyclerView) findViewById(R.id.recyclerbelow);
        LinearLayoutManager verticalLayoutManagaer = new LinearLayoutManager(AppHubActivity.this, LinearLayoutManager.VERTICAL, false);
        recycler_2.setLayoutManager(verticalLayoutManagaer);
        recycler_2.setItemAnimator(new DefaultItemAnimator());
        appListResponseCall = apiInterface1.MORE_APPS_CALL();
        appListResponseCall.enqueue(new Callback<AppOnlineDialogResponse>() {
            @Override
            public void onResponse(Call<AppOnlineDialogResponse> call, Response<AppOnlineDialogResponse> response) {
                Log.d("response code ", String.valueOf(response.code()));
                if (!response.body().isError()) {

                    List<AppOnlineDialogResponse.AppList> data = response.body().getRow();
                    moreAppsRecyclerViewAdapter = new RecyclerdownAdapter(AppHubActivity.this, data);
                    recycler_2.setAdapter(moreAppsRecyclerViewAdapter);
                } else {
                    Toast.makeText(AppHubActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AppOnlineDialogResponse> call, Throwable t) {
                Toast.makeText(AppHubActivity.this, "Failure Connection..!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
