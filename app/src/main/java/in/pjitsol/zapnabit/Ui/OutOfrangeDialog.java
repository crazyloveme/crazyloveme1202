package in.pjitsol.zapnabit.Ui;

import in.pjitsol.zapnabit.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class OutOfrangeDialog extends Dialog {

    private final String type;
    Context context;
    String FromWhere;
    int position;
    private Button btn_okotp;
    private TextView milesinfo;

    public OutOfrangeDialog(Context context, String type) {
        super(context);
        this.context = context;
        this.type = type;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.outofrangedialog);
        btn_okotp = (Button) findViewById(R.id.btn_okotp);
        milesinfo = (TextView) findViewById(R.id.milesinfo);
       /* if(type.equalsIgnoreCase("distance"))
            milesinfo.setText(context.getResources().getString(R.string.outsiderange));
        else if(type.equalsIgnoreCase("menu"))
            milesinfo.setText(context.getResources().getString(R.string.menu));
        else if(type.equalsIgnoreCase("profile"))
            milesinfo.setText(context.getResources().getString(R.string.profile));*/
        btn_okotp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();

            }
        });


    }

}