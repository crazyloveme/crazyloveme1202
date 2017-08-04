package in.pjitsol.zapnabit;

import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Login.LoginSocialActivity;
import in.pjitsol.zapnabit.Login.LoginSocialFragment;
import in.pjitsol.zapnabit.Util.TrackGPS;
import in.pjitsol.zapnabit.Util.Util;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SplashActivity extends Activity{
	private TrackGPS gps;
	private double mLongitude;
	private double mLatitude;
	public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;
	public static int OVERLAY_PERMISSION_REQ_CODE = 1234;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);


		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (!PrefHelper.getStoredBooleanG(SplashActivity.this, PrefHelper.PREF_FILE_NAME,
						StaticConstants.HAS_PERMISSIONS)) {
					if (Build.VERSION.SDK_INT >= 23) {
						someMethod();
					} else
						checkPermission(SplashActivity.this);
				} else
					GetLocation();
			}
		}, 2000);
	}
	public void someMethod() {
		if (!Settings.canDrawOverlays(this)) {
			Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
					Uri.parse("package:" + getPackageName()));
			startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
		}
		else
			checkPermission(this);
	}

	public void checkPermission(final Context context) {
		List<String> permissionsNeeded = new ArrayList<String>();
		final List<String> permissionsList = new ArrayList<String>();
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
			permissionsNeeded.add("GPS");
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
			permissionsNeeded.add("Location");
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE))
			permissionsNeeded.add("Network");
		if (!addPermission(permissionsList, Manifest.permission.INTERNET))
			permissionsNeeded.add("Internet");
		if (!addPermission(permissionsList, Manifest.permission.GET_ACCOUNTS))
			permissionsNeeded.add("Receive Updates");

		if (permissionsList.size() > 0) {
			requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
					REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
			return;
		}
		GetLocation();
	}


	private boolean addPermission(List<String> permissionsList, String permission) {
		if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
			permissionsList.add(permission);
			// Check for Rationale Option
			if (!shouldShowRequestPermissionRationale(permission))
				return false;
		}
		return true;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
				Map<String, Integer> perms = new HashMap<String, Integer>();
				perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.INTERNET, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.GET_ACCOUNTS, PackageManager.PERMISSION_GRANTED);
				for (int i = 0; i < permissions.length; i++)
					perms.put(permissions[i], grantResults[i]);
				if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
					PrefHelper.storeBoolean(this, PrefHelper.PREF_FILE_NAME,
							StaticConstants.HAS_PERMISSIONS, true);
					GetLocation();
				} else {
					checkPermission(this);
					/*Toast.makeText(this, "Some Permission is Denied", Toast.LENGTH_SHORT)
							.show();*/
				}
			}
			break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	public void showSettingsAlert() {
		android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
		alertDialog.setTitle("GPS Not Enabled");
		alertDialog.setMessage("Do you want to turn On GPS");
		alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivityForResult(intent, 123);
			}
		});
		alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		alertDialog.show();

	}

	private void GetLocation() {
		gps = new TrackGPS(this);
		if (gps.canGetLocation()) {
			mLongitude = gps.getLongitude();
			mLatitude = gps.getLatitude();
			if(!TextUtils.isEmpty(PrefHelper.getStoredString(SplashActivity.this,
					PrefHelper.PREF_FILE_NAME, StaticConstants.MERCHANT_ID))){
				startActivity(new Intent(SplashActivity.this,BaseFragmentActivity.class).
						putExtra(StaticConstants.USER_TYPE,StaticConstants.MERCHANT));
				finish();
			}
			else if(!TextUtils.isEmpty(PrefHelper.getStoredString(SplashActivity.this,
					PrefHelper.PREF_FILE_NAME, StaticConstants.USER_ID))){
				startActivity(new Intent(SplashActivity.this,BaseFragmentActivity.class).
						putExtra(StaticConstants.USER_TYPE,StaticConstants.DRIVER));
				finish();
			}
			else if(!TextUtils.isEmpty(PrefHelper.getStoredString(SplashActivity.this,
					PrefHelper.PREF_FILE_NAME, StaticConstants.DRIVER_ID))){
				startActivity(new Intent(SplashActivity.this,BaseFragmentActivity.class).
						putExtra(StaticConstants.USER_TYPE,StaticConstants.ASSIGNED_DRIVER));
				finish();
			}
			else{
				startActivity(new Intent(SplashActivity.this,LoginSocialActivity.class)
						.putExtra(StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK,  PrefHelper.getStoredBooleanG(SplashActivity.this,
								PrefHelper.PREF_FILE_NAME, StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK))
						.putExtra(StaticConstants.IS_LOGGED_IN_VIA_GOOGLE,  PrefHelper.getStoredBooleanG(SplashActivity.this,
								PrefHelper.PREF_FILE_NAME, StaticConstants.IS_LOGGED_IN_VIA_GOOGLE)))
				;
				finish();
			}

		} else {
			showSettingsAlert();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
			if (Settings.canDrawOverlays(this)) {
				checkPermission(this);
			} else
				checkPermission(this);
		}
		else if (requestCode == 123) {
			GetLocation();
		}
	}



}
