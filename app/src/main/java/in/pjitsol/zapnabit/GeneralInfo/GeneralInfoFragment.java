package in.pjitsol.zapnabit.GeneralInfo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Ui.ProgressHUD;

/**
 * Created by websnoox android on 4/12/2017.
 */

public class GeneralInfoFragment extends Fragment implements
        OnBackPressListener, View.OnClickListener {
    private LayoutInflater inflater;
    private Context context;
    private TextView text_scan;
    private TextView text_scanpublish;
    private ProgressHUD progressDialog;
    private int value;
    private TextView aboutus;
    private TextView privacypolicies;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.generalinfo,
                container, false);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {
        aboutus = (TextView) view.findViewById(R.id.aboutus);
        privacypolicies = (TextView) view.findViewById(R.id.aboutus);
        aboutus.setOnClickListener(this);
        privacypolicies.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.aboutus:
                ((BaseFragmentActivity)context).OnAboutusFragment();
                break;
            case R.id.privacypolicies:
                ((BaseFragmentActivity)context).OnPrivacyFragment();
                break;
        }

    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
