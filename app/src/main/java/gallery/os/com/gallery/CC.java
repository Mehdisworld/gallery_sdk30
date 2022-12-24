package gallery.os.com.gallery;

public class CC
{
	public static boolean  needToShowAdmobFull = true, flagForFB = true, singleTime = true;
	private static final String ADSPREF = "AdChangesGallery";
	public static boolean admobBannerForHome = true,admobBannerForImageViewActivity = true,admobBannerForMemory = true;
	//public static boolean admobFullForSetting = true,admobFullForAll = true;
    /*public static void SavePreferences(Context c, String key, int value)
    {
        SharedPreferences sharedPreferences = c.getSharedPreferences("",c.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int showPreferences(Context c, String key)
    {
        SharedPreferences sharedPreferences = c.getSharedPreferences("",c.MODE_PRIVATE);
        int savedPref = sharedPreferences.getInt(key, CC.lastColor);
        return savedPref;
    }*/

	/*public static int randInt(int min, int max)
	{
		Random rand = new Random();
		int randomNum = rand.nextInt((max - min) + 1) + min;
		Log.d(" random value", ""+randomNum);
		return randomNum;
	}*/

	/*public static void setAdPrefData(Context context, Boolean flag){
		SharedPreferences.Editor editor = context.getSharedPreferences(ADSPREF, MODE_PRIVATE).edit();
		editor.putBoolean("fulladspriority", flag);
		editor.apply();
	}

	public static boolean getAdPrefData(Context context){
		SharedPreferences prefs = context.getSharedPreferences(ADSPREF, MODE_PRIVATE);
		Boolean flag = prefs.getBoolean("fulladspriority", true);
		return flag;
	}
*/
}
