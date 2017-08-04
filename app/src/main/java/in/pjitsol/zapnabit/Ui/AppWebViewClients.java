package in.pjitsol.zapnabit.Ui;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

/**
 * Created by websnoox android on 3/16/2017.
 */

public class AppWebViewClients extends WebViewClient {
    private ProgressBar progressBar;

    public AppWebViewClients(ProgressBar progressBar) {
        this.progressBar = progressBar;
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
        if (view.getProgress() == 100)
            progressBar.setVisibility(View.GONE);
        else
            progressBar.setVisibility(View.VISIBLE);

    }
}
