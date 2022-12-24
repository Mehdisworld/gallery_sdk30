package gallery.os.com.gallery.ActionBar;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import gallery.os.com.gallery.Activity.SlideShowActivity;
import gallery.os.com.gallery.Database.MomentDB;
import gallery.os.com.gallery.Extera.HeaderGridView;
import gallery.os.com.gallery.Extera.Squre_RoundedImageView;
import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.Activity.MemoriesActivity.sMomentChangeNotify;
import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;
import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimaryDark;

public class NBA_NoBoringActionBarActivity extends Activity {

    private static final String TAG = "NBA_NoBoringActionBarActivity";
    private int mActionBarTitleColor;
    private int mActionBarHeight;
    private int mHeaderHeight;
    private int mMinHeaderTranslation;
    //private ListView mListView;
    HeaderGridView gridView;
    private NBA_KenBurnsView mHeaderPicture;
    private ImageView mHeaderLogo;
    private View mHeader;
    private View mPlaceHolderView;
    private AccelerateDecelerateInterpolator mSmoothInterpolator;

    private RectF mRect1 = new RectF();
    private RectF mRect2 = new RectF();

    private NBA_AlphaForegroundColorSpan mAlphaForegroundColorSpan;
    private SpannableString mSpannableString;

    private TypedValue mTypedValue = new TypedValue();

    TextView tv_momentName, tv_count;

    public static ArrayList<String> imagesList = new ArrayList<>();


    String momentID, momentIconPath;
    public Boolean sSelectionFlag = false;

    RelativeLayout Rl_MainIcone, rl_Edit, rl_setcover, rl_delete, rl_rename;

    LinearLayout Ll_bottom;
    ImageView iv_close;
    EditText editText;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            mSmoothInterpolator = new AccelerateDecelerateInterpolator();
            mHeaderHeight = getResources().getDimensionPixelSize(R.dimen.header_height);
            mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();

