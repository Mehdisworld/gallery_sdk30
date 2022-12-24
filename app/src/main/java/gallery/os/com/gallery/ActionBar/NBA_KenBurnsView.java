package gallery.os.com.gallery.ActionBar;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.ActionBar.NBA_NoBoringActionBarActivity.imagesList;


public class NBA_KenBurnsView extends FrameLayout {

    private static final String TAG = "KenBurnsView";

    private final Handler mHandler;
    private ImageView[] mImageViews;
    private int mActiveImageIndex = -1;

    private final Random random = new Random();
    private int mSwapMs = 10000;
    private int mFadeInOutMs = 400;

    private float maxScaleFactor = 1.5F;
    private float minScaleFactor = 1.2F;
    Context context;

    String momentIconPath;

    private Runnable mSwapImageRunnable = new Runnable() {
        @Override
        public void run() {
            swapImage();
            mHandler.postDelayed(mSwapImageRunnable, mSwapMs - mFadeInOutMs * 2);
        }
    };

    public NBA_KenBurnsView(Context context) {
        this(context, null);
        this.context = context;
    }

    public NBA_KenBurnsView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public NBA_KenBurnsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        mHandler = new Handler();
    }

    public void setImagesFile(String momentIconPath) {
        this.momentIconPath = momentIconPath;
        try {
            fillImageViews();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void swapImage() {
        try {
            try {
                if (mActiveImageIndex == -1) {
                    try {
                        mActiveImageIndex = 1;
                        animate(mImageViews[mActiveImageIndex]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return;
                }

                int inactiveIndex = mActiveImageIndex;

                try {
                    mActiveImageIndex = (1 + mActiveImageIndex) % mImageViews.length;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final ImageView activeImageView = mImageViews[mActiveImageIndex];
                activeImageView.setAlpha(0.0f);
                ImageView inactiveImageView = mImageViews[inactiveIndex];
                try {
                    animate(activeImageView);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.setDuration(mFadeInOutMs);
                    animatorSet.playTogether(
                            ObjectAnimator.ofFloat(inactiveImageView, "alpha", 1.0f, 0.0f),
                            ObjectAnimator.ofFloat(activeImageView, "alpha", 0.0f, 1.0f)
                    );
                    animatorSet.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void start(View view, long duration, float fromScale, float toScale, float fromTranslationX, float fromTranslationY, float toTranslationX, float toTranslationY) {

        try {
            view.setScaleX(fromScale);
            view.setScaleY(fromScale);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {

            view.setTranslationX(fromTranslationX);
            view.setTranslationY(fromTranslationY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            ViewPropertyAnimator propertyAnimator = view.animate().translationX(toTranslationX).translationY(toTranslationY).scaleX(toScale).scaleY(toScale).setDuration(duration);
            propertyAnimator.start();
            Log.d(TAG, "starting Ken Burns animation " + propertyAnimator);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private float pickScale() {
        return this.minScaleFactor + this.random.nextFloat() * (this.maxScaleFactor - this.minScaleFactor);
    }

    private float pickTranslation(int value, float ratio) {
        return value * (ratio - 1.0f) * (this.random.nextFloat() - 0.5f);
    }

    public void animate(View view) {
        try {
            float fromScale = pickScale();
            float toScale = pickScale();
            float fromTranslationX = pickTranslation(view.getWidth(), fromScale);
            float fromTranslationY = pickTranslation(view.getHeight(), fromScale);
            float toTranslationX = pickTranslation(view.getWidth(), toScale);
            float toTranslationY = pickTranslation(view.getHeight(), toScale);
            start(view, this.mSwapMs, fromScale, toScale, fromTranslationX, fromTranslationY, toTranslationX, toTranslationY);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        try {
            startKenBurnsAnimation();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {

            mHandler.removeCallbacks(mSwapImageRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startKenBurnsAnimation() {
        try {

            mHandler.post(mSwapImageRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final View view = inflate(getContext(), R.layout.nba_view_kenburns, this);
        try {
            mImageViews = new ImageView[imagesList.size()];

            mImageViews = new ImageView[4];
            mImageViews[0] = (ImageView) view.findViewById(R.id.image0);
            mImageViews[1] = (ImageView) view.findViewById(R.id.image1);
            mImageViews[2] = (ImageView) view.findViewById(R.id.image2);
            mImageViews[3] = (ImageView) view.findViewById(R.id.image3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d(TAG, "onFinishInflate =" + mActiveImageIndex);
    }

    private void fillImageViews() {
        try {
            Random rand = new Random();
            for (int i = 0; i < mImageViews.length; i++) {
                try {
                    if (i != 3) {
                        try {
                            int n = rand.nextInt(imagesList.size() - 1) + 1;
                            Glide.with(context).load(imagesList.get(n)).into(mImageViews[i]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        try {
                            Glide.with(context).load(momentIconPath).into(mImageViews[i]);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            mHandler.post(mSwapImageRunnable);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
