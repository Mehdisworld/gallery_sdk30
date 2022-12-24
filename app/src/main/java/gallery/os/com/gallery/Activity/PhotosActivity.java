package gallery.os.com.gallery.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import gallery.os.com.gallery.BannerAndFullAD;
import gallery.os.com.gallery.CC;
import gallery.os.com.gallery.Database.MomentDB;
import gallery.os.com.gallery.Dialog.CreateMoment;
import gallery.os.com.gallery.Dialog.Delete;
import gallery.os.com.gallery.Dialog.Information;
import gallery.os.com.gallery.Dialog.Multi_Information;
import gallery.os.com.gallery.Dialog.NewFolder;
import gallery.os.com.gallery.Dialog.Rename;
import gallery.os.com.gallery.Extera.CustomTabLayout;
import gallery.os.com.gallery.Extera.CustomViewPager;
import gallery.os.com.gallery.Extera.RecyclePagerAdapter;
import gallery.os.com.gallery.Extera.SizeUnit;
import gallery.os.com.gallery.MainActivity;
import gallery.os.com.gallery.Model.Directorywise;
import gallery.os.com.gallery.R;

import static androidx.viewpager.widget.PagerAdapter.POSITION_NONE;
import static gallery.os.com.gallery.MainActivity.mtheme_bg_tab;
import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;
import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimaryDark;

