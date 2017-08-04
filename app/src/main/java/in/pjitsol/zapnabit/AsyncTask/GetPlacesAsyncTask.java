package in.pjitsol.zapnabit.AsyncTask;

import in.pjitsol.zapnabit.Login.IcityInfo;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

public class GetPlacesAsyncTask extends AsyncTask<String, String, String> implements OnCancelListener {
	private Context context;
	String city;
	String currentzip;
	String Miles;
	private IcityInfo ICityInfo;
	public GetPlacesAsyncTask(Context context,String city, IcityInfo signUpFragment) {
		this.context=context;
		this.city=city;
		this.ICityInfo=signUpFragment;
		//mDialog = new CustomAsynctaskLoader(context);

	}

	@SuppressWarnings("static-access")
	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		//mDialog.ShowDialog();
	}

	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub

		try {
			 Miles=(String) BaseNetwork.postData(city);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Miles;
	}

	@Override
	protected void onPostExecute(String result) {
		
		//mDialog.DismissDialog();
		
		ICityInfo.gotCityinfo();

	}

	@Override
	public void onCancel(DialogInterface arg0) {
		// TODO Auto-generated method stub
		
	}

}
