package gallery.os.com.gallery.Activity;

import android.annotation.SuppressLint;
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
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
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
import java.util.List;

import gallery.os.com.gallery.BannerAndFullAD;
import gallery.os.com.gallery.Database.MomentDB;
import gallery.os.com.gallery.Dialog.CreateMoment;
import gallery.os.com.gallery.Dialog.Delete;
import gallery.os.com.gallery.Dialog.Information;
import gallery.os.com.gallery.Dialog.Multi_Information;
import gallery.os.com.gallery.Extera.SizeUnit;
import gallery.os.com.gallery.MainActivity;
import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;
import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimaryDark;

public class RecentActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener {

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
    ArrayList<String> mSelectFiles = new ArrayList<>();
    RecentAdapter recentAdapter;

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

                thumbRequest = Glide.with(RecentActivity.this).load(R.drawable.loading).apply(options);
                sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);


                if (sharedPreferences.getBoolean("getGridview", false)) {
                    try {
                        mrecycleview.setLayoutManager(new GridLayoutManager(RecentActivity.this, 4));
                        Glide.with(RecentActivity.this).load(R.drawable.ic_list_view).into(mIv_ChangeGrid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        mrecycleview.setLayoutManager(new LinearLayoutManager(RecentActivity.this, LinearLayoutManager.VERTICAL, false));
                        Glide.with(RecentActivity.this).load(R.drawable.ic_grid_view).into(mIv_ChangeGrid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



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


            boolean switchOnOFF = sharedPreferences.getBoolean("getGridview", false);
            if (switchOnOFF) {
                try {
                    Glide.with(RecentActivity.this).load(R.drawable.ic_grid_view).into(mIv_ChangeGrid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    Glide.with(RecentActivity.this).load(R.drawable.ic_list_view).into(mIv_ChangeGrid);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            try {
                getLoaderManager().initLoader(300, null, this);
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
        return new CursorLoader(RecentActivity.this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, MediaStore.Images.Media.DATE_TAKEN + " DESC");

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (loader.getId() == 300) {
            mImageList = new ArrayList<>();
            try {
                cursor.moveToFirst();
                do {
                    try {
                        if (mImageList.size() < 50) {
                            mImageList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                        } else {
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } while (cursor.moveToNext());

            } catch (Exception e) {
                e.printStackTrace();
            }
            recentAdapter = new RecentAdapter();
            mrecycleview.setAdapter(recentAdapter);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_change_grid:
                try {
                    boolean switchOnOFF = sharedPreferences.getBoolean("getGridview", false);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("getGridview", !switchOnOFF);
                    editor.apply();
                    if (!switchOnOFF) {
                        try {
                            mrecycleview.setLayoutManager(new GridLayoutManager(RecentActivity.this, 4));
                            Glide.with(RecentActivity.this).load(R.drawable.ic_list_view).into(mIv_ChangeGrid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            mrecycleview.setLayoutManager(new LinearLayoutManager(RecentActivity.this, LinearLayoutManager.VERTICAL, false));
                            Glide.with(RecentActivity.this).load(R.drawable.ic_grid_view).into(mIv_ChangeGrid);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        recentAdapter = new RecentAdapter();
                        mrecycleview.setAdapter(recentAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_close:
                try {
                    recentAdapter.clearAdepter();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rl_bottom_album:
                try {
                    mSelectFiles = recentAdapter.getSelectionList();
                    if (mSelectFiles.size() >= 4) {
                        try {
                            final MomentDB momentDB = new MomentDB(RecentActivity.this);
                            momentDB.open();
                            CreateMoment createMoment = new CreateMoment(new CreateMoment.OnClickListener() {
                                @Override
                                public void Create_Btn(View view, Dialog dialog, String s) {
                                    try {
                                        momentDB.createMoment(s, mSelectFiles, mSelectFiles.get(0));
                                        momentDB.close();
                                        mSelectFiles.clear();
                                        recentAdapter.clearAdepter();
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
                    mSelectFiles = recentAdapter.getSelectionList();
                    new ShareAsyncTask().execute();
                    BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rl_bottom_info:
                try {
                    ArrayList<String> strings = recentAdapter.getSelectionList();
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

                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.rl_bottom_delete:
                //viewPagerAdapter.getSelectionFile(mViewpager.getCurrentItem());
                try {
                    Delete delete = new Delete(new Delete.OnDeleteListener() {
                        @Override
                        public void Delete_Btn(View view, Dialog dialog) {
                            try {
                                mSelectFiles = recentAdapter.getSelectionList();
                                dialog.dismiss();
                                new DeleteAsyncTask().execute();
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
                    BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
        }
    }


    public class RecentAdapter extends RecyclerView.Adapter<RecentAdapter.MyViewHolder> {

        Boolean selectionFlag = false;
        SparseBooleanArray booleanArray = new SparseBooleanArray();

        @NonNull
        @Override
        public RecentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView;
            if (sharedPreferences.getBoolean("getGridview", false)) {
                itemView = LayoutInflater.from(RecentActivity.this).inflate(R.layout.viewpager_item, parent, false);
            } else {
                itemView = LayoutInflater.from(RecentActivity.this).inflate(R.layout.photo_list_recycler_item, parent, false);
            }
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull RecentAdapter.MyViewHolder holder, int position) {
            try {
                Glide.with(RecentActivity.this)
                        .load(mImageList.get(position))
                        .thumbnail(thumbRequest)
                        .into(holder.photo);

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (!sharedPreferences.getBoolean("getGridview", false)) {
                    File file = new File(mImageList.get(position));
                    holder.mtv_name.setText(file.getName());
                    try {
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

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (selectionFlag) {
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
                                        imageView.setVisibility(View.GONE);
                                    } else {
                                        imageView.setVisibility(View.VISIBLE);
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
                                            lv_select.setVisibility(View.GONE);
                                            notifyDataSetChanged();
                                            mtv_title.setText(mDirectoryName);
                                            mIv_Close.setVisibility(View.GONE);
                                            mBottomMenu.setVisibility(View.GONE);
                                            mRl_toolbar.setVisibility(View.VISIBLE);
                                            mIv_ChangeGrid.setVisibility(View.VISIBLE);
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
                                    Intent intent = new Intent(RecentActivity.this, ImageViewrActivity.class);
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
                                    //  mlvBottom.setVisibility(View.GONE);
                                    mIv_ChangeGrid.setVisibility(View.GONE);
                                    mBottomMenu.setVisibility(View.VISIBLE);
                                    selectionFlag = true;
                                    mRl_toolbar.setVisibility(View.VISIBLE);
                                    lv_select.setVisibility(View.VISIBLE);
                                    v.performClick();
                                    notifyDataSetChanged();
                                } else {
                                    v.performClick();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Can`t Select SD-CARD Images", Toast.LENGTH_SHORT).show();
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
                        try {
                            if (mImageList.get(i).contains("/storage/emulated/0/")) {
                                try {
                                    arrayList.add(mImageList.get(i));

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
                recentAdapter.clearAdepter();
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
                recentAdapter.clearAdepter();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}
