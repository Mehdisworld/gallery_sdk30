
package gallery.os.com.gallery.Dialog;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;

import com.makeramen.roundedimageview.RoundedImageView;

import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;


public class ColorChooserDialog extends DialogFragment implements View.OnClickListener {
    private Button buttonDisagree, buttonAgree;

    private int currentTheme;
    private SharedPreferences sharedPreferences;
    Dialog dialog;

    RoundedImageView mIV_theme, mIV_theme1, mIV_theme2, mIV_theme3, mIV_theme4, mIV_theme5, mIV_theme6, mIV_theme7, mIV_theme8;
    int color[] = new int[]{R.color.colorPrimaryDark, R.color.colorPrimaryDark1, R.color.colorPrimaryDark2, R.color.colorPrimaryDark3, R.color.colorPrimaryDark4, R.color.colorPrimaryDark5, R.color.colorPrimaryDark6,
            R.color.colorPrimaryDark7, R.color.colorPrimaryDark8};


    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {try {
        sharedPreferences = getActivity().getSharedPreferences("VALUES", Context.MODE_PRIVATE);
        currentTheme = sharedPreferences.getInt("THEME", 0);

        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //noinspection ConstantConditions
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_theme);

        dialogButtons();
    }catch (Exception e){e.printStackTrace();}






