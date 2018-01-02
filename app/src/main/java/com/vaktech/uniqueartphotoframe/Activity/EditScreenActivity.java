package com.vaktech.uniqueartphotoframe.Activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.vaktech.uniqueartphotoframe.Adaptor.FontsAdapter;
import com.vaktech.uniqueartphotoframe.Adaptor.RecyclerStickerAdapter;
import com.vaktech.uniqueartphotoframe.BuildConfig;
import com.vaktech.uniqueartphotoframe.Extras.entity.MotionEntity;
import com.vaktech.uniqueartphotoframe.Extras.entity.MotionView;
import com.vaktech.uniqueartphotoframe.Extras.entity.TextEntity;
import com.vaktech.uniqueartphotoframe.Extras.utils.FontProvider;
import com.vaktech.uniqueartphotoframe.Extras.viewmodel.Font;
import com.vaktech.uniqueartphotoframe.Extras.viewmodel.TextLayer;
import com.vaktech.uniqueartphotoframe.Fragment.TextEditorDialogFragment;
import com.vaktech.uniqueartphotoframe.R;
import com.vaktech.uniqueartphotoframe.View.StickerImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditScreenActivity extends AppCompatActivity implements TextEditorDialogFragment.OnTextLayerCallback {

    private static final int NONE = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private static final int NONEStk = 0;
    private static final int DRAGStk = 1;
    private static final int ZOOMStk = 2;
    RelativeLayout relativeLayout, relative_save_image, relativeLayouttextpanel;
    private FontProvider fontProvider;
    static EditScreenActivity editScreen;
    RecyclerStickerAdapter recyclerStickerAdapter;
    FrameLayout stickerFrameLayout;
    LinearLayout stickerlayout, textlayout, framelayout, savelayout;
    RecyclerView recycler_sticker, frameRecylerview;
    ImageView imgsticker, imageselect, imageframeselect, image;
    private static MotionView motionView;

    StickerImageView ivSticker, ivPiercing;
    ArrayList<Integer> stickerImageList;
    ArrayList<Integer> frameimageList;
    Button download;
    int flag = 0, flag1 = 0, flag2 = 0, flag3 = 0;
    private ImageButton font, color;
    ArrayList<StickerImageView> ivStickerArraylist, ivPiervingarrayList;
    private LinearLayout mainMotionTextEntityEditPanel;
    int reques = 99;
    private int currentBackgroundColor = 0xffffffff;
    private AdView mAdView;
    private Matrix matrix = new Matrix();
    private Matrix savedMatrix = new Matrix();
    private PointF start = new PointF();
    private PointF mid = new PointF();
    private Matrix matrixStk = new Matrix();
    private Matrix savedMatrixStk = new Matrix();
    private PointF startStk = new PointF();
    private PointF midStk = new PointF();
    int mode = NONE;
    float oldDist = 1f;
    float d = 0f;
    float newRot = 0f;
    float[] lastEvent = null;
    int modeStk = NONEStk;
    float oldDistStk = 1f;
    float dStk = 0f;
    float newRotStk = 0f;
    float[] lastEventStk = null;
    private View root;
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bmp;
    private AdView adView;
    private InterstitialAd interstitialAd;
    LinearLayout main_motion_text_entity_edit_panel;
    ImageButton btndel;
    EditText editText;


    public static EditScreenActivity getInstance() {
        return editScreen;
    }




  /*  protected View textEntity;
    private final MotionView.MotionViewCallback mymotion=new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
            if  (entity instanceof TextEntity) {
                textEntity.setVisibility(View.VISIBLE);
            } else {
                textEntity.setVisibility(View.GONE);
            }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
            //textEntity.setVisibility(View.VISIBLE);

        }
    };*/

    protected View textEntityEditPanel;
    private final MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {
            if (entity instanceof TextEntity) {
                textEntityEditPanel.setVisibility(View.VISIBLE);
                color.setVisibility(View.VISIBLE);
                font.setVisibility(View.VISIBLE);
                btndel.setVisibility(View.VISIBLE);


            } else {
                textEntityEditPanel.setVisibility(View.GONE);
                color.setVisibility(View.GONE);
                font.setVisibility(View.GONE);
                btndel.setVisibility(View.GONE);

            }
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {
            startTextEntityEditing();
        }

    };

    private void startTextEntityEditing() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextEditorDialogFragment fragment = TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
            fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
        }
    }

    @Nullable
    private TextEntity currentTextEntity() {
        if (motionView != null && motionView.getSelectedEntity() instanceof TextEntity) {
            return ((TextEntity) motionView.getSelectedEntity());
        } else {
            return null;
        }
    }

    public void hideControls() {
        int iv_sicker_arraylist_size = ivStickerArraylist.size();
        if (!ivStickerArraylist.isEmpty()) {
            for (int i = 0; i <= iv_sicker_arraylist_size - 1; i++) {
                ivSticker = ivStickerArraylist.get(i);
                ivSticker.setControlItemsHidden(true);
                ivSticker.setControlsVisibility(false);
            }
        }
    }

    public void hide() {
        int iv_piercing_arraylist_size = ivPiervingarrayList.size();
        if (!ivStickerArraylist.isEmpty()) {
            for (int i = 0; i <= iv_piercing_arraylist_size - 1; i++) {
                ivPiercing = ivPiervingarrayList.get(i);

                ivPiercing.setControlItemsHidden(true);
                ivPiercing.setControlsVisibility(false);
            }
        }
    }

    private void showInterstitial() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_screen);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle(null);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);*/
        //download= (Button) findViewById(R.id.download);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.back1);
        getSupportActionBar().setTitle(Html.fromHtml("<small>Unique art Photo Frame</small>"));
        relativeLayout = (RelativeLayout) findViewById(R.id.rl_content_root);
        relative_save_image = (RelativeLayout) findViewById(R.id.relative_save_image);
        imgsticker = (ImageView) findViewById(R.id.img_sticker);
        stickerlayout = (LinearLayout) findViewById(R.id.sticker_layout);
        recycler_sticker = (RecyclerView) findViewById(R.id.recycler_sticker);
        stickerFrameLayout = (FrameLayout) findViewById(R.id.sticker_framelayout);
        color = (ImageButton) findViewById(R.id.text_entity_color_change);
        font = (ImageButton) findViewById(R.id.text_entity_font_change);
        textlayout = (LinearLayout) findViewById(R.id.text_layout);
        framelayout = (LinearLayout) findViewById(R.id.frame_layout);
        frameRecylerview = (RecyclerView) findViewById(R.id.recycler_frame);
        textEntityEditPanel = findViewById(R.id.main_motion_text_entity_edit_panel);
        relativeLayouttextpanel = (RelativeLayout) findViewById(R.id.relativeLayouttextpanel);

        imageselect = (ImageView) findViewById(R.id.img_main_select);
        imageframeselect = (ImageView) findViewById(R.id.frame_image_view);
        savelayout = (LinearLayout) findViewById(R.id.save_layout);
        btndel = (ImageButton) findViewById(R.id.btndel);
        editText = (EditText) findViewById(R.id.edit_text_view);
        adView = (AdView) findViewById(R.id.ad_view_editimg);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);
        interstitialAd = new InterstitialAd(EditScreenActivity.this);
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
        editScreen = this;
        frameRecylerview.setVisibility(View.VISIBLE);
        //recycler_sticker.setVisibility(View.VISIBLE);
        motionView = (MotionView) findViewById(R.id.main_motion_view);
        motionView.setMotionViewCallback(motionViewCallback);

        //recycler_sticker.setVisibility(View.INVISIBLE);
        imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame10));
        frameRecylerview.setVisibility(View.VISIBLE);
        final Bundle extras = getIntent().getExtras();
        Uri myUri = Uri.parse(extras.getString("imageUri"));
        imageselect.setImageURI(myUri);
        ivStickerArraylist = new ArrayList<>();
        ivPiervingarrayList = new ArrayList<>();

        createEffect();
        bottomTab();
        createFrame();
        //stickerImageList = new ArrayList<>();
       /* RelativeLayout item = (RelativeLayout)findViewById(R.id.text_editor_root);
        View child = getLayoutInflater().inflate(R.layout.text_editor_layout, null);
        item.addView(child);


*/


        this.fontProvider = new FontProvider(getResources());
        color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextEntityColor();
            }
        });
        font.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTextEntityFont();
            }
        });

        btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View layout = inflater.inflate(R.layout.text_editor_layout, null);
                editText = (EditText) layout.findViewById(R.id.edit_text_view);
                motionView.deletedSelectedEntity();
                font.setVisibility(View.GONE);
                color.setVisibility(View.GONE);
                btndel.setVisibility(View.GONE);


            }
        });
       /* savelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();

            }
        });*/
        imageselect.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        savedMatrix.set(matrix);
                        start.set(event.getX(), event.getY());
                        mode = DRAG;
                        lastEvent = null;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        oldDist = spacing(event);
                        if (oldDist > 10f) {
                            savedMatrix.set(matrix);
                            midPoint(mid, event);
                            mode = ZOOM;
                        }
                        lastEvent = new float[4];
                        lastEvent[0] = event.getX(0);
                        lastEvent[1] = event.getX(1);
                        lastEvent[2] = event.getY(0);
                        lastEvent[3] = event.getY(1);
                        d = rotation(event);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        mode = NONE;
                        lastEvent = null;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (mode == DRAG) {
                            matrix.set(savedMatrix);
                            float dx = event.getX() - start.x;
                            float dy = event.getY() - start.y;
                            matrix.postTranslate(dx, dy);
                        } else if (mode == ZOOM && event.getPointerCount() == 2) {
                            float newDist = spacing(event);
                            matrix.set(savedMatrix);
                            if (newDist > 10f) {
                                float scale = newDist / oldDist;
                                matrix.postScale(scale, scale, mid.x, mid.y);
                            }
                            if (lastEvent != null) {
                                newRot = rotation(event);
                                float r = newRot - d;
                                matrix.postRotate(r, imageselect.getMeasuredWidth() / 2, imageselect.getMeasuredHeight() / 2);
                            }
                        }
                        break;
                }
                imageselect.setImageMatrix(matrix);
                return true;
            }
        });
        recyclerStickerAdapter = new RecyclerStickerAdapter(EditScreenActivity.this, frameimageList);
        frameRecylerview.setAdapter(recyclerStickerAdapter);
        LinearLayoutManager horizontalLayout = new LinearLayoutManager(EditScreenActivity.this, LinearLayoutManager.HORIZONTAL, false);
        frameRecylerview.setLayoutManager(horizontalLayout);
        frameRecylerview.setVisibility(View.VISIBLE);
        frameRecylerview.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {


                        if (position == 0) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame1));

                        }
                        if (position == 1) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame2));

                        }
                        if (position == 2) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame3));
                        }
                        if (position == 3) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame4));
                        }
                        if (position == 4) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame5));
                        }
                        if (position == 5) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame6));
                        }
                        if (position == 6) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame7));
                        }
                        if (position == 7) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame8));
                        }
                        if (position == 8) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame9));
                        }
                        if (position == 9) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame10));
                        }
                        if (position == 10) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame11));
                        }
                        if (position == 11) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame12));
                        }
                        if (position == 12) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame13));
                        }
                        if (position == 13) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame14));
                        }
                        if (position == 14) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame15));
                        }
                        if (position == 15) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame16));
                        }
                        if (position == 16) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame17));
                        }
                        if (position == 17) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame18));
                        }
                        if (position == 18) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame19));
                        }
                        if (position == 19) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame20));
                        }
                        if (position == 20) {
                            imageframeselect.setBackgroundDrawable(getResources().getDrawable(R.drawable.frame21));
                        }

                    }
                }));

      /*  LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(EditScreenActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recycler_sticker.setLayoutManager(horizontalLayoutManagaer);*/
        //recycler_sticker.setVisibility(View.VISIBLE);
        recycler_sticker.addOnItemTouchListener(new RecyclerItemClickListener(EditScreenActivity.this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (position == 0) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker1);
                }
                if (position == 1) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker2);
                }
                if (position == 2) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker3);
                }
                if (position == 3) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker4);
                }
                if (position == 4) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker5);
                }
                if (position == 5) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker6);
                }
                if (position == 6) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker7);
                }
                /*if (position == 7) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker8);
                }*/
                if (position == 7) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker9);
                }
                if (position == 8) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker10);
                }
                if (position == 9) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker11);
                }
                if (position == 10) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker12);
                }
                if (position == 11) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker13);
                }
                if (position == 12) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker14);
                }
                if (position == 13) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker15);
                }
                if (position == 14) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker16);
                }
                if (position == 15) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker17);
                }
                if (position == 16) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker18);
                }
                if (position == 17) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker19);
                }
                if (position == 18) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker20);
                }
                if (position == 19) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker21);
                }
                if (position == 20) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker22);
                }
                if (position == 21) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker23);
                }
                if (position == 22) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker24);
                }
                if (position == 23) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker25);
                }
                if (position == 24) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker26);
                }
                if (position == 25) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker27);
                }
                if (position == 26) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker28);
                }
                if (position == 27) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker29);
                }
                if (position == 28) {
                    ivSticker = new StickerImageView(EditScreenActivity.this);
                    stickerFrameLayout.addView(ivSticker);
                    ivStickerArraylist.add(ivSticker);
                    ivSticker.setBackgroundResource(R.drawable.sticker30);
                }

            }
        }));
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
        relative_save_image.invalidate();
        relative_save_image.buildDrawingCache();
        bitmap1 = relative_save_image.getDrawingCache();
        bitmap2 = Bitmap.createScaledBitmap(bitmap1, relative_save_image.getWidth(), relative_save_image.getHeight(), false);
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
        int permissionCheck = ContextCompat.checkSelfPermission(EditScreenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditScreenActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, reques);

        } else {
            save();
        }
    }

    @Override
    public void textChanged(@NonNull String text) {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextLayer textLayer = textEntity.getLayer();
            if (!text.equals(textLayer.getText())) {
                textLayer.setText(text);
                textEntity.updateEntity();
                this.motionView.invalidate();
            }
        }

    }

    private float rotation(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float spacing(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPoint(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    private float rotationStk(MotionEvent event) {
        double delta_x = (event.getX(0) - event.getX(1));
        double delta_y = (event.getY(0) - event.getY(1));
        double radians = Math.atan2(delta_y, delta_x);
        return (float) Math.toDegrees(radians);
    }

    private float spacingStk(MotionEvent event) {
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);
        return (float) Math.sqrt(x * x + y * y);
    }

    private void midPointStk(PointF point, MotionEvent event) {
        float x = event.getX(0) + event.getX(1);
        float y = event.getY(0) + event.getY(1);
        point.set(x / 2, y / 2);
    }

    public static class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
        private final OnItemClickListener mListener;
        GestureDetector mGestureDetector;

        public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
            mListener = listener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View childView = rv.findChildViewUnder(e.getX(), e.getY());
            if (childView != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(childView, rv.getChildAdapterPosition(childView));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }

        public interface OnItemClickListener {
            void onItemClick(View view, int position);
        }
    }

    private void createEffect() {
        stickerImageList = new ArrayList<Integer>();
        stickerImageList.add(R.drawable.sticker1);
        stickerImageList.add(R.drawable.sticker2);
        stickerImageList.add(R.drawable.sticker3);
        stickerImageList.add(R.drawable.sticker4);
        stickerImageList.add(R.drawable.sticker5);
        stickerImageList.add(R.drawable.sticker6);
        stickerImageList.add(R.drawable.sticker7);
        //  stickerImageList.add(R.drawable.sticker8);
        stickerImageList.add(R.drawable.sticker9);
        stickerImageList.add(R.drawable.sticker10);
        stickerImageList.add(R.drawable.sticker11);
        stickerImageList.add(R.drawable.sticker12);
        stickerImageList.add(R.drawable.sticker13);
        stickerImageList.add(R.drawable.sticker14);
        stickerImageList.add(R.drawable.sticker15);
        stickerImageList.add(R.drawable.sticker16);
        stickerImageList.add(R.drawable.sticker17);
        stickerImageList.add(R.drawable.sticker18);
        stickerImageList.add(R.drawable.sticker19);
        stickerImageList.add(R.drawable.sticker20);
        stickerImageList.add(R.drawable.sticker21);
        stickerImageList.add(R.drawable.sticker22);
        stickerImageList.add(R.drawable.sticker23);
        stickerImageList.add(R.drawable.sticker24);
        stickerImageList.add(R.drawable.sticker25);
        stickerImageList.add(R.drawable.sticker26);
        stickerImageList.add(R.drawable.sticker27);
        stickerImageList.add(R.drawable.sticker28);
        stickerImageList.add(R.drawable.sticker29);
        stickerImageList.add(R.drawable.sticker30);
    }

    private void createFrame() {
        frameimageList = new ArrayList<Integer>();
        frameimageList.add(R.drawable.frame1);
        frameimageList.add(R.drawable.frame2);
        frameimageList.add(R.drawable.frame3);
        frameimageList.add(R.drawable.frame4);
        frameimageList.add(R.drawable.frame5);
        frameimageList.add(R.drawable.frame6);
        frameimageList.add(R.drawable.frame7);
        frameimageList.add(R.drawable.frame8);
        frameimageList.add(R.drawable.frame9);
        frameimageList.add(R.drawable.frame10);
        frameimageList.add(R.drawable.frame11);
        frameimageList.add(R.drawable.frame12);
        frameimageList.add(R.drawable.frame13);
        frameimageList.add(R.drawable.frame14);
        frameimageList.add(R.drawable.frame15);
        frameimageList.add(R.drawable.frame16);
        frameimageList.add(R.drawable.frame17);
        frameimageList.add(R.drawable.frame18);
        frameimageList.add(R.drawable.frame19);
        frameimageList.add(R.drawable.frame20);
        frameimageList.add(R.drawable.frame21);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
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
            if (id == R.id.share) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Unique Art PhotoFrame app by #dreamyinfotech \nhttps://goo.gl/5M3doA");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Unique Art PhotoFrame app ");
                startActivity(Intent.createChooser(sharingIntent, "Share using"));

                return true;
            }

            return super.onOptionsItemSelected(item);
        }


    public void bottomTab() {
        savelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                hideControls();
                hide();
                motionView.unselectEntity();
                motionView.setVisibility(View.VISIBLE);

                //textEntityEditPanel.setVisibility(View.GONE);

                if (relative_save_image.getVisibility() == View.VISIBLE) {
                    relative_save_image.invalidate();
                    relative_save_image.buildDrawingCache();

                    Bitmap bitmap1 = relative_save_image.getDrawingCache();
                    Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1, relative_save_image.getMeasuredWidth(), relative_save_image.getMeasuredHeight(), true);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte[] byteArray = stream.toByteArray();
                    String s = Base64.encodeToString(byteArray, Base64.DEFAULT);

                    SharedPreferences shared = getSharedPreferences("Hello", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("picture", s);
                    editor.commit();
                    startActivity(new Intent(EditScreenActivity.this, SaveScreenActivity.class));

                }
                /*if (flag == 0) {
                    flag = 1;
                    flag1 = 0;
                    flag2 = 0;
                    savelayout.setVisibility(View.VISIBLE);
                    recycler_sticker.setVisibility(View.GONE);
                    frameRecylerview.setVisibility(View.INVISIBLE);
                    color.setVisibility(View.GONE);
                    font.setVisibility(View.GONE);
                    btndel.setVisibility(View.GONE);
                    motionView.unselectEntity();
                    checkPermission();


                } else if (flag == 1) {
                    flag = 0;
                    flag1 = 0;
                    flag2 = 0;
                    savelayout.setVisibility(View.GONE);
                }*/


            }

        });
        framelayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (flag1 == 0) {
                    flag1 = 1;
                    flag2 = 0;
                    flag = 0;
                    frameRecylerview.setVisibility(View.VISIBLE);
                    recycler_sticker.setVisibility(View.GONE);
                    color.setVisibility(View.GONE);
                    font.setVisibility(View.GONE);
                    btndel.setVisibility(View.GONE);
                    motionView.unselectEntity();
                    RecyclerStickerAdapter recycle_adapter = new RecyclerStickerAdapter(EditScreenActivity.this, frameimageList);
                    frameRecylerview.setAdapter(recycle_adapter);
                    LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(EditScreenActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    frameRecylerview.setLayoutManager(horizontalLayoutManagaer);


                } else if (flag1 == 1) {
                    flag1 = 0;
                    flag = 0;
                    flag2 = 0;
                    frameRecylerview.setVisibility(View.GONE);
                }


            }
        });
        stickerlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag2 == 0) {
                    flag2 = 1;
                    flag1 = 0;
                    flag = 0;
                    recycler_sticker.setVisibility(View.VISIBLE);
                    frameRecylerview.setVisibility(View.INVISIBLE);
                    motionView.unselectEntity();
                    color.setVisibility(View.GONE);
                    font.setVisibility(View.GONE);
                    btndel.setVisibility(View.GONE);
                    recyclerStickerAdapter = new RecyclerStickerAdapter(EditScreenActivity.this, stickerImageList);
                    recycler_sticker.setAdapter(recyclerStickerAdapter);
                    LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(EditScreenActivity.this, LinearLayoutManager.HORIZONTAL, false);
                    recycler_sticker.setLayoutManager(horizontalLayoutManagaer);



                } else if (flag2 == 1) {
                    flag2 = 0;
                    flag = 0;
                    flag2 = 0;
                    recycler_sticker.setVisibility(View.GONE);
                }
            }


        });

        textlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayouttextpanel.setVisibility(View.VISIBLE);
                frameRecylerview.setVisibility(View.GONE);
                recycler_sticker.setVisibility(View.GONE);
                Toast.makeText(EditScreenActivity.this, "Double Click to Enter Text", Toast.LENGTH_SHORT).show();
                addTextSticker();
            }
        });

    }


    protected void addTextSticker() {

        TextLayer textLayer = createTextLayer();
        TextEntity textEntity = new TextEntity(textLayer, motionView.getWidth(),
                motionView.getHeight(), fontProvider);
        motionView.addEntityAndPosition(textEntity);
        PointF center = textEntity.absoluteCenter();
        center.y = center.y * 0.5F;
        textEntity.moveCenterTo(center);


        motionView.invalidate();
        startTextEntityEditing();
    }

    private TextLayer createTextLayer() {
        TextLayer textLayer = new TextLayer();
        Font font = new Font();

        font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
        font.setTypeface(fontProvider.getDefaultFontName());
        textLayer.setFont(font);
        if (BuildConfig.DEBUG) {
            textLayer.setText("");
        }
        return textLayer;
    }

    private void changeTextEntityFont() {
        final List<String> fonts = fontProvider.getFontNames();
        FontsAdapter fontsAdapter = new FontsAdapter(EditScreenActivity.this, fonts, fontProvider);
        new AlertDialog.Builder(EditScreenActivity.this)
                .setTitle("Select Font")
                .setAdapter(fontsAdapter, new DialogInterface.OnClickListener() {
                    @Override

                    public void onClick(DialogInterface dialogInterface, int which) {
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().setTypeface(fonts.get(which));
                            textEntity.updateEntity();
                            motionView.invalidate();
                        }
                    }
                })
                .show();
    }

    private void changeTextEntityColor() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity == null) {
            return;
        }
        int initialColor = textEntity.getLayer().getFont().getColor();

        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose Color")
                .initialColor(initialColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(8) // magic number
                .setPositiveButton("Ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().setColor(selectedColor);
                            textEntity.updateEntity();
                            motionView.invalidate();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }



  /*  @Override
    public void onBackPressed() {
        EditScreenActivity.this.finish();
    }*/
}














