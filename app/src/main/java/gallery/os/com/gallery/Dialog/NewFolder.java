package gallery.os.com.gallery.Dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import gallery.os.com.gallery.R;

import static gallery.os.com.gallery.MainActivity.s_theme_colorPrimary;


@SuppressLint("ValidFragment")
public class NewFolder extends DialogFragment implements View.OnClickListener {

    Activity mactivity;
    Dialog mdialog;
    Button mrename, mCancelBTN;
    EditText meditText;
    OnClickListener onClickListener;

    @SuppressLint("ValidFragment")
    public NewFolder(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public interface OnClickListener {
        void NewFolder_Btn(View view, Dialog dialog, String s);

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
            mdialog.setContentView(R.layout.dialog_newfolder);

            meditText = mdialog.findViewById(R.id.editText);
            meditText.getBackground().mutate().setColorFilter(s_theme_colorPrimary, PorterDuff.Mode.SRC_ATOP);

            mrename = mdialog.findViewById(R.id.btn_newfolder);
            mCancelBTN = mdialog.findViewById(R.id.btn_cancle);

            mrename.setTextColor(s_theme_colorPrimary);
            mCancelBTN.setTextColor(s_theme_colorPrimary);

            mrename.setOnClickListener(this);
            mCancelBTN.setOnClickListener(this);


            meditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    try {
                        if (charSequence.toString().isEmpty()) {
                            // mclose.setVisibility(View.INVISIBLE);
                            try {

                                mrename.setEnabled(false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            try {

                                if (!(/*mclose.getVisibility() == View.VISIBLE &&*/ mrename.isEnabled())) {
                                    // mclose.setVisibility(View.VISIBLE);
                                    try {
                                        mrename.setEnabled(true);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


        return mdialog;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_newfolder:
                try {
                    this.onClickListener.NewFolder_Btn(view, mdialog, meditText.getText().toString());
                    mdialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_cancle:
                try {
                    dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
