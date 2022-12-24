package gallery.os.com.gallery.Dialog;


import android.content.Context;

import gallery.os.com.gallery.R;


public class Theme {
    public static void settingTheme(Context context, int theme) {
        try {
            switch (theme) {
                case 1:
                    context.setTheme(R.style.AppTheme);
                    break;
                case 2:
                    context.setTheme(R.style.AppTheme1);
                    break;
                case 3:
                    context.setTheme(R.style.AppTheme2);
                    break;
                case 4:
                    context.setTheme(R.style.AppTheme3);
                    break;
                case 5:
                    context.setTheme(R.style.AppTheme4);
                    break;
                case 6:
                    context.setTheme(R.style.AppTheme5);
                    break;
                case 7:
                    context.setTheme(R.style.AppTheme6);
                    break;
                case 8:
                    context.setTheme(R.style.AppTheme7);
                    break;
                case 9:
                    context.setTheme(R.style.AppTheme8);
                    break;
                default:
                    context.setTheme(R.style.AppTheme);
                    break;
            }
        } catch (Exception ignored) {
        }

    }
}

