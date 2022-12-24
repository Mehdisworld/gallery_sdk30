package gallery.os.com.gallery;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class BannerAndFullAD extends AdListener
{
	static AdView adView;
	public static InterstitialAd interstitialAd,interstitialAdForSetting;
	static Handler second;
	private Context mContext;

	public static void loadBannerAd(final LinearLayout frame, Context c)
	{
		try{
			second = new Handler(new Handler.Callback() {

				@Override
				public boolean handleMessage(Message msg) {
					if (msg.what == 121) {
						frame.setVisibility(View.VISIBLE);
					}
					return false;
				}
			});

			adView = new AdView(c);
			adView.setAdUnitId(MyApp.BannerAdmob);
			adView.setAdSize(AdSize.SMART_BANNER);
			adView.setAdListener(new BannerAdListner(c, second));
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT,
					Gravity.CENTER_HORIZONTAL);
			frame.addView(adView, params);
			adView.loadAd(new AdRequest.Builder().build());
		}catch (Exception e){}

	}


	public static boolean isNetworkAvailable(Context c)
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}


	public static void showLoadedFullAD(final Context c)
	{
		try{
			if (null != interstitialAd && interstitialAd.isLoaded())
			{
				interstitialAd.show();
			}
		}catch (Exception e){}

	}
	public static void loadFullAD(final Context c)
	{
		try {
			if (null == interstitialAd || !interstitialAd.isLoaded())
			{
				interstitialAd = new InterstitialAd(c);
				interstitialAd.setAdUnitId(MyApp.InterstitialAdmob);
				interstitialAd.loadAd(new AdRequest.Builder().build());
				interstitialAd.setAdListener(new AdListener()
				{
					@Override
					public void onAdClosed()
					{
						interstitialAd.loadAd(new AdRequest.Builder().build());
						CC.needToShowAdmobFull = false;
					}
				});
			}

		}catch (Exception e){}
	}


	@Override
	public void onAdLoaded() {

	}

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

	@Override
	public void onAdOpened() {

	}

	@Override
	public void onAdClosed() {

	}

	@Override
	public void onAdLeftApplication() {

	}


	public static void showLoadedFullADForOtherwhere(final Context c)
	{
		try{
			if(CC.singleTime){
				if (null != interstitialAdForSetting && interstitialAdForSetting.isLoaded())
				{
					interstitialAdForSetting.show();
				}
			}

		}catch (Exception e){}

	}
	public static void loadFullADForOtherWhere(final Context c)
	{
		try {
			if (null == interstitialAdForSetting || !interstitialAdForSetting.isLoaded())
			{
				interstitialAdForSetting = new InterstitialAd(c);
				interstitialAdForSetting.setAdUnitId(MyApp.InterstitialAdmob);
				interstitialAdForSetting.loadAd(new AdRequest.Builder().build());
				interstitialAdForSetting.setAdListener(new AdListener()
				{
					@Override
					public void onAdClosed()
					{
						interstitialAdForSetting.loadAd(new AdRequest.Builder().build());
						CC.singleTime = false;
					}
				});
			}

		}catch (Exception e){}

	}
}
