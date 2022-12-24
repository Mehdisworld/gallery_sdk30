package gallery.os.com.gallery.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import gallery.os.com.gallery.Database.MomentDB;
import gallery.os.com.gallery.R;



public class SlideShowActivity extends AppCompatActivity implements View.OnClickListener {

    private SliderLayout mDemoSlider;
    ImageView SliderClick;
    String effectName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slideshow);
        try {
            System.gc();
        } catch (Exception ignored) {
        }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        SliderClick = (ImageView) findViewById(R.id.sliderclick);


        SharedPreferences sharedPreferences = getSharedPreferences("com.phonei.bluegallery", Context.MODE_PRIVATE);
        effectName = sharedPreferences.getString("Slidshow_Effect", "Random");

        SliderClick.setOnClickListener(this);
        try {
            if (getIntent().getBooleanExtra("Moment_Fragment", true)) {
                MomentDB momentDB = new MomentDB(SlideShowActivity.this);
                momentDB.open();
                try {
                    Cursor cursor = momentDB.getMomentlistdata(getIntent().getStringExtra("MomentId"));
                    cursor.moveToFirst();
                    do {
                        try {
                            File file = new File(cursor.getString(cursor.getColumnIndex("filepath")));
                            if (file.exists()) {
                                try {
                                    TextSliderView textSliderView = new TextSliderView(this);
                                    textSliderView.image(file).setScaleType(BaseSliderView.ScaleType.CenterCrop);
                                    mDemoSlider.addSlider(textSliderView);
                                    mDemoSlider.getPagerIndicator().setVisibility(View.GONE);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    momentDB.deleteMomentImage(getIntent().getStringExtra("MomentId"), file.getAbsolutePath());
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
                momentDB.close();
            } else {
                try {
                    if (getIntent().getBooleanExtra("ImageviewrActivity", false)) {




                        ArrayList<String> mImageviewrPhotos = getIntent().getStringArrayListExtra("photo");
                        for (int i = 0; i < mImageviewrPhotos.size(); i++) {
                            try {
                                File file = new File(mImageviewrPhotos.get(i));
                                TextSliderView textSliderView = new TextSliderView(this);
                                textSliderView.image(file).setScaleType(BaseSliderView.ScaleType.CenterCrop);
                                mDemoSlider.addSlider(textSliderView);
                                mDemoSlider.getPagerIndicator().setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            mDemoSlider.setCurrentPosition(getIntent().getIntExtra("currentPosition", 0));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                       /* mDemoSlider.setPadding(20, 20, 20, 20);
                        for (int i = 0; i < 3; i++) {
                            try {
                                TextSliderView textSliderView = new TextSliderView(this);
                                textSliderView.image(R.drawable.glass).setScaleType(BaseSliderView.ScaleType.CenterInside);
                                mDemoSlider.addSlider(textSliderView);
                                mDemoSlider.getPagerIndicator().setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }*/
                    } else {
                        ArrayList<String> mRandomPaths = getIntent().getStringArrayListExtra("RandomPaths");
                        for (int i = 0; i < mRandomPaths.size(); i++) {
                            try {
                                File file = new File(mRandomPaths.get(i));
                                TextSliderView textSliderView = new TextSliderView(this);
                                textSliderView.image(file).setScaleType(BaseSliderView.ScaleType.CenterCrop);
                                mDemoSlider.addSlider(textSliderView);
                                mDemoSlider.getPagerIndicator().setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            mDemoSlider.setCurrentPosition(getIntent().getIntExtra("currentPosition", 0));
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
        mDemoSlider.setDuration(4000);

        if (effectName.equalsIgnoreCase("Random")) {
            try {
                mDemoSlider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageSelected(int position) {
                        try {
                            int random = new Random().nextInt((SliderLayout.Transformer.values().length) - 1);
                            mDemoSlider.setPresetTransformer(String.valueOf(SliderLayout.Transformer.values()[random]));
                            //mDemoSlider.setCustomAnimation(new DescriptionAnimation());
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
        } else {
            try {
                mDemoSlider.setPresetTransformer(effectName);
             //   mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
        try {

            mDemoSlider.startAutoCycle();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            mDemoSlider.stopAutoCycle();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {

    }
}
