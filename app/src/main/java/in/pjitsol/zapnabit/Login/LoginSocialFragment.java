package in.pjitsol.zapnabit.Login;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.LoginTypeDialog;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Util.TrackGPS;
import in.pjitsol.zapnabit.Util.Util;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginSocialFragment extends Fragment implements
        OnBackPressListener, OnClickListener,
        OnConnectionFailedListener, IAsyncTaskRunner, OnCancelListener {
    private GoogleApiClient mGoogleApiClient;
    private boolean signedInUser;
    private ConnectionResult mConnectionResult;

    /*double mLatitude=40.7565319;
    double mLongitude=-73.9723997;*/
    double mLatitude;
    double mLongitude;
    private LayoutInflater inflater;
    OnBackPressListener currentBackListener;
    private TextView text_login;
    private TextView text_signup;
    private FragmentActivity context;
    private TextView text_merchantlogin;
    private TextView text_location;
    private ImageView img_google;
    private boolean mIntentInProgress;
    private static final int RC_SIGN_IN = 0;

    private SharedPreferences mPrefs;
    private ImageView img_facebook;
    private ProgressHUD progressDialog;
    private String personName;
    private String email;
    private TextView text_website;
    //private LoginButton loginButton;
    private CallbackManager callbackManager;
    private TrackGPS gps;
    private int FROM_GOBACK;
    private Boolean IS_LOGGED_IN_VIA_GOOGLE;
    private Boolean IS_LOGGED_IN_VIA_FACEBOOK;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.login_social,
                container, false);

        FROM_GOBACK = getArguments().getInt(StaticConstants.FROM_GOBACK);

        this.inflater = inflater;
        currentBackListener = this;
        initView(view);
        return view;
    }

    private void initView(View view) {
        IS_LOGGED_IN_VIA_GOOGLE = getArguments().getBoolean(StaticConstants.IS_LOGGED_IN_VIA_GOOGLE);
        IS_LOGGED_IN_VIA_FACEBOOK = getArguments().getBoolean(StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK);
        text_location = (TextView) view.findViewById(R.id.text_location);
        text_login = (TextView) view.findViewById(R.id.text_login);
        text_signup = (TextView) view.findViewById(R.id.text_signup);
        text_website = (TextView) view.findViewById(R.id.text_website);
        text_merchantlogin = (TextView) view.findViewById(R.id.text_merchantlogin);
        img_google = (ImageView) view.findViewById(R.id.img_google);
        img_facebook = (ImageView) view.findViewById(R.id.img_facebook);
        text_login.setOnClickListener(this);
        text_signup.setOnClickListener(this);
        text_merchantlogin.setOnClickListener(this);
        img_google.setOnClickListener(this);
        img_facebook.setOnClickListener(this);
        text_website.setOnClickListener(this);

        GetLocation();

       /* if (!PrefHelper.getStoredBooleanG(context, PrefHelper.PREF_FILE_NAME,
                StaticConstants.HAS_PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= 23) {
                someMethod();
            } else
                checkPermission(context);
        } else
            GetLocation();*/



        boolean isGoogleReSession = PrefHelper.getStoredBooleanG(getActivity(),
                PrefHelper.PREF_FILE_NAME, StaticConstants.IS_LOGGED_IN_VIA_GOOGLE);

        boolean isFacebookLoginReSession = PrefHelper.getStoredBooleanG(getActivity(),
                PrefHelper.PREF_FILE_NAME, StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK);
        if (IS_LOGGED_IN_VIA_GOOGLE && isGoogleReSession) {
            callGoogleLoginSession();
        } else if (IS_LOGGED_IN_VIA_FACEBOOK && isFacebookLoginReSession) {
            callFacebookLoginSession();
        } else {
            if (!TextUtils.isEmpty(PrefHelper.getStoredString(getActivity(),
                    PrefHelper.PREF_FILE_NAME, StaticConstants.LAST_LOGIN_TYPE))
                    ) {

                if (PrefHelper.getStoredString(getActivity(),
                        PrefHelper.PREF_FILE_NAME, StaticConstants.LAST_LOGIN_TYPE).equalsIgnoreCase(
                        PrefHelper.getStoredString(getActivity(),
                                PrefHelper.PREF_FILE_NAME, StaticConstants.CURRENT_LOGIN_TYPE)
                )) {

                    PrefHelper.storeBoolean(context,
                            PrefHelper.PREF_FILE_NAME,
                            StaticConstants.CHANGE_LOGIN_TYPE_SHOWED_ONCE, true);
                } else {
                    PrefHelper.storeBoolean(context,
                            PrefHelper.PREF_FILE_NAME,
                            StaticConstants.CHANGE_LOGIN_TYPE_SHOWED_ONCE, false);
                }

                if (!PrefHelper.getStoredBoolean(context, PrefHelper.PREF_FILE_NAME, StaticConstants.CHANGE_LOGIN_TYPE_SHOWED_ONCE)) {
                    {

                        if (PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.LOGOUTTYPE)
                                .equalsIgnoreCase(StaticConstants.DRIVER))
                            new LoginTypeDialog(context).show();


                    }
                }


            }

        }
    }

   /* @Override
    public void onResume() {
        super.onResume();
        GetLocation();
    }*/

    private void GetLocation() {
        gps = new TrackGPS(context);
        if (gps.canGetLocation()) {
            mLongitude = gps.getLongitude();
            mLatitude = gps.getLatitude();
            if (mLatitude == 0) {
                GetLocation();
            } else
                text_location.setText("Current Location: " + Util.getcityName(mLatitude, mLongitude, context));


        } else {
            showSettingsAlert();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_login:
                Bundle bundle = new Bundle();
                bundle.putString(StaticConstants.USER_TYPE, StaticConstants.DRIVER);
                bundle.putString(StaticConstants.USER_LOGIN, StaticConstants.LOGIN);
                ((LoginSocialActivity) context).OnLoginBAseFragment(bundle);

                break;
            case R.id.text_signup:
                Bundle bundle1 = new Bundle();
                bundle1.putString(StaticConstants.USER_TYPE, StaticConstants.DRIVER);
                bundle1.putString(StaticConstants.USER_LOGIN, StaticConstants.SIGNUP);
                ((LoginSocialActivity) context).OnLoginBAseFragment(bundle1);
                break;
            case R.id.text_merchantlogin:
                Bundle bundle2 = new Bundle();
                bundle2.putString(StaticConstants.USER_TYPE, StaticConstants.MERCHANT);
                bundle2.putString(StaticConstants.USER_LOGIN, StaticConstants.LOGIN);
                ((LoginSocialActivity) context).OnLoginBAseFragment(bundle2);
                break;
            case R.id.img_google:
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .enableAutoManage(context, this)
                        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                        .build();
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
                break;
            case R.id.img_facebook:
                loginToFacebook();
                break;
            case R.id.text_website:
                Uri uri = Uri.parse("http://www.zapnabit.com"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    private void callGoogleLoginSession() {
        img_google.performClick();
    }

    private void callFacebookLoginSession() {
        img_facebook.performClick();
    }

    public void SignUpCall(String email, String name, String type) {

        progressDialog = ProgressHUD.show(context,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_USER_EMAIL, email
        );
        mHashMap.put(StaticConstants.JSON_USER_PASSWORD, "");
        mHashMap.put(StaticConstants.JSON_USER_NAME, name);
        mHashMap.put(StaticConstants.JSON_USER_CITY, "");
        mHashMap.put(StaticConstants.JSON_USER_PHONE, "");
        mHashMap.put(StaticConstants.JSON_USER_DEVICEID, PrefHelper.getStoredString(context,
                PrefHelper.PREF_FILE_NAME, StaticConstants.FCM_TOKEN));
        mHashMap.put(StaticConstants.JSON_REGISTEREDBY, type);

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                context, this,
                BaseNetwork.obj().KEY_SIGNUP_USER, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);
    }

    private void loginToFacebook() {

        callbackManager = CallbackManager.Factory.create();

        // Set permissions
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email", "user_photos", "public_profile"));

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        PrefHelper.storeBoolean(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.IS_LOGGED_IN_VIA_GOOGLE, false);

                        PrefHelper.storeBoolean(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK, true);

                        String lastloginType = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.CURRENT_LOGIN_TYPE);

                        if (TextUtils.isEmpty(lastloginType))
                            PrefHelper.storeString(context,
                                    PrefHelper.PREF_FILE_NAME,
                                    StaticConstants.LAST_LOGIN_TYPE, "last");
                        else
                            PrefHelper.storeString(context,
                                    PrefHelper.PREF_FILE_NAME,
                                    StaticConstants.LAST_LOGIN_TYPE, lastloginType);


                        PrefHelper.storeString(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.CURRENT_LOGIN_TYPE, StaticConstants.FB);


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
                                            String name = json.getString("name");
                                            String picture = json.getString("picture");
                                            String fburl = "https://graph.facebook.com/" + str_id + "/picture?type=large";
                                            PrefHelper.storeString(context,
                                                    PrefHelper.PREF_FILE_NAME, StaticConstants.FB_ID, str_id);
                                            PrefHelper.storeString(context,
                                                    PrefHelper.PREF_FILE_NAME, StaticConstants.USER_NAME, name);
                                            PrefHelper.storeString(context,
                                                    PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_IMAGE, fburl);
                                            String str_firstname = json.getString("name");
                                            SignUpCall(str_email, str_firstname, StaticConstants.FB);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,picture,location");
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

    public void UpdateGoogle(Intent data) {
        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            // Get account information
            String mFullName = acct.getDisplayName();
            String mEmail = acct.getEmail();
            Uri photoUrl = acct.getPhotoUrl();
            if (photoUrl != null)
                PrefHelper.storeString(context,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_IMAGE, photoUrl.toString());
            else
                PrefHelper.storeString(context,
                        PrefHelper.PREF_FILE_NAME, StaticConstants.JSON_USER_IMAGE, "");
            SignUpCall(mEmail, mFullName, StaticConstants.GOGGLE);

            PrefHelper.storeBoolean(context,
                    PrefHelper.PREF_FILE_NAME,
                    StaticConstants.IS_LOGGED_IN_VIA_GOOGLE, true);

            PrefHelper.storeBoolean(context,
                    PrefHelper.PREF_FILE_NAME,
                    StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK, false);


            String lastloginType = PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                    StaticConstants.CURRENT_LOGIN_TYPE);
            if (TextUtils.isEmpty(lastloginType))
                PrefHelper.storeString(context,
                        PrefHelper.PREF_FILE_NAME,
                        StaticConstants.LAST_LOGIN_TYPE, "last");
            else
                PrefHelper.storeString(context,
                        PrefHelper.PREF_FILE_NAME,
                        StaticConstants.LAST_LOGIN_TYPE, lastloginType);


            PrefHelper.storeString(context,
                    PrefHelper.PREF_FILE_NAME,
                    StaticConstants.CURRENT_LOGIN_TYPE, StaticConstants.GOGGLE);
            PrefHelper.storeString(context,
                    PrefHelper.PREF_FILE_NAME, StaticConstants.USER_NAME, mFullName);
        }
    }

    @Override
    public boolean onBackPressed() {
        /*Toast.makeText(context,"status chenged",Toast.LENGTH_LONG).show();
        Log.d("status","status change");*/
        return false;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.context = (FragmentActivity) activity;
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
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

    public void updatetext() {
        GetLocation();
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
        LoginManager.getInstance().logOut();
        //callGoogleLogout();
        startActivity(new Intent(context, BaseFragmentActivity.class).
                putExtra(StaticConstants.USER_TYPE, StaticConstants.DRIVER));
        context.finish();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            UpdateGoogle(data);

        } else if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (Settings.canDrawOverlays(context)) {
                checkPermission(context);
            }
            else
                checkPermission(context);
        } else
            callbackManager.onActivityResult(requestCode, resultCode, data);


    }


    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 123;
    public static int OVERLAY_PERMISSION_REQ_CODE = 1234;

    public void someMethod() {
        if (!Settings.canDrawOverlays(context)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + context.getPackageName()));
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (!Settings.canDrawOverlays(this)) {
                // SYSTEM_ALERT_WINDOW permission not granted...
            }
        }
    }*/
    public void checkPermission(final Context context) {

        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION))
            permissionsNeeded.add("GPS");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION))
            permissionsNeeded.add("Location");
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_NETWORK_STATE))
            permissionsNeeded.add("Network");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            permissionsNeeded.add("Sd card");
        if (!addPermission(permissionsList, Manifest.permission.READ_EXTERNAL_STORAGE))
            permissionsNeeded.add("Sd card");
        if (!addPermission(permissionsList, Manifest.permission.CALL_PHONE))
            permissionsNeeded.add("Call");
        if (!addPermission(permissionsList, Manifest.permission.CAMERA))
            permissionsNeeded.add("Camera");


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
                perms.put(Manifest.permission.ACCESS_NETWORK_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with results
                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted

                    PrefHelper.storeBoolean(context, PrefHelper.PREF_FILE_NAME,
                            StaticConstants.HAS_PERMISSIONS, true);
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


    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle("GPS Not Enabled");
        alertDialog.setMessage("Do you want to turn On GPS");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivityForResult(intent, 123);
            }
        });


        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        alertDialog.show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mGoogleApiClient!=null){
            mGoogleApiClient.stopAutoManage(getActivity());
            mGoogleApiClient.disconnect();
        }
    }

}


