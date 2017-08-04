package in.pjitsol.zapnabit.Ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.R;

/**
 * Created by websnoox android on 3/23/2017.
 */

public class OrderPlacedDialog extends Dialog {

    Context context;
    String FromWhere;
    int position;

    public OrderPlacedDialog(Context context)

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
        setContentView(R.layout.order_placed_dialog);

        Button skip = (Button) findViewById(R.id.skipButton);
        Button call = (Button) findViewById(R.id.callnowButton);
        // Button no=(Button)findViewById(R.id.cancelButton);


        skip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                ((BaseFragmentActivity) context).OnUserMyOrderFragment();

                //callBackFinish.ideleteConfirmation();
            }

        });

        call.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                ((BaseFragmentActivity) context).OnUserMyOrderFragment();
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + PrefHelper.getStoredString(context,PrefHelper.PREF_FILE_NAME,
                        StaticConstants.MERCHANT_PHONE)));
                context.startActivity(callIntent);
               /* if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }*/


			}
		});
    }

}