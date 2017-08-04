package in.pjitsol.zapnabit.Login;

import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginBaseFragment extends Fragment implements OnBackPressListener,OnClickListener{

	private LayoutInflater inflater;
	OnBackPressListener currentBackListener;
	private TextView text_signin;
	private Activity context;
	private TextView text_merchantlogin;
	private TextView text_signup;
	private View view1;
	private View view2;
	private String USER_TYPE;
	private LinearLayout linear_signinsignup;
	private String USER_LOGIN;
	private TextView text_satticlogininfo;
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		final View view = inflater.inflate(R.layout.login_basefragment,
				container, false);

		this.inflater = inflater;
		currentBackListener=this;
		USER_TYPE=getArguments().getString(StaticConstants.USER_TYPE);
		USER_LOGIN=getArguments().getString(StaticConstants.USER_LOGIN);

		initView(view);
		return view;
	}

	private void initView(View view) {
		linear_signinsignup=(LinearLayout)view.findViewById(R.id.linear_signinsignup);
		text_signin=(TextView)view.findViewById(R.id.text_signin);
		text_signup=(TextView)view.findViewById(R.id.text_signup);
		text_satticlogininfo=(TextView)view.findViewById(R.id.text_satticlogininfo);
		view1=(View)view.findViewById(R.id.view1);
		view2=(View)view.findViewById(R.id.view2);
		text_signin.setOnClickListener(this);
		text_signup.setOnClickListener(this);


		if(USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER)){
			PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME,StaticConstants.USER_TYPE,StaticConstants.DRIVER);
			text_satticlogininfo.setVisibility(View.INVISIBLE);
			linear_signinsignup.setVisibility(View.VISIBLE);
		}
		else{
			PrefHelper.storeString(context,PrefHelper.PREF_FILE_NAME,StaticConstants.USER_TYPE,StaticConstants.DRIVER);
			text_satticlogininfo.setVisibility(View.VISIBLE);
			linear_signinsignup.setVisibility(View.INVISIBLE);
		}


		if(USER_LOGIN.equalsIgnoreCase(StaticConstants.LOGIN)){
			OnLoginFragment();
			text_signup.setTextColor(getResources().getColor(R.color.lines));
			text_signin.setTextColor(getResources().getColor(R.color.red));
			view2.setBackgroundColor(getResources().getColor(R.color.lines));
			view1.setBackgroundColor(getResources().getColor(R.color.red));
		}
		else{
			text_signup.setTextColor(getResources().getColor(R.color.red));
			text_signin.setTextColor(getResources().getColor(R.color.lines));
			view1.setBackgroundColor(getResources().getColor(R.color.lines));
			view2.setBackgroundColor(getResources().getColor(R.color.red));
			OnSignupFragment();
		}


	}

	private void OnLoginFragment() {
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		Bundle bundle=new Bundle();
		bundle.putString(StaticConstants.USER_TYPE,  USER_TYPE);
		LoginFragment login = new LoginFragment();
		login.setArguments(bundle);
		ft.replace(R.id.baseLogincontainer, login, StaticConstants.LOGIN_TAG);
		currentBackListener=login;
		ft.commit();
	}
	private void OnSignupFragment() {
		FragmentTransaction ft=getFragmentManager().beginTransaction();
		Bundle bundle=new Bundle();
		bundle.putString(StaticConstants.USER_TYPE,  USER_TYPE);
		SignupFragment login = new SignupFragment();
		login.setArguments(bundle);
		ft.replace(R.id.baseLogincontainer, login, StaticConstants.SIGNUP_TAG);
		currentBackListener=login;
		ft.commit();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.text_signin:
			text_signup.setTextColor(getResources().getColor(R.color.lines));
			text_signin.setTextColor(getResources().getColor(R.color.red));
			view2.setBackgroundColor(getResources().getColor(R.color.lines));
			view1.setBackgroundColor(getResources().getColor(R.color.red));
			OnLoginFragment();
			break;
		case R.id.text_signup:
			text_signup.setTextColor(getResources().getColor(R.color.red));
			text_signin.setTextColor(getResources().getColor(R.color.lines));
			view1.setBackgroundColor(getResources().getColor(R.color.lines));
			view2.setBackgroundColor(getResources().getColor(R.color.red));
			OnSignupFragment();
			break;

		default:
			break;
		}
	}

	@Override
	public void onAttach(Activity context) {
		super.onAttach(context);
		this.context=context;
	}

	@Override
	public boolean onBackPressed() {
		return false;
	}

}
