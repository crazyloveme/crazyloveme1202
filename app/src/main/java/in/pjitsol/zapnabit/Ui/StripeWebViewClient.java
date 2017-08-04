package in.pjitsol.zapnabit.Ui;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by Bhawna on 6/9/2017.
 */

public class StripeWebViewClient  extends WebViewClient
{
    private final IcallBackWebView icallback;
    private ProgressBar progressBar;

    public StripeWebViewClient(ProgressBar progressBar,IcallBackWebView icallBackWebView) {
        this.progressBar = progressBar;
        this.icallback=icallBackWebView;
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // TODO Auto-generated method stub
        view.loadUrl(url);
        return true;
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        // TODO Auto-generated method stub
        super.onPageFinished(view, url);
        if (view.getProgress() == 100){
            if(url.contains("thankyou")){
                icallback.callbackWebView(true);
                progressBar.setVisibility(View.GONE);
            }
            else  if(url.contains("payment_fail")){
                icallback.callbackWebView(false);
                progressBar.setVisibility(View.GONE);
            }

        }
        else
            progressBar.setVisibility(View.VISIBLE);

    }
}

