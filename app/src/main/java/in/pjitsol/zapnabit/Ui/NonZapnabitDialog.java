package in.pjitsol.zapnabit.Ui;

import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Yelp.YelpEntity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class NonZapnabitDialog extends Dialog{

	Context context;
	String FromWhere;
	int position;
	private Button btn_okotp;
	private YelpEntity yelpitem;
	public NonZapnabitDialog(Context context,YelpEntity item) 
	
	{
		super(context);
		this.context=context;
		yelpitem=item;
		
	}

	@Override  
	protected void onCreate(Bundle savedInstanceState) {  
		super.onCreate(savedInstanceState);

		getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	     setContentView(R.layout.nonzapnabitdialog);
	     btn_okotp=(Button)findViewById(R.id.btn_okotp);
	     btn_okotp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dismiss();
				context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", yelpitem.Phone, null)));
				
			}
		});
	     
	    
	}
	
}