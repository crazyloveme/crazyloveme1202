package in.pjitsol.zapnabit.Constants;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PrefHelper{

	public static String PREF_FILE_NAME = "Eschool_PREF_FILE";
	public static String PREF_FILE_NAME_ONLY = "Eschool_PREF_FILE_ONLY";
	public static final String GCM_DEVICE_TOKEN = "GcmDeviceToken";


	static public String getStoredString(Context aContext, String FileName,
			String aKey) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		return sharedPrefs.getString(aKey, "");
	}

	static public float getStoredFloat(Context aContext, String FileName,
			String aKey) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		return sharedPrefs.getFloat(aKey, 0.0f);
	}

	static public boolean getStoredBoolean(Context aContext, String FileName,
			String aKey) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		return sharedPrefs.getBoolean(aKey, true);
	}

	static public int getStoredInt(Context aContext, String FileName,
			String aKey) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		return sharedPrefs.getInt(aKey, 0);
	}

	static public void storeString(Context aContext, String FileName,
			String aKey, String aValue) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		Editor editor = sharedPrefs.edit();
		editor.putString(aKey, aValue);
		editor.commit();
	}

	static public void storeInt(Context aContext, String FileName, String aKey,
			int aValue) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		Editor editor = sharedPrefs.edit();
		editor.putInt(aKey, aValue);
		editor.commit();
	}
	
	static public void storeFloat(Context aContext, String FileName, String aKey,
			float aValue) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		Editor editor = sharedPrefs.edit();
		editor.putFloat(aKey, aValue);
		editor.commit();
	}

	static public void storeDouble(Context aContext, String FileName,
			String aKey, double aValue) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		Editor editor = sharedPrefs.edit();
		editor.putFloat(aKey, (float) aValue);
		editor.commit();
	}

	static public void storeBoolean(Context aContext, String FileName,
			String aKey, boolean aValue) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		Editor editor = sharedPrefs.edit();
		editor.putBoolean(aKey, aValue);
		editor.commit();
	}
	static public void ClearAll(Context aContext, String FileName){
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		Editor editor = sharedPrefs.edit();
		editor.clear();
		editor.commit();
	}
	static public boolean getStoredBooleanG(Context aContext, String FileName,
											String aKey) {
		SharedPreferences sharedPrefs = aContext.getSharedPreferences(FileName,
				0);
		return sharedPrefs.getBoolean(aKey, false);
	}
	/*public static void ClearInfo(Context aContext, String FileName){
		SharedPreferences sharedpreferences =aContext.getSharedPreferences(FileName,
				0);
		Editor editor = sharedpreferences.edit();
		editor.remove(StaticConstants.HAS_REDEEMED_COUPON);
		editor.remove(StaticConstants.VOUCHER_CODE);
		editor.remove(StaticConstants.VOUCHER_ABOVE_AMOUNT);
		editor.remove(StaticConstants.SPECIAL_INSTRUCTION);
		editor.remove(StaticConstants.JSON_TAG_OR_MEAL_ADDON_PRICE);
		editor.remove(StaticConstants.JSON_TAG_ORDERTYPE);
		editor.commit();
	}
	
	
	public static void ClearSharedPrefOnLogout(Context context) {
		
		PrefHelper.storeInt(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USR_USERID, 0);
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USERFIRSTNAME, "");
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USERMAILID, "");
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USERPHONENUMBER, "");
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USERNAME, "");
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USERLASTNAME, "");
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USR_COUNTRY, "");
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USR_ADDRESS1, "");
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USR_ADDRESS2, "");
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USR_CITY, "");
		PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_TAG_USR_POSTCODE, "");
		
		

	}*/
}
