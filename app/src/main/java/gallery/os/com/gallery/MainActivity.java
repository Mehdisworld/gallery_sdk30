package gallery.os.com.gallery;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.facebook.ads.AudienceNetworkAds;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import gallery.os.com.gallery.Activity.MemoriesActivity;
import gallery.os.com.gallery.Activity.PhotosActivity;
import gallery.os.com.gallery.Activity.RecentActivity;
import gallery.os.com.gallery.Activity.ShorcutActivity;
import gallery.os.com.gallery.Activity.VideosActivity;
import gallery.os.com.gallery.Dialog.ColorChooserDialog;
import gallery.os.com.gallery.Dialog.NewFolder;
import gallery.os.com.gallery.Dialog.Theme;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {
    public static final int RequestPermissionCode = 1100;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    public static SharedPreferences s_sharedPreferences;
    public static SharedPreferences.Editor s_editor;
    private TextView mFolderCount, mPhotosCount, mVideosCount, mViewAll;
    RelativeLayout mRl_Bottom_Home, mRl_Bottom_Photos, mRl_Bottom_Videos, mRl_Bottom_Albums;
    private RecyclerView mRecyclerView;
    public ArrayList<String> mImageList = new ArrayList<>();
    public ArrayList<String> mVideoList = new ArrayList<>();
    int countFolders = 0;
    RequestOptions options;
    public RequestBuilder<Drawable> thumbRequest;
    Intent photos_intent, videos_intent, albums_intent;

    public static int s_theme_colorPrimary, s_theme_colorPrimaryDark, s_theme_colorAccent, mtheme_bg_tab;
    private int mtheme_bg_image;
    public static Boolean s_ThemeStatus = false;
    LinearLayout mbtn_nv_Themes, mlv_header, mlv_btn_cammera, mlv_btn_moment, mlv_btn_screenshot, mlv_btn_newfolder;
    ImageView mIv_nv_theme, mIV_nv_header;
    TextView mTv_nv_theme, mTv_gallery_TITLE, moreFromDev, privacyPolicy,shareApp;

    ImageView mIv_bottom_home, mIv_menu;
    TextView mTv_bottom_home;

    Switch mappLock;

    LinearLayout mlvContiner,bannerAds;
    @Override
    protected void onResume() {
        super.onResume();

        if (s_ThemeStatus) {
            s_ThemeStatus = false;
            finish();
            startActivity(MainActivity.this.getIntent());
        }

        mFolderCount.setTextColor(s_theme_colorPrimary);
        mPhotosCount.setTextColor(s_theme_colorPrimary);
        mVideosCount.setTextColor(s_theme_colorPrimary);
    }


    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            theme();
            mGetCurrentTheme();

            try {
                //getHashKey();
                bannerAds = findViewById(R.id.lv_ads);
                if(BannerAndFullAD.isNetworkAvailable(getApplicationContext())){
                    BannerAndFullAD.loadFullAD(getApplicationContext());

                    if(CC.admobBannerForHome){
                        CC.admobBannerForHome = false;
                        BannerAndFullAD.loadBannerAd(bannerAds,getApplicationContext());
                    }
                }
                //FirebaseAnalytics.getInstance(this);
                AudienceNetworkAds.initialize(this);

            } catch (Exception e) {
                e.printStackTrace();
            }

            s_sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
            s_editor = s_sharedPreferences.edit();

            mTv_gallery_TITLE = findViewById(R.id.tv_gallery_title);

            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);

            mRl_Bottom_Home = findViewById(R.id.rl_bttom_home);
            mRl_Bottom_Photos = findViewById(R.id.rl_bttom_photos);
            mRl_Bottom_Videos = findViewById(R.id.rl_bttom_videos);
            mRl_Bottom_Albums = findViewById(R.id.rl_bttom_albums);

            mFolderCount = findViewById(R.id.folder_count);
            mPhotosCount = findViewById(R.id.photos_count);
            mVideosCount = findViewById(R.id.videos_count);
            mRecyclerView = findViewById(R.id.recent_recyclerview);

            mViewAll = findViewById(R.id.viewall);

            mIv_menu = findViewById(R.id.iv_menu);
            mlvContiner = findViewById(R.id.lv_content);


            mbtn_nv_Themes = findViewById(R.id.nv_themes);
            mIv_nv_theme = findViewById(R.id.nv_iv_theme);
            mTv_nv_theme = findViewById(R.id.nv_tv_theme);

            moreFromDev = findViewById(R.id.morefromdev);
            shareApp = findViewById(R.id.shareap);
            privacyPolicy = findViewById(R.id.privacypolicy);


            mIv_bottom_home = findViewById(R.id.iv_bottom_home);
            mTv_bottom_home = findViewById(R.id.tv_bottom_home);

            mlv_header = findViewById(R.id.lv_header);
            mIV_nv_header = findViewById(R.id.iv_nv_header);

            mappLock = findViewById(R.id.swapplock);


            mlv_btn_cammera = findViewById(R.id.lv_btn_camera);
            mlv_btn_moment = findViewById(R.id.lv_btn_memont);
            mlv_btn_screenshot = findViewById(R.id.lv_btn_screenshot);
            mlv_btn_newfolder = findViewById(R.id.lv_btn_newfolder);

            options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).override(Target.SIZE_ORIGINAL).dontTransform();
            thumbRequest = Glide.with(MainActivity.this).load(R.drawable.loading).apply(options);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));

            Typeface titletf = Typeface.createFromAsset(getAssets(), "font/gothic.ttf");
            mTv_gallery_TITLE.setTypeface(titletf);
            Typeface type = Typeface.createFromAsset(getAssets(), "font/bebas.ttf");
            mPhotosCount.setTypeface(type);
            mFolderCount.setTypeface(type);
            mVideosCount.setTypeface(type);

            photos_intent = new Intent(MainActivity.this, PhotosActivity.class);
            photos_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            photos_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            photos_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            videos_intent = new Intent(MainActivity.this, VideosActivity.class);
            videos_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            videos_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            videos_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            albums_intent = new Intent(MainActivity.this, MemoriesActivity.class);
            albums_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            albums_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            albums_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            if (checkPermission()) {
                try {
                    getLoaderManager().initLoader(200, null, this);
                    getLoaderManager().initLoader(100, null, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("getGridview", true);
                    editor.apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                requestPermission();
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        mIv_nv_theme.setImageTintList(ColorStateList.valueOf(s_theme_colorPrimary));
                        mTv_nv_theme.setTextColor(s_theme_colorPrimary);
                        mIv_bottom_home.setImageTintList(ColorStateList.valueOf(s_theme_colorPrimary));
                        mTv_bottom_home.setTextColor(s_theme_colorPrimary);
                        mViewAll.setTextColor(s_theme_colorPrimary);
                        mlv_header.setBackgroundResource(mtheme_bg_image);
                        mIV_nv_header.setImageResource(mtheme_bg_image);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            try {
                                mappLock.setThumbTintList(ColorStateList.valueOf(s_theme_colorPrimary));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            mRl_Bottom_Photos.setOnClickListener(this);
            mRl_Bottom_Videos.setOnClickListener(this);
            mRl_Bottom_Albums.setOnClickListener(this);
            mbtn_nv_Themes.setOnClickListener(this);
            mlv_btn_cammera.setOnClickListener(this);
            mlv_btn_moment.setOnClickListener(this);
            mlv_btn_screenshot.setOnClickListener(this);
            mlv_btn_newfolder.setOnClickListener(this);
            mViewAll.setOnClickListener(this);
            mIv_menu.setOnClickListener(this);
            privacyPolicy.setOnClickListener(this);
            shareApp.setOnClickListener(this);
            moreFromDev.setOnClickListener(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getHashKey(){
        try
        {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),
                    PackageManager
                            .GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void theme() {
        try {
            s_sharedPreferences = getSharedPreferences("VALUES", Context.MODE_PRIVATE);
            int theme = s_sharedPreferences.getInt("THEME", 0);
            Theme.settingTheme(MainActivity.this, theme);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void mGetCurrentTheme() {
        try {
            TypedValue typedValue = new TypedValue();
            this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
            s_theme_colorPrimary = typedValue.data;
            this.getTheme().resolveAttribute(R.attr.colorPrimaryDark, typedValue, true);
            s_theme_colorPrimaryDark = typedValue.data;
            this.getTheme().resolveAttribute(R.attr.colorAccent, typedValue, true);
            s_theme_colorAccent = typedValue.data;

            this.getTheme().resolveAttribute(R.attr.icon, typedValue, true);
            mtheme_bg_image = typedValue.resourceId;

            this.getTheme().resolveAttribute(R.attr.actionModeBackground, typedValue, true);
            mtheme_bg_tab = typedValue.resourceId;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    getWindow().setStatusBarColor(s_theme_colorPrimaryDark);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_bttom_photos:
                try {
                    if (!checkPermission()) {
                        try {
                            Toast.makeText(this, "Photos", Toast.LENGTH_SHORT).show();
                            Snackbar snackbar = Snackbar
                                    .make(mlvContiner, "First Get Gallery Storage Permission", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {

                            startActivity(photos_intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rl_bttom_videos:
                try {
                    if (!checkPermission()) {
                        try {

                            Snackbar snackbar = Snackbar
                                    .make(mlvContiner, "First Get Gallery Storage Permission", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {

                            startActivity(videos_intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rl_bttom_albums:
                try {
                    if (!checkPermission()) {
                        try {
                            Snackbar snackbar = Snackbar
                                    .make(mlvContiner, "First Get Gallery Storage Permission", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            startActivity(albums_intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_menu:
                try {
                    drawerLayout.openDrawer(Gravity.LEFT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.nv_themes:
                try {
                    ColorChooserDialog dialog = new ColorChooserDialog();
                    dialog.setOnItemChoose(new ColorChooserDialog.OnItemChoose() {
                        @Override
                        public void onClick(int position) {
                            try {
                                setThemeFragment(position);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onSaveChange() {
                            try {
                                startActivity(new Intent(MainActivity.this, MainActivity.class));
                                finish();
                                overridePendingTransition(0, 0);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    });
                    dialog.show(getFragmentManager(), "fragment_color_chooser");
                    drawerLayout.closeDrawer(Gravity.LEFT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

              /*  Intent intent1 = new Intent(MainActivity.this, ThemeActivity.class);
                startActivity(intent1);*/
            /*    getFolderFlag*/

                break;

            case R.id.lv_btn_camera:
                try {
                    if (!checkPermission()) {
                        try {
                            Snackbar snackbar = Snackbar
                                    .make(mlvContiner, "First Get Gallery Storage Permission", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Intent lv_btn_cammera_intent = new Intent(MainActivity.this, ShorcutActivity.class);
                            lv_btn_cammera_intent.putExtra("getFolderFlag", 1);
                            startActivity(lv_btn_cammera_intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.lv_btn_memont:
                try {
                    if (!checkPermission()) {
                        try {
                            Snackbar snackbar = Snackbar
                                    .make(mlvContiner, "First Get Gallery Storage Permission", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Intent lv_btn_memont_intent = new Intent(MainActivity.this, ShorcutActivity.class);
                            lv_btn_memont_intent.putExtra("getFolderFlag", 2);
                            startActivity(lv_btn_memont_intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.lv_btn_screenshot:
                try {
                    if (!checkPermission()) {
                        try {
                            Snackbar snackbar = Snackbar
                                    .make(mlvContiner, "First Get Gallery Storage Permission", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Intent lv_btn_screenshot_intent = new Intent(MainActivity.this, ShorcutActivity.class);
                            lv_btn_screenshot_intent.putExtra("getFolderFlag", 3);
                            startActivity(lv_btn_screenshot_intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.lv_btn_newfolder:
                try {
                    final NewFolder newFolder = new NewFolder(new NewFolder.OnClickListener() {
                        @Override
                        public void NewFolder_Btn(View view, Dialog dialog, String s) {
                            File myDirectory = new File(Environment.getExternalStorageDirectory(), s);
                            if (!myDirectory.exists()) {
                                myDirectory.mkdirs();
                            }
                            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.moments_thumb);
                            File file = new File(myDirectory, "zzzzzz.png");
                            try {
                                OutputStream outStream = new FileOutputStream(file);
                                bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                                outStream.flush();
                                outStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            MediaScannerConnection.scanFile(MainActivity.this, new String[]{file.getAbsolutePath()}, null,
                                    new MediaScannerConnection.OnScanCompletedListener() {
                                        @Override
                                        public void onScanCompleted(String path, Uri uri) {
                                        }
                                    });

                        }

                        @Override
                        public void Cancel_Btn(View view, Dialog dialog) {

                        }
                    });
                    newFolder.show(getFragmentManager(), "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.viewall:
                try {
                    if (!checkPermission()) {
                        try {
                            Snackbar snackbar = Snackbar
                                    .make(mlvContiner, "First Get Gallery Storage Permission", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            startActivity(new Intent(MainActivity.this, RecentActivity.class));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.morefromdev:
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://search?q=pub:LM+Inc"));
                    startActivity(intent);
                }catch (Exception e){}
                break;
            case R.id.shareap:
                shareAppLink();
                break;
            case R.id.privacypolicy:
                try{
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://lminc1.blogspot.com/"));
                    startActivity(intent);
                }catch (Exception e){}
                break;
        }
    }

    public void shareAppLink(){
        try{
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            String shareSubText = "Gallery - The Beautiful Photo App";
            String shareBodyText = "https://play.google.com/store/apps/details?id=com.phone.stargallery";
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubText);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(shareIntent, "Share With"));
        }catch (Exception e){}
    }


    private void CountAnimation(final TextView textView, int count) {
        try {
            ValueAnimator animator = new ValueAnimator();
            animator.setObjectValues(0, count);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    try {
                        textView.setText(String.valueOf(animation.getAnimatedValue()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            animator.setEvaluator(new TypeEvaluator<Integer>() {
                public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                    return Math.round(startValue + (endValue - startValue) * fraction);
                }
            });
            animator.setDuration(1000);
            animator.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        if (i == 100) {
            mImageList = new ArrayList<>();
            return new CursorLoader(MainActivity.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
        } else if (i == 200) {
            mVideoList = new ArrayList<>();
            return new CursorLoader(MainActivity.this, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Video.Media.DATE_TAKEN + " DESC");
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == 100) {
            mImageList = new ArrayList<>();
            ArrayList<String> folderlist = new ArrayList<>();
            Boolean boolean_folder = false;
            try {
                cursor.moveToFirst();
                try {
                    do {
                        mImageList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        File file = new File(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))).getParentFile().getAbsoluteFile();
                        try {
                            for (int i = 0; i < folderlist.size(); i++) {
                                try {
                                    if (folderlist.get(i).equals(file.getAbsolutePath())) {
                                        boolean_folder = true;
                                        break;
                                    } else {
                                        boolean_folder = false;
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                            try {
                                if (!boolean_folder) {
                                    folderlist.add(file.getAbsolutePath());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {

                countFolders = folderlist.size();

                CountAnimation(mPhotosCount, mImageList.size());
                CountAnimation(mFolderCount, countFolders);
                mRecyclerView.setAdapter(new RecentAdapter(mImageList));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (loader.getId() == 200) {
            try {
                CountAnimation(mVideosCount, cursor.getCount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<Cursor> loader) {
    }

    public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.MyViewHolder> {
        private List<String> recentList;

        RecentAdapter(List<String> recentList) {
            this.recentList = recentList;
        }

        @NonNull
        @Override
        public RecentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_recycler_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecentAdapter.MyViewHolder holder, int position) {
            try {
                Glide.with(MainActivity.this)
                        .load(recentList.get(position))
                        .thumbnail(thumbRequest)
                        .into(holder.photo);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            if (recentList.size() > 10) {
                return Math.min(recentList.size(), 10);
            } else {
                return recentList.size();
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            RoundedImageView photo;

            MyViewHolder(View itemView) {
                super(itemView);
                photo = itemView.findViewById(R.id.recent_image);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this, RecentActivity.class));
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            moveTaskToBack(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*System.gc();
        System.exit(0);*/
    }


    public void setThemeFragment(int theme) {
        switch (theme) {
            case 1:
                try {
                    s_editor = s_sharedPreferences.edit();
                    s_editor.putInt("THEME", 1).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 2:
                try {
                    s_editor = s_sharedPreferences.edit();
                    s_editor.putInt("THEME", 2).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 3:
                try {
                    s_editor = s_sharedPreferences.edit();
                    s_editor.putInt("THEME", 3).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 4:
                try {
                    s_editor = s_sharedPreferences.edit();
                    s_editor.putInt("THEME", 4).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 5:
                try {
                    s_editor = s_sharedPreferences.edit();
                    s_editor.putInt("THEME", 5).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 6:
                try {
                    s_editor = s_sharedPreferences.edit();
                    s_editor.putInt("THEME", 6).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case 7:
                try {
                    s_editor = s_sharedPreferences.edit();
                    s_editor.putInt("THEME", 7).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 8:
                try {
                    s_editor = s_sharedPreferences.edit();
                    s_editor.putInt("THEME", 8).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 9:
                try {
                    s_editor = s_sharedPreferences.edit();
                    s_editor.putInt("THEME", 9).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == RequestPermissionCode) {
                try {
                    if (checkPermission()) {
                        try {
                            getLoaderManager().initLoader(200, null, this);
                            getLoaderManager().initLoader(100, null, this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            mRl_Bottom_Photos.setOnClickListener(this);
                            mRl_Bottom_Videos.setOnClickListener(this);
                            mRl_Bottom_Albums.setOnClickListener(this);
                            mbtn_nv_Themes.setOnClickListener(this);
                            mlv_btn_cammera.setOnClickListener(this);
                            mlv_btn_moment.setOnClickListener(this);
                            mlv_btn_screenshot.setOnClickListener(this);
                            mViewAll.setOnClickListener(this);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Snackbar snackbar = Snackbar
                                    .make(mlvContiner, "First Get Gallery Storage Permission", Snackbar.LENGTH_LONG)
                                    .setAction("OK", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            try {
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivity(intent);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    });

                            snackbar.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void requestPermission() {
        try {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{WRITE_EXTERNAL_STORAGE,}, RequestPermissionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkPermission() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

}
