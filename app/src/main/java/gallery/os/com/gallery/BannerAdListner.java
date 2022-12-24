package gallery.os.com.gallery;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;

public class BannerAdListner extends AdListener {

	private Context mContext;
	private Handler mHandler;

	public BannerAdListner(Context context, Handler handler) {
		this.mContext = context;
		this.mHandler = handler;
	}

	@Override
	public void onAdClosed() {
	}

	@Override
	public void onAdLoaded() {

		Message msg = new Message();
		msg.what = 121;
		if (mHandler != null) {
			mHandler.sendMessage(msg);
		}
	}

	@Override
	public void onAdOpened() {
	}

	public void onAdLeftApplication() {
	};

	@Override
	public void onAdFailedToLoad(int errorCode) {
		String errorReason = "";
		switch (errorCode) {
		case AdRequest.ERROR_CODE_INTERNAL_ERROR:
			errorReason = "Internal error";
			break;
		case AdRequest.ERROR_CODE_INVALID_REQUEST:
			errorReason = "Invalid request";
			break;
		case AdRequest.ERROR_CODE_NETWORK_ERROR:
			errorReason = "Network Error";
			break;
		case AdRequest.ERROR_CODE_NO_FILL:
			errorReason = "No fill";
			break;
		}
	}
}