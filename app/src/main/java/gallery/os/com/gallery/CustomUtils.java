package gallery.os.com.gallery;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.WindowManager;

public class CustomUtils {

    public static final int TIME = 5000; // 5000ms = 5s


    public static final String JSON_LINK = "https://raw.githubusercontent.com/Mehdisworld/testjson/main/jstest.json";
    public final static String JSON_GUIDE_DATA="UpadeInfo";
    public static final String  JSON_ADS="AdsController";

    public static final String JSON_NETWORKADS="NetworkAds";
    public static final String JSON_BRADMOB="BannerAdmob";
    public static final String JSON_INTADMOB="InterstitialAdmob";

    public static final String JSON_BRFACEBOOK="BannerFacebook";
    public static final String JSON_INTFACEBOOK="InterstitialFacebook";

    public static final String JSON_IMGBANNER="ImageBanner";
    public static final String JSON_IMGBANNERIMG="ImageBannerImg";
    public static final String JSON_IMGBANNERURL="ImageBannerURL";



    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static int getScreenSize(Activity context, boolean isWidth) {
        Point size = new Point();
        WindowManager w = context.getWindowManager();

        w.getDefaultDisplay().getSize(size);
        if(isWidth) {
            return size.x;
        }
        else {
            return size.y;
        }
    }

}
