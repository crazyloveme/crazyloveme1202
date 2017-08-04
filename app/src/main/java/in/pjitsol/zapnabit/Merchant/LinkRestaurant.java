package in.pjitsol.zapnabit.Merchant;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTaskYelp;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.NearbySearch.UserPlaceDetailAdapter;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.CongratulationDialog;
import in.pjitsol.zapnabit.Ui.IOtpCallback;
import in.pjitsol.zapnabit.Ui.NoticeMerchantDialog;
import in.pjitsol.zapnabit.Ui.OtpDialog;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.SearchInfoDialog;
import in.pjitsol.zapnabit.Util.TrackGPS;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.ISdkCallback;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LinkRestaurant extends Fragment implements
        OnBackPressListener, OnClickListener
        , ISdkCallback, OnCancelListener, IAsyncTaskRunner,
        OnItemClickListener, IOtpCallback ,InoticeCallback{
    private LayoutInflater inflater;
    private Activity context;
    double latitude;
    double longitude;
    private TrackGPS gps;
    //25.7791591,-80.135141
   /* double latitude = 37.786882;
    double longitude = -122.4021607;*/
    /*private double latitude=37.612720;
    private double longitude=-97.046708;*/
    private ProgressHUD progressDialog;
    private String queryStringTerm;
    private ImageView text_go;
    private EditText et_search_term;
    private String queryStringLocation;
    private ListView list_searchresult;
    private PlaceDetailAdapter adapter;
    private ArrayList<YelpEntity> recentMarkerList = new ArrayList<>();
    private YelpEntity itemSelected;
    private RelativeLayout rel_norecord;
    private TextView textverify;
    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.merchant_search,
                container, false);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {

        checkPermission(context);
        // GetLocation();

        new NoticeMerchantDialog(context,this).show();
        list_searchresult = (ListView) view.findViewById(R.id.list_searchresult);
        adapter = new PlaceDetailAdapter(context, recentMarkerList);
        View headerview = (ViewGroup) inflater.inflate(R.layout.listsearchheader, list_searchresult, false);
        list_searchresult.addHeaderView(headerview, null, false);
        list_searchresult.setAdapter(adapter);
        list_searchresult.setOnItemClickListener(this);
        rel_norecord = (RelativeLayout) view.findViewById(R.id.rel_norecord);
        textverify = (TextView) view.findViewById(R.id.textverify);
        et_search_term = (EditText) view.findViewById(R.id.et_search_find);
        text_go = (ImageView) view.findViewById(R.id.text_go);
        text_go.setOnClickListener(this);
        et_search_term.setOnEditorActionListener(onEditChangeListener());
        textverify.setOnClickListener(this);
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

    protected void PerFormSdkLinking(Marker marker) {
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID)
        );
        mHashMap.put(StaticConstants.SDK_INFO, Util.getSdkInfo(marker).toString());

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_MERCHANT_SDK_REGISTER, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }

    private OnEditorActionListener onEditChangeListener() {
        return new OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_DONE)) {

                    PerformSearchFunction();
                }
                return false;
            }
        };
    }

    @Override
    public boolean onBackPressed() {

		/*fragment.getView().setVisibility(View.INVISIBLE);
        ((BaseFragmentActivity)context).HideSendButton(View.INVISIBLE);
		((BaseFragmentActivity)context).SetHeaderText(StaticConstants.LINK_YOUR_STORE);*/
        return true;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.text_go:
                if (!TextUtils.isEmpty(et_search_term.getText().toString())) {
                    PerformSearchFunction();
                } else
                    Toast.makeText(context, "Please enter your query", Toast.LENGTH_LONG).show();
                break;
            case R.id.textverify:
                ((BaseFragmentActivity) context).OnMerchantVerifyFragment();

                break;

            default:
                break;
        }

    }


    public void PerformSearchFunction() {
        queryStringTerm = et_search_term.getText().toString().trim();

        if (TextUtils.isEmpty(PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_ACESSTOKEN))) {
            progressDialog = ProgressHUD.show(context,
                    getResources().getString(R.string.label_loading_refresh), true, true, this);
            HashMap<String, String> mHashMap = new HashMap<String, String>();
            mHashMap.put(StaticConstants.JSON_YELP_CLIENT_ID, StaticConstants.YELP_CLIENT_ID);
            mHashMap.put(StaticConstants.JSON_YELP_CLIENT_SECRET, StaticConstants.YELP_CLIENT_SECRET);
            AuthCommanTaskYelp<HashMap<String, String>, ResultMessage> task = new AuthCommanTaskYelp<HashMap<String, String>, ResultMessage>(
                    context, this,
                    BaseNetwork.obj().KEY_YELP_TOKEN, progressDialog, StaticConstants.POST_METHOD);
            task.execute(mHashMap);
        } else {
            progressDialog = ProgressHUD.show(context,
                    getResources().getString(R.string.label_loading_refresh), true, true, this);
            HashMap<String, String> mHashMap = new HashMap<String, String>();
            mHashMap.put(StaticConstants.JSON_YELP_TERM, queryStringTerm);
            mHashMap.put(StaticConstants.JSON_YELP_LATITUDE, String.valueOf(latitude));
            mHashMap.put(StaticConstants.JSON_YELP_LONGITUDE, String.valueOf(longitude));
            mHashMap.put(StaticConstants.JSON_YELP_RADIUS, "3218");
            AuthCommanTaskYelp<HashMap<String, String>, ResultMessage> task = new AuthCommanTaskYelp<HashMap<String, String>, ResultMessage>(
                    context, this,
                    BaseNetwork.obj().KEY_YELP_SEARCH, progressDialog, StaticConstants.GET_METHOD);
            task.execute(mHashMap);


        }


        //PerformYelpTask(queryStringTerm,queryStringLocation);
    }

    @Override
    public void SearchResult(ArrayList<YelpEntity> list, String SearchType) {
        AddMarkersYelp(list);

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.context = activity;
    }

    protected void AddMarkersYelp(ArrayList<YelpEntity> result) {

        if (result != null && result.size() > 0) {
            recentMarkerList = result;
            adapter.placeList = recentMarkerList;
            adapter.notifyDataSetChanged();
            rel_norecord.setVisibility(View.GONE);
        } else {
            recentMarkerList = new ArrayList<YelpEntity>();
            adapter.placeList = recentMarkerList;
            adapter.notifyDataSetChanged();
            rel_norecord.setVisibility(View.VISIBLE);
        }
    }

    /*public void PerformYelpTask(String query,String location) {
        //((BaseFragmentActivity)context).changeSdkTypeIcon(StaticConstants.YELP_SEARCH);
        progressDialog = ProgressHUD.show(context,
                "Loading",this);
        new YelpTask(LinkRestaurant.this, context,progressDialog,StaticConstants.YELP_SEARCH,query,location).execute();

    }*/
    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        gps.stopUsingGPS();
        Fragment f = (Fragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null) {
            getFragmentManager().beginTransaction().remove(f).commitAllowingStateLoss();
        }


    }

    @Override
    public void taskStarting() {
        // TODO Auto-generated method stub

    }

    @Override
    public void taskCompleted(Object result) {


        ResultMessage object = (ResultMessage) result;
        if (object.TYPE.equalsIgnoreCase(BaseNetwork.obj().KEY_YELP_TOKEN)) {
            progressDialog = ProgressHUD.show(context,
                    getResources().getString(R.string.label_loading_refresh), true, true, this);
            HashMap<String, String> mHashMap = new HashMap<String, String>();
            mHashMap.put(StaticConstants.JSON_YELP_TERM, queryStringTerm);
            mHashMap.put(StaticConstants.JSON_YELP_LATITUDE, String.valueOf(latitude));
            mHashMap.put(StaticConstants.JSON_YELP_LONGITUDE, String.valueOf(longitude));
            mHashMap.put(StaticConstants.JSON_YELP_RADIUS, "3218");
            AuthCommanTaskYelp<HashMap<String, String>, ResultMessage> task = new AuthCommanTaskYelp<HashMap<String, String>, ResultMessage>(
                    context, this,
                    BaseNetwork.obj().KEY_YELP_SEARCH, progressDialog, StaticConstants.GET_METHOD);
            task.execute(mHashMap);
        } else if (object.TYPE.equalsIgnoreCase(BaseNetwork.obj().KEY_YELP_SEARCH)) {
            AddMarkersYelp(object.businessNames);

        } else if (object.TYPE.equalsIgnoreCase(StaticConstants.SEND_OTP))
            new OtpDialog(context, LinkRestaurant.this).show();
        else if (object.TYPE.equalsIgnoreCase(StaticConstants.RESEND_OTP))
            new OtpDialog(context, LinkRestaurant.this).show();
        else
            new CongratulationDialog(context).show();

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        itemSelected = (YelpEntity) parent.getItemAtPosition(position);
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID));
        mHashMap.put(StaticConstants.MERCHANT_PHONE, "+918010814110");

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_SEND_OTP, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

        //	new OtpDialog(context,LinkRestaurant.this).show();
		/*YelpEntity item=(YelpEntity) parent.getItemAtPosition(position);

		PerFormSdkLinkingItem(item);*/

    }

    public void resendOtp() {
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID));
        mHashMap.put(StaticConstants.MERCHANT_PHONE, "+918010814110");

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_RESEND_OTP, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);
    }

    protected void PerFormSdkLinkingItem(YelpEntity marker) {
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID)
        );
        mHashMap.put(StaticConstants.SDK_INFO, Util.getSdkInfoItem(itemSelected).toString());

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_MERCHANT_SDK_REGISTER, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }


    @Override
    public void Otpcallback(String Type) {
        if (Type.equalsIgnoreCase(BaseNetwork.obj().KEY_SEND_OTP))
            PerFormSdkLinkingItem(itemSelected);
        else
            resendOtp();

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

    @Override
    public void noticeCallback() {
        new SearchInfoDialog(context).show();
    }
}