        return dialog;
    }


    private void dialogButtons() {
        try { mIV_theme = dialog.findViewById(R.id.iv_1);
            mIV_theme1 = dialog.findViewById(R.id.iv_2);
            mIV_theme2 = dialog.findViewById(R.id.iv_3);
            mIV_theme3 = dialog.findViewById(R.id.iv_4);
            mIV_theme4 = dialog.findViewById(R.id.iv_5);
            mIV_theme5 = dialog.findViewById(R.id.iv_6);
            mIV_theme6 = dialog.findViewById(R.id.iv_7);
            mIV_theme7 = dialog.findViewById(R.id.iv_8);
            mIV_theme8 = dialog.findViewById(R.id.iv_9);


            buttonDisagree = (Button) dialog.findViewById(R.id.btn_cancel);
            buttonAgree = (Button) dialog.findViewById(R.id.btn_ok);


            buttonAgree.setTextColor(s_theme_colorPrimary);

            if (currentTheme == 1) {
                int color1 = getResources().getColor(color[currentTheme -1]);
                mIV_theme.setBorderColor(color1);
            } else if (currentTheme == 2) {
                int color1 = getResources().getColor(color[currentTheme -1]);
                mIV_theme1.setBorderColor(color1);
            } else if (currentTheme == 3) {
                int color1 = getResources().getColor(color[currentTheme -1]);
                mIV_theme2.setBorderColor(color1);
            } else if (currentTheme == 4) {
                int color1 = getResources().getColor(color[currentTheme -1]);
                mIV_theme3.setBorderColor(color1);
            } else if (currentTheme == 5) {
                int color1 = getResources().getColor(color[currentTheme -1]);
                mIV_theme4.setBorderColor(color1);
            } else if (currentTheme == 6) {
                int color1 = getResources().getColor(color[currentTheme -1]);
                mIV_theme5.setBorderColor(color1);
            } else if (currentTheme == 7) {
                int color1 = getResources().getColor(color[currentTheme -1]);
                mIV_theme6.setBorderColor(color1);
            } else if (currentTheme == 8) {
                int color1 = getResources().getColor(color[currentTheme -1]);
                mIV_theme7.setBorderColor(color1);
            } else if (currentTheme == 9) {
                int color1 = getResources().getColor(color[currentTheme -1]);
                mIV_theme8.setBorderColor(color1);
            }else {
                int color1 = getResources().getColor(color[0]);
                mIV_theme.setBorderColor(color1);
            }

            mIV_theme.setOnClickListener(this);
            mIV_theme1.setOnClickListener(this);
            mIV_theme2.setOnClickListener(this);
            mIV_theme3.setOnClickListener(this);
            mIV_theme4.setOnClickListener(this);
            mIV_theme5.setOnClickListener(this);
            mIV_theme6.setOnClickListener(this);
            mIV_theme7.setOnClickListener(this);
            mIV_theme8.setOnClickListener(this);


            buttonDisagree.setOnClickListener(this);
            buttonAgree.setOnClickListener(this);

        }catch (Exception e){e.printStackTrace();}



    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_1:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getOnItemChoose(0);
                if (onItemChoose != null)
                    onItemChoose.onClick(1);
                break;
            case R.id.iv_2:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getOnItemChoose(1);
                if (onItemChoose != null)
                    onItemChoose.onClick(2);
                break;
            case R.id.iv_3:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getOnItemChoose(2);
                if (onItemChoose != null)
                    onItemChoose.onClick(3);
                break;
            case R.id.iv_4:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getOnItemChoose(3);
                if (onItemChoose != null)
                    onItemChoose.onClick(4);
                break;
            case R.id.iv_5:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getOnItemChoose(4);
                if (onItemChoose != null)
                    onItemChoose.onClick(5);
                break;
            case R.id.iv_6:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getOnItemChoose(5);
                if (onItemChoose != null)
                    onItemChoose.onClick(6);
                break;
            case R.id.iv_7:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getOnItemChoose(6);
                if (onItemChoose != null)
                    onItemChoose.onClick(7);
                break;
            case R.id.iv_8:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getOnItemChoose(7);
                if (onItemChoose != null)
                    onItemChoose.onClick(8);
                break;
            case R.id.iv_9:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getOnItemChoose(8);
                if (onItemChoose != null)
                    onItemChoose.onClick(9);
                break;

            case R.id.btn_cancel:
                sharedPreferences.edit().putBoolean("THEMECHANGED", false).apply();

                if (onItemChoose != null)
                    onItemChoose.onClick(currentTheme);

                getDialog().dismiss();
                break;
            case R.id.btn_ok:
                sharedPreferences.edit().putBoolean("THEMECHANGED", true).apply();
                getDialog().dismiss();
                if (onItemChoose != null)
                    onItemChoose.onSaveChange();
                break;
        }
    }

    public void getOnItemChoose(int position) {
        try {
            int color1 = getResources().getColor(color[position]);

            if (position == 0) {
                try {
                    mIV_theme.setBorderColor(color1);
                    mIV_theme1.setBorderColor(Color.WHITE);
                    mIV_theme2.setBorderColor(Color.WHITE);
                    mIV_theme3.setBorderColor(Color.WHITE);
                    mIV_theme4.setBorderColor(Color.WHITE);
                    mIV_theme5.setBorderColor(Color.WHITE);
                    mIV_theme6.setBorderColor(Color.WHITE);
                    mIV_theme7.setBorderColor(Color.WHITE);
                    mIV_theme8.setBorderColor(Color.WHITE);
                }catch (Exception e){e.printStackTrace();}


            } else if (position == 1) {
                try {

                    mIV_theme1.setBorderColor(color1);
                    mIV_theme.setBorderColor(Color.WHITE);
                    mIV_theme2.setBorderColor(Color.WHITE);
                    mIV_theme3.setBorderColor(Color.WHITE);
                    mIV_theme4.setBorderColor(Color.WHITE);
                    mIV_theme5.setBorderColor(Color.WHITE);
                    mIV_theme6.setBorderColor(Color.WHITE);
                    mIV_theme7.setBorderColor(Color.WHITE);
                    mIV_theme8.setBorderColor(Color.WHITE);
                }catch (Exception e){e.printStackTrace();}


            } else if (position == 2) {
                try {

                    mIV_theme2.setBorderColor(color1);
                    mIV_theme.setBorderColor(Color.WHITE);
                    mIV_theme1.setBorderColor(Color.WHITE);
                    mIV_theme3.setBorderColor(Color.WHITE);
                    mIV_theme4.setBorderColor(Color.WHITE);
                    mIV_theme5.setBorderColor(Color.WHITE);
                    mIV_theme6.setBorderColor(Color.WHITE);
                    mIV_theme7.setBorderColor(Color.WHITE);
                    mIV_theme8.setBorderColor(Color.WHITE);
                }catch (Exception e){e.printStackTrace();}


            } else if (position == 3) {
                try {

                    mIV_theme3.setBorderColor(color1);
                    mIV_theme.setBorderColor(Color.WHITE);
                    mIV_theme1.setBorderColor(Color.WHITE);
                    mIV_theme2.setBorderColor(Color.WHITE);
                    mIV_theme4.setBorderColor(Color.WHITE);
                    mIV_theme5.setBorderColor(Color.WHITE);
                    mIV_theme6.setBorderColor(Color.WHITE);
                    mIV_theme7.setBorderColor(Color.WHITE);
                    mIV_theme8.setBorderColor(Color.WHITE);
                }catch (Exception e){e.printStackTrace();}


            } else if (position == 4) {
                try {

                    mIV_theme4.setBorderColor(color1);
                    mIV_theme.setBorderColor(Color.WHITE);
                    mIV_theme1.setBorderColor(Color.WHITE);
                    mIV_theme2.setBorderColor(Color.WHITE);
                    mIV_theme3.setBorderColor(Color.WHITE);
                    mIV_theme5.setBorderColor(Color.WHITE);
                    mIV_theme6.setBorderColor(Color.WHITE);
                    mIV_theme7.setBorderColor(Color.WHITE);
                    mIV_theme8.setBorderColor(Color.WHITE);
                }catch (Exception e){e.printStackTrace();}


            } else if (position == 5) {
                try {

                    mIV_theme5.setBorderColor(color1);
                    mIV_theme.setBorderColor(Color.WHITE);
                    mIV_theme1.setBorderColor(Color.WHITE);
                    mIV_theme2.setBorderColor(Color.WHITE);
                    mIV_theme3.setBorderColor(Color.WHITE);
                    mIV_theme4.setBorderColor(Color.WHITE);

                    mIV_theme6.setBorderColor(Color.WHITE);
                    mIV_theme7.setBorderColor(Color.WHITE);
                    mIV_theme8.setBorderColor(Color.WHITE);
                }catch (Exception e){e.printStackTrace();}


            } else if (position == 6) {
                try {

                    mIV_theme6.setBorderColor(color1);
                    mIV_theme.setBorderColor(Color.WHITE);
                    mIV_theme1.setBorderColor(Color.WHITE);
                    mIV_theme2.setBorderColor(Color.WHITE);
                    mIV_theme3.setBorderColor(Color.WHITE);
                    mIV_theme4.setBorderColor(Color.WHITE);
                    mIV_theme5.setBorderColor(Color.WHITE);
                    mIV_theme7.setBorderColor(Color.WHITE);
                    mIV_theme8.setBorderColor(Color.WHITE);
                }catch (Exception e){e.printStackTrace();}


            } else if (position == 7) {
                try {

                    mIV_theme7.setBorderColor(color1);
                    mIV_theme.setBorderColor(Color.WHITE);
                    mIV_theme1.setBorderColor(Color.WHITE);
                    mIV_theme2.setBorderColor(Color.WHITE);
                    mIV_theme3.setBorderColor(Color.WHITE);
                    mIV_theme4.setBorderColor(Color.WHITE);
                    mIV_theme5.setBorderColor(Color.WHITE);
                    mIV_theme6.setBorderColor(Color.WHITE);
                    mIV_theme8.setBorderColor(Color.WHITE);
                }catch (Exception e){e.printStackTrace();}


            } else if (position == 8) {
                try {

                    mIV_theme8.setBorderColor(color1);
                    mIV_theme.setBorderColor(Color.WHITE);
                    mIV_theme1.setBorderColor(Color.WHITE);
                    mIV_theme2.setBorderColor(Color.WHITE);
                    mIV_theme3.setBorderColor(Color.WHITE);
                    mIV_theme4.setBorderColor(Color.WHITE);
                    mIV_theme5.setBorderColor(Color.WHITE);
                    mIV_theme6.setBorderColor(Color.WHITE);
                    mIV_theme7.setBorderColor(Color.WHITE);
                }catch (Exception e){e.printStackTrace();}


            }


        }catch (Exception e){e.printStackTrace();}



    }

    public void setOnItemChoose(OnItemChoose onItemChoose) {
        this.onItemChoose = onItemChoose;
    }

    public interface OnItemChoose {
        public void onClick(int position);

        public void onSaveChange();
    }

    public OnItemChoose onItemChoose;


}