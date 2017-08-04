package in.pjitsol.zapnabit.Ui;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class CongratulationDialog extends Dialog{

	Context context;
	String FromWhere;
	int position;
	public CongratulationDialog(Context context) 
	
	{
		super(context);
		this.context=context;
		
	}

	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);

		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	     setContentView(R.layout.congratulations_popup);
	     
	     Button yes=(Button)findViewById(R.id.deleteButton);
	    // Button no=(Button)findViewById(R.id.cancelButton);
	     
	    
	    	
	     
	     yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				PrefHelper.storeBoolean(context,
						PrefHelper.PREF_FILE_NAME,
						StaticConstants.JSON_MERCHANT_YELP_REGISTERED, true);

				((BaseFragmentActivity)context).UpdateAdapter();
				((BaseFragmentActivity)context).OnMerchantMyOrderFragment();
			
				//callBackFinish.ideleteConfirmation();
			}
			
		});
	     
	    /* no.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
			
			}
		});*/
	}
	
}
