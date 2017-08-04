package in.pjitsol.zapnabit.Login;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.ForgetPassword;
import in.pjitsol.zapnabit.Ui.ProgressHUD;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment implements
        OnBackPressListener, OnClickListener, IAsyncTaskRunner, OnCancelListener {

    private LayoutInflater inflater;
    private TextView text_signin;
    private Activity context;
    private String USER_TYPE;
    private ProgressHUD progressDialog;
    private EditText et_email;
    private EditText et_password;
    private TextView text_goback;
    private TextView txt_forgot;
    private RadioGroup RadioGroup_logintype;
    private String loginType;
    private RadioButton merchant;
    private RadioButton driver;


    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.signin,
                container, false);
        USER_TYPE = getArguments().getString(StaticConstants.USER_TYPE);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/GothamLight.ttf");
        txt_forgot = (TextView) view.findViewById(R.id.txt_forgot);
        text_signin = (TextView) view.findViewById(R.id.text_signin);
        text_signin.setOnClickListener(this);
        et_email = (EditText) view.findViewById(R.id.et_email);
        et_password = (EditText) view.findViewById(R.id.et_password);
        text_goback = (TextView) view.findViewById(R.id.text_goback);
        merchant = (RadioButton) view.findViewById(R.id.merchant);
        driver = (RadioButton) view.findViewById(R.id.driver);
        text_goback.setOnClickListener(this);
        txt_forgot.setOnClickListener(this);
        et_email.setTypeface(tf);
        et_password.setTypeface(tf);
        merchant.setTypeface(tf);
        driver.setTypeface(tf);

        RadioGroup_logintype = (RadioGroup) view.findViewById(R.id.RadioGroup_logintype);

        if (USER_TYPE.equalsIgnoreCase(StaticConstants.MERCHANT))
            RadioGroup_logintype.setVisibility(View.VISIBLE);
        else
            RadioGroup_logintype.setVisibility(View.GONE);

        RadioGroup_logintype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.merchant) {
                    loginType = "Merchant";

                } else if (checkedId == R.id.driver) {
                    loginType = "Driver";
                }
            }

        });


    }

    @Override
    public boolean onBackPressed() {
        Bundle bundle = new Bundle();
        bundle.putInt(StaticConstants.FROM_GOBACK, 1);
        ((LoginSocialActivity) context).OnLoginSocialFragment(bundle);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_signin:
                if (USER_TYPE.equalsIgnoreCase(StaticConstants.MERCHANT)) {
                    if (TextUtils.isEmpty(loginType))
                        Toast.makeText(context, "Please select login type.", Toast.LENGTH_SHORT).show();
                    else {
                        if (loginType.equalsIgnoreCase("Merchant")) {

                            progressDialog = ProgressHUD.show(context,
                                    getResources().getString(R.string.label_loading_refresh), true, true, this);
                            HashMap<String, String> mHashMap = new HashMap<String, String>();
                            mHashMap.put(StaticConstants.JSON_MERCHANT_EMAIL, et_email.getText().toString().trim()
                            );
                            mHashMap.put(StaticConstants.JSON_MERCHANT_PASSWORD, et_password.getText().toString().trim());
                            mHashMap.put(StaticConstants.JSON_USER_DEVICEID, PrefHelper.
                                    getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.FCM_TOKEN));
                            AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                                    context, LoginFragment.this,
                                    BaseNetwork.obj().KEY_LOGIN_MERCHANT, progressDialog, StaticConstants.POST_METHOD);
                            task.execute(mHashMap);

                        } else {

                            progressDialog = ProgressHUD.show(context,
                                    getResources().getString(R.string.label_loading_refresh), true, true, this);
                            HashMap<String, String> mHashMap = new HashMap<String, String>();
                            mHashMap.put(StaticConstants.JSON_DRIVER_EMAIL, et_email.getText().toString().trim()
                            );
                            mHashMap.put(StaticConstants.JSON_DRIVER_PASSWORD, et_password.getText().toString().trim());
                            mHashMap.put(StaticConstants.JSON_USER_DEVICEID, PrefHelper.
                                    getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.FCM_TOKEN));
                            AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                                    context, LoginFragment.this,
                                    BaseNetwork.obj().KEY_DRIVER_LOGIN, progressDialog, StaticConstants.POST_METHOD);
                            task.execute(mHashMap);
                        }
                        PrefHelper.storeBoolean(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.IS_LOGGED_IN_VIA_GOOGLE, false);

                        PrefHelper.storeBoolean(context,
                                PrefHelper.PREF_FILE_NAME,
                                StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK, false);
                    }


                } else {
                    progressDialog = ProgressHUD.show(context,
                            getResources().getString(R.string.label_loading_refresh), true, true, this);
                    HashMap<String, String> mHashMap = new HashMap<String, String>();
                    mHashMap.put(StaticConstants.JSON_USER_EMAIL, et_email.getText().toString().trim()
                    );
                    mHashMap.put(StaticConstants.JSON_USER_PASSWORD, et_password.getText().toString().trim());
                    mHashMap.put(StaticConstants.JSON_USER_DEVICEID, PrefHelper.
                            getStoredString(context, PrefHelper.PREF_FILE_NAME, StaticConstants.FCM_TOKEN));
                    AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                            context, LoginFragment.this,
                            BaseNetwork.obj().KEY_LOGIN_USER, progressDialog, StaticConstants.POST_METHOD);
                    task.execute(mHashMap);

                    PrefHelper.storeBoolean(context,
                            PrefHelper.PREF_FILE_NAME,
                            StaticConstants.IS_LOGGED_IN_VIA_GOOGLE, false);

                    PrefHelper.storeBoolean(context,
                            PrefHelper.PREF_FILE_NAME,
                            StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK, false);

                }


			/*startActivity(new Intent(context,BaseFragmentActivity.class).
                    putExtra(StaticConstants.USER_TYPE,USER_TYPE));
			context.finish();*/

                break;
            case R.id.text_goback:
                Bundle bundle = new Bundle();
                bundle.putInt(StaticConstants.FROM_GOBACK, 1);
                ((LoginSocialActivity) context).OnLoginSocialFragment(bundle);
                break;
            case R.id.txt_forgot:
                new ForgetPassword(context).show();
                break;

            default:
                break;
        }

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.context = activity;
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
        if (!TextUtils.isEmpty(loginType))
            if (loginType.equalsIgnoreCase("Driver")) {
                USER_TYPE = StaticConstants.ASSIGNED_DRIVER;
                PrefHelper.getStoredString
                        (context, PrefHelper.PREF_FILE_NAME, StaticConstants.USER_TYPE)
                        .equalsIgnoreCase(StaticConstants.ASSIGNED_DRIVER);
            }


        startActivity(new Intent(context, BaseFragmentActivity.class).
                putExtra(StaticConstants.USER_TYPE, USER_TYPE));

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
}
