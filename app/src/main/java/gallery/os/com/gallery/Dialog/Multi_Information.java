package gallery.os.com.gallery.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import gallery.os.com.gallery.Extera.SizeUnit;
import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;


@SuppressLint("ValidFragment")
public class Multi_Information extends DialogFragment {

    Activity mactivity;
    Dialog mdialog;
    ArrayList<String> mfilepath;
    Button mBtn_ok;
    String[] type = new String[]{"Images", "Videos"};

    @SuppressLint("ValidFragment")
    public Multi_Information(ArrayList<String> mfilepath) {
        this.mfilepath = mfilepath;
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
            mdialog.setContentView(R.layout.dialog_multi_information);

            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(mdialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            mdialog.getWindow().setAttributes(lp);

            int id = getArguments().getInt("id", 0);

            mBtn_ok = mdialog.findViewById(R.id.btn_ok);
            mBtn_ok.setTextColor(s_theme_colorPrimary);
            mBtn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dismiss();
                }
            });
            TextView tv_content_type, tv_size, tv_count, tv_type;
            tv_content_type = mdialog.findViewById(R.id.tv_content_type);
            tv_count = mdialog.findViewById(R.id.tv_count);
            tv_size = mdialog.findViewById(R.id.tv_size);
            tv_type = mdialog.findViewById(R.id.tv_type);
            Double totleSize = 0.0;
            for (int i = 0; i < mfilepath.size(); i++) {
                File file1 = new File(mfilepath.get(i));
                totleSize = totleSize + file1.length();
            }
            SizeUnit sizeUnitkb = SizeUnit.KB;
            SizeUnit sizeUnitmb = SizeUnit.MB;
            SizeUnit sizeUnitgb = SizeUnit.GB;
            if ((int) (totleSize / sizeUnitkb.inBytes()) > 1024) {
                if ((int) (totleSize / sizeUnitmb.inBytes()) > 1024) {
                    try {

                        tv_size.setText((int) (totleSize / sizeUnitgb.inBytes()) + "gb");

                    }catch (Exception e){e.printStackTrace();}

   } else {try {

                    tv_size.setText((int) (totleSize / sizeUnitmb.inBytes()) + "mb");

                }catch (Exception e){e.printStackTrace();}

  }
            } else {try {
                tv_size.setText((int) (totleSize / sizeUnitkb.inBytes()) + "kb");

            }catch (Exception e){e.printStackTrace();}


               }

            tv_content_type.setText("" + type[id]);
            tv_type.setText("Multiple");
            tv_count.setText("" + mfilepath.size());
        }catch (Exception e){e.printStackTrace();}


        return mdialog;
    }
}
