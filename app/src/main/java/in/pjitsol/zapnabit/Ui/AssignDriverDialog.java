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

import static in.pjitsol.zapnabit.R.styleable.View;

/**
 * Created by Bhawna on 6/6/2017.
 */

public class AssignDriverDialog extends Dialog {

    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;
    private TextView milesinfo;

    public AssignDriverDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.assign_driverdialog);
        btn_okotp = (Button) findViewById(R.id.btn_okotp);

        btn_okotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();

            }
        });


    }

}

