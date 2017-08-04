package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Yelp.ISdkCallback;

/**
 * Created by websnoox android on 3/9/2017.
 */

public class VerifyDialog extends Dialog {

    private final IVerifyCallback callback;
    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;

    public VerifyDialog(Context context, IVerifyCallback callback)

    {
        super(context);
        this.context = context;
        this.callback = callback;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.verifydialog);
        btn_okotp = (Button) findViewById(R.id.btn_okotp);
        btn_okotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                callback.VerifyCallback();
            }
        });


    }

}
