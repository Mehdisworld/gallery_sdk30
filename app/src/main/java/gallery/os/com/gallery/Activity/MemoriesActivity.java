package gallery.os.com.gallery.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gallery.os.com.gallery.ActionBar.NBA_NoBoringActionBarActivity;
import gallery.os.com.gallery.BannerAndFullAD;
import gallery.os.com.gallery.CC;
import gallery.os.com.gallery.Database.MomentDB;
import gallery.os.com.gallery.Dialog.Delete;
import gallery.os.com.gallery.Dialog.Rename;
import gallery.os.com.gallery.FBNative;
import gallery.os.com.gallery.MainActivity;
import gallery.os.com.gallery.Model.MomentModel;
import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;
import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimaryDark;

public class MemoriesActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout mRl_Bottom_Home, mRl_Bottom_Photos, mRl_Bottom_Videos, mRl_Bottom_Albums;
    Intent home_intent, photos_intent, videos_intent;
    RelativeLayout mRl_titlebar;
    TextView mTv_Title;

    ImageView mIv_bottom_Memories, miv_delete, miv_rename, mIv_empty;
    TextView mTv_bottom_Memories;

    ListView listView;
    ArrayList<MomentModel> mtotleMoMent = new ArrayList<>();
    MomentDB momentDB;
    int colorINT;

    LinearLayout bannerAds,sNativeAdvanceLayout;

    public static boolean sMomentChangeNotify = false;
    public Boolean sSelectionFlag = false;
    CustomAdapter customAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);try {
            setContentView(R.layout.activity_memories);
            checkFullAdStatus();
            bannerAds = findViewById(R.id.lv_ads);
            sNativeAdvanceLayout = findViewById(R.id.fbnativeadsetting);
            BannerAndFullAD.loadBannerAd(bannerAds,getApplicationContext());

           /*
            if(BannerAndFullAD.isNetworkAvailable(getApplicationContext())){
                if(CC.admobBannerForMemory){
                    CC.admobBannerForMemory = false;
                    BannerAndFullAD.loadBannerAd(bannerAds,getApplicationContext());
                }else{
                    FBNative.showNativeAd(getApplicationContext(),sNativeAdvanceLayout);
                    //showNativeAd();
                }
            }
            */
            mRl_Bottom_Home = findViewById(R.id.rl_bttom_home);
            mRl_Bottom_Photos = findViewById(R.id.rl_bttom_photos);
            mRl_Bottom_Videos = findViewById(R.id.rl_bttom_videos);
            mRl_Bottom_Albums = findViewById(R.id.rl_bttom_memories);

            listView = findViewById(R.id.lv_memories);


            mIv_bottom_Memories = findViewById(R.id.iv_bottom_memories);
            mTv_bottom_Memories = findViewById(R.id.tv_bottom_memories);

            mRl_titlebar = findViewById(R.id.lv_titlebar);
            mTv_Title = findViewById(R.id.tv_title);

            miv_delete = findViewById(R.id.iv_delete);
            miv_rename = findViewById(R.id.iv_rename);


            mIv_empty = findViewById(R.id.iv_empty);
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        mRl_titlebar.setBackgroundColor(s_theme_colorPrimary);
                        mIv_bottom_Memories.setImageTintList(ColorStateList.valueOf(s_theme_colorPrimary));
                        mTv_bottom_Memories.setTextColor(s_theme_colorPrimary);
                        getWindow().setStatusBarColor(s_theme_colorPrimaryDark);
                    }catch (Exception e){e.printStackTrace();}

                }
            }catch (Exception e){e.printStackTrace();}


            colorINT = (s_theme_colorPrimary & 0x00FFFFFF) | 0x50000000;


            home_intent = new Intent(MemoriesActivity.this, MainActivity.class);
            home_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            home_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            photos_intent = new Intent(MemoriesActivity.this, PhotosActivity.class);
            photos_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            photos_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            photos_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            videos_intent = new Intent(MemoriesActivity.this, VideosActivity.class);
            videos_intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            videos_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            videos_intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            try {
                momentDB = new MomentDB(MemoriesActivity.this);
                momentDB.open();
            }catch (Exception e){e.printStackTrace();}


            getAdepterData();
            try {
                if (mtotleMoMent.size() != 0) {try {
                    mIv_empty.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    customAdapter = new CustomAdapter(mtotleMoMent, MemoriesActivity.this);
                    listView.setAdapter(customAdapter);
                }catch (Exception e){e.printStackTrace();}

                } else {try {

                    listView.setVisibility(View.GONE);
                    mIv_empty.setVisibility(View.VISIBLE);
                }catch (Exception e){e.printStackTrace();}
                }
            }catch (Exception e){e.printStackTrace();}

            try {
                mRl_Bottom_Home.setOnClickListener(this);
                mRl_Bottom_Photos.setOnClickListener(this);
                mRl_Bottom_Videos.setOnClickListener(this);
                mRl_Bottom_Albums.setOnClickListener(this);
                miv_delete.setOnClickListener(this);
                miv_rename.setOnClickListener(this);
            }catch (Exception e){e.printStackTrace();}


        }catch (Exception e){e.printStackTrace();}

    }

    public void checkFullAdStatus(){
        if(CC.needToShowAdmobFull){
            BannerAndFullAD.showLoadedFullAD(getApplicationContext());
        }else if(CC.singleTime){
            BannerAndFullAD.showLoadedFullADForOtherwhere(getApplicationContext());
        }else{}
    }


    public void getAdepterData() {
        mtotleMoMent = new ArrayList<>();
        try {
            Cursor cursor = momentDB.getMomentlist();
            cursor.moveToFirst();
            do {
                try {
                    if (new File(cursor.getString(cursor.getColumnIndex("momentThumbImage"))).exists()) {
                        try {
                            mtotleMoMent.add(new MomentModel(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("momentname")), cursor.getString(cursor.getColumnIndex("momentThumbImage")), cursor.getLong(cursor.getColumnIndex("momentDate"))));

                        }catch (Exception e){e.printStackTrace();}        } else {
                        try {
                            mtotleMoMent.add(new MomentModel(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("momentname")), null, cursor.getLong(cursor.getColumnIndex("momentDate"))));

                        }catch (Exception e){e.printStackTrace();}
                       }
                } catch (Exception ignored) {
                }
            } while (cursor.moveToNext());
        } catch (Exception ignored) {
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_bttom_home:
                try {
                    startActivity(home_intent);
                }catch (Exception e){e.printStackTrace();}

                break;
            case R.id.rl_bttom_photos:
                try {
                    startActivity(photos_intent);
                }catch (Exception e){e.printStackTrace();}

                break;
            case R.id.rl_bttom_videos:
                try {
                    startActivity(videos_intent);
                }catch (Exception e){e.printStackTrace();}

                break;
            case R.id.iv_delete:
                try {
                    Delete delete = new Delete(new Delete.OnDeleteListener() {
                        @Override
                        public void Delete_Btn(View view, Dialog dialog) {
                            try {
                                ArrayList<MomentModel> momentModel = customAdapter.getSelectedImage();
                                for (int i = 0; i < momentModel.size(); i++) {
                                    try {
                                        int position = mtotleMoMent.indexOf(momentModel.get(i));
                                        momentDB.removeMoment(momentModel.get(i).getId());
                                        mtotleMoMent.remove(position);
                                    }catch (Exception e){e.printStackTrace();}

                                }
                                if (mtotleMoMent.size() != 0) {
                                    try {
                                        customAdapter.cleareSelected();
                                    }catch (Exception e){e.printStackTrace();}

                                } else {
                                    try {
                                        customAdapter.cleareSelected();
                                        listView.setVisibility(View.GONE);
                                        mIv_empty.setVisibility(View.VISIBLE);
                                    }catch (Exception e){e.printStackTrace();}

                                }
                            }catch (Exception e){e.printStackTrace();}

                            dialog.dismiss();
                        }

                        @Override
                        public void Cancel_Btn(View view, Dialog dialog) {
                            dialog.dismiss();
                        }
                    });
                    delete.setCancelable(false);
                    delete.show(getFragmentManager(), "");
                }catch (Exception e){e.printStackTrace();}

                break;
            case R.id.iv_rename:
                try {
                    final MomentModel momentModel = customAdapter.getSelectedImage().get(0);
                    Rename rename = new Rename(new Rename.OnClickListener() {
                        @Override
                        public void Rename_Btn(View view, Dialog dialog, String s) {
                            int position = mtotleMoMent.indexOf(momentModel);
                            try {
                                momentDB.updateMomentName(s, momentModel.getId());
                                MomentModel momentModel1 = new MomentModel(momentModel.getId(), s, momentModel.getPath(), momentModel.getDate());
                                mtotleMoMent.set(position, momentModel1);
                                customAdapter.cleareSelected();
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
                    rename.setCancelable(false);
                    Bundle bundle = new Bundle();
                    bundle.putString("filename", momentModel.getName());
                    rename.setArguments(bundle);
                    rename.show(getFragmentManager(), "");
                }catch (Exception e){e.printStackTrace();}

                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       try {
            moveTaskToBack(true);
        }catch (Exception e){e.printStackTrace();}

       /* System.gc();
        System.exit(0);*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (sMomentChangeNotify) {
                sMomentChangeNotify = false;
                mtotleMoMent = new ArrayList<>();
                try {
                    try {
                        Cursor cursor = momentDB.getMomentlist();
                        cursor.moveToFirst();
                        do {
                            try {
                                mtotleMoMent.add(new MomentModel(cursor.getString(cursor.getColumnIndex("id")), cursor.getString(cursor.getColumnIndex("momentname")), cursor.getString(cursor.getColumnIndex("momentThumbImage")), cursor.getLong(cursor.getColumnIndex("momentDate"))));
                            } catch (Exception ignored) {
                            }
                        } while (cursor.moveToNext());
                    } catch (Exception ignored) {
                    }
                    customAdapter = new CustomAdapter(mtotleMoMent, MemoriesActivity.this);
                    listView.setAdapter(customAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){e.printStackTrace();}


    }



    public class CustomAdapter extends ArrayAdapter<MomentModel> {

        private ArrayList<MomentModel> dataSet;
        Context mContext;
        SparseBooleanArray booleanArray = new SparseBooleanArray();

        private class ViewHolder {
            ImageView iv_MM_Icon;
            RoundedImageView iv_date_bg, iv_select;
            TextView tv_Name, tv_Sub, tv_Date, tv_month;
            CardView cardView, sub_card;
        }

        CustomAdapter(ArrayList<MomentModel> data, Context context) {
            super(context, R.layout.moment_adepter, data);
            this.dataSet = data;
            this.mContext = context;
        }

        @Override
        public int getCount() {
            return this.dataSet.size();
        }


        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            final MomentModel momentModel = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(MemoriesActivity.this);
                convertView = inflater.inflate(R.layout.moment_adepter, parent, false);
                try {
                    viewHolder.tv_Name = (TextView) convertView.findViewById(R.id.tv_name);
                    viewHolder.cardView = (CardView) convertView.findViewById(R.id.cardview);
                    viewHolder.sub_card = (CardView) convertView.findViewById(R.id.sub_card);
                    viewHolder.tv_Sub = (TextView) convertView.findViewById(R.id.tv_sub);
                    viewHolder.tv_Date = (TextView) convertView.findViewById(R.id.tv_date);
                    viewHolder.tv_month = (TextView) convertView.findViewById(R.id.tv_month);
                    viewHolder.iv_MM_Icon = (ImageView) convertView.findViewById(R.id.iv_icon);
                    viewHolder.iv_date_bg = (RoundedImageView) convertView.findViewById(R.id.iv_date_bg);
                    viewHolder.iv_select = (RoundedImageView) convertView.findViewById(R.id.iv_select);

                }catch (Exception e){e.printStackTrace();}

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_Name.setText(momentModel.getName());
            if (momentModel.getPath() == null) {
                Glide.with(MemoriesActivity.this).load(R.drawable.moments_thumb).into(viewHolder.iv_MM_Icon);
            } else {
                Glide.with(MemoriesActivity.this).load(momentModel.getPath()).into(viewHolder.iv_MM_Icon);
            }


            viewHolder.iv_date_bg.setColorFilter(s_theme_colorPrimary);
            viewHolder.iv_select.setBackgroundColor(colorINT);

            try {
                Cursor cursor = momentDB.getMomentlistdata(momentModel.getId());
                cursor.moveToFirst();
                viewHolder.tv_Sub.setText(cursor.getCount() + "  Photos Taken");

                for (int i = 0; i < cursor.getCount(); i++) {
                    try {
                        cursor.moveToPosition(i);
                        File file = new File(cursor.getString(cursor.getColumnIndex("filepath")));
                        if (!file.exists()) {
                            try {
                                momentDB.deleteMomentImage(momentModel.getId(), cursor.getString(cursor.getColumnIndex("filepath")));

                            }catch (Exception e){e.printStackTrace();}
                           }
                    }catch (Exception e){e.printStackTrace();}

                }
                try {
                    if (!(cursor.getCount() >= 4)) {
                        try {
                            momentDB.removeMoment(momentModel.getId());
                            mtotleMoMent.remove(momentModel);
                            try {
                                Toast.makeText(getApplicationContext(), "Delete " + momentModel.getName() + "Memories in yout storage", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (mtotleMoMent.size() != 0) {
                                try {
                                    mIv_empty.setVisibility(View.GONE);
                                    listView.setVisibility(View.VISIBLE);
                                    notifyDataSetChanged();
                                }catch (Exception e){e.printStackTrace();}

                            } else {
                                try {
                                    listView.setVisibility(View.GONE);
                                    mIv_empty.setVisibility(View.VISIBLE);
                                }catch (Exception e){e.printStackTrace();}

                            }
                        }catch (Exception e){e.printStackTrace();}

                    }
                }catch (Exception e){e.printStackTrace();}



            } catch (Exception e) {
                e.printStackTrace();
            }
            try { Date date = new Date(momentModel.getDate());
                String dateformate = new SimpleDateFormat("dd").format(date);
                String dateformate2 = new SimpleDateFormat("MMM").format(date);

                viewHolder.tv_Date.setText(dateformate);
                viewHolder.tv_month.setText(dateformate2);


            }catch (Exception e){e.printStackTrace();}


            try {
                viewHolder.iv_select.setVisibility(View.GONE);
                if (sSelectionFlag) {
                    if (this.booleanArray.get(position)) {
                        try {
                            viewHolder.iv_select.setVisibility(View.VISIBLE);

                        }catch (Exception e){e.printStackTrace();}
                        }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (sSelectionFlag) {
                        try {
                            boolean z = false;
                            boolean value = booleanArray.get(position);
                            SparseBooleanArray sparseBooleanArray = booleanArray;
                            if (!value) {
                                z = true;
                            }
                            sparseBooleanArray.put(position, z);

                            if (value) {
                                viewHolder.iv_select.setVisibility(View.GONE);
                            }
                            if (!value) {
                                viewHolder.iv_select.setVisibility(View.VISIBLE);
                            }

                            int i = getCheckedCount();
                            if (i == 0) {
                                try {
                                    miv_delete.setVisibility(View.GONE);
                                    miv_rename.setVisibility(View.GONE);
                                    sSelectionFlag = false;
                                    notifyDataSetChanged();
                                }catch (Exception e){e.printStackTrace();}

                            }
                            if (i == 1) {
                                try {
                                    miv_delete.setVisibility(View.VISIBLE);
                                    miv_rename.setVisibility(View.VISIBLE);
                                }catch (Exception e){e.printStackTrace();}

                            }
                            if (i > 1) {
                                try {
                                    miv_delete.setVisibility(View.VISIBLE);
                                    miv_rename.setVisibility(View.GONE);
                                }catch (Exception e){e.printStackTrace();}

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Intent intent = new Intent(MemoriesActivity.this, NBA_NoBoringActionBarActivity.class);
                            intent.putExtra("MomentId", momentModel.getId());
                            intent.putExtra("image", momentModel.getPath());
                            intent.putExtra("name", momentModel.getName());
                            startActivity(intent);
                        }catch (Exception e){e.printStackTrace();}

                    }
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    try {
                        if (!sSelectionFlag) {
                            try {
                                sSelectionFlag = true;
                                notifyDataSetChanged();
                            }catch (Exception e){e.printStackTrace();}

                        }
                        view.performClick();
                    }catch (Exception e){e.printStackTrace();}

                    return true;
                }
            });


            return convertView;
        }

        private Integer getCheckedCount() {
            Integer in = 0;
            try {
                for (int i = 0; i < this.getCount(); i++) {
                    try {
                        if (this.booleanArray.get(i)) {
                            try {
                                in = in + 1;
                            }catch (Exception e){e.printStackTrace();}

                        }
                    }catch (Exception e){e.printStackTrace();}

                }
            }catch (Exception e){e.printStackTrace();}

            return in;
        }

        ArrayList<MomentModel> getSelectedImage() {
            ArrayList<MomentModel> mTempArry = new ArrayList<>();
            try {
                for (int i = 0; i < this.getCount(); i++) {
                    try {
                        if (this.booleanArray.get(i)) {
                            try {
                                mTempArry.add(dataSet.get(i));
                            } catch (Exception ignored) {
                            }
                        }
                    }catch (Exception e){e.printStackTrace();}

                }
            } catch (Exception ignored) {
            }
            return mTempArry;
        }

        void cleareSelected() {
            try {
                this.booleanArray.clear();
                sSelectionFlag = false;
                this.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                miv_delete.setVisibility(View.GONE);
                miv_rename.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
