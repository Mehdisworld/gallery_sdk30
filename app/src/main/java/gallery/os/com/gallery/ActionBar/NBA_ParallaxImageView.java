package gallery.os.com.gallery.ActionBar;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by f.laurent on 21/11/13.
 * antoine-merle.com inspiration
 */
public class NBA_ParallaxImageView extends ImageView {

    private int mCurrentTranslation;

    public NBA_ParallaxImageView(Context context) {
        super(context);
    }

    public NBA_ParallaxImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NBA_ParallaxImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setCurrentTranslation(int currentTranslation) {
        mCurrentTranslation = currentTranslation;
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        try {
            canvas.save();
            canvas.translate(0, -mCurrentTranslation / 2)  ;
            super.draw(canvas);
            canvas.restore();
        }catch (Exception e){e.printStackTrace();}

    }
}
