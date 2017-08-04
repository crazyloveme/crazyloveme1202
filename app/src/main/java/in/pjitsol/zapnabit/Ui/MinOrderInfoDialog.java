package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.R;

/**
 * Created by Bhawna on 6/2/2017.
 */

public class MinOrderInfoDialog extends Dialog {

    private final String minValue;
    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;
    private TextView milesinfo;

    public MinOrderInfoDialog(Context context,String minValue) {
        super(context);
        this.context = context;
        this.minValue=minValue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.min_order_popup);
        btn_okotp = (Button) findViewById(R.id.btn_okotp);
        milesinfo = (TextView) findViewById(R.id.miniinfo);
        milesinfo.setText("This merchant requires a minimum order of "+StaticConstants.DOLLAR+minValue);

        btn_okotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();

            }
        });


    }

}

