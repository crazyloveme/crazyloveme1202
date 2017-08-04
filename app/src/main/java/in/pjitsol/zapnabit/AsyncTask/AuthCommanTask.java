package in.pjitsol.zapnabit.AsyncTask;

import in.pjitsol.zapnabit.Constants.PrefHelper;
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

/**
 * This is the task used for parsing data for every end point.
 * 
 * @author Bhawna Verma
 * 
 */
public class AuthCommanTask<Parems, Result> extends
AsyncTaskRunner<Parems, Object, Result> {
	String UserId = "";
	ZapnabitJsonParser jsonParser;
	String MEthodtype;
	HashMap<String, String> paramsHashmap = new HashMap<>();
	private int position;

	public AuthCommanTask(Context context,
			IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
			ProgressBar Progressloader, String methodtype) {
		super(context, Keyword, Progressloader, asyncTaskRunner);
		this.MEthodtype = methodtype;
	}

	public AuthCommanTask(Context context,
			IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
			String methodtype) {
		super(context, Keyword, asyncTaskRunner);
		this.MEthodtype = methodtype;
	}

	public AuthCommanTask(Context context,
			IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
			String UserId, String methodtype) {
		super(context, Keyword, asyncTaskRunner);
		this.UserId = UserId;
		this.MEthodtype = methodtype;
	}

	public AuthCommanTask(Context context,
			IAsyncTaskRunner<Object, Result> asyncTaskRunner, String Keyword,
			ProgressHUD Progressloader, String methodtype) {
		super(context, Keyword, Progressloader, asyncTaskRunner);
		this.MEthodtype = methodtype;
	}

	public AuthCommanTask(Context context,
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
		jsonString = BaseNetwork.obj().PostMethodWay(context, urlString,
				(HashMap<String, String>) params[0], BaseNetwork.obj().TimeOut,
				this.MEthodtype);

		Logger.d("AuthLoginTask", "::" + jsonString);
		if (!TextUtils.isEmpty(jsonString)) {

			if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_LOGIN_MERCHANT)) {

				PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME,
						StaticConstants.CALLING_FROM_LOGINMERCHANT,"1");

				jsonParser.ParseMerchantLoginInfo(context, resultMessage, jsonString);
			}
			if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_DRIVER_LOGIN)) {

				PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME,
						StaticConstants.CALLING_FROM_LOGINMERCHANT,"1");

				jsonParser.ParseDriverLoginInfo(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_LOGIN_USER)) {

				jsonParser.ParseUserLoginInfo(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_SIGNUP_USER)) {

				jsonParser.ParseRegisterInfo(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_MERCHANT_SDK_REGISTER)) {
				PrefHelper.storeString(context,
						PrefHelper.PREF_FILE_NAME,
						StaticConstants.MERCHANT_STATUS,
						"0");
				PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME,
						StaticConstants.CALLING_FROM_LOGINMERCHANT,"0");

				resultMessage.STATUS=StaticConstants.ASYN_RESULT_OK;
				resultMessage.TYPE=StaticConstants.MERCHANT_SDK_REGISTER;
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_ZAPNABIT_REGISTERED_RESTO_LIST)) {
				jsonParser.ParseZabnabitRegistredRestoList(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_ZAPNABIT_SEARCH_RESTO)) {
				jsonParser.ParseZabnabitRegistredRestoList(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_RESTO_GENERALSETTING)) {
				jsonParser.ParseRestoGeneralSetting(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_PAYMENT)) {
				resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
				resultMessage.TYPE=StaticConstants.PAYMENT_SUCCESSFUL;
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_FETCH_MENU)) {
				ParseProduct parseProduct = new ParseProduct();
				try {
					if (!TextUtils.isEmpty(jsonString)){
						parseProduct.parseMenu_item(context, jsonString, resultMessage);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_PLACE_ORDER)) {
				jsonParser.ParsePlaceOrder(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_USER_HISTORY)) {
				jsonParser.ParseUserHistory(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_MERCHANT_HISTORY)) {
				jsonParser.ParseMerchantHistory(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_DRIVER_HISTORY)) {
				jsonParser.ParseMerchantHistory(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_USER_ORDER_UPDATE)) {
				resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
				resultMessage.TYPE=StaticConstants.UPDATE_ORDER;
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_ZAPNABIT_MERCHANT_STATUS)) {
				PrefHelper.storeString(context,
						PrefHelper.PREF_FILE_NAME,
						StaticConstants.MERCHANT_STATUS,
								paramsHashmap.get(StaticConstants.JSON_MERCHANT_STATUS));
				resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
				resultMessage.TYPE=StaticConstants.UPDATE_MERCHANT_STATUS;
				resultMessage.Position=Integer.valueOf(
						paramsHashmap.get(StaticConstants.JSON_MERCHANT_STATUS));
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_SEND_OTP)) {
				jsonParser.ParseOtpInfo(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_RESEND_OTP)) {
				jsonParser.ParseOtpInfo(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_EDIT_PROFILE)) {
				jsonParser.ParseUserEditInfo(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_CHANGE_PASS)) {
				jsonParser.ParseChangePassInfo(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_FORGET_PASS)) {
				jsonParser.ParseForgetPassInfo(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_WITHOUT_YELP)) {
				jsonParser.ParseOtpInfo(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_SCAN_QRCODE)) {
				jsonParser.ParseScanQrcode(context, resultMessage, jsonString);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_ADD_ADDRESS)) {
				jsonParser.ParseAddress(context, resultMessage, jsonString,paramsHashmap);
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_SAVE_QRCODE_INFO_IN_MENU)) {
				resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
				resultMessage.TYPE = StaticConstants.SAVE_QRCODE_IN_MENU;
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_SAVE_QRCODE_INFO_IN_TEMP)) {
				resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
				resultMessage.TYPE = StaticConstants.SAVE_QRCODE_IN_TEMP;
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_SAVE_PHOTO_INFO_IN_TEMP)) {
				resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_USER_FAVOURITE)) {
				resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
				resultMessage.TYPE = StaticConstants.USER_FAVOURITE;
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_ASSIGN_DRIVER)) {
				resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
				resultMessage.TYPE = StaticConstants.ASSIGN_DRIVER;
			}
			else if (urlString.equalsIgnoreCase(BaseNetwork.obj().KEY_USER_UNFAVOURITE)) {
				resultMessage.STATUS = StaticConstants.ASYN_RESULT_OK;
				resultMessage.TYPE = StaticConstants.USER_UNFAVOURITE;
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
