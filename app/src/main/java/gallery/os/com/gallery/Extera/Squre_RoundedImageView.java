package gallery.os.com.gallery.Extera;

import android.content.Context;
import android.util.AttributeSet;



public class Squre_RoundedImageView extends com.makeramen.roundedimageview.RoundedImageView {

    public Squre_RoundedImageView(Context context) {
        super(context);
    }

    public Squre_RoundedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Squre_RoundedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec);













    }

}
