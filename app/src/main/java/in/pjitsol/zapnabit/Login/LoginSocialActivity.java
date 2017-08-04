package in.pjitsol.zapnabit.Login;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.ProgressHUD;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

public class LoginSocialActivity extends FragmentActivity implements OnClickListener,
        IAsyncTaskRunner, OnCancelListener, ConnectionCallbacks, OnConnectionFailedListener {
    OnBackPressListener currentBackListener;
    private ProgressHUD progressDialog;
    private Boolean IS_LOGGED_IN_VIA_GOOGLE;
    private Boolean IS_LOGGED_IN_VIA_FACEBOOK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_social_basefragment);
        IS_LOGGED_IN_VIA_GOOGLE = getIntent().getBooleanExtra(StaticConstants.IS_LOGGED_IN_VIA_GOOGLE,false);
        IS_LOGGED_IN_VIA_FACEBOOK = getIntent().getBooleanExtra(StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK,false);
        initview();
    }

    private void initview() {


        if (IS_LOGGED_IN_VIA_GOOGLE || IS_LOGGED_IN_VIA_FACEBOOK){
            Bundle bundle=new Bundle();
            bundle.putBoolean(StaticConstants.IS_LOGGED_IN_VIA_GOOGLE,IS_LOGGED_IN_VIA_GOOGLE);
            bundle.putBoolean(StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK,IS_LOGGED_IN_VIA_FACEBOOK);
            OnLoginSocialFragment(bundle);
        }
        else{
            OnLoginSocialFragment(new Bundle());
        }



    }

    public void OnLoginSocialFragment(Bundle bundle) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LoginSocialFragment login = new LoginSocialFragment();
        ft.replace(R.id.baseSocialLogincontainer, login, StaticConstants.LOGINSOCIAL_TAG);
        login.setArguments(bundle);
        currentBackListener = login;
        ft.commit();
    }

    public void OnLoginBAseFragment(Bundle bundle2) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        LoginBaseFragment login = new LoginBaseFragment();
        login.setArguments(bundle2);
        ft.replace(R.id.baseSocialLogincontainer, login, StaticConstants.LOGINSOCIAL_TAG);
        currentBackListener = login;
        ft.commit();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {
            Fragment fragmentcreate = getSupportFragmentManager().findFragmentById(
                    R.id.baseSocialLogincontainer);

            if (fragmentcreate != null) {
                if (fragmentcreate.getTag().toString()
                        .equalsIgnoreCase(StaticConstants.LOGINSOCIAL_TAG)) {

                    LoginSocialFragment textNotiffrgament = (LoginSocialFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.baseSocialLogincontainer);
                    textNotiffrgament.updatetext();
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }


    public void SignUpCall(String email, String name) {

        progressDialog = ProgressHUD.show(LoginSocialActivity.this,
                getResources().getString(R.string.label_loading_refresh), true, true, this);
        HashMap<String, String> mHashMap = new HashMap<String, String>();
        mHashMap.put(StaticConstants.JSON_USER_EMAIL, email
        );
        mHashMap.put(StaticConstants.JSON_USER_PASSWORD, "");
        mHashMap.put(StaticConstants.JSON_USER_NAME, name);
        mHashMap.put(StaticConstants.JSON_USER_CITY, "");
        mHashMap.put(StaticConstants.JSON_USER_PHONE, "");
        mHashMap.put(StaticConstants.JSON_USER_DEVICEID, "123");
        mHashMap.put(StaticConstants.JSON_REGISTEREDBY, StaticConstants.FB);

        AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                LoginSocialActivity.this, LoginSocialActivity.this,
                BaseNetwork.obj().KEY_SIGNUP_USER, progressDialog, StaticConstants.POST_METHOD);
        task.execute(mHashMap);
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

        //callFacebookLogout(LoginSocialActivity.this);


        startActivity(new Intent(LoginSocialActivity.this, BaseFragmentActivity.class).
                putExtra(StaticConstants.USER_TYPE, StaticConstants.DRIVER));
        LoginSocialActivity.this.finish();

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
    public void onConnected(Bundle connectionHint) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionSuspended(int cause) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // TODO Auto-generated method stub

    }

}
