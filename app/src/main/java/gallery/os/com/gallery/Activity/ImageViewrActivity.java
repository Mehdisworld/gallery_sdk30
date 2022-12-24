package gallery.os.com.gallery.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.util.ArrayList;

import gallery.os.com.gallery.BannerAndFullAD;
import gallery.os.com.gallery.CC;
import gallery.os.com.gallery.Database.MomentDB;
import gallery.os.com.gallery.Dialog.AddToMoment;
import gallery.os.com.gallery.Dialog.Delete;
import gallery.os.com.gallery.Dialog.Information;
import gallery.os.com.gallery.Dialog.Rename;
import gallery.os.com.gallery.R;


public class ImageViewrActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;

    ArrayList<String> mImagelist = new ArrayList<>();
    ImageView mUp_Btn, miv_delete, miv_share, miv_back;
    ImageView mBottom_Btn, mOption;
    RelativeLayout mBottomlv, mLv_Bottomtop, mLv_top;
    LinearLayout mbottomView;
    TextView mbottomtext, mbottomuptext;
    CustomPagerAdapter customPagerAdapter;
    LinearLayout mLv_edit, mLv_crop, mLv_album, mLv_hide, mLv_flip, mLv_wallpaper, mLv_print, mLv_contact, mLv_open_with;

    int tempcurrentPosition = 0;
    private int mcount = 0, mitemCount = 0;

    RequestOptions options;
    public RequestBuilder<Drawable> thumbRequest;
    LinearLayout bannerAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_imageviewr);
            try {
                mImagelist = getIntent().getStringArrayListExtra("ImageFiles");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                bannerAds = findViewById(R.id.banner_container);
                if (BannerAndFullAD.isNetworkAvailable(getApplicationContext())) {
                    if (CC.admobBannerForImageViewActivity) {
                        CC.admobBannerForImageViewActivity = false;
                        BannerAndFullAD.loadBannerAd(bannerAds, getApplicationContext());
                    }
                    if (CC.needToShowAdmobFull) {
                        BannerAndFullAD.showLoadedFullAD(getApplicationContext());
                    }
                }
                mLv_top = findViewById(R.id.top);
                mUp_Btn = findViewById(R.id.up_btn);
                mBottom_Btn = findViewById(R.id.down_btn);
                mBottomlv = findViewById(R.id.bottom);
                mbottomView = findViewById(R.id.bottom_dialogue);
                mOption = findViewById(R.id.iv_option);
                miv_delete = findViewById(R.id.iv_delete);
                miv_back = findViewById(R.id.iv_back);
                miv_share = findViewById(R.id.iv_share);
                mLv_Bottomtop = findViewById(R.id.lv_bottomtop);
                mbottomtext = findViewById(R.id.imagename);
                mbottomuptext = findViewById(R.id.bottomuptext);
            } catch (Exception e) {
                e.printStackTrace();
            }


            try {
                mLv_edit = findViewById(R.id.edit);
                mLv_crop = findViewById(R.id.crop);
                mLv_album = findViewById(R.id.album);
                mLv_hide = findViewById(R.id.hide);
                mLv_flip = findViewById(R.id.flip);
                mLv_wallpaper = findViewById(R.id.wallpaper);
                mLv_print = findViewById(R.id.print);
                mLv_contact = findViewById(R.id.contact);
                mLv_open_with = findViewById(R.id.open_with);
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                options = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).override(Target.SIZE_ORIGINAL).dontTransform();

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                thumbRequest = Glide.with(ImageViewrActivity.this).load(R.drawable.loading).apply(options);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                viewPager = findViewById(R.id.viewpager);
                customPagerAdapter = new CustomPagerAdapter(ImageViewrActivity.this);
                viewPager.setAdapter(customPagerAdapter);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                File file = new File(mImagelist.get(getIntent().getIntExtra("position", 0)));
                mbottomtext.setText(file.getName());
                mbottomuptext.setText(file.getName());

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        try {
                            File file = new File(mImagelist.get(position));
                            mbottomtext.setText(file.getName());
                            mbottomuptext.setText(file.getName());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });

                viewPager.setCurrentItem(getIntent().getIntExtra("position", 0));

            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mBottomlv.setOnClickListener(this);
                mLv_Bottomtop.setOnClickListener(this);
                miv_delete.setOnClickListener(this);
                miv_share.setOnClickListener(this);
                miv_back.setOnClickListener(this);

                mLv_edit.setOnClickListener(this);
                mLv_crop.setOnClickListener(this);
                mLv_album.setOnClickListener(this);
                mLv_hide.setOnClickListener(this);
                mLv_flip.setOnClickListener(this);
                mLv_wallpaper.setOnClickListener(this);
                mLv_print.setOnClickListener(this);
                mLv_contact.setOnClickListener(this);
                mLv_open_with.setOnClickListener(this);
            } catch (Exception e) {
                e.printStackTrace();
            }


            mOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        PopupMenu popup = new PopupMenu(ImageViewrActivity.this, v);
                        popup.inflate(R.menu.option_menu);
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.rename:
                                        try {
                                            if (mImagelist.get(viewPager.getCurrentItem()).contains("/storage/emulated/0/")) {
                                                try {
                                                    final File file = new File(mImagelist.get(viewPager.getCurrentItem()));
                                                    String filename;
                                                    try {
                                                        filename = file.getName().substring(0, file.getName().lastIndexOf("."));
                                                    } catch (Exception e) {
                                                        filename = file.getName();
                                                    }
                                                    final String extension = file.getName().substring(file.getName().lastIndexOf("."));
                                                    Rename rename = new Rename(new Rename.OnClickListener() {
                                                        @Override
                                                        public void Rename_Btn(View view, Dialog dialog, String s) {
                                                            try {
                                                                final File renameFile = checkFileIsExtist(new File(file.getParent() + "/" + s + "" + extension));
                                                                if (file.renameTo(renameFile)) {


                                                                    try {
                                                                        MediaScannerConnection.scanFile(ImageViewrActivity.this, new String[]{file.getAbsolutePath()}, null,
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
                                                                    try {
                                                                        MediaScannerConnection.scanFile(ImageViewrActivity.this, new String[]{renameFile.getAbsolutePath()}, null,
                                                                                new MediaScannerConnection.OnScanCompletedListener() {
                                                                                    @Override
                                                                                    public void onScanCompleted(String path, Uri uri1) {

                                                                                    }
                                                                                });
                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }


                                                                    try {
                                                                        tempcurrentPosition = viewPager.getCurrentItem();
                                                                        mImagelist.remove(tempcurrentPosition);
                                                                        customPagerAdapter = new CustomPagerAdapter(ImageViewrActivity.this);
                                                                        viewPager.setAdapter(customPagerAdapter);
                                                                        viewPager.setCurrentItem(tempcurrentPosition);

                                                                    } catch (Exception e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }
                                                            } catch (Exception e) {
                                                                e.printStackTrace();
                                                            }


                                                        }

                                                        @Override
                                                        public void Cancel_Btn(View view, Dialog dialog) {

                                                        }
                                                    });
                                                    Bundle bundle = new Bundle();
                                                    bundle.putString("filename", filename);
                                                    rename.setArguments(bundle);
                                                    rename.setCancelable(false);
                                                    rename.show(getFragmentManager(), "");

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        return true;
                                    case R.id.details:
                                        try {
                                            Information information = new Information(mImagelist.get(viewPager.getCurrentItem()));
                                            information.setCancelable(false);
                                            information.show(getFragmentManager(), "");
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        return true;
                                    case R.id.slideshow:
                                        try {
                                            Intent intent = new Intent(ImageViewrActivity.this, SlideShowActivity.class);
                                            intent.putExtra("Moment_Fragment", false);
                                            intent.putExtra("ImageviewrActivity", true);
                                            intent.putExtra("currentPosition", viewPager.getCurrentItem());
                                            intent.putStringArrayListExtra("photo", mImagelist);
                                            startActivity(intent);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        break;
                                }

                                return true;

                            }
                        });

                        popup.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bottom:
                try {
                    mBottomlv.setVisibility(View.GONE);
                    mbottomView.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.lv_bottomtop:
                try {
                    mbottomView.setVisibility(View.GONE);
                    mBottomlv.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_delete:
                try {
                    final File file = new File(mImagelist.get(viewPager.getCurrentItem()));
                    if (file.getAbsolutePath().startsWith("/storage/emulated/0/")) {
                        final Delete delete = new Delete(new Delete.OnDeleteListener() {
                            @Override
                            public void Delete_Btn(View view, Dialog dialog) {
                                try {
                                    if (file.exists()) {
                                        file.delete();
                                        try {
                                            MediaScannerConnection.scanFile(ImageViewrActivity.this, new String[]{file.getAbsolutePath()}, null,
                                                    new MediaScannerConnection.OnScanCompletedListener() {
                                                        @Override
                                                        public void onScanCompleted(String path, Uri uri) {
                                                            try {
                                                                getContentResolver().delete(uri, null, null);
                                                            } catch (Exception ignored) {
                                                            }
                                                        }
                                                    });

                                            tempcurrentPosition = viewPager.getCurrentItem();
                                            mImagelist.remove(tempcurrentPosition);
                                            customPagerAdapter = new CustomPagerAdapter(ImageViewrActivity.this);
                                            viewPager.setAdapter(customPagerAdapter);
                                            viewPager.setCurrentItem(tempcurrentPosition);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
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


                    } else {
                        Toast.makeText(getApplicationContext(), "Can't Delete this file", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.iv_share:
                try {

                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("image/*");
                    // int i = viewPager.getCurrentItem();
                    Uri fileUri = FileProvider.getUriForFile(ImageViewrActivity.this, "com.phone.stargallery.provider", new File(mImagelist.get(viewPager.getCurrentItem())));
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    shareIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
                    startActivity(Intent.createChooser(shareIntent, "Share using"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_back:
                try {
                    onBackPressed();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;
            case R.id.edit:
                try {

                    Intent editIntent = new Intent(Intent.ACTION_EDIT);
                    Uri editUri = FileProvider.getUriForFile(ImageViewrActivity.this, "com.phone.stargallery.provider", new File(mImagelist.get(viewPager.getCurrentItem())));
                    editIntent.setDataAndType(editUri, "image/*");
                    editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(Intent.createChooser(editIntent, null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.album:
                try {
                    AddToMoment addToMoment = new AddToMoment(new AddToMoment.OnSelectListener() {
                        @Override
                        public void Select_Moment(View view, Dialog dialog, String id) {
                            try {
                                MomentDB momentDB = new MomentDB(ImageViewrActivity.this);
                                momentDB.open();
                                momentDB.insertMomentImage(id, mImagelist.get(viewPager.getCurrentItem()));
                                momentDB.close();
                                Toast.makeText(ImageViewrActivity.this, "Add this Photo " + id + " Album", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }

                        @Override
                        public void Create_New_Moment(View view, Dialog dialog) {
                            try {
                                Toast.makeText(ImageViewrActivity.this, "Select Multiple Photo and Create Album ", Toast.LENGTH_SHORT).show();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Bundle bundle = new Bundle();
                    bundle.getBoolean("CreateBtn", false);
                    addToMoment.setArguments(bundle);
                    addToMoment.show(getFragmentManager(), "");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

            case R.id.wallpaper:
                try {
                    Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    Uri wallpaperUri = FileProvider.getUriForFile(ImageViewrActivity.this, "com.phone.stargallery.provider", new File(mImagelist.get(viewPager.getCurrentItem())));
                    intent.setDataAndType(wallpaperUri, "image/jpeg");
                    intent.putExtra("mimeType", "image/jpeg");
                    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    this.startActivity(Intent.createChooser(intent, "Set as:"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.id.open_with:
                try {
                    Intent openwithIntent = new Intent(Intent.ACTION_VIEW);
                    Uri openwithUri = FileProvider.getUriForFile(ImageViewrActivity.this, "com.phone.stargallery.provider", new File(mImagelist.get(viewPager.getCurrentItem())));
                    openwithIntent.setDataAndType(openwithUri, "image/*");
                    openwithIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    this.startActivity(Intent.createChooser(openwithIntent, "Open With :"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }


    private File checkFileIsExtist(File file) {
        mcount++;
        if (file.exists()) {

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
        }
        return file;
    }

    public class CustomPagerAdapter extends PagerAdapter {

        private Context mContext;

        CustomPagerAdapter(Context context) {
            mContext = context;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup collection, int position) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.viewpager_adepter, collection, false);
            try {
                GestureImageView image = layout.findViewById(R.id.photoview);
                Glide.with(ImageViewrActivity.this).load(mImagelist.get(position)).thumbnail(thumbRequest).into(image);

                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mLv_top.getVisibility() == View.VISIBLE) {
                            showSystemUi(false);
                            mBottomlv.setVisibility(View.GONE);
                            mLv_top.setVisibility(View.GONE);
                            mbottomView.setVisibility(View.GONE);
                        } else {
                            showSystemUi(true);
                            mLv_top.setVisibility(View.VISIBLE);
                            mBottomlv.setVisibility(View.VISIBLE);
                            mbottomView.setVisibility(View.GONE);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            collection.addView(layout);
            return layout;
        }


        @Override
        public void destroyItem(@NonNull ViewGroup collection, int position, @NonNull Object view) {
            try {
                collection.removeView((View) view);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        @Override
        public int getCount() {
            return mImagelist.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

    }

    private void showSystemUi(boolean show) {
        try {
            int flags = View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE;
            getWindow().getDecorView().setSystemUiVisibility(show ? 0 : flags);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