            setContentView(R.layout.nba_activity_noboringactionbar);
            try {
                Rl_MainIcone = findViewById(R.id.lv_moment);
                Ll_bottom = findViewById(R.id.lv_bottommenu);
                gridView = findViewById(R.id.memori_grid);
                tv_momentName = findViewById(R.id.tv_moment_name);
                tv_count = findViewById(R.id.tv_count);
                rl_Edit = findViewById(R.id.lv_edit);
                rl_setcover = findViewById(R.id.rl_bottom_cover);
                rl_delete = findViewById(R.id.rl_bottom_delete);
                rl_rename = findViewById(R.id.rl_bottom_rename);
                iv_close = findViewById(R.id.iv_close);
                // iv_checkmark = findViewById(R.id.iv_done);
                editText = findViewById(R.id.editText);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    try {
                        getWindow().setStatusBarColor(s_theme_colorPrimaryDark);
                        GradientDrawable drawable = (GradientDrawable) rl_Edit.getBackground();
                        drawable.setStroke(3, s_theme_colorPrimary);
                        rl_Edit.setBackground(drawable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                momentID = getIntent().getStringExtra("MomentId");
                momentIconPath = getIntent().getStringExtra("image");

                tv_momentName.setText(getIntent().getStringExtra("name"));

            } catch (Exception e) {
                e.printStackTrace();
            }
            final MomentDB momentDB = new MomentDB(NBA_NoBoringActionBarActivity.this);
            momentDB.open();
            try {
                imagesList = new ArrayList<>();
                try {
                    Cursor cursor = momentDB.getMomentlistdata(momentID);
                    cursor.moveToFirst();
                    do {
                        try {
                            if (new File(cursor.getString(cursor.getColumnIndex("filepath"))).exists()) {
                                imagesList.add(cursor.getString(cursor.getColumnIndex("filepath")));
                            } else {
                                momentDB.deleteMomentImage(momentID, cursor.getString(cursor.getColumnIndex("filepath")));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (cursor.moveToNext());

                    try {
                        tv_count.setText(imagesList.size() + "  Photos");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                mHeaderPicture = (NBA_KenBurnsView) findViewById(R.id.header_picture);
                mHeaderPicture.setImagesFile(momentIconPath);
                mHeader = findViewById(R.id.header);
                mHeaderLogo = (ImageView) findViewById(R.id.header_logo);


                mActionBarTitleColor = getResources().getColor(R.color.actionbar_title_color);

                mSpannableString = new SpannableString(tv_momentName.getText());
                mAlphaForegroundColorSpan = new NBA_AlphaForegroundColorSpan(mActionBarTitleColor);

            } catch (Exception e) {
                e.printStackTrace();
            }


            setupActionBar();
            setupListView();

            mHeaderLogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Intent intent = new Intent(NBA_NoBoringActionBarActivity.this, SlideShowActivity.class);
                        intent.putExtra("MomentId", momentID);
                        intent.putExtra("Moment_Fragment", true);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            tv_momentName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        tv_momentName.setVisibility(View.GONE);
                        rl_rename.setVisibility(View.VISIBLE);
                        editText.setText(tv_momentName.getText());
                        rl_Edit.setVisibility(View.VISIBLE);
                        gridView.setPadding(0, 0, 0, 165);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
            iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {

                        rl_Edit.setVisibility(View.GONE);
                        rl_rename.setVisibility(View.GONE);
                        tv_momentName.setVisibility(View.VISIBLE);
                        gridView.setPadding(0, 0, 0, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            rl_rename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        MomentDB momentDB = new MomentDB(NBA_NoBoringActionBarActivity.this);
                        momentDB.open();
                        momentDB.updateMomentName(editText.getText().toString(), momentID);
                        momentDB.close();

                        tv_momentName.setText(editText.getText().toString());
                        mSpannableString = new SpannableString(tv_momentName.getText());
                        sMomentChangeNotify = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {

                        rl_Edit.setVisibility(View.GONE);
                        rl_rename.setVisibility(View.GONE);
                        tv_momentName.setVisibility(View.VISIBLE);
                        gridView.setPadding(0, 0, 0, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            rl_setcover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ArrayList<String> strings = customAdapter.getSelectedImage();
                        momentIconPath = strings.get(0);
                        momentDB.updateMomentTitleIcon(strings.get(0), momentID);
                        customAdapter.cleareSelected();
                        sMomentChangeNotify = true;
                        gridView.setPadding(0, 0, 0, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            rl_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean aBoolean = false;
                    ArrayList<String> strings = customAdapter.getSelectedImage();
                    try {
                        Cursor cursor = momentDB.getMomentlistdata(momentID);
                        int size = cursor.getCount() - strings.size();
                        if (!(size >= 4)) {
                            try {
                                Toast.makeText(getApplicationContext(), "Can`t delete below 4 Memories  images", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            customAdapter.cleareSelected();
                            return;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < strings.size(); i++) {
                        if (Objects.equals(strings.get(i), momentIconPath)) {
                            aBoolean = true;
                        }
                        momentDB.deleteMomentImage(momentID, strings.get(i));
                    }
                    imagesList = new ArrayList<>();
                    try {
                        Cursor cursor = momentDB.getMomentlistdata(momentID);
                        cursor.moveToFirst();
                        try {
                            tv_count.setText(cursor.getCount() + "  Photos");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        do {
                            try {
                                if (new File(cursor.getString(cursor.getColumnIndex("filepath"))).exists()) {
                                    imagesList.add(cursor.getString(cursor.getColumnIndex("filepath")));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } while (cursor.moveToNext());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (aBoolean) {
                        try {

                            momentIconPath = imagesList.get(0);
                            momentDB.updateMomentTitleIcon(momentIconPath, momentID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //Glide.with(NBA_NoBoringActionBarActivity.this).load(momentIconPath).into(iv_Icon);
                    }
                    try {
                        customAdapter = new CustomAdapter(imagesList, NBA_NoBoringActionBarActivity.this);
                        gridView.setAdapter(customAdapter);
                        Ll_bottom.setVisibility(View.GONE);
                        gridView.setPadding(0, 0, 0, 0);
                        sMomentChangeNotify = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setupListView() {
        try {
            customAdapter = new CustomAdapter(imagesList, NBA_NoBoringActionBarActivity.this);

            mPlaceHolderView = getLayoutInflater().inflate(R.layout.nba_view_header_placeholder, gridView, false);
            gridView.addHeaderView(mPlaceHolderView);
            gridView.setNumColumns(4);
            gridView.setAdapter(customAdapter);
            gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    try {
                        int scrollY = getScrollY();
                        mHeader.setTranslationY(Math.max(-scrollY, mMinHeaderTranslation));
                        float ratio = clamp(mHeader.getTranslationY() / mMinHeaderTranslation, 0.0f, 1.0f);
                        interpolate(mHeaderLogo, getActionBarIconView(), mSmoothInterpolator.getInterpolation(ratio));
                        interpolate(Rl_MainIcone, getActionBarIconView(), mSmoothInterpolator.getInterpolation(ratio));

                        if (mSmoothInterpolator.getInterpolation(ratio) == 1.0) {
                            try {

                                mHeaderLogo.setVisibility(View.GONE);
                                Rl_MainIcone.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {

                                mHeaderLogo.setVisibility(View.VISIBLE);
                                Rl_MainIcone.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        setTitleAlpha(clamp(5.0F * ratio - 4.0F, 0.0F, 1.0F));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void setTitleAlpha(float alpha) {
        try {
            mAlphaForegroundColorSpan.setAlpha(alpha);
            mSpannableString.setSpan(mAlphaForegroundColorSpan, 0, mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            getActionBar().setTitle(mSpannableString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(value, max));
    }

    private void interpolate(View view1, View view2, float interpolation) {
        try {
            getOnScreenRect(mRect1, view1);
            getOnScreenRect(mRect2, view2);

            float scaleX = 1.0F + interpolation * (mRect2.width() / mRect1.width() - 1.0F);
            float scaleY = 1.0F + interpolation * (mRect2.height() / mRect1.height() - 1.0F);
            float translationX = 0.5F * (interpolation * (mRect2.left + mRect2.right - mRect1.left - mRect1.right));
            float translationY = 0.5F * (interpolation * (mRect2.top + mRect2.bottom - mRect1.top - mRect1.bottom));

            view1.setTranslationX(translationX);
            view1.setTranslationY(translationY - mHeader.getTranslationY());

            try {
                view1.setScaleX(scaleX);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                view1.setScaleY(scaleY);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getOnScreenRect(RectF rect, View view) {
        try {
            rect.set(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getScrollY() {

        View c = gridView.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = gridView.getFirstVisiblePosition();
        int top = c.getTop();

        int headerHeight = 0;
        try {
            if (firstVisiblePosition >= 1) {
                try {

                    headerHeight = mPlaceHolderView.getHeight();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }

    private void setupActionBar() {
        try {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                try {
                    actionBar.setIcon(R.drawable.ic_transparent);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        //getActionBarTitleView().setAlpha(0f);
    }

    private ImageView getActionBarIconView() {
        ImageView imageView = (ImageView) findViewById(android.R.id.home);
        try {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        onBackPressed();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return imageView;
    }

    private TextView getActionBarTitleView() {
        int id = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        return (TextView) findViewById(id);
    }

    public int getActionBarHeight() {
        try {
            if (mActionBarHeight != 0) {
                try {
                    return mActionBarHeight;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            try {
                getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue, true);
                mActionBarHeight = TypedValue.complexToDimensionPixelSize(mTypedValue.data, getResources().getDisplayMetrics());

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mActionBarHeight;
    }

    public class CustomAdapter extends ArrayAdapter<String> implements View.OnClickListener {

        private ArrayList<String> dataSet;
        Context mContext;
        SparseBooleanArray booleanArray = new SparseBooleanArray();

        private class ViewHolder {
            Squre_RoundedImageView iv_MM_Icon;
            ImageView mCheckBox;
            RelativeLayout lv_select;
        }

        CustomAdapter(ArrayList<String> data, Context context) {
            super(context, R.layout.recent_recycler_item_width, data);
            try {
                this.dataSet = data;
                this.mContext = context;
                sSelectionFlag = false;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public void onClick(View v) {
            try {
                int position = (Integer) v.getTag();
                Object object = getItem(position);
                String s = (String) object;
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @Override
        public int getCount() {
            return this.dataSet.size();
        }


        @NonNull
        @Override
        public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
            String s = getItem(position);
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(NBA_NoBoringActionBarActivity.this);
                convertView = inflater.inflate(R.layout.viewpager_item, parent, false);
                try {
                    viewHolder.iv_MM_Icon = (Squre_RoundedImageView) convertView.findViewById(R.id.recent_image);
                    viewHolder.mCheckBox = (ImageView) convertView.findViewById(R.id.ic_select);
                    viewHolder.mCheckBox.setColorFilter(s_theme_colorPrimaryDark);
                    viewHolder.lv_select = convertView.findViewById(R.id.lv_select);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            Glide.with(NBA_NoBoringActionBarActivity.this).load(s).into(viewHolder.iv_MM_Icon);
            try {
                if (sSelectionFlag) {
                    try {
                        viewHolder.lv_select.setVisibility(View.VISIBLE);
                        if (this.booleanArray.get(position)) {
                            try {

                                viewHolder.mCheckBox.setVisibility(View.VISIBLE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {

                                viewHolder.mCheckBox.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {

                        viewHolder.lv_select.setVisibility(View.GONE);
                        viewHolder.mCheckBox.setVisibility(View.GONE);
                    } catch (Exception e) {
                        e.printStackTrace();
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
                                viewHolder.mCheckBox.setVisibility(View.GONE);
                            }
                            if (!value) {
                                viewHolder.mCheckBox.setVisibility(View.VISIBLE);
                            }
                            int i = getCheckedCount();
                            if (i == 0) {
                                try {
                                    viewHolder.lv_select.setVisibility(View.GONE);
                                    rl_setcover.setVisibility(View.GONE);
                                    Ll_bottom.setVisibility(View.GONE);
                                    gridView.setPadding(0, 0, 0, 0);
                                    sSelectionFlag = false;
                                    notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            if (i == 1) {
                                try {
                                    Ll_bottom.setVisibility(View.VISIBLE);
                                    gridView.setPadding(0, 0, 0, 165);
                                    rl_setcover.setVisibility(View.VISIBLE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                            if (i > 1) {
                                try {
                                    Ll_bottom.setVisibility(View.VISIBLE);
                                    rl_setcover.setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (!sSelectionFlag) {
                        try {
                            sSelectionFlag = true;
                            viewHolder.lv_select.setVisibility(View.VISIBLE);
                            notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    view.performClick();
                    return true;
                }
            });
            return convertView;
        }


        private Integer getCheckedCount() {
            Integer in = 0;
            for (int i = 0; i < this.getCount(); i++) {
                if (this.booleanArray.get(i)) {
                    in = in + 1;
                }
            }
            return in;
        }

        ArrayList<String> getSelectedImage() {
            ArrayList<String> mTempArry = new ArrayList<>();
            try {
                for (int i = 0; i < this.getCount(); i++) {
                    try {
                        if (this.booleanArray.get(i)) {
                            try {
                                mTempArry.add(dataSet.get(i));
                            } catch (Exception ignored) {
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

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
                Ll_bottom.setVisibility(View.GONE);
                gridView.setPadding(0, 0, 0, 0);
            } catch (Exception ignored) {
            }
        }
    }


}
