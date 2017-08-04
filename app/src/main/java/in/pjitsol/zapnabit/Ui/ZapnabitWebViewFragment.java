package in.pjitsol.zapnabit.Ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import in.pjitsol.zapnabit.BaseFragmentActivity;
import in.pjitsol.zapnabit.Constants.StaticConstants;
import in.pjitsol.zapnabit.Interface.OnBackPressListener;
import in.pjitsol.zapnabit.R;

/**
 * Created by websnoox android on 4/17/2017.
 */

public class ZapnabitWebViewFragment extends Fragment implements OnBackPressListener {

    private Context context;
    private LayoutInflater inflater;
    private WebView mWebView;
    private String RESTO_URL;
    private Bundle bundle;
    private ProgressBar progress_bar;
    private String USER_TYPE;

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.webview,
                container, false);
        bundle = getArguments();
        RESTO_URL = getArguments().getString(StaticConstants.RESTO_URL);
        USER_TYPE = getArguments().getString(StaticConstants.USER_TYPE);
        this.inflater = inflater;
        initView(view);
        return view;
    }

    private void initView(View view) {
        mWebView = (WebView) view.findViewById(R.id.webview);
        progress_bar = (ProgressBar) view.findViewById(R.id.progress_bar);
        mWebView.loadUrl(RESTO_URL);

        // Enable Javascript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        mWebView.setWebViewClient(new AppWebViewClients(progress_bar));

    }

    public Bundle getBundle() {
        return bundle;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public boolean onBackPressed() {
        if (USER_TYPE.equalsIgnoreCase(StaticConstants.DRIVER))
            ((BaseFragmentActivity) context).OnNearBySearchFragment();
        else
            ((BaseFragmentActivity) context).OnMerchantMyOrderFragment();
        return true;
    }
}