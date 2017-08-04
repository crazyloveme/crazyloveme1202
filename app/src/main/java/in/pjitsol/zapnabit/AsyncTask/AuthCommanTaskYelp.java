package in.pjitsol.zapnabit.AsyncTask;

import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Exception.SearchException;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Network.ZapnabitJsonParser;
import in.pjitsol.zapnabit.PlaceOrder.ParseProduct;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Util.Logger;

import java.util.HashMap;

import org.json.JSONException;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ProgressBar;

public class AuthCommanTaskYelp<Parems, Result> extends
AsyncTaskRunner<Parems, Object, Result> {
	String UserId = "";
	ZapnabitJsonParser jsonParser;
	String MEthodtype;
	HashMap<String, String> paramsHashmap = new HashMap<>();
	private int position;

	public AuthCommanTaskYelp(Context context,
			IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
			ProgressBar Progressloader, String methodtype) {
		super(context, Keyword, Progressloader, asyncTaskRunner);
		this.MEthodtype = methodtype;
	}

	public AuthCommanTaskYelp(Context context,
			IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
			String methodtype) {
		super(context, Keyword, asyncTaskRunner);
		this.MEthodtype = methodtype;
	}

	public AuthCommanTaskYelp(Context context,
			IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
			String UserId, String methodtype) {
		super(context, Keyword, asyncTaskRunner);
		this.UserId = UserId;
		this.MEthodtype = methodtype;
	}

	public AuthCommanTaskYelp(Context context,
			IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
			ProgressHUD Progressloader, String methodtype) {
		super(context, Keyword, Progressloader, asyncTaskRunner);
		this.MEthodtype = methodtype;
	}

	public AuthCommanTaskYelp(Context context,
			IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
			ProgressHUD Progressloader, String methodtype, int position) {
		super(context, Keyword, Progressloader, asyncTaskRunner);
		this.MEthodtype = methodtype;
		this.position = position;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Result doInBackground(Parems... params) {
		ResultMessage resultMessage = new ResultMessage();
		resultMessage.STATUS = StaticConstants.ASYN_NETWORK_FAIL;
		if (!BaseNetwork.obj().checkConnOnline(context))
			return (Result) resultMessage;

		resultMessage.TYPE = urlString;
		paramsHashmap = (HashMap<String, String>) params[0];
		jsonParser = new ZapnabitJsonParser();

		jsonString = BaseNetwork.obj().PostMethodWayYElp(context, urlString,
				(HashMap<String, String>) params[0], BaseNetwork.obj().TimeOut,
				this.MEthodtype);

		Logger.d("AuthLoginTask", "::" + jsonString);
		if (!TextUtils.isEmpty(jsonString)) {

			if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_YELP_TOKEN)) {

				jsonParser.ParseYElpToken(context, resultMessage, jsonString);
			} 
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_YELP_SEARCH)) {

				jsonParser.ParseYElpSearch(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_YELP_BUISENESS)) {

				jsonParser.ParseYElpBuiseness(context, resultMessage, jsonString);
			}
		} else {
			resultMessage.STATUS = SearchException.SERVER_DELAY;
			return (Result) resultMessage;
		}
		return (Result) resultMessage;
	}

	@Override
	protected void onProgressUpdate(Object... progress) {
		super.onProgressUpdate(progress);
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
	}

}
