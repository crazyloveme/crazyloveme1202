package in.pjitsol.zapnabit.Ui;

import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DeleteConfirmationDialog  extends Dialog{

	Context context;
	IDeleteConfirmation callBackFinish;
	String FromWhere;
	int position;
	public DeleteConfirmationDialog(Context context,IDeleteConfirmation  callBackFinish)
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
	     setContentView(R.layout.delete_confirmation_popup);
	     
	     Button yes=(Button)findViewById(R.id.deleteButton);
	     Button no=(Button)findViewById(R.id.cancelButton);
	     TextView deleteSureText = (TextView)findViewById(R.id.deleteSureText);
	     
	    
	     
	     yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			
				callBackFinish.ideleteConfirmation();
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
