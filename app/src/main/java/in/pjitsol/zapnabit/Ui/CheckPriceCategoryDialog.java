package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.R;

/**
 * Created by websnoox android on 4/20/2017.
 */

public class CheckPriceCategoryDialog  extends Dialog {

    private final IcallBackBarcode icallback;
    Context context;
    String FromWhere;
    int position;

    public CheckPriceCategoryDialog(Context context,IcallBackBarcode icallBackBarcode)

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
        setContentView(R.layout.check_price_and_cat);

        Button skip = (Button) findViewById(R.id.skipButton);
        Button call = (Button) findViewById(R.id.callnowButton);
        // Button no=(Button)findViewById(R.id.cancelButton);


        skip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();


                //callBackFinish.ideleteConfirmation();
            }

        });

        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                icallback.callbackBarcode();
               // ((BaseFragmentActivity) context).OnUserMyOrderFragment();
            }
        });
    }

}
