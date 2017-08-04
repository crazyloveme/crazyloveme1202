package in.pjitsol.zapnabit.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.PlaceOrder.CartFragment;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Util.TrackGPS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class UserOrdersFragment extends Fragment implements 
OnBackPressListener,OnClickListener,OnItemClickListener
,IAsyncTaskRunner,OnCancelListener{
	private LayoutInflater inflater;
	private Activity context;
	private UserOrdersAdapter adapter;
	private ListView list_myOrders;
	private ProgressHUD progressDialog;
	private double latitude;
	private double longitude;
	private TrackGPS gps;

	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.merchant_my_orders,
				container, false);

		this.inflater = inflater;
		initView(view);
		return view;
	}

	private void initView(View view) {
		checkPermission(context);
		//GetLocation();
		list_myOrders=(ListView)view.findViewById(R.id.list_myOrders);
		adapter=new UserOrdersAdapter(context,StaticConstants.historyItems,latitude,longitude);
		list_myOrders.setAdapter(adapter);
		list_myOrders.setOnItemClickListener(this);
		
		FetchHistory();
	}

	public void FetchHistory() {
		HashMap<String, String> par = new HashMap<String, String>();
		par.put(StaticConstants.JSON_USER_ID, PrefHelper.
				getStoredString(context, PrefHelper.PREF_FILE_NAME,
						StaticConstants.USER_ID));
		progressDialog = ProgressHUD.show(context,
				getResources().getString(R.string.label_loading_refresh), true,true,this);
		AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
				context,UserOrdersFragment.this,
				BaseNetwork.obj().KEY_USER_HISTORY, progressDialog,StaticConstants.POST_METHOD);
		task.execute(par);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}
	private void GetLocation() {
		gps = new TrackGPS(context);
		if(gps.canGetLocation()){
			longitude = gps.getLongitude();
			latitude = gps .getLatitude();
		}
		else
		{
			gps.showSettingsAlert();
		}
	}
	@Override
	public boolean onBackPressed() {
		((BaseFragmentActivity) context).OnNearBySearchFragment();
		return true;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.context=activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Bundle bundle=new Bundle();
		bundle.putInt(StaticConstants.HISTORY_ITEM_POSITION, position);
		((BaseFragmentActivity) context).OnUserOrderDEtailFragment(bundle);

	}

	@Override
	public void taskStarting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskCompleted(Object result) {
		adapter.notifyDataSetChanged();		
	}

	@Override
	public void taskProgress(String urlString, Object progress) {

	}

	@Override
	public void taskErrorMessage(Object result) {

	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

	public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;

	public void checkPermission(final Context context) {

		List<String> permissionsNeeded = new ArrayList<String>();

		final List<String> permissionsList = new ArrayList<String>();
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
			permissionsNeeded.add("GPS");
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
			permissionsNeeded.add("Location");


		if (permissionsList.size() > 0) {
			if (permissionsNeeded.size() > 0) {
				// Need Rationale
				String message = "You need to grant access to " + permissionsNeeded.get(0);
				for (int i = 1; i < permissionsNeeded.size(); i++)
					message = message + ", " + permissionsNeeded.get(i);
				showMessageOKCancel(message,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
										REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
							}
						});
				return;
			}
			requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
					REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
			return;
		}
		GetLocation();
	}

	private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
		new AlertDialog.Builder(context)
				.setMessage(message)
				.setPositiveButton("OK", okListener)
				.setNegativeButton("Cancel", null)
				.create()
				.show();
	}
	private boolean addPermission(List<String> permissionsList, String permission) {
		if (ContextCompat.checkSelfPermission(context,permission) != PackageManager.PERMISSION_GRANTED) {
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
			case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
			{
				Map<String, Integer> perms = new HashMap<String, Integer>();
				// Initial
				perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
				// Fill with results
				for (int i = 0; i < permissions.length; i++)
					perms.put(permissions[i], grantResults[i]);
				// Check for ACCESS_FINE_LOCATION
				if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
						) {
					// All Permissions Granted
					GetLocation();
				} else {
					// Permission Denied
					Toast.makeText(context, "Some Permission is Denied", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}
}
