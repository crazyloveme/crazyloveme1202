package in.pjitsol.zapnabit.Merchant;

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
import in.pjitsol.zapnabit.Ui.CustomerViewPopup;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.User.UserOrdersFragment;
import in.pjitsol.zapnabit.Util.TrackGPS;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MerchantOrdersFragment extends Fragment implements 
OnBackPressListener,OnClickListener,OnItemClickListener,IAsyncTaskRunner,OnCancelListener{
	private LayoutInflater inflater;
	private Activity context;
	private MerchantOrdersAdapter adapter;
	private ListView list_myOrders;
	private ProgressHUD progressDialog;
	private TextView txt_rcvorder;
	public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;
	private TrackGPS gps;
	private double longitude;
	private  double latitude;
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.merchant_my_orders,
				container, false);

		this.inflater = inflater;
		initView(view);
		return view;
	}

	private void initView(View view) {

		if(PrefHelper.getStoredString(context,PrefHelper.PREF_FILE_NAME,StaticConstants.CALLING_FROM_LOGINMERCHANT)
				.equalsIgnoreCase("1")){
			if(PrefHelper.getStoredString(context,PrefHelper.PREF_FILE_NAME,StaticConstants.MERCHANT_STATUS)
					.equalsIgnoreCase("0"))
				new CustomerViewPopup(context).show();
		}

		txt_rcvorder=(TextView)view.findViewById(R.id.txt_rcvorder);
		list_myOrders=(ListView)view.findViewById(R.id.list_myOrders);
		adapter=new MerchantOrdersAdapter(context,StaticConstants.historyItems);
		list_myOrders.setAdapter(adapter);
		list_myOrders.setOnItemClickListener(this);

		if(StaticConstants.historyItems!=null && StaticConstants.historyItems.size()>0){
			txt_rcvorder.setVisibility(View.GONE);
		}
		else
			txt_rcvorder.setVisibility(View.VISIBLE);

		FetchHistory();
	}


	public void FetchHistory() {
		HashMap<String, String> par = new HashMap<String, String>();
		par.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
				getStoredString(context, PrefHelper.PREF_FILE_NAME,
						StaticConstants.MERCHANT_ID));
		progressDialog = ProgressHUD.show(context,
				getResources().getString(R.string.label_loading_refresh), true,true,this);
		AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
				context,MerchantOrdersFragment.this,
				BaseNetwork.obj().KEY_MERCHANT_HISTORY, progressDialog,StaticConstants.POST_METHOD);
		task.execute(par);

	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onBackPressed() {
		if(!PrefHelper.getStoredBoolean(context,
				PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_MERCHANT_YELP_REGISTERED))
			((BaseFragmentActivity) context).OnLinkFragment();
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
		((BaseFragmentActivity) context).OnMerchantOrderDEtailFragment(bundle);

	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskStarting() {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskCompleted(Object result) {

		if(StaticConstants.historyItems!=null && StaticConstants.historyItems.size()>0){
			txt_rcvorder.setVisibility(View.GONE);
		}
		else
			txt_rcvorder.setVisibility(View.VISIBLE);
		adapter.notifyDataSetChanged();		
	}

	@Override
	public void taskProgress(String urlString, Object progress) {
		// TODO Auto-generated method stub

	}

	@Override
	public void taskErrorMessage(Object result) {
		// TODO Auto-generated method stub

	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public void checkPermission(final Context context) {

		List<String> permissionsNeeded = new ArrayList<String>();

		final List<String> permissionsList = new ArrayList<String>();
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
			permissionsNeeded.add("GPS");
		if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
			permissionsNeeded.add("Location");
		if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
			permissionsNeeded.add("Read write");
		if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
			permissionsNeeded.add("Photos");
		if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
			permissionsNeeded.add("Call");
		if (!addPermission(permissionsList, Manifest.permission.CAMERA))
			permissionsNeeded.add("Camera");

		if (permissionsList.size() > 0) {
			requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
					REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
			return;
		}
		GetLocation();
	}

	private boolean addPermission(List<String> permissionsList, String permission) {
		if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
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
				// Initial
				perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
				perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
				for (int i = 0; i < permissions.length; i++)
					perms.put(permissions[i], grantResults[i]);
				if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
						&& perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
						) {
					GetLocation();
				} else {
					checkPermission(context);
					Toast.makeText(context, "Some Permission is Denied", Toast.LENGTH_SHORT)
							.show();
				}
			}
			break;
			default:
				super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		}
	}

	private void GetLocation() {
		gps = new TrackGPS(context);
		if (gps.canGetLocation()) {
			longitude = gps.getLongitude();
			latitude = gps.getLatitude();
		} else {
			gps.showSettingsAlert();
		}
	}

}
