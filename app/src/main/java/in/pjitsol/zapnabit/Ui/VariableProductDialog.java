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
 * Created by Bhawna on 5/23/2017.
 */

public class VariableProductDialog extends Dialog {

    Context context;
    IDeleteVaribaleProduct callBackFinish;
    String FromWhere;
    int position;
    public VariableProductDialog(Context context,IDeleteVaribaleProduct  callBackFinish)
    {
        super(context);
        this.callBackFinish=callBackFinish;
        this.context=context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.variable_productinfo);

        Button yes=(Button)findViewById(R.id.deleteButton);
        Button no=(Button)findViewById(R.id.cancelButton);
        TextView deleteSureText = (TextView)findViewById(R.id.deleteSureText);



        yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();

                callBackFinish.IdeleteVariableProduct();
            }

        });

        no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();

            }
        });
    }


}
