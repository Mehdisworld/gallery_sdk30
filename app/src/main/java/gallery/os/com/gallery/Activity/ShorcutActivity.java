package gallery.os.com.gallery.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.util.ArrayList;

import gallery.os.com.gallery.BannerAndFullAD;
import gallery.os.com.gallery.Database.MomentDB;
import gallery.os.com.gallery.Dialog.CreateMoment;
import gallery.os.com.gallery.Dialog.Delete;
import gallery.os.com.gallery.Dialog.Information;
import gallery.os.com.gallery.Dialog.Multi_Information;
import gallery.os.com.gallery.Extera.SizeUnit;
import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;
import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimaryDark;


public class ShorcutActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

    RelativeLayout mRl_Bottom_Album, mRl_Bottom_Share, mRl_Bottom_Info, mRl_Bottom_Delete;
    RecyclerView mrecycleview;
    ArrayList<String> mImageList = new ArrayList<>();
    RequestOptions options;
    public RequestBuilder<Drawable> thumbRequest;
    SharedPreferences sharedPreferences;
    ImageView mIv_ChangeGrid, mIv_Close;
    RelativeLayout mRl_toolbar;
    TextView mtv_title;
    LinearLayout mBottomMenu;

    int getFoldertype = 0;
    SizeUnit sizeUnitkb = SizeUnit.KB;
    SizeUnit sizeUnitmb = SizeUnit.MB;
    SizeUnit sizeUnitgb = SizeUnit.GB;
    String mDirectoryName;
    ShorcutAdepter mshorcutAdepter;
    ArrayList<String> mSelectFiles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_recent);
            try {
                BannerAndFullAD.loadFullADForOtherWhere(getApplicationContext());
                mrecycleview = findViewById(R.id.rv_recent);
                mIv_ChangeGrid = findViewById(R.id.iv_change_grid);
                mRl_toolbar = findViewById(R.id.lv_toolbar);
                mtv_title = findViewById(R.id.tv_title);
                mBottomMenu = findViewById(R.id.lv_bottommenu);
                mIv_Close = findViewById(R.id.iv_close);

                mRl_Bottom_Album = findViewById(R.id.rl_bottom_album);
                mRl_Bottom_Share = findViewById(R.id.rl_bottom_share);
                mRl_Bottom_Info = findViewById(R.id.rl_bottom_info);
                mRl_Bottom_Delete = findViewById(R.id.rl_bottom_delete);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
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
            try {
                mRl_toolbar.setBackgroundColor(s_theme_colorPrimary);

                options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).override(Target.SIZE_ORIGINAL).dontTransform();

                thumbRequest = Glide.with(ShorcutActivity.this).load(R.drawable.loading).apply(options);
                sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


                if (sharedPreferences.getBoolean("getGridview", false)) {
                    try {
                        mrecycleview.setLayoutManager(new GridLayoutManager(ShorcutActivity.this, 4));
                        Glide.with(ShorcutActivity.this).load(R.drawable.ic_list_view).into(mIv_ChangeGrid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        mrecycleview.setLayoutManager(new LinearLayoutManager(ShorcutActivity.this, LinearLayoutManager.VERTICAL, false));
                        Glide.with(ShorcutActivity.this).load(R.drawable.ic_grid_view).into(mIv_ChangeGrid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                findViewById(R.id.ic_back).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            finish();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                getFoldertype = getIntent().getIntExtra("getFolderFlag", 0);
                if (getFoldertype == 1) {
                    try {
                        mDirectoryName = "Camera Photos";
                        mtv_title.setText("Camera Photos");
                        getLoaderManager().initLoader(400, null, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (getFoldertype == 2) {
                    try {
                        mDirectoryName = "Gallery";
                        mtv_title.setText("Gallery");
                        mRl_Bottom_Share.setVisibility(View.GONE);
                        mRl_Bottom_Info.setVisibility(View.GONE);
                        mRl_Bottom_Delete.setVisibility(View.GONE);
                        getLoaderManager().initLoader(500, null, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (getFoldertype == 3) {
                    try {
                        mDirectoryName = "Screenshots";
                        mtv_title.setText("Screenshots");
                        getLoaderManager().initLoader(600, null, this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                mIv_ChangeGrid.setOnClickListener(this);
                mIv_Close.setOnClickListener(this);

                mRl_Bottom_Album.setOnClickListener(this);
                mRl_Bottom_Share.setOnClickListener(this);
                mRl_Bottom_Info.setOnClickListener(this);
                mRl_Bottom_Delete.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        mImageList = new ArrayList<>();
        return new CursorLoader(ShorcutActivity.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mImageList = new ArrayList<>();
        if (loader.getId() == 400) {
            try {
                cursor.moveToFirst();
                do {
                    try {
                        if (cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)).contains("/storage/emulated/0/DCIM/Camera/")) {
                            try {
                                mImageList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (loader.getId() == 500) {
            try {
                cursor.moveToFirst();
                do {
                    try {
                        if (cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)).contains("/storage/emulated/0/")) {
                            try {
                                mImageList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (loader.getId() == 600) {
            try {
                File file = new File("/storage/emulated/0/Pictures/Screenshots/");
                File file1 = new File("/storage/emulated/0/DCIM/Screenshots/");
                if (file.exists()) {
                    cursor.moveToFirst();
                    do {
                        try {
                            if (cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)).contains("/storage/emulated/0/Pictures/Screenshots/")) {
                                mImageList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                } else if (file1.exists()) {
                    do {
                        try {
                            if (cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)).contains("/storage/emulated/0/DCIM/Screenshots/")) {
                                mImageList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {

            mshorcutAdepter = new ShorcutAdepter();
            mrecycleview.setAdapter(mshorcutAdepter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view) {
        try {
            switch (view.getId()) {
                case R.id.iv_change_grid:
                    try {
                        boolean switchOnOFF = sharedPreferences.getBoolean("getGridview", false);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("getGridview", !switchOnOFF);
                        editor.apply();
                        if (!switchOnOFF) {
                            try {
                                mrecycleview.setLayoutManager(new GridLayoutManager(ShorcutActivity.this, 4));
                                Glide.with(ShorcutActivity.this).load(R.drawable.ic_list_view).into(mIv_ChangeGrid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                mrecycleview.setLayoutManager(new LinearLayoutManager(ShorcutActivity.this, LinearLayoutManager.VERTICAL, false));
                                Glide.with(ShorcutActivity.this).load(R.drawable.ic_grid_view).into(mIv_ChangeGrid);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        mshorcutAdepter = new ShorcutAdepter();
                        mrecycleview.setAdapter(mshorcutAdepter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.iv_close:
                    try {
                        mshorcutAdepter.clearAdepter();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;

                case R.id.rl_bottom_album:
                    try {
                        mSelectFiles = mshorcutAdepter.getSelectionList();
                        if (mSelectFiles.size() >= 4) {
                            try {
                                final MomentDB momentDB = new MomentDB(ShorcutActivity.this);
                                momentDB.open();
                                CreateMoment createMoment = new CreateMoment(new CreateMoment.OnClickListener() {
                                    @Override
                                    public void Create_Btn(View view, Dialog dialog, String s) {
                                        try {
                                            momentDB.createMoment(s, mSelectFiles, mSelectFiles.get(0));
                                            momentDB.close();
                                            mSelectFiles.clear();
                                            mshorcutAdepter.clearAdepter();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void Cancel_Btn(View view, Dialog dialog) {
                                        dialog.dismiss();
                                    }
                                });
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
                    // viewPagerAdapter.getSelectionFile(mViewpager.getCurrentItem());
                    try {
                        BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
                        mSelectFiles = mshorcutAdepter.getSelectionList();
                        new ShareAsyncTask().execute();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.rl_bottom_info:
                    try {
                        ArrayList<String> strings = mshorcutAdepter.getSelectionList();
                        if (strings.size() == 1) {
                            try {
                                Information information = new Information(strings.get(0));
                                information.show(getFragmentManager(), "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            try {
                                Multi_Information multi_information = new Multi_Information(strings);
                                Bundle bundle1 = new Bundle();
                                bundle1.putInt("id", 0);
                                multi_information.setArguments(bundle1);
                                multi_information.show(getFragmentManager(), "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case R.id.rl_bottom_delete:
                    BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
                    try {
                        Delete delete = new Delete(new Delete.OnDeleteListener() {
                            @Override
                            public void Delete_Btn(View view, Dialog dialog) {
                                try {
                                    mSelectFiles = mshorcutAdepter.getSelectionList();
                                    dialog.dismiss();
                                    new DeleteAsyncTask().execute();
                                    mshorcutAdepter.clearAdepter();
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
                        delete.setCancelable(false);
                        delete.show(getFragmentManager(), "");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    class ShorcutAdepter extends RecyclerView.Adapter<ShorcutAdepter.MyViewHolder> {

        Boolean selectionFlag = false;
        SparseBooleanArray booleanArray = new SparseBooleanArray();


        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;
            if (sharedPreferences.getBoolean("getGridview", false)) {
                itemView = LayoutInflater.from(ShorcutActivity.this).inflate(R.layout.viewpager_item, parent, false);
            } else {
                itemView = LayoutInflater.from(ShorcutActivity.this).inflate(R.layout.photo_list_recycler_item, parent, false);
            }
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            try {
                try {
                    Glide.with(ShorcutActivity.this)
                            .load(mImageList.get(position))
                            .thumbnail(thumbRequest)
                            .into(holder.photo);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (!sharedPreferences.getBoolean("getGridview", false)) {
                    try {
                        File file = new File(mImageList.get(position));
                        holder.mtv_name.setText(file.getName());

                        if ((int) (file.length() / sizeUnitkb.inBytes()) > 1024) {
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
                        } else {
                            try {
                                holder.mtv_size.setText((int) (file.length() / sizeUnitkb.inBytes()) + "kb");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getItemCount() {
            return mImageList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder {
            RoundedImageView photo;
            ImageView imageView;

            RelativeLayout lv_select;
            TextView mtv_name, mtv_size;

            MyViewHolder(View itemView) {
                super(itemView);
                try {
                    photo = itemView.findViewById(R.id.recent_image);
                    imageView = itemView.findViewById(R.id.ic_select);
                    imageView.setColorFilter(s_theme_colorPrimaryDark);
                    lv_select = itemView.findViewById(R.id.lv_select);

                    if (!sharedPreferences.getBoolean("getGridview", false)) {
                        mtv_name = itemView.findViewById(R.id.name);
                        mtv_size = itemView.findViewById(R.id.size);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (selectionFlag) {
                                if (mImageList.get(getAdapterPosition()).startsWith("/storage/emulated/0/")) {
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
                                            lv_select.setVisibility(View.GONE);
                                            mtv_title.setText(mDirectoryName);
                                            mIv_Close.setVisibility(View.GONE);
                                            mBottomMenu.setVisibility(View.GONE);
                                            mRl_toolbar.setVisibility(View.VISIBLE);
                                            mIv_ChangeGrid.setVisibility(View.VISIBLE);
                                            selectionFlag = false;
                                            notifyDataSetChanged();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        try {
                                            lv_select.setVisibility(View.VISIBLE);
                                            mIv_ChangeGrid.setVisibility(View.GONE);
                                            mIv_Close.setVisibility(View.VISIBLE);
                                            mtv_title.setText(count + " Selected");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                } else {
                                    try {
                                        Toast.makeText(getApplicationContext(), "Can`t Select SD-CARD Images", Toast.LENGTH_SHORT).show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                try {
                                    Intent intent = new Intent(ShorcutActivity.this, ImageViewrActivity.class);
                                    intent.putStringArrayListExtra("ImageFiles", mImageList);
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
                    public boolean onLongClick(View v) {
                        try {
                            if (mImageList.get(getAdapterPosition()).startsWith("/storage/emulated/0/")) {
                                if (!selectionFlag) {
                                    try {
                                        mIv_ChangeGrid.setVisibility(View.GONE);
                                        mBottomMenu.setVisibility(View.VISIBLE);
                                        selectionFlag = true;
                                        lv_select.setVisibility(View.VISIBLE);
                                        v.performClick();
                                        notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                } else {
                                    try {
                                        v.performClick();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            } else {
                                try {
                                    Toast.makeText(getApplicationContext(), "Can`t Select SD-CARD Images", Toast.LENGTH_SHORT).show();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
            try {
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
            } catch (Exception e) {
                e.printStackTrace();
            }

            return in;
        }

        void clearAdepter() {
            try {
                this.selectionFlag = false;
                mBottomMenu.setVisibility(View.GONE);
                mIv_Close.setVisibility(View.GONE);
                mIv_ChangeGrid.setVisibility(View.VISIBLE);
                booleanArray = new SparseBooleanArray();
                mtv_title.setText(mDirectoryName);
                this.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        ArrayList<String> getSelectionList() {
            ArrayList<String> arrayList = new ArrayList<>();
            for (int i = 0; i < this.getItemCount(); i++) {
                try {
                    if (this.booleanArray.get(i)) {
                        if (mImageList.get(i).contains("/storage/emulated/0/")) {
                            try {
                                arrayList.add(mImageList.get(i));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            return arrayList;
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class DeleteAsyncTask extends AsyncTask<String, String, String> {

        ArrayList<String> mediaScanDelete = new ArrayList<>();


        @Override
        protected String doInBackground(String... strings) {
            try {
                File destinationFile = new File(mSelectFiles.get(0)).getParentFile();
                if (destinationFile.getAbsolutePath().contains("/storage/emulated/0")) {
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
                //mshorcutAdepter.clearAdepter();
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
                try {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        for (int i = 0; i < mSelectFiles.size(); i++) {
                            try {
                                files.add(FileProvider.getUriForFile(getApplicationContext(), getPackageName() + ".provider", new File(mSelectFiles.get(i))));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        for (int i = 0; i < mSelectFiles.size(); i++) {
                            try {
                                files.add(Uri.fromFile(new File(mSelectFiles.get(i))));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
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
                mshorcutAdepter.clearAdepter();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }
}

