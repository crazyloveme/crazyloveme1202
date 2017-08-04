package in.pjitsol.zapnabit.PrivacyPolicy;

import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class PrivacyFragment extends Fragment implements 
OnBackPressListener{
	private LayoutInflater inflater;
	private Activity context;
	@Override
	public View onCreateView(final LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.privacy_policy,
				container, false);
		this.inflater = inflater;
		initView(view);
		return view;
	}

	private void initView(View view) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return false;
	}
}
