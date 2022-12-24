package gallery.os.com.gallery.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import gallery.os.com.gallery.Extera.SizeUnit;
import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;


@SuppressLint("ValidFragment")
public class Information extends DialogFragment {

    Activity mactivity;
    Dialog mdialog;

    String mfilepath;
    Button mBtn_ok;

    @SuppressLint("ValidFragment")
    public Information(String mfilepath) {

        this.mfilepath=mfilepath;
    }


    @SuppressLint("SetTextI18n")
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            mactivity = getActivity();

            //noinspection ConstantConditions
            mdialog = new Dialog(getActivity());
            mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //noinspection ConstantConditions
            mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mdialog.setContentView(R.layout.dialog_information);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(mdialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            mdialog.getWindow().setAttributes(lp);


            mBtn_ok = mdialog.findViewById(R.id.btn_ok);
            mBtn_ok.setTextColor(s_theme_colorPrimary);
            mBtn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            TextView tv_name, tv_path, tv_size, tv_date;


            tv_name = mdialog.findViewById(R.id.tv_name);
            tv_path = mdialog.findViewById(R.id.tv_path);
            tv_size = mdialog.findViewById(R.id.tv_size);
            tv_date = mdialog.findViewById(R.id.tv_date);

            //Toast.makeText(mactivity,""+mfileuri,Toast.LENGTH_SHORT).show();

            File  file = new File(mfilepath);




            try {

                tv_name.setText(file.getName());
                tv_path.setText(file.getAbsolutePath());
                SizeUnit sizeUnitkb = SizeUnit.KB;
                SizeUnit sizeUnitmb = SizeUnit.MB;
                SizeUnit sizeUnitgb = SizeUnit.GB;

                if ((int) (file.length() / sizeUnitkb.inBytes()) > 1024) {
                    if ((int) (file.length() / sizeUnitmb.inBytes()) > 1024) {
                        tv_size.setText((int) (file.length() / sizeUnitgb.inBytes()) + "gb");
                    } else {
                        tv_size.setText((int) (file.length() / sizeUnitmb.inBytes()) + "mb");
                    }
                } else {
                    tv_size.setText((int) (file.length() / sizeUnitkb.inBytes()) + "kb");
                }
                Date date = new Date(file.lastModified());
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat df2 = new SimpleDateFormat("d MMM yyyy,hh:mm");
                String dateText = df2.format(date);

                tv_date.setText(dateText);
            }catch (Exception ignored){}
        }catch (Exception e){e.printStackTrace();}




        return mdialog;
    }

}
