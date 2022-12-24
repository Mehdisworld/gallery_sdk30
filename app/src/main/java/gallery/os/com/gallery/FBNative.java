package gallery.os.com.gallery;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 17-10-2018.
 */

public class FBNative {

   static NativeAd nativeAd;
   static LinearLayout adView;
    public static void showNativeAd(final Context context,final LinearLayout sNativeAdvanceLayout)
    {

        nativeAd = new NativeAd(context, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID");
        //nativeAd = new NativeAd(context, "323601261810287_323602851810128");
        nativeAd.setAdListener(new NativeAdListener()
        {
            @Override
            public void onError(Ad ad, AdError error) {
                // Ad error callback
                //Toast.makeText(context,"error"+error,Toast.LENGTH_SHORT).show();
                //Log.e("error",""+ad +"     and error is"+error);
            }

            @Override
            public void onMediaDownloaded(Ad ad) {

            }

            @Override
            public void onAdLoaded(Ad ad)
            {
                // Add the Ad view into the ad container.

                try{


                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }


                    if (nativeAd != null) {
                        nativeAd.unregisterView();
                    }

                    // Add the Ad view into the ad container.
                    LayoutInflater inflater = LayoutInflater.from(context);
                    adView = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, sNativeAdvanceLayout, false);
                    sNativeAdvanceLayout.addView(adView);

                    // Add the AdChoices icon
                    LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
                    AdChoicesView adChoicesView = new AdChoicesView(context, nativeAd, true);
                    adChoicesContainer.addView(adChoicesView, 0);

                    // Create native UI using the ad metadata.
                    AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
                    TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
                    MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
                    TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
                    TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
                    TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
                    Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

                    // Set the Text.
                    nativeAdTitle.setText(nativeAd.getAdvertiserName());
                    nativeAdBody.setText(nativeAd.getAdBodyText());
                    nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
                    nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
                    nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
                    sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

                    // Create a list of clickable views
                    List<View> clickableViews = new ArrayList<>();
                    clickableViews.add(nativeAdTitle);
                    clickableViews.add(nativeAdCallToAction);

                    // Register the Title and CTA button to listen for clicks.
                    nativeAd.registerViewForInteraction(
                            adView,
                            nativeAdMedia,
                            nativeAdIcon,
                            clickableViews);
                }catch (Exception e){}

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // On logging impression callback
            }
        });

        try{
            //AdSettings.addTestDevice("e2e0334e-38df-49fe-ae63-ec7677e82cec");
            nativeAd.loadAd();
        }catch (Exception e){}

    }

}
