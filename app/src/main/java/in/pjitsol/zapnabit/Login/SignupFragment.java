package in.pjitsol.zapnabit.Login;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.AsyncTask.GetPlacesAsyncTask;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.Ui.CustomAutoCompleteTextView;
import in.pjitsol.zapnabit.Ui.ProgressHUD;
import in.pjitsol.zapnabit.Util.Util;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

public class SignupFragment extends Fragment implements 
OnBackPressListener,OnClickListener,IAsyncTaskRunner,OnCancelListener,
IcityInfo{
	
	private LayoutInflater inflater;
	private TextView text_signup;
	private Activity context;
	private ProgressHUD progressDialog;
	private EditText et_name;
	private EditText et_email;
	private EditText et_password;
	private EditText et_phone;
	private CustomAutoCompleteTextView et_city;
	private String USER_TYPE;
	private TextView text_goback;
	AuthCommanTask<HashMap<String, String>, ResultMessage> task;
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.signup,
				container, false);
		USER_TYPE=getArguments().getString(StaticConstants.USER_TYPE);
		this.inflater = inflater;
		initView(view);
		return view;
	}

	private void initView(View view) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/GothamLight.ttf");
		text_signup=(TextView)view.findViewById(R.id.text_signup);
		et_name=(EditText)view.findViewById(R.id.et_name);
		et_email=(EditText)view.findViewById(R.id.et_email);
		et_password=(EditText)view.findViewById(R.id.et_password);
		et_phone=(EditText)view.findViewById(R.id.et_phone);
		et_city=(CustomAutoCompleteTextView)view.findViewById(R.id.et_city);
		text_goback=(TextView)view.findViewById(R.id.text_goback);
		text_signup.setOnClickListener(this);
		text_goback.setOnClickListener(this);
		
		et_name.setTypeface(tf);
		et_email.setTypeface(tf);
		et_password.setTypeface(tf);
		et_phone.setTypeface(tf);
		et_city.setTypeface(tf);
		
		  et_city.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
			    InputMethodManager in = (InputMethodManager)context. getSystemService(Context.INPUT_METHOD_SERVICE);
			    in.hideSoftInputFromWindow(getView().getWindowToken(), 0);
			  }
			});
		
		 et_city.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				@Override
				public void afterTextChanged(Editable s) {
					
						if (s != null && !s.toString().equals("") && s.length()>3) {
							
							if (task == null) {
								if(Util.isNetworkAvailable(context))
									GEtAddress(s.toString());
							} else {
								task.cancel(true);
								if(Util.isNetworkAvailable(context))
									GEtAddress(s.toString());
							}
						} else {
							if (task != null) {
								task.cancel(true);
							}
						}
					
				}
			});
		
	}
	
	protected void GEtAddress(String string) {
		new GetPlacesAsyncTask(context, string,SignupFragment.this).execute();
		
	}

	@Override
	public boolean onBackPressed() {
		Bundle bundle=new Bundle();
		bundle.putInt(StaticConstants.FROM_GOBACK,1);
		((LoginSocialActivity)context).OnLoginSocialFragment(bundle);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_signup:
			progressDialog = ProgressHUD.show(context,
					getResources().getString(R.string.label_loading_refresh), true,true,this);
			HashMap<String, String> mHashMap = new HashMap<String, String>();
			mHashMap.put(StaticConstants.JSON_USER_EMAIL, et_email.getText().toString().trim()
					);
			mHashMap.put(StaticConstants.JSON_USER_PASSWORD,et_password.getText().toString().trim());
			mHashMap.put(StaticConstants.JSON_USER_NAME,et_name.getText().toString().trim());
			mHashMap.put(StaticConstants.JSON_USER_CITY,et_city.getText().toString().trim());
			mHashMap.put(StaticConstants.JSON_USER_PHONE,et_phone.getText().toString().trim());
			mHashMap.put(StaticConstants.JSON_USER_DEVICEID, PrefHelper.
					getStoredString(context,PrefHelper.PREF_FILE_NAME,StaticConstants.FCM_TOKEN));
			mHashMap.put(StaticConstants.JSON_REGISTEREDBY,StaticConstants.ZAPNABIT);

			AuthCommanTask<HashMap<String, String>, ResultMessage> task = new AuthCommanTask<HashMap<String, String>, ResultMessage>(
					context, SignupFragment.this,
					BaseNetwork.obj().KEY_SIGNUP_USER, progressDialog,StaticConstants.POST_METHOD);
			task.execute(mHashMap);
			PrefHelper.storeBoolean(context,
					PrefHelper.PREF_FILE_NAME,
					StaticConstants.IS_LOGGED_IN_VIA_GOOGLE, false);

			PrefHelper.storeBoolean(context,
					PrefHelper.PREF_FILE_NAME,
					StaticConstants.IS_LOGGED_IN_VIA_FACEBOOK, false);
			
			break;
		case R.id.text_goback:
			Bundle bundle=new Bundle();
			bundle.putInt(StaticConstants.FROM_GOBACK,1);
			((LoginSocialActivity)context).OnLoginSocialFragment(bundle);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.context=activity;
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskStarting() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskCompleted(Object result) {
		startActivity(new Intent(context,BaseFragmentActivity.class).
				putExtra(StaticConstants.USER_TYPE,USER_TYPE));
				
		context.finish();
		
	}

	@Override
	public void taskProgress(String urlString, Object progress) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void taskErrorMessage(Object result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Context getContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void gotCityinfo() {

		AutoCompleteAdapter adapter = new AutoCompleteAdapter(
				context,
				android.R.layout.simple_dropdown_item_1line,
				android.R.id.text1, StaticConstants.PlacesList);
		et_city.setAdapter(adapter);
		
	}
}
