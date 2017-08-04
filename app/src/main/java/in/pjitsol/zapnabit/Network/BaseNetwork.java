package in.pjitsol.zapnabit.Network;

import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Login.PlaceInfo;
import in.pjitsol.zapnabit.Util.Logger;
import in.pjitsol.zapnabit.Util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class BaseNetwork {

//	public final String URL_HOST = "http://dev.ikkapower.com/zapnabit/admin/api/launcher/";
//	public final String URL_HOST = "http://zapnabit.com/zapnabit/admin/api/launcher/";
	public final String URL_HOST = "http://zapnabit.com/app/admin/api/launcher/";
	public final String URL_HOST_YELP = "https://api.yelp.com/oauth2/token";
	public final String URL_HOST_YELP_BUISENESS = "https://api.yelp.com/v3/businesses/";
	public final String URL_HOST_YELP_SEARCH = "https://api.yelp.com/v3/businesses/search?";
	public final String KEY_YELP_TOKEN = "token";
	public final String KEY_YELP_SEARCH = "search";
	public final String KEY_YELP_BUISENESS = "buiseness";
	public final String KEY_LOGIN_MERCHANT = "login_merchant";
	public final String KEY_MERCHANT_SDK_REGISTER = "merchant_sdk_register";
	public final String KEY_LOGIN_USER = "user_login";
	public final String KEY_SIGNUP_USER = "user_signup";
	public final String KEY_ZAPNABIT_REGISTERED_RESTO_LIST = "registered_restaurants_list";
	public final String KEY_RESTO_GENERALSETTING = "general_setting";
	public  final String KEY_FETCH_MENU = "fetch_menu";
	public  final String KEY_PLACE_ORDER = "place_order";
	public  final String KEY_SEND_OTP = "send_otp";
	public  final String KEY_RESEND_OTP = "resend_otp";
	public  final String KEY_USER_HISTORY = "user_history";
	public  final String KEY_MERCHANT_HISTORY = "merchant_history";
	public  final String KEY_USER_ORDER_UPDATE = "update_order";
	public final String KEY_ZAPNABIT_SEARCH_RESTO = "registered_single_restaurant";
	public final String KEY_ZAPNABIT_MERCHANT_STATUS = "change_status";
	public  final String KEY_EDIT_PROFILE = "edit_profile";
	public  final String KEY_CHANGE_PASS = "change_password";
	public  final String KEY_FORGET_PASS = "forget_password";
	public static final String KEY_WITHOUT_YELP = "without_yelp";
	public static final String KEY_SCAN_QRCODE= "scan_qrcode";
	public static final String KEY_SAVE_QRCODE_INFO_IN_MENU= "save_qrcode_info_in_menu";
	public static final String KEY_SAVE_QRCODE_INFO_IN_TEMP= "save_qrcode_info_in_temp";
	public static final String KEY_SAVE_PHOTO_INFO_IN_TEMP= "save_photo_info_in_temp";
	public final String KEY_USER_FAVOURITE = "user_favourite";
	public final String KEY_USER_UNFAVOURITE = "user_unfavourite";
	public  final String KEY_ADD_ADDRESS = "add_address";
	public  final String KEY_ASSIGN_DRIVER = "assign_order";
	public  final String KEY_DRIVER_LOGIN = "driver_login";
	public  final String KEY_DRIVER_HISTORY = "driver_order_listing";
	public  final String KEY_PAYMENT = "payment";

	private static BaseNetwork obj = null;
	public int TimeOut = 5000;

	public synchronized static BaseNetwork obj() {
		if (obj == null)
			obj = new BaseNetwork();
		return obj;
	}

	public boolean checkConnOnline(Context _context) {
		ConnectivityManager conMgr = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (conMgr != null) {
			NetworkInfo i = conMgr.getActiveNetworkInfo();
			if (i == null) {
				return false;
			}
			if (!i.isConnected()) {
				return false;
			}
			if (!i.isAvailable()) {
				return false;
			}
			return true;
		} else
			return false;
	}

	public String PostMethodWay(Context context, String KeyWord,
			HashMap<String, String> map, int timeOut,String methodtype) {
		String hostName = URL_HOST + KeyWord;
		HttpClient httpClient = getHttpClient();
		HttpEntity httpEntity = null;
		HttpResponse httpResponse = null;
		HttpPost httpPost = new HttpPost(hostName);
		String response_str = "";
		try {

			if(methodtype.equalsIgnoreCase(StaticConstants.POST_METHOD)){
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				for (String key : map.keySet()) {
					postParameters.add(new BasicNameValuePair(key, map.get(key)));
					Logger.i("key"+key,"map"+ map.get(key));
				}
				httpPost.setHeader("api_key","zapnabit#rONoXK4mdO8n6sMMgu3ADwk8ICZyDnix");
				httpPost.setEntity(new UrlEncodedFormEntity(postParameters,"UTF-8"));
				httpResponse = httpClient.execute(httpPost);
			}
			else if(methodtype.equalsIgnoreCase(StaticConstants.GET_METHOD)){
				String url=GenrateUrl(KeyWord, map);
				url=url.replaceAll(" ", "%20");
				HttpGet httpGet = new HttpGet(url);

				httpResponse = httpClient.execute(httpGet);

			}
			else if(methodtype.equalsIgnoreCase(StaticConstants.DELETE_METHOD)){
				String url=GenrateUrl(KeyWord, map);
				HttpDelete httpGet = new HttpDelete(url);

				httpResponse = httpClient.execute(httpGet);

			}
			httpEntity = httpResponse.getEntity();
			response_str = EntityUtils.toString(httpEntity);
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response_str;
	}




	public String PostMethodWayYElp(Context context, String KeyWord,
			HashMap<String, String> map, int timeOut,String methodtype) {
		String hostName;
		if(KeyWord.equalsIgnoreCase(KEY_YELP_TOKEN))
			hostName = URL_HOST_YELP;
		else if(KeyWord.equalsIgnoreCase(KEY_YELP_SEARCH))
			hostName = URL_HOST_YELP_SEARCH;
		else
			hostName = URL_HOST_YELP_BUISENESS;
		HttpClient httpClient = createHttpClient();
		HttpEntity httpEntity = null;
		HttpResponse httpResponse = null;
		HttpPost httpPost = new HttpPost(hostName);
		String response_str = "";
		try {

			if(methodtype.equalsIgnoreCase(StaticConstants.POST_METHOD)){
				List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
				for (String key : map.keySet()) {
					postParameters.add(new BasicNameValuePair(key, map.get(key)));
					Logger.i("key"+key,"map"+ map.get(key));
				}
				httpPost.setEntity(new UrlEncodedFormEntity(postParameters,"UTF-8"));
				httpResponse = httpClient.execute(httpPost);
			}
			else if(methodtype.equalsIgnoreCase(StaticConstants.GET_METHOD)){
				if(KeyWord.equalsIgnoreCase(KEY_YELP_BUISENESS)){

					String url=hostName+map.get(StaticConstants.JSON_YELP_TERM);
					url=url.replaceAll(" ", "%20");
					HttpGet httpGet = new HttpGet(url);
					httpGet.setHeader("Authorization","Bearer "+PrefHelper
							.getStoredString(context, PrefHelper.PREF_FILE_NAME,
									StaticConstants.USER_ACESSTOKEN));
					httpResponse = httpClient.execute(httpGet);
				}
				else{
					String url=GenrateUrl(KeyWord, map);
					url=url.replaceAll(" ", "%20");
					HttpGet httpGet = new HttpGet(url);
					httpGet.setHeader("Authorization","Bearer "+PrefHelper
							.getStoredString(context, PrefHelper.PREF_FILE_NAME,
									StaticConstants.USER_ACESSTOKEN));
					httpResponse = httpClient.execute(httpGet);
				}


			}
			else if(methodtype.equalsIgnoreCase(StaticConstants.DELETE_METHOD)){
				String url=GenrateUrl(KeyWord, map);
				HttpDelete httpGet = new HttpDelete(url);

				httpResponse = httpClient.execute(httpGet);

			}
			httpEntity = httpResponse.getEntity();
			response_str = EntityUtils.toString(httpEntity);
		} catch (SocketException se) {
			se.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response_str;
	}
	public static final int TIME_OUT_CONNECTION=50*1000;

	public static HttpClient getHttpClient(){
		HttpClient mhttpclient=null;
		if(mhttpclient==null){
			mhttpclient=new DefaultHttpClient(); 
			final HttpParams mhttpparams=mhttpclient.getParams();
			HttpConnectionParams.setSoTimeout(mhttpparams, TIME_OUT_CONNECTION);
			HttpConnectionParams.setConnectionTimeout(mhttpparams, TIME_OUT_CONNECTION);
		}
		return mhttpclient;
	}
	
	public static HttpClient createHttpClient()
	{
	    HttpParams params = new BasicHttpParams();
	    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
	    HttpProtocolParams.setContentCharset(params, HTTP.DEFAULT_CONTENT_CHARSET);
	    HttpProtocolParams.setUseExpectContinue(params, true);

	    SchemeRegistry schReg = new SchemeRegistry();
	    schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
	    schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
	    ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);

	    return new DefaultHttpClient(conMgr, params);
	}

	public void showErrorNetworkDialog(final Context c, String message) {
		View layout = ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
				inflate(R.layout.dialog_layout, null, false);
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setView(layout);
		Button btn = (Button) layout.findViewById(R.id.btn_ok);
		TextView text = (TextView) layout.findViewById(R.id.text_message);
		text.setText(message);
		final AlertDialog alert = builder.create();
		alert.show();
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.cancel();
			}
		});
		/*AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setMessage(message).setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();*/
	}

	/*public void showConfirmationDialog(final Context c, String message,final ISendConfirm senconfirm) {
		View layout = ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
				inflate(R.layout.dialog_layout, null, false);
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setView(layout);
		Button btn = (Button) layout.findViewById(R.id.btn_ok);
		TextView text = (TextView) layout.findViewById(R.id.text_message);
		text.setText(message);
		final AlertDialog alert = builder.create();
		alert.show();
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				alert.cancel();
				senconfirm.ISendConfirmation("Cancel");
			}
		});

		AlertDialog.Builder builder = new AlertDialog.Builder(c);

		builder.setMessage(message).setPositiveButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
				senconfirm.ISendConfirmation("Cancel");
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}*/


	public void showSingleConfirmationDialog(final Context c, String message) {
		View layout = ((LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).
				inflate(R.layout.dialog_layout, null, false);
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setView(layout);
		Button btn = (Button) layout.findViewById(R.id.btn_ok);
		TextView text = (TextView) layout.findViewById(R.id.text_message);
		text.setText(message);
		final AlertDialog alert = builder.create();
		alert.show();
		btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				alert.cancel();
			}
		});

		builder.setMessage(message).setNegativeButton("Ok",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});


	}

	public String getGoogleMap(String milesUrl) {
		InputStream is = null;
		// Making HTTP request
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(milesUrl.replace(" ", "%20"));
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			String responce = sb.toString();
			// System.out.println("responce is--------------> " + responce);
			return responce;
			// System.out.println("length of json array is --------------> "+array.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String GenrateUrl(String action,
			HashMap<String, String> mHashMap) {
		String url = URL_HOST_YELP_SEARCH;
		String containpart = "";
		try {

			for (String key : mHashMap.keySet()) {
				Logger.i(":: " + key, ":: " + mHashMap.get(key));
				if (!TextUtils.isEmpty(containpart)){
					String value = mHashMap.get(key);
					String encoded = URLEncoder.encode(value, "UTF-8");
					containpart += "&" + key + "=" + encoded;
				}

				else{
					String value = mHashMap.get(key);

					String encoded = URLEncoder.encode(value, "UTF-8");
					containpart = key + "=" + encoded;
				}

			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url + containpart;

	}



	public static Object postData(String addr)
			throws JSONException, ClientProtocolException, IOException {
		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+addr+"&types=(cities)&language=en&key=AIzaSyAuuYp3XQ4ButKc6G4zpyujeR0BiyG-sxY");
		JSONObject json = new JSONObject();

		System.out.print(json);
		HttpResponse response = httpclient.execute(httppost);

		String str = "";
		// for JSON:
		if (response != null) {
			InputStream is = response.getEntity().getContent();

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			str = sb.toString();
			Log.i("data", str);

		}
		JSONObject jsonPredictions= new JSONObject(str);
		if(jsonPredictions!=null && 
				jsonPredictions.has("status")){
			if (Util.CheckJsonIsEmpty(jsonPredictions,
					"status")
					.equalsIgnoreCase("OK")){
				StaticConstants.PlacesList.clear();
				JSONArray jsonpredictions=jsonPredictions.getJSONArray("predictions");
				for(int i=0;i<jsonpredictions.length();i++){
					PlaceInfo item=new PlaceInfo();
					JSONObject jsonObject=jsonpredictions.getJSONObject(i);
					String discription=jsonObject.getString("description");
					item.placeName=discription;
					StaticConstants.PlacesList.add(item);
				}
			}
		}


		return "true";

	}




}
