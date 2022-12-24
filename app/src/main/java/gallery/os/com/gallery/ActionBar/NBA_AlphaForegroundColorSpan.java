package gallery.os.com.gallery.ActionBar;

import android.graphics.Color;
import android.os.Parcel;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;

public class NBA_AlphaForegroundColorSpan extends ForegroundColorSpan {

    private float mAlpha;

    NBA_AlphaForegroundColorSpan(int color) {
        super(color);
	}

    public NBA_AlphaForegroundColorSpan(Parcel src) {
        super(src);
        try {
            mAlpha = src.readFloat();

        }catch (Exception e){e.printStackTrace();}
     }

    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        try {
            dest.writeFloat(mAlpha);
        }catch (Exception e){e.printStackTrace();}

    }

	@Override
	public void updateDrawState(TextPaint ds) {
		ds.setColor(getAlphaColor());
	}

    public void setAlpha(float alpha) {
        mAlpha = alpha;
    }

    public float getAlpha() {
        return mAlpha;
    }

    private int getAlphaColor() {
        int foregroundColor = getForegroundColor();
        return Color.argb((int) (mAlpha * 255), Color.red(foregroundColor), Color.green(foregroundColor), Color.blue(foregroundColor));
    }
}