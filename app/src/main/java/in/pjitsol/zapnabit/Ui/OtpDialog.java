package in.pjitsol.zapnabit.Ui;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Util.Util;
import in.pjitsol.zapnabit.Yelp.YelpEntity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class OtpDialog extends Dialog {

    private final IOtpCallback callBack;
    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;
    private EditText et_otp;
    private TextView resendotp;

    public OtpDialog(Context context, IOtpCallback callback)

    {
        super(context);
        this.context = context;
        this.callBack = callback;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.otpdialog);
        btn_okotp = (Button) findViewById(R.id.btn_okotp);
        resendotp = (TextView) findViewById(R.id.resendotp);
        et_otp = (EditText) findViewById(R.id.et_otp);
        et_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        et_otp.setText(PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                StaticConstants.MERCHANT_OTP));
        btn_okotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                if(et_otp.getText().toString().trim().equalsIgnoreCase(
                        PrefHelper.getStoredString(context, PrefHelper.PREF_FILE_NAME,
                                StaticConstants.MERCHANT_OTP)
                )){
                    dismiss();
                    callBack.Otpcallback(BaseNetwork.obj().KEY_SEND_OTP);
                }
                else{
                    et_otp.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.alert_red, 0);
                    Toast.makeText(context,"Invalid Otp",Toast.LENGTH_LONG).show();
                }

            }
        });

        resendotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dismiss();
                callBack.Otpcallback(BaseNetwork.obj().KEY_RESEND_OTP);


            }
        });


    }

}