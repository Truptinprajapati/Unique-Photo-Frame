package com.vaktech.uniqueartphotoframe.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.vaktech.uniqueartphotoframe.Adaptor.RecyclerExitAdapter;
import com.vaktech.uniqueartphotoframe.R;
import com.vaktech.uniqueartphotoframe.Response.AppDialogResponse;
import com.vaktech.uniqueartphotoframe.Response.AppOnlineDialogResponse;
import com.vaktech.uniqueartphotoframe.Utilities.ApiInterface;
import com.vaktech.uniqueartphotoframe.Utilities.Apiclient2;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {
    private Uri mCropImageUri;
    Button btncrop;

    private Button cancelBtn, okBtn;
    Call<AppOnlineDialogResponse> appOnlineDialogResponseCall;
    private RecyclerExitAdapter exitRecyclerViewAdapter;
    RecyclerView recyclerexit;
    private Call<AppDialogResponse> appDialogResponseCall;
    private ApiInterface apiInterface1;
    private static boolean mHasItRun = false;
    Apiclient2 apiclient2;
    GifImageView gifImageView;
    private ImageView appIconImg,shareImage;
    private TextView appNameTv, appDesTv;
    String appLink;
    InterstitialAd mInterstitialAd;
    private AdView mAdView;

    public void initilizeadd() {

        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        mInterstitialAd.loadAd(adRequest);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }

    public void nativeadd(){
        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView1);
        adView.loadAd(new AdRequest.Builder().build());
    }

    /*public void banneradd() {
        mAdView = (AdView) findViewById(R.id.adView_main_screen);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        mAdView.loadAd(adRequest);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btncrop= (Button) findViewById(R.id.btn_crop);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        recyclerexit= (RecyclerView) findViewById(R.id.recyclerexit);
        gifImageView= (GifImageView) findViewById(R.id.gifimage);

        nativeadd();
       // banneradd();
        initilizeadd();

        apiInterface1 = apiclient2.getClient().create(ApiInterface.class);
        btncrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setMultiTouchEnabled(true)
                        .start(MainActivity.this);

            }
        });

        AdRequest adRequest = new AdRequest.Builder()
                .build();
        //mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mInterstitialAd.loadAd(adRequest1);
        mInterstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                mInterstitialAd.show();
            }
        });

        if (getIntent().getBooleanExtra("EXIT", false))
        {
            finish();
        }

        if (getIntent().getBooleanExtra("close", false))
        {
            finish();
        }
        if (!mHasItRun) {
            showDialog();
            mHasItRun = true;
        }
        gifImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkAvailable()) {
                    Intent intent = new Intent(MainActivity.this,AppHubActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Pleasae Turn Your Internet Connection On..!!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onBackPressed() {

        final Context context = this;

        Button btnexit;
        Button btncancel;
        final LinearLayout lvimg1, lvimg2, lvimg3, lvimg4, lvimg5, lvimg6,linerLayout1,linerLayout2;
        final RecyclerView recyclerView;
        final Dialog dialog = new Dialog(this,android.R.style.Theme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.offlinealertdialog);
        dialog.show();

        btnexit = (Button) dialog.findViewById(R.id.btnexit);
        btncancel= (Button) dialog.findViewById(R.id.btncancel);
        linerLayout1= (LinearLayout) dialog.findViewById(R.id.lv1);
        linerLayout2= (LinearLayout) dialog.findViewById(R.id.lv2);

        lvimg1= (LinearLayout) dialog.findViewById(R.id.lvimg1);
        lvimg2= (LinearLayout) dialog.findViewById(R.id.lvimg2);
        lvimg3= (LinearLayout) dialog.findViewById(R.id.lvimg3);
        lvimg4= (LinearLayout) dialog.findViewById(R.id.lvimg4);
        lvimg5= (LinearLayout) dialog.findViewById(R.id.lvimg5);
        lvimg6= (LinearLayout) dialog.findViewById(R.id.lvimg6);
        recyclerView= (RecyclerView) dialog.findViewById(R.id.recyclerexit);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, GridLayoutManager.VERTICAL);
        int numberOfColumns = 3;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(isNetworkAvailable()){

            appOnlineDialogResponseCall=apiInterface1.EXIT_APPS_CALL();
            appOnlineDialogResponseCall.enqueue(new Callback<AppOnlineDialogResponse>() {
                @Override
                public void onResponse(Call<AppOnlineDialogResponse> call, Response<AppOnlineDialogResponse> response) {
                    if (!response.body().isError()) {

                        List<AppOnlineDialogResponse.AppList> data = response.body().getRow();
                        exitRecyclerViewAdapter = new RecyclerExitAdapter(MainActivity.this, data);
                        recyclerView.setAdapter(exitRecyclerViewAdapter);
                    } else {
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AppOnlineDialogResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failure Connection..!", Toast.LENGTH_SHORT).show();
                }
            });



        }else{
            Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
            animation.setDuration(500);
            lvimg1.startAnimation(animation);
            lvimg2.startAnimation(animation);
            lvimg3.startAnimation(animation);
            lvimg4.startAnimation(animation);
            lvimg5.startAnimation(animation);
            lvimg6.startAnimation(animation);

            linerLayout1.setVisibility(View.VISIBLE);
            linerLayout2.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);


            lvimg1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.bfffriendshiptestapp&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });

            lvimg2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.blendmephotoeditor&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });

            lvimg3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.application.vakratunda.fingerbloodpressurecheck&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });
            lvimg4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.vastushastratpisforhome&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });
            lvimg5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.cookingtipssweetandspicy&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });
            lvimg6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.vaktech.jockesreadandenjoy&hl=en"));
                        startActivity(intent);
                    } catch (android.content.ActivityNotFoundException e) {
                    }
                }
            });
        }
        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });


        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);
        Window window = dialog.getWindow();
        dialog.setCancelable(false);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();

        cancelBtn = (Button) dialog.findViewById(R.id.btn_cancle);
        okBtn = (Button) dialog.findViewById(R.id.btn_ok);

        appIconImg = (ImageView) dialog.findViewById(R.id.logo);
        appNameTv = (TextView) dialog.findViewById(R.id.txtappname);
        appDesTv = (TextView) dialog.findViewById(R.id.app_description);
        if (isNetworkAvailable()) {
            appDialogResponseCall = apiInterface1.ALERT_APPS_CALL();
            appDialogResponseCall.enqueue(new Callback<AppDialogResponse>() {
                @Override
                public void onResponse(Call<AppDialogResponse> call, Response<AppDialogResponse> response) {
                    if (!response.body().isError()) {
                        appNameTv.setText(response.body().getName());
                        appDesTv.setText(response.body().getDescription());
                        Glide.with(MainActivity.this).load(response.body().getImage()).into(appIconImg);
                        appLink = response.body().getLink();
                    } else {

                        // pDialog.dismiss();
                        Toast.makeText(MainActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AppDialogResponse> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Failure Connection...!", Toast.LENGTH_SHORT).show();
                }
            });
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

        } else {
            dialog.dismiss();
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appLink));
                startActivity(intent);
            }
        });
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri imageUri = result.getUri();
                Intent intent = new Intent(MainActivity.this, EditScreenActivity.class);
                intent.putExtra("imageUri", imageUri.toString());
                startActivity(intent);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.getError(), Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home) {
            Intent intent = new Intent("android.intent.action.SEND");
            String applink = "Unique Art PhotoFrame app by #dreamyinfotech \n https://goo.gl/5M3doA";
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, applink);
            startActivity(Intent.createChooser(intent, "Share with"));
            return true;
        }
        if (id == R.id.rateus) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/5M3doA"));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://goo.gl/5M3doA"));
                startActivity(intent);
            }
            return true;
        }
        if (id == R.id.moreapp) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=dreamyinfotech&hl=en"));
                startActivity(intent);
            } catch (android.content.ActivityNotFoundException e) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/search?q=dreamyinfotech&hl=en"));
                startActivity(intent);
            }
            return true;
        }
        if (id == R.id.privacypolicy) {
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.privacydialog);
            ImageView imageView = (ImageView) dialog.findViewById(R.id.img_close);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