public class PhotosActivity extends AppCompatActivity implements View.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    RelativeLayout mRl_Bottom_Home, mRl_Bottom_Photos, mRl_Bottom_Videos, mRl_Bottom_Memories;
    RelativeLayout mRl_Bottom_Album, mRl_Bottom_Share, mRl_Bottom_info, mRl_Bottom_Delete;
    Intent home_intent, videos_intent, albums_intent;
    ArrayList<Directorywise> mDirectoryViewData = new ArrayList<>();
    CustomTabLayout mTabLayout;
    CustomViewPager mViewpager;
    TextView mTv_Select, mTv_Cancel;
    RequestOptions options;
    public RequestBuilder<Drawable> thumbRequest;
    ViewPagerAdapter viewPagerAdapter;
    RelativeLayout mRl_toolbar, mRl_titlebar;
    ImageView mIv_ChangeGrid, mIV_newFolder, mIV_delete, mIV_rename;
    SharedPreferences sharedPreferences;
    ImageView mIv_Close, mIv_Copy, mIv_Move, mIv_SelectAll, mIv_pasteClose, mIv_Paste;
    TextView mTv_Count, mTv_PasteCount, mTv_Title;
    ArrayList<String> mSelectFiles = new ArrayList<>();
    int mCopyMoveFlag = 0;
    LinearLayout mBottomMenu, mlvBottom;
    ImageView mIv_bottom_Photos;
    TextView mTv_bottom_Photos;
    SizeUnit sizeUnitkb = SizeUnit.KB;
    SizeUnit sizeUnitmb = SizeUnit.MB;
    SizeUnit sizeUnitgb = SizeUnit.GB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {

            setContentView(R.layout.activity_photos);
            try {
                BannerAndFullAD.loadFullADForOtherWhere(getApplicationContext());
                mTabLayout = findViewById(R.id.circle_tab_view);

                mViewpager = findViewById(R.id.viewpager);
                mBottomMenu = findViewById(R.id.lv_bottommenu);
                mlvBottom = findViewById(R.id.lv_bottom);

                mRl_Bottom_Home = findViewById(R.id.rl_bttom_home);
                mRl_Bottom_Photos = findViewById(R.id.rl_bttom_photos);
                mRl_Bottom_Videos = findViewById(R.id.rl_bttom_videos);
                mRl_Bottom_Memories = findViewById(R.id.rl_bttom_albums);

                mRl_Bottom_Album = findViewById(R.id.rl_bottom_album);
                mRl_Bottom_Share = findViewById(R.id.rl_bottom_share);
                mRl_Bottom_info = findViewById(R.id.rl_bottom_info);
                mRl_Bottom_Delete = findViewById(R.id.rl_bottom_delete);

                mRl_toolbar = findViewById(R.id.lv_toolbar);
                mRl_titlebar = findViewById(R.id.lv_titlebar);
                mIv_ChangeGrid = findViewById(R.id.iv_change_grid);
                mIV_newFolder = findViewById(R.id.iv_newfolder);
                mIV_delete = findViewById(R.id.iv_delete);
                mIV_rename = findViewById(R.id.iv_rename);
                mIv_Close = findViewById(R.id.iv_close);
                mIv_Copy = findViewById(R.id.iv_copy);
                mIv_Move = findViewById(R.id.iv_move);
                mIv_SelectAll = findViewById(R.id.iv_select_all);
                mTv_Count = findViewById(R.id.tv_selection_count);

                mIv_pasteClose = findViewById(R.id.iv_paste_close);
                mIv_Paste = findViewById(R.id.iv_paste);
                mTv_PasteCount = findViewById(R.id.tv_selected_items);
                mTv_Title = findViewById(R.id.tv_title);

                mIv_bottom_Photos = findViewById(R.id.iv_bottom_photos);
                mTv_bottom_Photos = findViewById(R.id.tv_bottom_photos);


                mTv_Select = findViewById(R.id.tv_select);
                mTv_Cancel = findViewById(R.id.tv_cancel);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


                options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).override(Target.SIZE_ORIGINAL).dontTransform();

                thumbRequest = Glide.with(PhotosActivity.this).load(R.drawable.loading).apply(options);

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                home_intent = new Intent(PhotosActivity.this, MainActivity.class);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                home_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                videos_intent = new Intent(PhotosActivity.this, VideosActivity.class);
                videos_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                videos_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                videos_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                albums_intent = new Intent(PhotosActivity.this, MemoriesActivity.class);
                albums_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                albums_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                albums_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                overridePendingTransition(0, 0);

                mRl_Bottom_Home.setOnClickListener(this);
                mRl_Bottom_Videos.setOnClickListener(this);
                mRl_Bottom_Memories.setOnClickListener(this);

                mRl_Bottom_Album.setOnClickListener(this);
                mRl_Bottom_Share.setOnClickListener(this);
                mRl_Bottom_info.setOnClickListener(this);
                mRl_Bottom_Delete.setOnClickListener(this);

                mIv_ChangeGrid.setOnClickListener(this);
                mIV_newFolder.setOnClickListener(this);
                mIV_delete.setOnClickListener(this);
                mIV_rename.setOnClickListener(this);
                mIv_Close.setOnClickListener(this);
                mIv_Copy.setOnClickListener(this);
                mIv_Move.setOnClickListener(this);
                mIv_SelectAll.setOnClickListener(this);
                mIv_pasteClose.setOnClickListener(this);
                mIv_Paste.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
            boolean switchOnOFF = sharedPreferences.getBoolean("getGridview", false);
            if (switchOnOFF) {
                try {
                    Glide.with(PhotosActivity.this).load(R.drawable.ic_list_view).into(mIv_ChangeGrid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Glide.with(PhotosActivity.this).load(R.drawable.ic_grid_view).into(mIv_ChangeGrid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            try {
                getLoaderManager().initLoader(400, null, this);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        mRl_titlebar.setBackgroundColor(s_theme_colorPrimary);
                        mRl_toolbar.setBackgroundColor(s_theme_colorPrimary);
                        mIv_bottom_Photos.setImageTintList(ColorStateList.valueOf(s_theme_colorPrimary));
                        mTv_bottom_Photos.setTextColor(s_theme_colorPrimary);
                        try {
                            mTabLayout.setTabBackgroundResId(mtheme_bg_tab);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            mTabLayout.setSelectedTabIndicator(mtheme_bg_tab);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            mTabLayout.setTabTextColors(s_theme_colorPrimary, Color.WHITE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            getWindow().setStatusBarColor(s_theme_colorPrimaryDark);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mTv_Select.setOnClickListener(this);
                mTv_Cancel.setOnClickListener(this);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void checkFullAdStatus(){
        if(CC.needToShowAdmobFull){
            BannerAndFullAD.showLoadedFullAD(getApplicationContext());
        }else if(CC.singleTime){
            BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
        }else{}
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            mTabLayout.setTabBackgroundResId(mtheme_bg_tab);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mTabLayout.setSelectedTabIndicator(mtheme_bg_tab);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mTabLayout.setTabTextColors(s_theme_colorPrimary, Color.WHITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_bttom_home:
                try {
                    startActivity(home_intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.rl_bttom_videos:
                try {
                    startActivity(videos_intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rl_bttom_albums:
                try {
                    startActivity(albums_intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.tv_select:
                try {
                    viewPagerAdapter.setSelection(mViewpager.getCurrentItem(), true);
                    mViewpager.setPagingEnabled(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_cancel:
                try {
                    //  photoTabAdapter.setSelectionPosition();
                    // photoTabAdapter.notifyDataSetChanged();
                    mTabLayout.setVisibility(View.VISIBLE);
                    mRl_toolbar.setVisibility(View.GONE);
                    //mRl_titlebar.setVisibility(View.VISIBLE);
                    viewPagerAdapter.deselection(mViewpager.getCurrentItem());
                    mViewpager.setPagingEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_change_grid:
                try {
                    int i = mViewpager.getCurrentItem();
                    boolean switchOnOFF = sharedPreferences.getBoolean("getGridview", false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("getGridview", !switchOnOFF);
                    editor.apply();

                    if (!switchOnOFF) {
                        try {
                            Glide.with(PhotosActivity.this).load(R.drawable.ic_list_view).into(mIv_ChangeGrid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Glide.with(PhotosActivity.this).load(R.drawable.ic_grid_view).into(mIv_ChangeGrid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    ArrayList<PhotosAdepter> photosAdepters = new ArrayList<>();
                    for (int j = 0; j < mDirectoryViewData.size(); j++) {
                        photosAdepters.add(new PhotosAdepter(j, PhotosActivity.this, false));
                    }
                    try {
                        //  viewPagerAdapter.notifyDataSetChanged();
                        viewPagerAdapter = new ViewPagerAdapter(photosAdepters);
                        mViewpager.setAdapter(viewPagerAdapter);
                        mViewpager.setCurrentItem(i);

                       /* try {
                            mTabLayout.setTabBackgroundResId(mtheme_bg_tab);
                            Drawable drawable = null;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                drawable = getDrawable(R.drawable.tab_selected_border);
                            }
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                drawable.setTint(s_theme_colorPrimary);
                            }
                            mTabLayout.setSelectedTabIndicator(drawable);
                            mTabLayout.setTabTextColors(s_theme_colorPrimary, Color.WHITE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_close:
                try {
                    mTabLayout.setVisibility(View.VISIBLE);
                    mRl_toolbar.setVisibility(View.GONE);
                    viewPagerAdapter.deselection(mViewpager.getCurrentItem());
                    mViewpager.setPagingEnabled(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_copy:
                try {
                    mCopyMoveFlag = 2;
                    mSelectFiles = new ArrayList<>();
                    viewPagerAdapter.getSelectionFile(mViewpager.getCurrentItem());
                    mTv_PasteCount.setText(" " + mSelectFiles.size() + " Copy");
                    mtitlebarSelection(false);
                    viewPagerAdapter.deselection(mViewpager.getCurrentItem());
                    mViewpager.setPagingEnabled(true);
                    mIV_delete.setVisibility(View.GONE);
                    mIV_rename.setVisibility(View.GONE);
                    mIv_ChangeGrid.setVisibility(View.GONE);
                    mIV_newFolder.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Copy Selected Files", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_move:
                try {
                    mCopyMoveFlag = 1;
                    mSelectFiles = new ArrayList<>();
                    viewPagerAdapter.getSelectionFile(mViewpager.getCurrentItem());
                    // mTabLayout.setVisibility(View.VISIBLE);
                    // mRl_toolbar.setVisibility(View.GONE);
                    mTv_PasteCount.setText(" " + mSelectFiles.size() + " Move");
                    mtitlebarSelection(false);
                    viewPagerAdapter.deselection(mViewpager.getCurrentItem());
                    mViewpager.setPagingEnabled(true);
                    mIv_ChangeGrid.setVisibility(View.GONE);
                    mIV_delete.setVisibility(View.GONE);
                    mIV_rename.setVisibility(View.GONE);
                    mIV_newFolder.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Cut Selected Files", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_select_all:
                try {
                    viewPagerAdapter.setSelection(mViewpager.getCurrentItem(), true);
                    mViewpager.setPagingEnabled(false);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_paste_close:
                try {
                    mtitlebarSelection(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_paste:
                try {
                    new PasteAsyncTask().execute();
                    Toast.makeText(getApplicationContext(), "Paste " + mSelectFiles.size() + " is selected", Toast.LENGTH_SHORT).show();
                    BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_bottom_album:
                try {
                    viewPagerAdapter.getSelectionFile(mViewpager.getCurrentItem());
                    if (mSelectFiles.size() >= 4) {
                        final MomentDB momentDB = new MomentDB(PhotosActivity.this);
                        momentDB.open();
                        CreateMoment createMoment = new CreateMoment(new CreateMoment.OnClickListener() {
                            @Override
                            public void Create_Btn(View view, Dialog dialog, String s) {
                                try {
                                    momentDB.createMoment(s, mSelectFiles, mSelectFiles.get(0));
                                    momentDB.close();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                mSelectFiles.clear();
                                mtitlebarSelection(true);
                                viewPagerAdapter.deselection(mViewpager.getCurrentItem());
                                mViewpager.setPagingEnabled(true);
                            }

                            @Override
                            public void Cancel_Btn(View view, Dialog dialog) {

                            }
                        });
                        try {
                            Bundle bundle = new Bundle();
                            bundle.putString("filename", "");
                            createMoment.setArguments(bundle);
                            createMoment.show(getFragmentManager(), "");
                            BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Select more then 4 Photos", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
            case R.id.rl_bottom_share:
                try {
                    viewPagerAdapter.getSelectionFile(mViewpager.getCurrentItem());
                    new ShareAsyncTask().execute();
                    BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rl_bottom_info:
                try {
                    viewPagerAdapter.getSelectionFile(mViewpager.getCurrentItem());
                    if (mSelectFiles.size() == 1) {
                        try {
                            Information information = new Information(mSelectFiles.get(0));
                            information.show(getFragmentManager(), "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Multi_Information multi_information = new Multi_Information(mSelectFiles);
                            Bundle bundle1 = new Bundle();
                            bundle1.putInt("id", 0);
                            multi_information.setArguments(bundle1);
                            multi_information.show(getFragmentManager(), "");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.rl_bottom_delete:
                try {
                    viewPagerAdapter.getSelectionFile(mViewpager.getCurrentItem());
                    Delete delete = new Delete(new Delete.OnDeleteListener() {
                        @Override
                        public void Delete_Btn(View view, Dialog dialog) {
                            try {
                                new DeleteAsyncTask().execute();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            dialog.dismiss();
                        }

                        @Override
                        public void Cancel_Btn(View view, Dialog dialog) {
                            dialog.dismiss();
                        }
                    });
                    delete.setCancelable(false);
                    delete.show(getFragmentManager(), "");
                    BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_newfolder:
                if (mIv_Paste.getVisibility() == View.VISIBLE) {
                    try {
                        NewFolder newFolder = new NewFolder(new NewFolder.OnClickListener() {
                            @Override
                            public void NewFolder_Btn(View view, Dialog dialog, String s) {
                                try {
                                    new PasteAsyncTask(s, true).execute();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void Cancel_Btn(View view, Dialog dialog) {
                                try {
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        newFolder.setCancelable(false);
                        newFolder.show(getFragmentManager(), "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        final NewFolder newFolder = new NewFolder(new NewFolder.OnClickListener() {
                            @Override
                            public void NewFolder_Btn(View view, Dialog dialog, String s) {
                                File myDirectory = new File(Environment.getExternalStorageDirectory(), s);
                                if (!myDirectory.exists()) {
                                    myDirectory.mkdirs();
                                }
                                Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.moments_thumb);
                                final File file = new File(myDirectory, "zzzzzz.png");
                                try {
                                    OutputStream outStream = new FileOutputStream(file);
                                    bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                                    outStream.flush();
                                    outStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                ArrayList<String> strings = new ArrayList<String>() {
                                };
                                strings.add(file.getAbsolutePath());
                                try {
                                    mDirectoryViewData.add(new Directorywise(s, strings));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    viewPagerAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {
                                    MediaScannerConnection.scanFile(PhotosActivity.this, new String[]{file.getAbsolutePath()}, null,
                                            new MediaScannerConnection.OnScanCompletedListener() {
                                                @Override
                                                public void onScanCompleted(String path, Uri uri) {
                                                }
                                            });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                try {

                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void Cancel_Btn(View view, Dialog dialog) {
                                try {
                                    dialog.dismiss();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        newFolder.show(getFragmentManager(), "");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.iv_delete:
                File deletefile = new File(mDirectoryViewData.get(mViewpager.getCurrentItem()).getDataList().get(0));
                if (deletefile.getAbsolutePath().contains("/storage/emulated/0")) {
                    Delete delete = new Delete(new Delete.OnDeleteListener() {
                        @Override
                        public void Delete_Btn(View view, Dialog dialog) {
                            new DeleteFolderAsyncTask().execute();
                            dialog.dismiss();
                        }

                        @Override
                        public void Cancel_Btn(View view, Dialog dialog) {
                            dialog.dismiss();
                        }
                    });
                    String s = "Delete " + deletefile.getParentFile().getName() + " Folder ?";
                    Bundle bundle = new Bundle();
                    bundle.putString("gettext", s);
                    delete.setArguments(bundle);
                    delete.setCancelable(false);
                    delete.show(getFragmentManager(), "");
                } else {
                    Toast.makeText(getApplicationContext(), "Can`t delete SD-card Folder ..", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.iv_rename:
                File renamefile = new File(mDirectoryViewData.get(mViewpager.getCurrentItem()).getDataList().get(0));
                if (renamefile.getAbsolutePath().contains("/storage/emulated/0")) {
                    Rename rename = new Rename(new Rename.OnClickListener() {
                        @Override
                        public void Rename_Btn(View view, Dialog dialog, String s) {
                            new RenameFolderAsyncTask(s).execute();
                            dialog.dismiss();
                        }

                        @Override
                        public void Cancel_Btn(View view, Dialog dialog) {
                            dialog.dismiss();
                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.putString("filename", renamefile.getParentFile().getName());
                    rename.setArguments(bundle);
                    rename.setCancelable(false);
                    rename.show(getFragmentManager(), "");
                } else {
                    Toast.makeText(getApplicationContext(), "Can`t delete SD-card Folder ..", Toast.LENGTH_SHORT).show();
                }

                break;

        }
    }

    public void mtitlebarSelection(Boolean aBoolean) {
        try {
            if (aBoolean) {
                try {
                    mIv_pasteClose.setVisibility(View.GONE);
                    mIv_Paste.setVisibility(View.GONE);
                    mTv_PasteCount.setVisibility(View.GONE);
                    mRl_toolbar.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.VISIBLE);
                    mIv_ChangeGrid.setVisibility(View.VISIBLE);
                    mIV_delete.setVisibility(View.VISIBLE);
                    mIV_rename.setVisibility(View.VISIBLE);
                    mIV_newFolder.setVisibility(View.VISIBLE);
                    mTv_Title.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    mIv_ChangeGrid.setVisibility(View.GONE);
                    mIV_delete.setVisibility(View.GONE);
                    mIV_rename.setVisibility(View.GONE);
                    mTv_Title.setVisibility(View.GONE);
                    mRl_toolbar.setVisibility(View.GONE);
                    mTabLayout.setVisibility(View.VISIBLE);
                    mIv_pasteClose.setVisibility(View.VISIBLE);
                    mIv_Paste.setVisibility(View.VISIBLE);
                    mIV_newFolder.setVisibility(View.VISIBLE);
                    mTv_PasteCount.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
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
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (id == 400) {
            return new CursorLoader(PhotosActivity.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor != null) {
            mDirectoryViewData = new ArrayList<>();
            boolean boolean_folder = false;
            try {
                int int_position = 0;
                int column_index_data;
                String absolutePathOfImage = null;
                column_index_data = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();
                try {
                    do {
                        absolutePathOfImage = cursor.getString(column_index_data);
                        String directorypath = new File(absolutePathOfImage).getParentFile().getAbsolutePath();
                        for (int i = 0; i < mDirectoryViewData.size(); i++) {
                            if (mDirectoryViewData.get(i).getDirectoryName().equalsIgnoreCase(directorypath)) {
                                boolean_folder = true;
                                int_position = i;
                                break;
                            } else {
                                boolean_folder = false;
                            }
                        }
                        if (boolean_folder) {
                            ArrayList<String> al_path = new ArrayList<>();
                            al_path.addAll(mDirectoryViewData.get(int_position).getDataList());
                            al_path.add(absolutePathOfImage);
                            mDirectoryViewData.get(int_position).setDataList(al_path);
                        } else {
                            ArrayList<String> al_path = new ArrayList<>();
                            al_path.add(absolutePathOfImage);
                            Directorywise obj_model = new Directorywise(directorypath, al_path);
                            mDirectoryViewData.add(obj_model);
                        }
                    } while (cursor.moveToNext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            ArrayList<PhotosAdepter> photosAdepters = new ArrayList<>();
            for (int i = 0; i < mDirectoryViewData.size(); i++) {
                photosAdepters.add(new PhotosAdepter(i, PhotosActivity.this, false));
            }

            int position = 0;
            try {
                position = mViewpager.getCurrentItem();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
               /* if (viewPagerAdapter != null) {
                    try {
                        viewPagerAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {*/
                try {
                    viewPagerAdapter = new ViewPagerAdapter(photosAdepters);
                    mViewpager.setAdapter(viewPagerAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mTabLayout.setupWithViewPager(null);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                mTabLayout.setupWithViewPager(mViewpager);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        try {
                            viewPagerAdapter.setNotifySubAdepter(position);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            mViewpager.setCurrentItem(position);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (loader.getId() == 400) {
            try {
                loader.reset();
                mDirectoryViewData.clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class PhotosAdepter extends RecyclerView.Adapter<PhotosAdepter.MyViewHolder> {
        int adepterPosition;
        Activity activity;
        Boolean selectionFlag = false;
        SparseBooleanArray booleanArray = new SparseBooleanArray();

        PhotosAdepter(int adepterPosition, Activity activity, Boolean aBoolean) {
            this.adepterPosition = adepterPosition;
            this.activity = activity;
            this.selectionFlag = aBoolean;
        }

        @NonNull
        @Override
        public PhotosAdepter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;
            if (sharedPreferences.getBoolean("getGridview", false)) {
                itemView = LayoutInflater.from(PhotosActivity.this).inflate(R.layout.viewpager_item, parent, false);
            } else {
                itemView = LayoutInflater.from(PhotosActivity.this).inflate(R.layout.photo_list_recycler_item, parent, false);
            }
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull final PhotosAdepter.MyViewHolder holder, final int position) {
            try {
                Glide.with(activity)
                        .load(mDirectoryViewData.get(adepterPosition).getDataList().get(position))
                        .thumbnail(thumbRequest)
                        .into(holder.photo);

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!sharedPreferences.getBoolean("getGridview", false)) {
                try {
                    File file = new File(mDirectoryViewData.get(adepterPosition).getDataList().get(position));
                    holder.mtv_name.setText(file.getName());
                    try {
                        if ((int) (file.length() / sizeUnitkb.inBytes()) > 1024) {
                            try{
                                if ((int) (file.length() / sizeUnitmb.inBytes()) > 1024) {
                                    try {
                                        holder.mtv_size.setText((int) (file.length() / sizeUnitgb.inBytes()) + "gb");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        holder.mtv_size.setText((int) (file.length() / sizeUnitmb.inBytes()) + "mb");

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }catch (Exception e){}

                        } else {
                            holder.mtv_size.setText((int) (file.length() / sizeUnitkb.inBytes()) + "kb");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            try {
                if (selectionFlag) {
                    try {
                        holder.lv_select.setVisibility(View.VISIBLE);
                        if (this.booleanArray.get(position)) {
                            try {
                                holder.imageView.setVisibility(View.VISIBLE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                holder.imageView.setVisibility(View.GONE);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        holder.lv_select.setVisibility(View.GONE);
                        holder.imageView.setVisibility(View.GONE);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return mDirectoryViewData.get(adepterPosition).getDataList().size();
        }

        public Boolean getaBoolean() {
            return selectionFlag;
        }

        void setaBoolean(Boolean aBoolean) {
            this.selectionFlag = aBoolean;
            try {
                for (int i = 0; i < mDirectoryViewData.get(adepterPosition).getDataList().size(); i++) {
                    try {
                        SparseBooleanArray sparseBooleanArray = booleanArray;
                        sparseBooleanArray.put(i, true);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        void setDeselect() {
            try {
                this.selectionFlag = false;
                mBottomMenu.setVisibility(View.GONE);
                mIv_ChangeGrid.setVisibility(View.GONE);
                mIV_delete.setVisibility(View.GONE);
                mIV_rename.setVisibility(View.GONE);
                mIV_newFolder.setVisibility(View.GONE);
                mlvBottom.setVisibility(View.VISIBLE);
                booleanArray = new SparseBooleanArray();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            RoundedImageView photo;
            ImageView imageView;
            TextView mtv_name, mtv_size;
            RelativeLayout lv_select;

            MyViewHolder(View itemView) {
                super(itemView);
                try {
                    photo = itemView.findViewById(R.id.recent_image);
                    imageView = itemView.findViewById(R.id.ic_select);
                    lv_select = itemView.findViewById(R.id.lv_select);
                    imageView.setColorFilter(s_theme_colorPrimaryDark);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    if (!sharedPreferences.getBoolean("getGridview", false)) {
                        try {
                            mtv_name = itemView.findViewById(R.id.name);
                            mtv_size = itemView.findViewById(R.id.size);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (selectionFlag) {
                                boolean z = false;
                                boolean value = booleanArray.get(getAdapterPosition());
                                if (value) {
                                    try {
                                        imageView.setVisibility(View.GONE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        imageView.setVisibility(View.VISIBLE);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }

                                SparseBooleanArray sparseBooleanArray = booleanArray;
                                if (!value) {
                                    z = true;
                                }
                                sparseBooleanArray.put(getAdapterPosition(), z);


                                int count = getCheckedCountForTextView();
                                if (count == 0) {
                                    try {
                                        selectionFlag = false;
                                        notifyDataSetChanged();
                                        mBottomMenu.setVisibility(View.GONE);
                                        mRl_toolbar.setVisibility(View.GONE);
                                        lv_select.setVisibility(View.GONE);
                                        mTabLayout.setVisibility(View.VISIBLE);
                                        mlvBottom.setVisibility(View.VISIBLE);
                                        mIv_ChangeGrid.setVisibility(View.VISIBLE);
                                        mIV_delete.setVisibility(View.VISIBLE);
                                        mIV_rename.setVisibility(View.VISIBLE);
                                        mIV_newFolder.setVisibility(View.VISIBLE);
                                        mViewpager.setPagingEnabled(true);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    try {
                                        mTv_Count.setText(count + " Selected");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                try {
                                    Intent intent = new Intent(PhotosActivity.this, ImageViewrActivity.class);
                                    intent.putStringArrayListExtra("ImageFiles", mDirectoryViewData.get(adepterPosition).getDataList());
                                    intent.putExtra("position", getAdapterPosition());
                                    startActivity(intent);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });

                itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        try {
                            if (mIv_Paste.getVisibility() == View.GONE) {
                                try {
                                    if (mDirectoryViewData.get(adepterPosition).getDataList().get(getAdapterPosition()).startsWith("/storage/emulated/0/")) {
                                        try {
                                            if (!selectionFlag) {
                                                try {
                                                    mlvBottom.setVisibility(View.GONE);
                                                    mIv_ChangeGrid.setVisibility(View.GONE);
                                                    mIV_delete.setVisibility(View.GONE);
                                                    mIV_rename.setVisibility(View.GONE);
                                                    mIV_newFolder.setVisibility(View.GONE);
                                                    mBottomMenu.setVisibility(View.VISIBLE);
                                                    selectionFlag = true;
                                                    mTabLayout.setVisibility(View.GONE);
                                                    mRl_toolbar.setVisibility(View.VISIBLE);
                                                    mViewpager.setPagingEnabled(false);
                                                    lv_select.setVisibility(View.VISIBLE);
                                                    view.performClick();
                                                    notifyDataSetChanged();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            } else {
                                                try {
                                                    view.performClick();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Can`t Select SD-CARD Images", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Remove frist selected items...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
            }
        }

        private Integer getCheckedCountForTextView() {
            Integer in = 0;
            for (int i = 0; i < this.getItemCount(); i++) {
                try {
                    if (this.booleanArray.get(i)) {
                        try {
                            in = in + 1;

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return in;
        }

        ArrayList<String> getSelectionList() {

            ArrayList<String> arrayList = new ArrayList<>();
            try {
                for (int i = 0; i < this.getItemCount(); i++) {
                    try {
                        if (this.booleanArray.get(i)) {
                            try {
                                if (mDirectoryViewData.get(adepterPosition).getDataList().get(i).startsWith("/storage/emulated/0/")) {

                                    try {
                                        arrayList.add(mDirectoryViewData.get(adepterPosition).getDataList().get(i));
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            return arrayList;
        }
    }

    public class ViewPagerAdapter extends RecyclePagerAdapter<ViewPagerAdapter.ViewHolder> {
        List<PhotosAdepter> photosAdepters = new ArrayList<>();

        Boolean selectionFlag = false;

        ViewPagerAdapter(List<PhotosAdepter> photosAdepters) {
            this.photosAdepters = photosAdepters;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return new File(mDirectoryViewData.get(position).getDirectoryName()).getName();
            //  return mDirectoryViewData.get(position).getDirectoryName();
        }

        @Override
        public void startUpdate(@NonNull ViewGroup container) {
            super.startUpdate(container);
        }

        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup container) {
            return new ViewHolder(container);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            try {
                holder.recyclerView.setAdapter(photosAdepters.get(position));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getCount() {
            return mDirectoryViewData.size();
        }

        class ViewHolder extends RecyclePagerAdapter.ViewHolder {
            RecyclerView recyclerView;

            ViewHolder(ViewGroup container) {
                super(new RecyclerView(container.getContext()));
                recyclerView = (RecyclerView) itemView;
                if (sharedPreferences.getBoolean("getGridview", false)) {
                    try {
                        recyclerView.setLayoutManager(new GridLayoutManager(PhotosActivity.this, 4));
                     } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        recyclerView.setLayoutManager(new LinearLayoutManager(PhotosActivity.this, LinearLayoutManager.VERTICAL, false));
                      } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }


        void setSelection(int selection, Boolean aBoolean) {
            try {
                photosAdepters.get(selection).setaBoolean(aBoolean);
                photosAdepters.get(selection).notifyDataSetChanged();
                getSelectionFile(selection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void deselection(int selection) {
            try {
                selectionFlag = false;
                photosAdepters.get(selection).setDeselect();
                photosAdepters.get(selection).notifyDataSetChanged();
                mIv_ChangeGrid.setVisibility(View.VISIBLE);
                mIV_delete.setVisibility(View.VISIBLE);
                mIV_rename.setVisibility(View.VISIBLE);
                mIV_newFolder.setVisibility(View.VISIBLE);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void getSelectionFile(int selection) {
            try {
                mSelectFiles = photosAdepters.get(selection).getSelectionList();
                mTv_Count.setText(mSelectFiles.size() + " Selected");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        void setNotifySubAdepter(int notifySubAdepterposition) {
            try {
                photosAdepters.get(notifySubAdepterposition).notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class PasteAsyncTask extends AsyncTask<String, String, String> {

        String newFoldername = "";
        Boolean isnewFolder = false;
        ArrayList<String> mediaScanDelete = new ArrayList<>();
        ArrayList<String> mediaScanUpdate = new ArrayList<>();
        ProgressDialog progressDialog;
        private int mcount = 0, mitemCount = 0;

        public PasteAsyncTask(String newFoldername, Boolean isnewFolder) {
            this.newFoldername = newFoldername;
            this.isnewFolder = isnewFolder;
        }

        public PasteAsyncTask() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mCopyMoveFlag == 1) {
                try {
                    progressDialog = ProgressDialog.show(PhotosActivity.this, "Please wait", "moving photos....", true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (mCopyMoveFlag == 2) {
                try {
                    progressDialog = ProgressDialog.show(PhotosActivity.this, "Please wait", "paste photos....", true, false);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            if (isnewFolder) {
                File myDirectory = new File(Environment.getExternalStorageDirectory(), newFoldername);
                if (!myDirectory.exists()) {
                    myDirectory.mkdirs();
                }
                if (mCopyMoveFlag == 1) {
                    String s = null;
                    try {
                        if (mSelectFiles.size() != 0) {
                            try {
                                File file = new File(mSelectFiles.get(0));
                                s = file.getParentFile().getAbsolutePath();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        for (int i = 0; i < mSelectFiles.size(); i++) {
                            try {
                                File sorceFile = new File(mSelectFiles.get(i));

                                if (sorceFile.exists() && myDirectory.exists()) {
                                    try {
                                        File targetFile = new File(myDirectory + "/" + sorceFile.getName());
                                        mcount = 0;
                                        mitemCount = 0;
                                        File newtargetFile = checkFileIsExtist(targetFile);
                                        sorceFile.renameTo(newtargetFile);
                                        mediaScanUpdate.add(newtargetFile.getAbsolutePath());
                                        mediaScanDelete.add(sorceFile.getAbsolutePath());
                                    } catch (Exception ignored) {
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mDirectoryViewData.add(new Directorywise(newFoldername, mediaScanUpdate));
                        if (s != null) {
                            try {
                                mDirectoryViewData.remove(new Directorywise(s, mediaScanDelete));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (mCopyMoveFlag == 2) {
                    try {
                        for (int i = 0; i < mSelectFiles.size(); i++) {
                            try {
                                File sorceFile = new File(mSelectFiles.get(i));
                                if (sorceFile.exists() && myDirectory.exists()) {
                                    try {
                                        File targetFile = new File(myDirectory + "/" + sorceFile.getName());
                                        mcount = 0;
                                        mitemCount = 0;
                                        File newtargetFile = checkFileIsExtist(targetFile);
                                        useFileChannel(sorceFile, newtargetFile);
                                        mediaScanUpdate.add(newtargetFile.getAbsolutePath());
                                    } catch (Exception ignored) {
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        mDirectoryViewData.add(new Directorywise(newFoldername, mediaScanUpdate));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (mediaScanUpdate.size() != 0) {
                    try {
                        String[] changedFiles = mediaScanUpdate.toArray(new String[mediaScanUpdate.size()]);
                        MediaScannerConnection.scanFile(getApplicationContext(), changedFiles, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {

                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (mediaScanDelete.size() != 0) {
                    try {
                        String[] changedFiles = mediaScanDelete.toArray(new String[mediaScanDelete.size()]);
                        MediaScannerConnection.scanFile(getApplicationContext(), changedFiles, null,
                                new MediaScannerConnection.OnScanCompletedListener() {
                                    @Override
                                    public void onScanCompleted(String path, Uri uri) {
                                        try {
                                            getContentResolver().delete(uri, null, null);
                                        } catch (Exception ignored) {
                                        }
                                        //   Log.e("MediaScan", "==>>>Mediascan Complated");
                                        //  new GetDataAsyncTask().execute();
                                        //     getLoaderManager().initLoader(400, null, PhotosActivity.this).reset();
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } else {


                try {
                    File destinationFile = new File(mDirectoryViewData.get(mViewpager.getCurrentItem()).getDataList().get(0)).getParentFile();
                    if (mCopyMoveFlag == 1) {
                        String s = null;
                        try {
                            if (mSelectFiles.size() != 0) {
                                try {
                                    File file = new File(mSelectFiles.get(0));
                                    s = file.getParentFile().getAbsolutePath();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            for (int i = 0; i < mSelectFiles.size(); i++) {
                                try {
                                    File sorceFile = new File(mSelectFiles.get(i));
                                    if (sorceFile.exists() && destinationFile.exists()) {
                                        try {
                                            File targetFile = new File(destinationFile + "/" + sorceFile.getName());
                                            mcount = 0;
                                            mitemCount = 0;
                                            File newtargetFile = checkFileIsExtist(targetFile);
                                            sorceFile.renameTo(newtargetFile);
                                            mediaScanUpdate.add(newtargetFile.getAbsolutePath());
                                            mediaScanDelete.add(sorceFile.getAbsolutePath());
                                        } catch (Exception ignored) {
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            mDirectoryViewData.add(new Directorywise(newFoldername, mediaScanUpdate));
                            if (s != null) {
                                try {
                                    mDirectoryViewData.remove(new Directorywise(s, mediaScanDelete));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else if (mCopyMoveFlag == 2) {
                        try {
                            for (int i = 0; i < mSelectFiles.size(); i++) {
                                try {
                                    File sorceFile = new File(mSelectFiles.get(i));
                                    if (sorceFile.exists() && destinationFile.exists()) {
                                        try {
                                            File targetFile = new File(destinationFile + "/" + sorceFile.getName());
                                            mcount = 0;
                                            mitemCount = 0;
                                            File newtargetFile = checkFileIsExtist(targetFile);
                                            useFileChannel(sorceFile, newtargetFile);
                                            mediaScanUpdate.add(newtargetFile.getAbsolutePath());
                                        } catch (Exception ignored) {
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            mDirectoryViewData.add(new Directorywise(newFoldername, mediaScanUpdate));

                        } catch (Exception e) {
                            e.printStackTrace();

                        }

                    }

                    if (mediaScanUpdate.size() != 0) {
                        try {
                            String[] changedFiles = mediaScanUpdate.toArray(new String[mediaScanUpdate.size()]);
                            MediaScannerConnection.scanFile(getApplicationContext(), changedFiles, null,
                                    new MediaScannerConnection.OnScanCompletedListener() {
                                        @Override
                                        public void onScanCompleted(String path, Uri uri) {

                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    if (mediaScanDelete.size() != 0) {
                        try {
                            String[] changedFiles = mediaScanDelete.toArray(new String[mediaScanDelete.size()]);
                            MediaScannerConnection.scanFile(getApplicationContext(), changedFiles, null,
                                    new MediaScannerConnection.OnScanCompletedListener() {
                                        @Override
                                        public void onScanCompleted(String path, Uri uri) {
                                            try {
                                                getContentResolver().delete(uri, null, null);
                                            } catch (Exception ignored) {
                                            }
                                            //   Log.e("MediaScan", "==>>>Mediascan Complated");
                                            //  new GetDataAsyncTask().execute();
                                            //     getLoaderManager().initLoader(400, null, PhotosActivity.this).reset();
                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);/*
            mtv_paste.setText("");
            mtv_paste.setVisibility(View.GONE);

            if (scopymoveFLAG == 1) {
                Toast.makeText(getApplicationContext(), "Move SuccessFully", Toast.LENGTH_SHORT).show();
            } else if (scopymoveFLAG == 2) {
                Toast.makeText(getApplicationContext(), "Paste SuccessFully", Toast.LENGTH_SHORT).show();
            }
            scopymoveFLAG = 0;
            custorAdepter = new CustorAdepter(GalleryGridActivity.this, R.layout.list_item_photo);
            gridView.setAdapter(custorAdepter);
            custorAdepter.setNotifyOnChange(true);
            madepterSelection(false);*/

            int positon = mViewpager.getCurrentItem();
            ArrayList<PhotosAdepter> photosAdepters = new ArrayList<>();
            for (int j = 0; j < mDirectoryViewData.size(); j++) {
                photosAdepters.add(new PhotosAdepter(j, PhotosActivity.this, false));
            }
            try {
                viewPagerAdapter = new ViewPagerAdapter(photosAdepters);
                mViewpager.setAdapter(viewPagerAdapter);
                try {
                    mTabLayout.setupWithViewPager(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    mTabLayout.setupWithViewPager(mViewpager);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mViewpager.setCurrentItem(positon);
                for (int i = 0; i < mDirectoryViewData.size(); i++) {
                    if (mDirectoryViewData.get(i).getDataList().size() == 0) {
                        mTabLayout.removeTabAt(i);
                    }
                }
                try {
                    mCopyMoveFlag = 0;
                    progressDialog.dismiss();
                    mSelectFiles.clear();
                    mtitlebarSelection(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        private void useFileChannel(File file, File oFile) throws Exception {
            try {
                long time1 = System.currentTimeMillis();
                FileInputStream is = new FileInputStream(file);
                FileOutputStream fos = new FileOutputStream(oFile);
                FileChannel f = is.getChannel();
                FileChannel f2 = fos.getChannel();

                ByteBuffer buf = ByteBuffer.allocateDirect(64 * 1024);
                long len = 0;
                while ((len = f.read(buf)) != -1) {
                    try {
                        buf.flip();
                        f2.write(buf);
                        buf.clear();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                f2.close();
                f.close();

                long time2 = System.currentTimeMillis();
                mediaScanUpdate.add(file.getAbsolutePath());
                mediaScanUpdate.add(oFile.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        private File checkFileIsExtist(File file) {
            mcount++;
            if (file.exists()) {
                try {
                    String s = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                    String s1 = file.getName();
                    int fileExtPos = file.getName().lastIndexOf(".");
                    if (fileExtPos >= 0) {
                        try {
                            s1 = file.getName().substring(0, fileExtPos);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    String changename = "";
                    for (int i = 0; i < mcount; i++) {
                        try {
                            changename = "(" + mitemCount + ")";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    mitemCount++;
                    File file1 = new File(file.getParent() + "/" + s1 + changename + "." + s);
                    if (file1.exists()) {
                        try {
                            String s2 = file1.getAbsolutePath();
                            String s3 = s2.replace(changename, "");
                            file1 = new File(s3);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    return checkFileIsExtist(file1);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
            return file;
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class DeleteAsyncTask extends AsyncTask<String, String, String> {

        ArrayList<String> mediaScanDelete = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            try {
                File destinationFile = new File(mDirectoryViewData.get(mViewpager.getCurrentItem()).getDataList().get(0)).getParentFile();
                if (destinationFile.getAbsolutePath().contains("/storage/emulated/0")) {
                    try {
                        for (int i = 0; i < mSelectFiles.size(); i++) {
                            try {
                                File file = new File(mSelectFiles.get(i));
                                if (file.exists() && file.delete()) {
                                    try {
                                        mediaScanDelete.add(file.getAbsolutePath());
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
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String[] changedFiles = mediaScanDelete.toArray(new String[mediaScanDelete.size()]);
                MediaScannerConnection.scanFile(getApplicationContext(), changedFiles, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                try {
                                    getContentResolver().delete(uri, null, null);
                                } catch (Exception ignored) {
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                mSelectFiles.clear();
                mtitlebarSelection(true);
                viewPagerAdapter.deselection(mViewpager.getCurrentItem());
                mViewpager.setPagingEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    @SuppressLint("StaticFieldLeak")
    public class DeleteFolderAsyncTask extends AsyncTask<String, String, String> {
        ArrayList<String> mediaScanDelete = new ArrayList<>();
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressDialog = ProgressDialog.show(PhotosActivity.this, "Delete Folder", "Remove Photos from folder ....", true, false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<String> strings1 = new ArrayList<>();
            strings1 = mDirectoryViewData.get(mViewpager.getCurrentItem()).getDataList();


            String s = null;
            try {
                if (strings1.size() != 0) {
                    try {
                        File file = new File(strings1.get(0));
                        s = file.getParentFile().getAbsolutePath();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                for (int i = 0; i < strings1.size(); i++) {
                    try {
                        File file = new File(strings1.get(i));
                        if (file.exists() && file.delete()) {
                            try {
                                mediaScanDelete.add(file.getAbsolutePath());
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

            try {
                if (s != null) {
                    try {
                        mDirectoryViewData.remove(new Directorywise(s, mediaScanDelete));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                String[] changedFiles = mediaScanDelete.toArray(new String[mediaScanDelete.size()]);
                MediaScannerConnection.scanFile(getApplicationContext(), changedFiles, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                try {
                                    getContentResolver().delete(uri, null, null);
                                } catch (Exception ignored) {
                                }
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                viewPagerAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class RenameFolderAsyncTask extends AsyncTask<String, String, String> {
        ArrayList<String> newFolderImage = new ArrayList<>();
        ProgressDialog progressDialog;
        List<String> s_UpdateFilesPath = new ArrayList<>();
        String newFolderName;

        public RenameFolderAsyncTask(String newFolderName) {
            this.newFolderName = newFolderName;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                progressDialog = ProgressDialog.show(PhotosActivity.this, "Rename Folder", "Change Photos from folder ....", true, false);
                progressDialog.setCancelable(false);
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {

            ArrayList<String> strings1 = new ArrayList<>();
            strings1 = mDirectoryViewData.get(mViewpager.getCurrentItem()).getDataList();


            File file = new File(strings1.get(0));
            File renamefile = file.getParentFile();

            s_UpdateFilesPath.add(renamefile.getAbsolutePath());
            File newrenameFilePath = new File(renamefile.getParent() + "/" + newFolderName);
            if (renamefile.renameTo(new File(renamefile.getParent() + "/" + newFolderName))) {
                try {
                    newFolderImage.add(newrenameFilePath.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    s_UpdateFilesPath.add(newrenameFilePath.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < strings1.size(); i++) {
                try {
                    File file1 = new File(strings1.get(i));
                    s_UpdateFilesPath.add(newrenameFilePath.getAbsolutePath() + "/" + file1.getName());
                    s_UpdateFilesPath.add(strings1.get(i));
                    file1.delete();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            try {
                mDirectoryViewData.add(new Directorywise(newrenameFilePath.getAbsolutePath(), newFolderImage));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (renamefile.getAbsolutePath() != null) {
                    try {
                        mDirectoryViewData.remove(new Directorywise(renamefile.getAbsolutePath(), strings1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                String[] changedFiles1 = strings1.toArray(new String[strings1.size()]);
                MediaScannerConnection.scanFile(getApplicationContext(), changedFiles1, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                                getContentResolver().delete(uri, null, null);
                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                String[] changedFiles = s_UpdateFilesPath.toArray(new String[s_UpdateFilesPath.size()]);
                MediaScannerConnection.scanFile(getApplicationContext(), changedFiles, null,
                        new MediaScannerConnection.OnScanCompletedListener() {
                            @Override
                            public void onScanCompleted(String path, Uri uri) {
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                viewPagerAdapter.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @SuppressLint("StaticFieldLeak")
    public class ShareAsyncTask extends AsyncTask<String, String, String> {

        Intent shareIntent = new Intent();
        ArrayList<Uri> files = new ArrayList<>();

        @Override
        protected String doInBackground(String... strings) {
            try {
                shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Here are some files.");
                shareIntent.setType("image/*");

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    try {
                        for (int i = 0; i < mSelectFiles.size(); i++) {
                            try {
                                files.add(FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", new File(mSelectFiles.get(i))));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        for (int i = 0; i < mSelectFiles.size(); i++) {
                            try {
                                files.add(Uri.fromFile(new File(mSelectFiles.get(i))));
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


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
                startActivity(shareIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                mSelectFiles.clear();
                mtitlebarSelection(true);
                viewPagerAdapter.deselection(mViewpager.getCurrentItem());
                mViewpager.setPagingEnabled(true);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}
