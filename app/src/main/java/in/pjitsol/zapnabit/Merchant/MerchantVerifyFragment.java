package in.pjitsol.zapnabit.Merchant;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Ui.CongratulationDialog;
import in.pjitsol.zapnabit.Ui.CustomAutoCompleteTextView;
import in.pjitsol.zapnabit.Ui.IOtpCallback;
import in.pjitsol.zapnabit.Ui.IVerifyCallback;
import in.pjitsol.zapnabit.Ui.OtpDialog;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Ui.VerifyDialog;
import in.pjitsol.zapnabit.Util.Logger;
import in.pjitsol.zapnabit.Util.TrackGPS;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

/**
 * Created by websnoox android on 3/9/2017.
 */

public class MerchantVerifyFragment extends Fragment implements
        OnBackPressListener, View.OnClickListener
        , IAsyncTaskRunner, DialogInterface.OnCancelListener,
        IVerifyCallback, IOtpCallback, PlaceSelectionListener, GoogleApiClient.OnConnectionFailedListener {
    private LayoutInflater inflater;
    private FragmentActivity context;
    private MerchantOrdersAdapter adapter;
    private ListView list_myOrders;
    private ProgressHUD progressDialog;
    private TextView txt_rcvorder;
    private TextView text_next;
    private CustomAutoCompleteTextView autoCompView;
    private static final String LOG_TAG = "PlaceSelectionListener";
    private static LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(28.568160, 77.202394), new LatLng(28.568160, 77.202394));
    private TrackGPS gps;
    private double mLongitude;
    private double mLatitude;
    private TextView et_estaddress;
    private EditText et_estname;
    private Place placeSelected;
   // private GoogleApiClient mGoogleApiClient;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;
    private static final int RC_SIGN_IN = 0;
    private boolean mIntentInProgress;
    private boolean verified = false;
    private ImageView img_google;
    private ImageView img_facebook;
    private String userEmail="";

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.link_buiseness,
                container, false);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {
        GetLocation();
        et_estaddress = (TextView) view.findViewById(R.id.et_estaddress);
        et_estname = (EditText) view.findViewById(R.id.et_estname);
        /*autoCompView = (CustomAutoCompleteTextView) view.findViewById(R.id.et_estaddress);
        autoCompView.setAdapter(new GooglePlacesAutocompleteAdapter(context, R.layout.list_item));*/
        BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
                new LatLng(mLatitude, mLongitude), new LatLng(mLatitude, mLongitude));
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) context.
                getFragmentManager().findFragmentById(R.id.place_fragment);
        autocompleteFragment.setOnPlaceSelectedListener(this);
        autocompleteFragment.setHint("Enter your address here");
        autocompleteFragment.setBoundsBias(BOUNDS_MOUNTAIN_VIEW);
        text_next = (TextView) view.findViewById(R.id.text_next);
        text_next.setOnClickListener(this);
        img_google = (ImageView) view.findViewById(R.id.img_google);
        img_facebook = (ImageView) view.findViewById(R.id.img_facebook);
        img_google.setOnClickListener(this);
        img_facebook.setOnClickListener(this);
        ((EditText) autocompleteFragment.getView().
                findViewById(R.id.place_autocomplete_search_input)).setTextSize(15.0f);

       /* ((EditText) autocompleteFragment.getView().
                findViewById(R.id.place_autocomplete_search_input)).setHint("Registered address on Google");*/
    }


    private void GetLocation() {
        gps = new TrackGPS(context);
        if (gps.canGetLocation()) {
            mLongitude = gps.getLongitude();
            mLatitude = gps.getLatitude();
        } else {
            gps.showSettingsAlert();
        }

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_next:
                if (verified){

                        new VerifyDialog(context, this).show();

                }
                else
                    Toast.makeText(context, "Please verify yourself via Fb or Google+", Toast.LENGTH_SHORT).show();
                break;

            case R.id.img_google:
                if(!verified){
                    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                            .requestEmail()
                            .build();

                    GoogleApiClient   mGoogleApiClient = new GoogleApiClient.Builder(context)
                            .enableAutoManage(context, this)
                            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                            .build();
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                    verified = true;
                    UpdateNextColor();

                }

                break;
            case R.id.img_facebook:
                loginToFacebook();
                verified = true;
                break;
        }

    }

    private CallbackManager callbackManager;

    private void loginToFacebook() {

        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        System.out.println("Success");

                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject json,
                                            GraphResponse response) {
                                        String jsonresult = String.valueOf(json);
                                        System.out.println("JSON Result" + jsonresult);
                                        try {
                                            System.out.println("JSON Result" + jsonresult);
                                            String str_email = json.getString("email");
                                            String str_id = json.getString("id");
                                            PrefHelper.storeString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.FB_ID, str_id);
                                            String str_firstname = json.getString("name");
                                            userEmail=json.getString("email");
                                            verified=true;
                                            UpdateNextColor();
                                          //  SignUpCall(str_email, str_firstname);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        //	Log.d(TAG_CANCEL,"On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        //Log.d(TAG_ERROR,error.toString());
                    }
                });
    }

    private void UpdateNextColor() {

        if(placeSelected!=null && verified){
            text_next.setBackgroundColor(context.getResources().getColor(R.color.red));
        }
        else
            text_next.setBackgroundColor(context.getResources().getColor(R.color.redtransparent));
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void taskStarting() {

    }

    @Override
    public void taskCompleted(Object o) {
        ResultMessage meesage = (ResultMessage) o;
        if (meesage.TYPE.equalsIgnoreCase(StaticConstants.SEND_OTP)) {
            new OtpDialog(context, this).show();
        }
        else if (meesage.TYPE.equalsIgnoreCase(StaticConstants.RESEND_OTP)) {
            new OtpDialog(context, this).show();
        }
        else if (meesage.TYPE.equalsIgnoreCase(StaticConstants.UPDATE_MERCHANT_STATUS)) {
            new CongratulationDialog(context).show();
        }
        else
            ChangeMerchantStatus();


    }

    @Override
    public void taskProgress(String urlString, Object o) {

    }

    @Override
    public void taskErrorMessage(Object o) {

    }

    @Override
    public void VerifyCallback() {
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_LAT, String.valueOf(placeSelected.getLatLng().latitude));
        mHashMap.put(StaticConstants.JSON_LNG, String.valueOf(placeSelected.getLatLng().longitude));
        mHashMap.put(StaticConstants.JSON_MERCHANT_NAME, "merchant");
        mHashMap.put("Merchant_Id", PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID));
        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_WITHOUT_YELP, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);
    }

    /* mHashMap.put(StaticConstants.JSON_MERCHANT_ADDRESS, "marijuanapharmacy, Sawtelle Boulevard, Los Angeles, CA, United States");
     mHashMap.put(StaticConstants.JSON_MERCHANT_NAME, "New York");*/
    @Override
    public void Otpcallback(String type) {

        if (type.equalsIgnoreCase(BaseNetwork.obj().KEY_SEND_OTP))
            PerFormSdkLinkingItem();
        else
            resendOtp();

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
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (FragmentActivity) context;
    }


    protected void PerFormSdkLinkingItem() {
        progressDialog = ProgressHUD.show(context,
                context.getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID)
        );
        mHashMap.put(StaticConstants.JSON_MERCHANT_PREORDER, "0"
        );
        mHashMap.put(StaticConstants.SDK_INFO, Util.getSdkInfoItem(placeSelected).toString());

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_MERCHANT_SDK_REGISTER, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }

    @Override
    public void onPlaceSelected(Place place) {

        Logger.i("Selected", "Place Selected: " + place.getLatLng());
        placeSelected = place;
        et_estaddress.setText(place.getName());
        UpdateNextColor();
        /*locationTextView.setText(getString(R.string.formatted_place_data, place
                .getName(), place.getAddress(), place.getPhoneNumber(), place
                .getWebsiteUri(), place.getRating(), place.getId()));
        if (!TextUtils.isEmpty(place.getAttributions())){
            attributionsTextView.setText(Html.fromHtml(place.getAttributions().toString()));
        }*/
    }

    @Override
    public void onError(Status status) {
        Logger.e("Error", "onError: Status = " + status.toString());
        Toast.makeText(context, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), context, 0).show();
            return;
        }

        if (!mIntentInProgress) {
            // store mConnectionResult
            mConnectionResult = result;

            if (signedInUser) {
                //resolveSignInError();
            }
        }
    }
    public void UpdateGoogle(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            // Get account information
            String mFullName = acct.getDisplayName();
            String mEmail = acct.getEmail();
            userEmail=acct.getEmail();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            UpdateGoogle(data);

            Toast.makeText(context,
                    "Google+ is successfully linked. This Google Address will be used to complete this process.",Toast.LENGTH_LONG).show();

        } else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
            Toast.makeText(context,"Facebook is successfully linked. This Google Address will be used to complete this process."
                    ,Toast.LENGTH_LONG).show();
        }


		/*if(requestCode==123)
		{
			Toast.makeText(context, "yupz", Toast.LENGTH_SHORT).show();
		}*/
    }

    private void ChangeMerchantStatus() {
        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();

        mHashMap.put(StaticConstants.JSON_MERCHANT_ID, PrefHelper.
                getStoredString(context, PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_ID));
        mHashMap.put(StaticConstants.JSON_MERCHANT_STATUS, "0");

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_ZAPNABIT_MERCHANT_STATUS, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);

    }
}
