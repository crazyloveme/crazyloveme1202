package in.pjitsol.zapnabit.Ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import in.pjitsol.zapnabit.R;

/**
 * Created by Bhawna on 6/1/2017.
 */

public class LoginFailureDialog extends Dialog {

    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;
    private TextView milesinfo;
    private TextView text_ok;

    public LoginFailureDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login_failure_dialog);

        text_ok=(TextView)findViewById(R.id.text_ok);
        text_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


    }

}
