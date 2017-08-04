package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import in.pjitsol.zapnabit.Merchant.InoticeCallback;
import in.pjitsol.zapnabit.R;

/**
 * Created by Bhawna on 6/1/2017.
 */

public class NoticeMerchantDialog extends Dialog {

    private final InoticeCallback callBack;
    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;
    private TextView milesinfo;

    public NoticeMerchantDialog(Context context, InoticeCallback callback) {
        super(context);
        this.context = context;
        this.callBack=callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.notice_merchantpopup);
        btn_okotp = (Button) findViewById(R.id.btn_okotp);
        milesinfo = (TextView) findViewById(R.id.milesinfo);

        btn_okotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                callBack.noticeCallback();

            }
        });


    }

}
