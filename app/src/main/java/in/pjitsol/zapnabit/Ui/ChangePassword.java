package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.User.EditProfileFragment;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

/**
 * Created by websnoox android on 2/21/2017.
 */

public class ChangePassword extends Dialog implements View.OnClickListener,
        IAsyncTaskRunner, DialogInterface.OnCancelListener {

    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;
    private EditText et_oldpass;
    private EditText et_newpass;
    private EditText et_confirmnewpass;
    private ProgressHUD progressDialog;

    public ChangePassword(Context context)

    {
        super(context);
        this.context = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.change_pass);
        btn_okotp = (Button) findViewById(R.id.btn_okotp);
        et_oldpass = (EditText) findViewById(R.id.et_oldpass);
        et_newpass = (EditText) findViewById(R.id.et_newpass);
        et_confirmnewpass = (EditText) findViewById(R.id.et_confirmnewpass);
        btn_okotp.setOnClickListener(this);
        et_oldpass.setOnClickListener(this);
        et_newpass.setOnClickListener(this);
        et_confirmnewpass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_okotp:

                if ((!TextUtils.isEmpty(et_oldpass.getText().toString()))
                        && (!TextUtils.isEmpty(et_newpass.getText().toString()))
                        && (!TextUtils.isEmpty(et_confirmnewpass.getText().toString()))){

                    if(et_newpass.getText().toString().equalsIgnoreCase(et_confirmnewpass.getText().toString())){
                        if (Util.isNetworkAvailable(context)) {
                            progressDialog = ProgressHUD.show(context,
                                    context.getResources().getString(R.string.label_loading_refresh), true, true, this);
                            HashMap<String, String> mHashMap = new HashMap<String, String>();
                            mHashMap.put(StaticConstants.JSON_USER_ID, PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                    StaticConstants.USER_ID));
                            mHashMap.put(StaticConstants.JSON_USER_OLDPASSWORD, et_oldpass.getText().toString().trim());
                            mHashMap.put(StaticConstants.JSON_USER_NEWPASSWORD, et_newpass.getText().toString().trim());
                            @SuppressWarnings("unchecked")
                            AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                                    context, ChangePassword.this,
                                    BaseNetwork.obj().KEY_CHANGE_PASS, progressDialog, StaticConstants.POST_METHOD);
                            task.execute(mHashMap);
                        } else {
                            Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                        Toast.makeText(context, "Password did not match.", Toast.LENGTH_SHORT).show();
            }
            else
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show();


            dismiss();
            break;

        }

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void taskStarting() {

    }

    @Override
    public void taskCompleted(Object o) {
        Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void taskProgress(String urlString, Object o) {

    }

    @Override
    public void taskErrorMessage(Object o) {

    }
}