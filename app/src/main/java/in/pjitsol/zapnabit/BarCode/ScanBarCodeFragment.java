package in.pjitsol.zapnabit.BarCode;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.HashMap;

import in.pjitsol.zapnabit.AsyncTask.AuthCommanTask;
import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.PrefHelper;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.Network.BaseNetwork;
import in.pjitsol.zapnabit.Network.IAsyncTaskRunner;
import in.pjitsol.zapnabit.Network.ResultMessage;
import in.pjitsol.zapnabit.R;
import in.pjitsol.zapnabit.Ui.ProgressHUD;

/**
 * Created by websnoox android on 4/11/2017.
 */

public class ScanBarCodeFragment extends Fragment implements
        OnBackPressListener, View.OnClickListener {
    private LayoutInflater inflater;
    private Activity context;
    private TextView text_scan;
    private TextView text_scanpublish;
    private ProgressHUD progressDialog;
    private int value=1;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.scanbarcode,
                container, false);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {
        text_scan = (TextView) view.findViewById(R.id.text_scan);
        text_scanpublish = (TextView) view.findViewById(R.id.text_scanpublish);
        text_scan.setOnClickListener(this);
        text_scanpublish.setOnClickListener(this);

    }

    @Override
    public boolean onBackPressed() {
        ((BaseFragmentActivity) context).OnMerchantMyOrderFragment();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_scan:
                value = 0;
                //PerFormScanning("365702134102");
                new IntentIntegrator(context).initiateScan();
                break;
            case R.id.text_scanpublish:
                value = 1;
                new IntentIntegrator(context).initiateScan();
                break;
        }
    }

    public int scanOrpublish() {
        return value;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (Activity) context;
    }


}
