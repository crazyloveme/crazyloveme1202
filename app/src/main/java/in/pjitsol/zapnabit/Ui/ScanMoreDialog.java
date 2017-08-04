package in.pjitsol.zapnabit.Ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.zxing.integration.android.IntentIntegrator;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;

/**
 * Created by websnoox android on 4/12/2017.
 */

public class ScanMoreDialog extends Dialog {

    Activity context;
    int position;
    private Button btn_okotp;
    private Button btn_ok;
    private Button btn_exit;

    public ScanMoreDialog(Context context)

    {
        super(context);
        this.context = (Activity) context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.scanmoredialog);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        btn_ok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                new IntentIntegrator(context).initiateScan();
            }
        });
        btn_exit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                ((BaseFragmentActivity)context).OnScanBArcodeFragment();
            }
        });


    }

}

