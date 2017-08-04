package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wallet.FullWallet;
import com.google.android.gms.wallet.MaskedWallet;
import com.google.android.gms.wallet.fragment.SupportWalletFragment;

import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.R;

/**
 * Created by Bhawna on 6/30/2017.
 */

public class PaymentOptionDialog extends Dialog {

    private final IPaymentType callback;
    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;
    private TextView milesinfo;
    private RadioGroup RadioGroup_logintype;
    private String paymentType;



    public PaymentOptionDialog(Context context,IPaymentType type) {
        super(context);
        this.context = context;
        this.callback=type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.paymentoptiondialog);
        btn_okotp = (Button) findViewById(R.id.btn_okotp);
        RadioGroup_logintype = (RadioGroup)findViewById(R.id.RadioGroup_logintype);

        RadioGroup_logintype.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.paypal) {
                    paymentType = StaticConstants.PAYPAL;

                } else if (checkedId == R.id.card) {
                    paymentType = StaticConstants.CARD;
                }
                else{
                    paymentType = StaticConstants.ANDROIDPAY;
                }
            }

        });


        btn_okotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(paymentType)){
                    Toast.makeText(context,"Please select Payment Option.",Toast.LENGTH_SHORT).show();
                }
                else{
                    dismiss();
                    callback.PaymentType(paymentType);
                }



            }
        });


    }

}
