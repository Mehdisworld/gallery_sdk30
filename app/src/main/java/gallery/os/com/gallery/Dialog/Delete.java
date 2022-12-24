package gallery.os.com.gallery.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;

@SuppressLint("ValidFragment")
public class Delete extends DialogFragment implements View.OnClickListener {

    Activity mactivity;
    Dialog mdialog;
    Button mDeleteBTN, mCancelBTN;
    OnDeleteListener onDeleteListener;

    @SuppressLint("ValidFragment")
    public Delete(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface OnDeleteListener {
        void Delete_Btn(View view, Dialog dialog);
        void Cancel_Btn(View view, Dialog dialog);
    }
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        try {
            mactivity = getActivity();
            //noinspection ConstantConditions
            mdialog = new Dialog(getActivity());
            mdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //noinspection ConstantConditions
            mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mdialog.setContentView(R.layout.dialog_delete);
            try {
                String s = getArguments().getString("gettext", null);
                if (s != null) {
                    try {
                        TextView textView = mdialog.findViewById(R.id.tv_title);
                        textView.setText(s);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            mDeleteBTN = mdialog.findViewById(R.id.btn_delete);
            mCancelBTN = mdialog.findViewById(R.id.btn_cancle);
            mDeleteBTN.setTextColor(s_theme_colorPrimary);
            mCancelBTN.setTextColor(s_theme_colorPrimary);
            mDeleteBTN.setOnClickListener(this);
            mCancelBTN.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mdialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_delete:
                try {
                    onDeleteListener.Delete_Btn(view, mdialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_cancle:
                try {
                    onDeleteListener.Cancel_Btn(view, mdialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

