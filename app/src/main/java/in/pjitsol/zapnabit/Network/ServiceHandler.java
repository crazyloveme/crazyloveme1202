package in.pjitsol.zapnabit.Network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

public class ServiceHandler {
	public final static int GET = 1;
	public final static int POST = 2;
	public static final int TIME_OUT_CONNECTION=50*1000;
    

	public static String makeServiceCall(String url, int method,
			List<NameValuePair> params) {
		String response = null;
		try {
			HttpClient httpClient = getHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;

			if (method == POST) {
				HttpPost httpPost = new HttpPost(url);
				if (params != null) {
					httpPost.setHeader("Accept","application/json");
					httpPost.setHeader("X-API-KEY","rONoXK4mdO8n6sMMgu3ADwk8ICZyDnix");
					httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
				}
				httpResponse = httpClient.execute(httpPost);
			} 
			else if (method == GET) { 
				if (params != null) {
					String paramString = URLEncodedUtils
							.format(params, "utf-8");
					url += "?" + paramString;
				}
 				HttpGet httpGet = new HttpGet(url);
				httpResponse = httpClient.execute(httpGet);
			}
			httpEntity = httpResponse.getEntity();
			response = EntityUtils.toString(httpEntity);

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}
	
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
}
