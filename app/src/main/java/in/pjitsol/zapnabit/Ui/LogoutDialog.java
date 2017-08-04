package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;

/**
 * Created by websnoox android on 4/26/2017.
 */

public class LogoutDialog extends Dialog {

    private final IcallBackBarcode icallback;
    Context context;
    String FromWhere;
    int position;

    public LogoutDialog(Context context,IcallBackBarcode icallBackBarcode)

    {
        super(context);
        this.context = context;
        this.icallback=icallBackBarcode;


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.logout_popup);

        Button skip = (Button) findViewById(R.id.skipButton);
        Button call = (Button) findViewById(R.id.callnowButton);
        // Button no=(Button)findViewById(R.id.cancelButton);


        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                icallback.callbackBarcode();
                ((BaseFragmentActivity) context).OnUserMyOrderFragment();

                //callBackFinish.ideleteConfirmation();
            }

        });

        skip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}

