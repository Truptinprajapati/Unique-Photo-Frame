package com.vaktech.uniqueartphotoframe.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.vaktech.uniqueartphotoframe.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SaveScreenActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView save, whatsapp, hike, instagram, facebook, more, image;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bmp;
    Context context = this;
    FileOutputStream fout1;
    LinearLayout saveLayout;
    String applink = "Look my photo using this \n Unique Art PhotoFrame #dreamyinfotech \n https://goo.gl/5M3doA";
    private Bitmap mBitmap;
    int reques = 99;
    private AdView adView;
    private InterstitialAd interstitialAd;

    public void initialize() {
        saveLayout = (LinearLayout) findViewById(R.id.save_layout);
        save = (ImageView) findViewById(R.id.save_button);
        whatsapp = (ImageView) findViewById(R.id.whts_share_button);
        hike = (ImageView) findViewById(R.id.hike_share_button);
        facebook = (ImageView) findViewById(R.id.facebook_share_button);
        instagram = (ImageView) findViewById(R.id.insta_share_button);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_screen);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back1);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Unique art Photo Frame</small>"));
        initialize();
        SharedPreferences preferences = context.getSharedPreferences("Hello", Context.MODE_PRIVATE);
        String save = preferences.getString("picture", "");
        byte[] byteArray = Base64.decode(save, Base64.DEFAULT);
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        image = (ImageView) findViewById(R.id.save_image);
        image.setImageBitmap(bmp);
        //nativeadd();
        adView = (AdView) findViewById(R.id.ad_view_editimg);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        /*NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView1);
        adView.loadAd(new AdRequest.Builder().build());*/
        interstitialAd = new InterstitialAd(SaveScreenActivity.this);
        interstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest1 = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest1);
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                interstitialAd.show();
            }
        });
        interstitialAd.setAdListener(new AdListener() {
            public void onAdLoaded() {
                showInterstitial();
            }
        });
        saveLayout.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
        hike.setOnClickListener(this);
        facebook.setOnClickListener(this);
        instagram.setOnClickListener(this);

        }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_layout:
                checkPermission();
                break;

            case R.id.whts_share_button:
                Intent shareIntent = new Intent("android.intent.action.SEND");
                shareIntent.putExtra(Intent.EXTRA_TEXT, applink);
                shareIntent.setPackage("com.whatsapp");
                File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "temporaryy_file.jpg");
                try {

                    FileOutputStream fout1 = new FileOutputStream(file1);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout1);
                    fout1.flush();
                    fout1.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporaryy_file.jpg"));
                shareIntent.setType("image/*");
                try {
                    startActivity(shareIntent);
                } catch (Exception e) {
                    Toast.makeText(SaveScreenActivity.this, "Install Whatsup first..!!", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.hike_share_button:
                Intent intenthike = new Intent("android.intent.action.SEND");
                intenthike.setPackage("com.bsb.hike");
                File filehike = new File(Environment.getExternalStorageDirectory() + File.separator + "temporaryy_file.jpg");
                try {
                    FileOutputStream fout1 = new FileOutputStream(filehike);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout1);
                    fout1.flush();
                    fout1.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intenthike.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporaryy_file.jpg"));
                intenthike.putExtra(android.content.Intent.EXTRA_TEXT, applink);
                intenthike.setType("image/*");
                startActivity(Intent.createChooser(intenthike, "Share Image"));
                break;

            case R.id.insta_share_button:
                Intent intentinsta = new Intent("android.intent.action.SEND");
                intentinsta.setPackage("com.instagram.android");
                File fileinsta = new File(Environment.getExternalStorageDirectory() + File.separator + "temporaryy_file.jpg");
                try {

                    FileOutputStream fout1 = new FileOutputStream(fileinsta);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout1);
                    fout1.flush();
                    fout1.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intentinsta.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporaryy_file.jpg"));
                intentinsta.putExtra(android.content.Intent.EXTRA_TEXT, applink);
                intentinsta.setType("image/*");
                startActivity(Intent.createChooser(intentinsta, "Share Image"));
                break;
            case R.id.facebook_share_button:
                Intent intentfacebbok = new Intent("android.intent.action.SEND");
                intentfacebbok.setPackage("com.facebook.katana");
                File filebacebook = new File(Environment.getExternalStorageDirectory() + File.separator + "temporaryy_file.jpg");
                try {

                    FileOutputStream fout1 = new FileOutputStream(filebacebook);
                    bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout1);
                    fout1.flush();
                    fout1.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                intentfacebbok.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporaryy_file.jpg"));
                intentfacebbok.putExtra(android.content.Intent.EXTRA_TEXT, applink);
                intentfacebbok.setType("image/*");
                startActivity(Intent.createChooser(intentfacebbok, "Share Image"));
                break;

        }
    }
    public void save() {
        Toast.makeText(getApplicationContext(), "Image Saved Successfully", Toast.LENGTH_SHORT).show();
        File mainFolder = new File(Environment.getExternalStorageDirectory() + File.separator + "Unique Art PhotoFrame");
        if (!mainFolder.exists() && !mainFolder.isDirectory()) {
            mainFolder.mkdirs();
            mainFolder.setExecutable(true);
            mainFolder.setReadable(true);
            mainFolder.setWritable(true);
            MediaScannerConnection.scanFile(getApplicationContext(), new String[]{mainFolder.toString()}, null, null);
        }
        SimpleDateFormat sf = new SimpleDateFormat("ddMMyyyyhhmmss");
        image.invalidate();
        image.buildDrawingCache();
        bitmap1 = image.getDrawingCache();
        bitmap2 = Bitmap.createScaledBitmap(bitmap1, image.getWidth(), image.getHeight(), false);
        File file = new File(Environment.getExternalStorageDirectory() + "/Unique Art PhotoFrame", "Unique Art PhotoFrame" + sf.format(Calendar.getInstance().getTime()) + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Unique Art PhotoFrame" + sf.format(Calendar.getInstance().getTime()));
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Unique Art PhotoFrame" + sf.format(Calendar.getInstance().getTime()));
        contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        contentValues.put(MediaStore.Images.ImageColumns.BUCKET_ID, file.toString().toLowerCase(Locale.US).hashCode());
        contentValues.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME, file.getName().toLowerCase(Locale.US));
        contentValues.put("_data", file.getAbsolutePath());
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
    }
    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(SaveScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SaveScreenActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reques);

        } else {
            save();
        }
    }

    private void checkPermissionShare() {
        int permissionCheck = ContextCompat.checkSelfPermission(SaveScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SaveScreenActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reques);

        } else {
            shareFile();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    save();
                    shareFile();

                } else {
                    Toast.makeText(SaveScreenActivity.this, "Permission denied to Write your External storage", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }
    private void shareFile() {
        Intent intentinsta = new Intent(Intent.ACTION_SEND);
        File fileinsta = new File(Environment.getExternalStorageDirectory() + File.separator + "temporaryy_file.jpg");
        try {

            FileOutputStream fout1 = new FileOutputStream(fileinsta);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fout1);
            fout1.flush();
            fout1.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        intentinsta.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporaryy_file.jpg"));
        intentinsta.putExtra(android.content.Intent.EXTRA_TEXT, applink);
        intentinsta.setType("image*//**//*");
        startActivity(Intent.createChooser(intentinsta, "Share Image"));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        int id = item.getItemId();

        if (id == R.id.home) {

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("close", true);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void nativeadd(){
//        NativeExpressAdView adView = (NativeExpressAdView) findViewById(R.id.adView1);
//        adView.loadAd(new AdRequest.Builder().build());
//    }
    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}