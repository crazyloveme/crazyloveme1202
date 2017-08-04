package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import in.pjitsol.zapnabit.Util.Util;

/**
 * Created by websnoox android on 2/22/2017.
 */

public class ForgetPassword  extends Dialog implements View.OnClickListener ,
        IAsyncTaskRunner,DialogInterface.OnCancelListener{

    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;
    private EditText et_emailid;
    private ProgressHUD progressDialog;

    public ForgetPassword(Context context)

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
        setContentView(R.layout.forget_password);
        btn_okotp = (Button) findViewById(R.id.btn_okotp);
        et_emailid = (EditText) findViewById(R.id.et_emailid);
        btn_okotp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_okotp:

                if (Util.isNetworkAvailable(context)) {
                    progressDialog = ProgressHUD.show(context,
                            context.getResources().getString(R.string.label_loading_refresh), true, true, this);
                    HashMap<String, String> mHashMap = new HashMap<String, String>();

                    mHashMap.put(StaticConstants.JSON_USER_EMAIL, et_emailid.getText().toString().trim());
                    @SuppressWarnings("unchecked")
                    AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
                            context, ForgetPassword.this,
                            BaseNetwork.obj().KEY_FORGET_PASS, progressDialog, StaticConstants.POST_METHOD);
                    task.execute(mHashMap);
                } else {
                    Toast.makeText(context, "Please check your internet connection", Toast.LENGTH_SHORT).show();
                }
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
        Toast.makeText(context,"We have sent you an email on your registered email id.",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void taskProgress(String urlString, Object o) {

    }

    @Override
    public void taskErrorMessage(Object o) {

    }
}